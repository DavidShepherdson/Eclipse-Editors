
package name.shepherdson.eclipse.editors.views.editors;

import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.part.EditorPart;

public class EditorChangeListener implements IPropertyListener
{

    private EditorTableView editorTableView;

    public EditorChangeListener(EditorTableView editorTableView)
    {
        this.editorTableView = editorTableView;
    }

    @Override
    public void propertyChanged(Object source, int property)
    {
        // TODO Do we need to filter for the event source?
        // System.err.println("Change: property " + property + ", source " + source);
        if (property == EditorPart.PROP_DIRTY)
        {
            editorTableView.refresh();
        }
        else if (property == EditorPart.PROP_TITLE)
        {
            editorTableView.refreshSoon();
        }
    }
}
