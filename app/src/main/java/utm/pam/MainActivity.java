package utm.pam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import utm.pam.db.CalendarItem;
import utm.pam.db.Storage;

import static java.util.Arrays.asList;
import static utm.pam.Util.showNotification;

public class MainActivity extends AppCompatActivity {
    private LocalDate date;

    public MainActivity() {
        date = LocalDate.now();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            date = LocalDate.of(year, month + 1, dayOfMonth);
        });

        // manage add
        Button addItem = (Button) findViewById(R.id.add);
        addItem.setOnClickListener((view) -> {
            Intent intent = new Intent(this, NewItemActivity.class);
            intent.putExtra("DATE", String.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE)));
            startActivity(intent);
        });

        // manage update
        Button updateItem = (Button) findViewById(R.id.update);
        updateItem.setOnClickListener((view) -> {
            Intent intent = new Intent(this, UpdateItemActivity.class);
            intent.putExtra("DATE", String.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE)));
            startActivity(intent);
        });

        // manage delete
        Button deleteItem = (Button) findViewById(R.id.remove);
        deleteItem.setOnClickListener((view) -> {
            try {
                Serializer serializer = new Persister();
                Storage storage = serializer.read(Storage.class, new File(getFilesDir(), "calendar_database.xml"));
                final String stringData = String.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE));
                List<CalendarItem> items = storage.getItems().stream().filter(e -> !stringData.equals(e.getDate()))
                        .collect(Collectors.toList());
                storage.setItems(items);
                serializer.write(storage, new File(getFilesDir(), "calendar_database.xml"));
                showNotification("Event deleted!", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // manage search
        Button search = (Button) findViewById(R.id.search);
        EditText editText = (EditText) findViewById(R.id.edit_query);
        String prefix = editText.getText() == null ? "" : editText.getText().toString();
        search.setOnClickListener((view) -> {
            Intent intent = new Intent(this, SearchItemActivity.class);
            intent.putExtra("TITLE", prefix);
            startActivity(intent);
        });
    }
}