package utm.pam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import utm.pam.db.CalendarItem;
import utm.pam.db.Storage;

import static java.util.Arrays.asList;
import static utm.pam.Util.showNotification;

public class UpdateItemActivity extends AppCompatActivity {
    private Storage storage;
    private CalendarItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        final Intent intent = getIntent();
        final String date = intent.getStringExtra("DATE");

        try {
            Serializer serializer = new Persister();
            storage = serializer.read(Storage.class, new File(getFilesDir(), "calendar_database.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> items = storage.getItems().stream().filter(e -> date.equals(e.getDate()))
                .map(CalendarItem::getTitle).collect(Collectors.toList());
        items.add(0, "---");
        Spinner spinner = (Spinner) findViewById(R.id.action_bar_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        spinner.setOnItemClickListener((parent, view, position, id) -> {
//            showNotification(parent + " " + view + " " + position + " " + id, this);
//        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView textView = (TextView) selectedItemView;
                String eventName = textView.getText().toString();
                selectedItem = storage.getItems().stream().filter(e -> eventName.equals(e.getTitle()))
                        .findFirst().orElse(new CalendarItem("", "", ""));
                EditText title = (EditText) findViewById(R.id.event_title);
                title.setText(selectedItem.getTitle());

                EditText description = (EditText) findViewById(R.id.event_description);
                description.setText(selectedItem.getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        final Button add = (Button) findViewById(R.id.update_button);
        add.setOnClickListener(view -> {
            final String title = ((EditText) findViewById(R.id.event_title)).getText().toString();
            final String description = ((EditText) findViewById(R.id.event_description)).getText().toString();

            try {
                Serializer serializer = new Persister();
                selectedItem.setTitle(title);
                selectedItem.setDescription(description);
                serializer.write(storage, new File(getFilesDir(), "calendar_database.xml"));
                showNotification("Event updated!");
                updateSpinnerValues(spinner, date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSpinnerValues(Spinner spinner, String date) {
        List<String> itemList = storage.getItems().stream().filter(e -> date.equals(e.getDate()))
                .map(CalendarItem::getTitle).collect(Collectors.toList());
        itemList.add(0, "---");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private void showNotification(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}