package name.shepherdson.eclipse.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import name.shepherdson.eclipse.editors.models.settings.EditorSetSettingsModel;
import name.shepherdson.eclipse.editors.services.SettingsService;
import name.shepherdson.eclipse.editors.views.DialogUtils;
import name.shepherdson.eclipse.editors.views.editors.EditorSetComboControl;
import name.shepherdson.eclipse.editors.views.save.SaveDialogView;

public class SaveSetAction extends Action {

	private SettingsService settingsService = SettingsService.getInstance();
	private EditorSetComboControl editorSetComboControl;

	public SaveSetAction(EditorSetComboControl editorSetComboControl) {
		this.editorSetComboControl = editorSetComboControl;
		setText("Save Set");
		setToolTipText("Save Set");
		setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
	}

	@Override
	public void run() {
		SaveDialogView dialog = new SaveDialogView(DialogUtils.getScreenCentredShell());
		dialog.create();
		if (dialog.open() == Window.OK) {
			String setName = dialog.getFileName();
			EditorSetSettingsModel newEditorSetSettingsModel = settingsService.copyEditorSetSettingsModel(setName);
			settingsService.addNewEditorSetSettingsModel(newEditorSetSettingsModel);
			editorSetComboControl.refreshData();
		}

	}

}
