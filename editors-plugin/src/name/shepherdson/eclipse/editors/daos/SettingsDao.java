
package name.shepherdson.eclipse.editors.daos;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

import com.google.gson.GsonBuilder;

import name.shepherdson.eclipse.editors.Activator;
import name.shepherdson.eclipse.editors.logging.LogWrapper;
import name.shepherdson.eclipse.editors.models.settings.SettingsModel;

public class SettingsDao
{
    private static LogWrapper log = new LogWrapper(SettingsDao.class);

    private static final String SETS_FILE_NAME = "settings.editors";
    private static final String SETS_ROOT = "OpenEditor";

    private static SettingsDao instance;

    public static SettingsDao getInstance()
    {
        if (instance == null)
        {
            instance = new SettingsDao();
        }
        return instance;
    }

    public SettingsModel loadSettings()
    {
        IPath path = Activator.getDefault().getStateLocation();
        String settingsFileName = getSettingsPath(path);
        DialogSettings settings = new DialogSettings(SETS_ROOT);
        try
        {
            settings.load(settingsFileName);

            IDialogSettings settingsModelJsonSection =
                    DialogSettings.getOrCreateSection(settings, "SettingsEntity");
            String json = settingsModelJsonSection.get("json");
            if (json != null)
            {
                return new GsonBuilder().create().fromJson(json, SettingsModel.class);
            }
        }
        catch (FileNotFoundException e)
        {
            log.info(
                    "Settings can not be loaded (IOException), probably because none have been saved yet");
        }
        catch (IOException e)
        {
            log.warn(e);
        }
        return new SettingsModel();
    }

    public String getSettingsPath(IPath path)
    {
        String settingsFileName = path.append(SETS_FILE_NAME).toOSString();
        return settingsFileName;
    }
}
