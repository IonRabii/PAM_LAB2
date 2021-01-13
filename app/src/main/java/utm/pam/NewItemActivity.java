package utm.pam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

import utm.pam.db.CalendarItem;
import utm.pam.db.Storage;

public class NewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        final Intent intent = getIntent();
        final String date = intent.getStringExtra("DATE");

        final Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(view -> {
            final String title = ((EditText) findViewById(R.id.event_title)).getText().toString();
            final String description = ((EditText) findViewById(R.id.event_description)).getText().toString();

            try {
                Serializer serializer = new Persister();
                Storage storage = serializer.read(Storage.class, new File(getFilesDir(), "calendar_database.xml"));

                CalendarItem item = new CalendarItem(date, title, description);
                storage.getItems().add(item);
                serializer.write(storage, new File(getFilesDir(), "calendar_database.xml"));
                showNotification("Event added!", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showNotification(String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, id) -> finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}