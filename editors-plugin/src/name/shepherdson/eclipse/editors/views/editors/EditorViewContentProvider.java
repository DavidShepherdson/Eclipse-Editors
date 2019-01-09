
package name.shepherdson.eclipse.editors.views.editors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import name.shepherdson.eclipse.editors.models.editor.IEditor;
import name.shepherdson.eclipse.editors.services.EditorService;

class EditorViewContentProvider implements IStructuredContentProvider
{

    private EditorService editorService = EditorService.getInstance();
    private EditorChangeListener listener;
    private IEditor[] openEditors;

    public EditorViewContentProvider(EditorChangeListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void inputChanged(Viewer v, Object oldInput, Object newInput)
    {
        // Do nothing
    }

    @Override
    public void dispose()
    {
        // Do nothing
    }

    @Override
    public Object[] getElements(Object parent)
    {
        if (openEditors == null || openEditors.length == 0)
        {
            openEditors = editorService.buildOpenEditors(listener);
        }
        return openEditors;
    }

    public void reset()
    {
        openEditors = null;
    }
}
