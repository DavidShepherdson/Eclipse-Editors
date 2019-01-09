
package name.shepherdson.eclipse.editors.views.editors;

import javax.annotation.PostConstruct;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import name.shepherdson.eclipse.editors.actions.SortAction;
import name.shepherdson.eclipse.editors.models.editor.EditorComparator;

//TODO clean this up
public class EditorsViewPart extends ViewPart
{

    private EditorTableView editorTableView;

    @Override
    @PostConstruct
    public void createPartControl(Composite parent)
    {

        IWorkbenchPartSite site = getSite();
        final IWorkbenchWindow workbenchWindow = site.getWorkbenchWindow();

        // Build the editor view
        editorTableView = new EditorTableView(parent, site, getViewSite());

        PartListener listener = new PartListener(editorTableView);
        workbenchWindow.getPartService().addPartListener(listener);

//		Action sortByAccessAction = new SortAction(editorTableView,
//				EditorComparator.SortType.ACCESS,
//				"Sort by Last Access",
//				"Sorts the tabs by last access using eclipse navigation history");
        Action sortByNameAction = new SortAction(
                editorTableView,
                EditorComparator.SortType.NAME,
                "Sort by Name",
                "Sorts the tabs by name");
        Action sortByPathAction = new SortAction(
                editorTableView,
                EditorComparator.SortType.PATH,
                "Sort by Path",
                "Sorts the tabs by full path");
        Action sortByNaturalAction = new SortAction(
                editorTableView,
                EditorComparator.SortType.NATURAL,
                "Sort by Natural (Opened) Order",
                "Sorts the tabs by the order in which they were opened");

        IActionBars bars = getViewSite().getActionBars();
        IMenuManager menuManager = bars.getMenuManager();
        menuManager.add(sortByNameAction);
        menuManager.add(sortByPathAction);
        menuManager.add(sortByNaturalAction);

        // TODO fix and add back in
        // menuManager.add(sortByAccessAction);
    }

    @Override
    public void setFocus()
    {
        // Do nothing

    }

}
