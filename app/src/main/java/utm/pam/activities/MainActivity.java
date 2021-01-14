package utm.pam.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import utm.pam.R;
import utm.pam.dao.EventRepository;
import utm.pam.services.Impl.SchedulerServiceImpl;
import utm.pam.services.SchedulerService;

import static utm.pam.services.Impl.SchedulerServiceImpl.CHANNEL_ID;
import static utm.pam.util.Util.createRepositoryInstance;
import static utm.pam.util.Util.showNotification;

public class MainActivity extends AppCompatActivity {
    private EventRepository eventRepository;
    private SchedulerService schedulerService;
    private LocalDate date;

    public MainActivity() {
        date = LocalDate.now();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventRepository = createRepositoryInstance(getFilesDir());
        schedulerService = new SchedulerServiceImpl(eventRepository);
        createNotificationChannnel();
        schedulerService.schedule("20210114", this);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            date = LocalDate.of(year, month + 1, dayOfMonth);
        });

        manageAddEvent();
        manageUpdateEvent();
        manageDeleteEvent();
        manageSearchEvents();
    }

    private void manageAddEvent() {
        Button addItem = (Button) findViewById(R.id.add);
        addItem.setOnClickListener((view) -> {
            Intent intent = new Intent(this, NewItemActivity.class);
            intent.putExtra("DATE", String.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE)));
            startActivity(intent);
        });
    }

    private void manageUpdateEvent() {
        Button updateItem = (Button) findViewById(R.id.update);
        updateItem.setOnClickListener((view) -> {
            Intent intent = new Intent(this, UpdateItemActivity.class);
            intent.putExtra("DATE", String.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE)));
            startActivity(intent);
        });
    }

    private void manageDeleteEvent() {
        Button deleteItem = (Button) findViewById(R.id.remove);
        deleteItem.setOnClickListener((view) -> {
            final String stringData = String.valueOf(date.format(DateTimeFormatter.BASIC_ISO_DATE));
            eventRepository.deleteByDate(stringData);
            showNotification("Event deleted!", this);
        });
    }

    private void manageSearchEvents() {
        Button search = (Button) findViewById(R.id.search);
        EditText editText = (EditText) findViewById(R.id.edit_query);
        search.setOnClickListener((view) -> {
            String prefix = editText.getText() == null ? "" : editText.getText().toString();
            Intent intent = new Intent(this, SearchItemActivity.class);
            intent.putExtra("TITLE", prefix);
            startActivity(intent);
        });
    }

    private void createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}