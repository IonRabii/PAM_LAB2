package utm.pam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import utm.pam.R;
import utm.pam.dao.EventRepository;
import utm.pam.model.Event;

import static utm.pam.util.Util.createRepositoryInstance;
import static utm.pam.util.Util.showNotification;

public class UpdateItemActivity extends AppCompatActivity {
    private EventRepository eventRepository;
    private Event selectedEvent;
    private final List<String> items;
    private final List<String> values;

    public UpdateItemActivity() {
        this.items = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        eventRepository = createRepositoryInstance(getFilesDir());

        final Intent intent = getIntent();
        final String date = intent.getStringExtra("DATE");
        final Spinner spinner = (Spinner) findViewById(R.id.action_bar_spinner);
        updateSpinnerValues(spinner, date);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateForm(values.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        final Button add = (Button) findViewById(R.id.update_button);
        add.setOnClickListener(view -> {
            updateEvent(spinner, date);
        });
    }

    private void updateForm(final String key) {
        if (key.equals("default")) return;

        selectedEvent = eventRepository.getById(key);
        EditText title = (EditText) findViewById(R.id.event_title);
        title.setText(selectedEvent.getTitle());

        EditText description = (EditText) findViewById(R.id.event_description);
        description.setText(selectedEvent.getDescription());
    }

    private void updateEvent(final Spinner spinner, final String date) {
        final String title = ((EditText) findViewById(R.id.event_title)).getText().toString();
        final String description = ((EditText) findViewById(R.id.event_description)).getText().toString();

        selectedEvent.setTitle(title);
        selectedEvent.setDescription(description);
        eventRepository.updateEvent(selectedEvent.getId(), date, title, description);
        showNotification("Event updated!", this);
        updateSpinnerValues(spinner, date);
    }

    private void updateSpinnerValues(final Spinner spinner, final String date) {
        final List<Event> events = eventRepository.getAll().stream()
                .filter(e -> date.equals(e.getDate()))
                .collect(Collectors.toList());

        events.forEach(e -> {
            items.add(e.getTitle());
            values.add(e.getId());
        });
        items.add(0, "---");
        values.add(0, "default");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
}