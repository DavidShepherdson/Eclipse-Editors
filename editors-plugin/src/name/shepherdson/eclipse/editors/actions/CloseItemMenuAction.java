
package name.shepherdson.eclipse.editors.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPartSite;

import name.shepherdson.eclipse.editors.logging.LogWrapper;
import name.shepherdson.eclipse.editors.models.editor.IEditor;
import name.shepherdson.eclipse.editors.services.EditorService;
import name.shepherdson.eclipse.editors.services.SettingsService;
import name.shepherdson.eclipse.editors.views.editors.EditorTableView;

public class CloseItemMenuAction extends Action
{
    private static LogWrapper log = new LogWrapper(CloseItemMenuAction.class);

    EditorService editorService = EditorService.getInstance();
    SettingsService settingsService = SettingsService.getInstance();
    private EditorTableView editorTableView;

    private IWorkbenchPartSite site;

    public CloseItemMenuAction(EditorTableView editorTableView, IWorkbenchPartSite site)
    {
        this.editorTableView = editorTableView;
        this.site = site;
        setText("Close");
    }

    @Override
    public void run()
    {
        List<IEditor> editors = editorTableView.getSelections();
        for (IEditor editor : editors)
        {
            try
            {
                if (editor.isOpened())
                {
                    editorService.closeEditor(editor, site);
                }
                settingsService.getActiveEditorSettingsSet().getEditorModels()
                        .remove(editor.getFilePath());
            }
            catch (Exception e)
            {
                log.warn(e, "Could not close editor: %s", editor.getFilePath());
            }
        }

        editorTableView.refresh();
    }
}
