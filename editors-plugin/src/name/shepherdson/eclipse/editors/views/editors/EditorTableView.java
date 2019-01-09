
package name.shepherdson.eclipse.editors.views.editors;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import name.shepherdson.eclipse.editors.Constants;
import name.shepherdson.eclipse.editors.logging.LogWrapper;
import name.shepherdson.eclipse.editors.models.editor.EditorComparator;
import name.shepherdson.eclipse.editors.models.editor.EditorComparator.SortType;
import name.shepherdson.eclipse.editors.models.editor.IEditor;
import name.shepherdson.eclipse.editors.services.EditorService;
import name.shepherdson.eclipse.editors.services.SettingsService;

public class EditorTableView implements ISelectionChangedListener
{
    private static LogWrapper log = new LogWrapper(EditorTableView.class);

    private static final int MAX_RETRY_DELAY = 4000; // In milliseconds.

    private SettingsService settingsService = SettingsService.getInstance();

    private EditorService editorService = EditorService.getInstance();

    private TableViewer tableViewer;
    private IWorkbenchPartSite site;

    private EditorComparator editorComparator;

    private IEditor activeEditor;

    private EditorItemMenuManager menuManager;

    private EditorViewContentProvider editorViewContentProvider;

    public EditorTableView(Composite parent, IWorkbenchPartSite site, IViewSite iViewSite)
    {
        tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.site = site;

        // Build sorter
        SortType sortBy = Constants.DEFAULT_SORTBY;
        editorComparator = new EditorComparator(sortBy);
        tableViewer.setComparator(editorComparator);

        editorViewContentProvider = new EditorViewContentProvider(new EditorChangeListener(this));
        tableViewer.setContentProvider(editorViewContentProvider);
        tableViewer.setLabelProvider(new EditorViewLabelProvider());
        tableViewer.setInput(iViewSite);

        tableViewer.addPostSelectionChangedListener(this);

        menuManager = new EditorItemMenuManager(this, site, parent);
        tableViewer.getTable().setMenu(menuManager.createContextMenu(parent));

    }

    public void refresh()
    {
        try
        {
            if (tableViewer != null && tableViewer.getControl() != null
                    && !tableViewer.getControl().isDisposed())
            {
                editorViewContentProvider.reset();
                tableViewer.refresh();
            }
        }
        catch (Exception e)
        {
            log.warn(e);
        }
    }

    public void refreshSoon()
    {
        runSoonOnUIThread(new Runnable()
        {
            @Override
            public void run()
            {
                refresh();
            }
        });
    }

    private void runSoonOnUIThread(Runnable whatToRun)
    {
        runSoonOnUIThread(whatToRun, 500);
    }

    private void runSoonOnUIThread(Runnable whatToRun, int howSoonInMilliseconds)
    {
        // This stupidly-convoluted code is because if we update our view immediately when the
        // certain events are triggered, we get stale information (such as a stale icon for the
        // file, or an editor that is not 'open'). So, instead, we
        // schedule the call to happen in 500 milliseconds, by which time this information
        // should be correct. But in order to schedule a delayed update (calling timeExec()), we
        // have to be on the UI thread, so we have to trigger *that* part of the code to run via
        // the Display.syncExec() first!
        try
        {
            Display display = Display.getCurrent();
            if (display == null)
            {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable()
            {
                @Override
                public void run()
                {
                    Display.getCurrent().timerExec(howSoonInMilliseconds, whatToRun);
                }
            });
        }
        catch (SWTException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch (RuntimeException re)
        {
            re.printStackTrace();
            throw re;
        }
    }

    public void setSortBy(EditorComparator.SortType sortBy)
    {
        editorComparator.setSortBy(sortBy);
        settingsService.getActiveEditorSettingsSet().setSortBy(sortBy);
        refresh();
    }

    @Override
    public void selectionChanged(SelectionChangedEvent arg0)
    {
        List<IEditor> editors = getSelections();
        if (editors.size() == 0)
        {
            if (activeEditor != null)
            {
                IEditorReference activeEditorReference = activeEditor.getReference();
                if (activeEditorReference == null)
                {
                    IEditorPart pageActiveEditor = PlatformUI.getWorkbench()
                            .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                    activeEditor = null; // Clear the field so 'reselecting' it looks like a change.
                    if (pageActiveEditor != null)
                    {
                        setActiveInput(pageActiveEditor.getEditorInput());
                    }
                }
                else
                {
                    try
                    {
                        setActiveInput(activeEditorReference.getEditorInput());
                    }
                    catch (PartInitException e)
                    {
                        log.warn(e);
                        updateActivePart();
                    }
                }
            }
            return;
        }
        if (editors.size() != 1)
        {
            // Only switch editors when exactly one has been selected.
            return;
        }
        IEditor editor = editors.iterator().next();
        if (editor != activeEditor)
        {
            // We've actually changed active editors, according to what we have cached.
            try
            {
                editorService.openEditor(editor, site);
            }
            catch (Exception e)
            {
                log.warn(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<IEditor> getSelections()
    {
        return tableViewer.getStructuredSelection().toList();
    }

    public IEditor getSelection()
    {
        return (IEditor) tableViewer.getStructuredSelection().getFirstElement();
    }

    public EditorComparator getSorter()
    {
        return editorComparator;
    }

    public Control getTable()
    {
        return tableViewer.getTable();
    }

    public void updateActivePart()
    {
        IEditorPart pageActiveEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().getActiveEditor();
        setActiveInput(pageActiveEditor == null ? null : pageActiveEditor.getEditorInput());
    }

    public void setActiveInput(IEditorInput editorInput)
    {
        setActiveEditor(
                editorInput,
                editorInput == null ? null : editorInput.getName(),
                EditorTableView::getInputForEditor,
                500);
    }

    private static IEditorInput getInputForEditor(IEditor editor)
    {
        try
        {
            IEditorReference reference = editor.getReference();
            return reference == null ? null : reference.getEditorInput();
        }
        catch (PartInitException e)
        {
            log.warn(e);
            return null;
        }
    }

    private <T> void setActiveEditor(T activeIdentifier, String identifierDescriptor,
            Function<IEditor, T> identifierAccessor, int howSoonToTryAgainInMilliseconds)
    {
        IEditor newActiveEditor = findMatchingEditor(activeIdentifier, identifierAccessor);
        if (newActiveEditor == null)
        {
            refresh();
            newActiveEditor = findMatchingEditor(activeIdentifier, identifierAccessor);
        }
        if (newActiveEditor != activeEditor)
        {
            if (newActiveEditor == null)
            {
                if (howSoonToTryAgainInMilliseconds > 0
                        && howSoonToTryAgainInMilliseconds <= MAX_RETRY_DELAY)
                {
                    runSoonOnUIThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setActiveEditor(
                                    activeIdentifier,
                                    identifierDescriptor,
                                    identifierAccessor,
                                    howSoonToTryAgainInMilliseconds * 2);
                        }
                    }, howSoonToTryAgainInMilliseconds);
                    return;
                }
            }
            activeEditor = newActiveEditor;
            tableViewer.setSelection(
                    activeEditor == null ? new StructuredSelection()
                            : new StructuredSelection(activeEditor),
                    true);
        }
    }

    private <T> IEditor findMatchingEditor(T desiredIdentifier,
            Function<IEditor, T> identifierAccessor)
    {
        IEditor newActiveEditor = null;
        TableItem[] items = tableViewer.getTable().getItems();
        for (TableItem item : items)
        {
            IEditor editor = ((IEditor) item.getData());
            T editorIdentifier = identifierAccessor.apply(editor);
            if (editorIdentifier != null && editorIdentifier.equals(desiredIdentifier))
            {
                if (editor.isOpened())
                {
                    newActiveEditor = editor;
                }
            }
        }
        return newActiveEditor;
    }

    public IWorkbenchPartSite getSite()
    {
        return site;
    }

}
