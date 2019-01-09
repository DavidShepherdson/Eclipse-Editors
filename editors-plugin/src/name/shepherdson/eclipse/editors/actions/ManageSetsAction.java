
package name.shepherdson.eclipse.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import name.shepherdson.eclipse.editors.services.EditorService;
import name.shepherdson.eclipse.editors.services.SettingsService;
import name.shepherdson.eclipse.editors.views.DialogUtils;
import name.shepherdson.eclipse.editors.views.ManageSets.ManageSetsView;
import name.shepherdson.eclipse.editors.views.editors.EditorSetComboControl;

public class ManageSetsAction extends Action
{
    // private static LogWrapper log = new LogWrapper(EditorTableView.class);

    EditorService editorService = EditorService.getInstance();
    SettingsService settingsService = SettingsService.getInstance();

    private EditorSetComboControl editorSetComboControl;

    public ManageSetsAction(EditorSetComboControl editorSetComboControl)
    {
        this.editorSetComboControl = editorSetComboControl;
        setText("Preferences");
        setToolTipText("Preferences");
        setImageDescriptor(
                PlatformUI.getWorkbench().getSharedImages()
                        .getImageDescriptor(ISharedImages.IMG_DEF_VIEW));
    }

    @Override
    public void run()
    {
        ManageSetsView dialog =
                new ManageSetsView(DialogUtils.getScreenCentredShell(), editorSetComboControl);
        dialog.create();
        dialog.open();
    }
}
