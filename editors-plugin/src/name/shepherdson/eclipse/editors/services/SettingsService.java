
package name.shepherdson.eclipse.editors.services;

import org.eclipse.swt.graphics.RGB;

import name.shepherdson.eclipse.editors.Constants;
import name.shepherdson.eclipse.editors.daos.SettingsDao;
import name.shepherdson.eclipse.editors.models.settings.EditorSetSettingsModel;
import name.shepherdson.eclipse.editors.models.settings.SettingsModel;

public class SettingsService
{
    // private static LogWrapper log = new LogWrapper(SettingsService.class);

    private SettingsDao settingsDao = SettingsDao.getInstance();

    private SettingsModel settings;

    private static SettingsService instance;

    public static SettingsService getInstance()
    {
        if (instance == null)
        {
            instance = new SettingsService();
        }
        return instance;
    }

    private SettingsModel getOrCreateSettings()
    {
        if (settings == null)
        {
            settings = settingsDao.loadSettings();
            // Create the default set name if it doesn't exist
            if (settings.keepOpenEditorsHistory()
                    || settings.getEditorSettingsSet(Constants.OPEN_EDITORS_SET_NAME) == null)
            {
                EditorSetSettingsModel editorSetSettingsSet = new EditorSetSettingsModel();
                editorSetSettingsSet.setSortBy(Constants.DEFAULT_SORTBY);
                settings.getEditorSettingsSets()
                        .put(Constants.OPEN_EDITORS_SET_NAME, editorSetSettingsSet);

            }
            // If the active set is null, change it to the default set
            if (!settings.stickyEditorSettings() || settings.getActiveEditorSettingsSet() == null)
            {
                settings.setActiveSetName(Constants.OPEN_EDITORS_SET_NAME);
            }
        }
        return settings;
    }

    public RGB getDirtyColor()
    {
        return getOrCreateSettings().getDirtyColor();
    }

    public RGB getPinnedColor()
    {
        return getOrCreateSettings().getPinnedColor();
    }

    public RGB getHighlightColor()
    {
        return getOrCreateSettings().getHighlightColor();
    }

    public EditorSetSettingsModel getActiveEditorSettingsSet()
    {
        return getOrCreateSettings().getActiveEditorSettingsSet();
    }

}
