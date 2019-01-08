
package com.deepnoodle.openeditors.views.openeditors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.deepnoodle.openeditors.models.editor.IEditor;
import com.deepnoodle.openeditors.services.EditorService;

class EditorViewContentProvider implements IStructuredContentProvider
{

    private EditorService openEditorService = EditorService.getInstance();
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
            openEditors = openEditorService.buildOpenEditors(listener);
        }
        return openEditors;
    }

    public void reset()
    {
        openEditors = null;
    }
}
