
package name.shepherdson.eclipse.editors.views.editors;

import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;

import name.shepherdson.eclipse.editors.actions.CloseItemMenuAction;
import name.shepherdson.eclipse.editors.models.editor.IEditor;

public class EditorItemMenuManager implements IMenuListener
{
    // private static LogWrapper log = new LogWrapper(EditorItemMenuManager.class);

    private EditorTableView editorTableView;

    private ActionContributionItem closeMenuItem;

    private MenuManager menuManager;

    public EditorItemMenuManager(final EditorTableView editorTableView,
            final IWorkbenchPartSite site, Composite parent)
    {
        this.editorTableView = editorTableView;
        menuManager = new MenuManager()
        {

            @Override
            public void fill(Menu parent, int index)
            {
                super.fill(parent, index);
            }

        };
        menuManager.addMenuListener(this);
        menuManager.setRemoveAllWhenShown(true);

        closeMenuItem = new ActionContributionItem(new CloseItemMenuAction(editorTableView, site));
        menuManager.add(closeMenuItem);

    }

    @Override
    public void menuAboutToShow(IMenuManager manager)
    {

        List<IEditor> selections = editorTableView.getSelections();

        closeMenuItem.setVisible(canClose(selections));

        menuManager.add(closeMenuItem);

    }

    private boolean canClose(List<IEditor> editors)
    {
        for (IEditor editor : editors)
        {
            if (editor.isOpened())
            {
                return true;
            }
        }
        return false;
    }

    public Menu createContextMenu(Control parent)
    {
        return menuManager.createContextMenu(parent);
    }

}
