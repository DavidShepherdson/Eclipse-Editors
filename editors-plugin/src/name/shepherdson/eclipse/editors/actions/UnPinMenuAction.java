package name.shepherdson.eclipse.editors.actions;

import java.util.List;

import org.eclipse.jface.action.Action;

import name.shepherdson.eclipse.editors.logging.LogWrapper;
import name.shepherdson.eclipse.editors.models.editor.IEditor;
import name.shepherdson.eclipse.editors.services.EditorService;
import name.shepherdson.eclipse.editors.services.SettingsService;
import name.shepherdson.eclipse.editors.views.editors.EditorTableView;

public class UnPinMenuAction extends Action {
	private static LogWrapper log = new LogWrapper(PinMenuAction.class);

	EditorService editorService = EditorService.getInstance();
	SettingsService settingsService = SettingsService.getInstance();
	private EditorTableView editorTableView;

	public UnPinMenuAction(EditorTableView editorTableView) {
		this.editorTableView = editorTableView;
		setText("Un-Pin");
	}

	@Override
	public void run() {
		List<IEditor> editors = editorTableView.getSelections();
		for (IEditor editor : editors) {
			try {
				editor.setPinned(false);
			} catch (Exception e) {
				log.warn(e, "Could not close editor: %s", editor.getFilePath());
			}
		}
		settingsService.saveSettings();
		editorTableView.refresh();
	}
}
