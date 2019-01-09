
package name.shepherdson.eclipse.editors.views.editors;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class PartListener implements IPartListener2, IResourceChangeListener
{
    private EditorTableView editorTableView;

    public PartListener(EditorTableView editorTableView)
    {
        this.editorTableView = editorTableView;
    }

    @Override
    public void partActivated(IWorkbenchPartReference partReference)
    {
        if (partReference instanceof IEditorReference)
        {
//            System.err.println(
//                    "Activated: " + partReference + " (" + partReference.getPartName() + ")");
            editorTableView.setActivePart(partReference);
            // editorTableView.refresh();
        }
        else
        {
//            System.err.println(
//                    "NON-EDITOR activated: " + partReference + " ("
//                            + (partReference == null ? null : partReference.getPartName()) + ")");
        }
    }

    @Override
    public void partClosed(IWorkbenchPartReference partReference)
    {
        if (partReference instanceof IEditorReference)
        {
            editorTableView.refresh();
        }
    }

    @Override
    public void partOpened(IWorkbenchPartReference partReference)
    {
        if (partReference instanceof IEditorReference)
        {
            // System.err.println("Opened: " + partReference);
            // editorTableView.refresh();
        }
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partReference)
    {
        if (partReference instanceof IEditorReference)
        {
            // editorTableView.refresh();
        }
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event)
    {
        // editorTableView.refresh();
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partReference)
    {
        // We don't care; everything is always shown in our list.
    }

    @Override
    public void partHidden(IWorkbenchPartReference partReference)
    {
        // We don't care; everything is always shown in our list.
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partReference)
    {
        // We don't care.
    }

    @Override
    public void partVisible(IWorkbenchPartReference partReference)
    {
        // We don't care; everything is always shown in our list.
    }
}
