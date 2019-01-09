
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
            editorTableView.updateActivePart();
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
        // We don't care.
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partReference)
    {
        // We don't care.
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event)
    {
        // We don't care.
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
