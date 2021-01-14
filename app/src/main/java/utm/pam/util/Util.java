package utm.pam.util;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

import utm.pam.dao.DatabaseManager;
import utm.pam.dao.EventRepository;
import utm.pam.dao.impl.DatabaseManagerImpl;
import utm.pam.dao.impl.EventRepositoryImpl;

public class Util {
    private static DatabaseManager databaseManager;
    private static EventRepository eventRepository;

    private Util() {
    }

    public static void showNotification(String msg, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showNotificationAndCloseActivity(String msg, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, id) -> activity.finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static DatabaseManager createDatabaseConnection(File path) {
        if (databaseManager == null)
            databaseManager = new DatabaseManagerImpl(path);
        return databaseManager;
    }

    public static EventRepository createRepositoryInstance(File path) {
        if (eventRepository == null)
            eventRepository = new EventRepositoryImpl(createDatabaseConnection(path));
        return eventRepository;
    }
}
