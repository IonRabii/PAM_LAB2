package utm.pam;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class Util {
    public static void showNotification(String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
