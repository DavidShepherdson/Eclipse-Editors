
package name.shepherdson.eclipse.editors.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import name.shepherdson.eclipse.editors.logging.LogWrapper;
import name.shepherdson.eclipse.editors.models.editor.IEditor;
import name.shepherdson.eclipse.editors.models.settings.EditorModel;
import name.shepherdson.eclipse.editors.models.settings.EditorSetSettingsModel;
import name.shepherdson.eclipse.editors.views.editors.EditorChangeListener;

public class EditorService
{
    private static LogWrapper log = new LogWrapper(EditorService.class);

    private SettingsService settingsService = SettingsService.getInstance();
    private static EditorService instance;

    public static EditorService getInstance()
    {
        if (instance == null)
        {
            instance = new EditorService();
        }
        return instance;
    }

    public void openEditor(IEditor editor, IWorkbenchPartSite site) throws PartInitException
    {
        if (editor.getReference() != null)
        {
            site.getWorkbenchWindow().getActivePage().openEditor(
                    editor.getReference().getEditorInput(),
                    editor.getReference().getId());
        }
        else
        {
            openEditor(editor.getFilePath(), site);
        }
    }

    private void openEditor(String filePath, IWorkbenchPartSite site) throws PartInitException
    {
        IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
        IPath location = new Path(filePath);
        IFile file = ws.getFile(location);

        IEditorDescriptor editorDescriptor =
                PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(filePath);
        String editorDescriptorId;
        if (editorDescriptor == null)
        {

            PlatformUI.getWorkbench().getEditorRegistry()
                    .findEditor(IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
            editorDescriptorId = IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID;
        }
        else
        {
            editorDescriptorId = editorDescriptor.getId();
        }
        site.getWorkbenchWindow().getActivePage()
                .openEditor(new FileEditorInput(file), editorDescriptorId);
    }

    public void closeEditor(IEditor editor, IWorkbenchPartSite site)
    {
        IEditorPart iEditorPart = editor.getReference().getEditor(true);
        if (iEditorPart != null)
        {
            site.getWorkbenchWindow().getActivePage().closeEditor(iEditorPart, true);
        }

    }

    private String getFilePath(IEditorReference reference)
    {
        String path = null;
        try
        {
            if (reference.getEditorInput() != null
                    && reference.getEditorInput() instanceof FileEditorInput)
            {
                FileEditorInput fileEditorInput = ((FileEditorInput) reference.getEditorInput());
                path = fileEditorInput.getFile().getFullPath().toString();
            }
        }
        catch (Exception e)
        {
            log.warn(e, "Problem getting filepath");
        }
        if (path == null && reference != null)
        {
            // Last ditch effort to try and get the filename from the tooltip
            try
            {
                return reference.getEditorInput().getToolTipText();
            }
            catch (Exception e)
            {
                log.warn(e, "Problem getting filepath from tooltip");
            }
        }
        return path;
    }

    // This could probably be optimized, but at this point it is fast enough
    // TODO break into it's own class
    public IEditor[] buildOpenEditors(EditorChangeListener listener)
    {
        IWorkbenchPage activePage =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

        EditorSetSettingsModel editorSetSettingsModel =
                settingsService.getActiveEditorSettingsSet();

        Map<String, EditorModel> editorModels = editorSetSettingsModel.getEditorModels();

        // Mark all as unopened, and clean transient values
        for (EditorModel editorModel : editorModels.values())
        {
            editorModel.setOpened(false);
            editorModel.setReference(null);
            editorModel.setTitleImage(null);
            editorModel.setDirty(false);
        }

        // get the navigation history
        // INavigationLocation[] locations = activePage.getNavigationHistory().getLocations();

        // Loop through open editors, add them to the set if need be.
        IEditorReference[] references = activePage.getEditorReferences();
        for (int i = 0; i < references.length; i++)
        {

            IEditorReference reference = references[i];
            String filePath = getFilePath(reference);
            EditorModel editorModel = editorModels.get(filePath);
            if (editorModel == null)
            {
                Integer naturalPosition = i;
                editorModel = new EditorModel(filePath);
                editorModel.setNaturalPosition(naturalPosition);

                editorModels.put(filePath, editorModel);

                reference.addPropertyListener(listener);
            }

            // Copy over any data that might not be in the editor
            editorModel.setOpened(true);
            editorModel.setReference(reference);
            editorModel.setTitleImage(reference.getTitleImage());
            editorModel.setTitleImagePath(reference.getTitleImage().getImageData().toString());
            editorModel.setDirty(reference.isDirty());

            // This could be optimized if need be
            // for (int h = 0; h < locations.length; h++) {
            // try {
            // INavigationLocation location = locations[h];
            // if (location != null
            // && reference != null
            // && location.getInput() == reference.getEditorInput()) {
            // editor.setHistoryPosition(h);
            // break;
            // }
            // } catch (Exception e) {
            // log.warn(e);
            // }
            // }

        }

        // remove any non open editors if need be
        List<String> editorsToClose = new LinkedList<>();
        for (EditorModel editorModel : editorModels.values())
        {
            if (!editorModel.isOpened())
            {
                editorsToClose.add(editorModel.getFilePath());
            }
        }

        for (String editorToClose : editorsToClose)
        {
            editorModels.remove(editorToClose);
        }

        return editorModels.values().toArray(new IEditor[editorModels.size()]);
    }

}
