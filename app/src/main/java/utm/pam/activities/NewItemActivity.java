package utm.pam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import utm.pam.R;
import utm.pam.dao.EventRepository;
import utm.pam.model.Event;

import static utm.pam.util.Util.createRepositoryInstance;
import static utm.pam.util.Util.showNotificationAndCloseActivity;

public class NewItemActivity extends AppCompatActivity {
    private EventRepository eventRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        eventRepository = createRepositoryInstance(getFilesDir());

        final Intent intent = getIntent();
        final String date = intent.getStringExtra("DATE");
        saveEvent(date);
    }

    private void saveEvent(String date) {
        final Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(view -> {
            final String title = ((EditText) findViewById(R.id.event_title)).getText().toString();
            final String description = ((EditText) findViewById(R.id.event_description)).getText().toString();
            Event event = new Event(UUID.randomUUID().toString().replaceAll("-", "_"), date, title, description);
            eventRepository.add(event);
            showNotificationAndCloseActivity("Event added!", this);
        });
    }
}