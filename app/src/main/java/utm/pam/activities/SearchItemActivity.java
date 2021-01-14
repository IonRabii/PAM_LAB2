package utm.pam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import utm.pam.R;
import utm.pam.model.Event;
import utm.pam.model.Database;

import static utm.pam.util.Util.showNotification;

public class SearchItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        final Intent intent = getIntent();
        final String title = intent.getStringExtra("TITLE");
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        try {
            Serializer serializer = new Persister();
            Database database = serializer.read(Database.class, new File(getFilesDir(), "calendar_database.xml"));

            Predicate<Event> filter = title == null || title.isEmpty()
                    ? e -> true
                    : e -> e.getTitle().startsWith(title);
            List<Event> events = database.getEvents().stream()
                    .filter(filter).collect(Collectors.toList());

            if (events.isEmpty()) {
                showNotification("No events were found", this);
            } else {
                int i = 1;
                for (Event event : events) {
                    View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                    TextView event_display_no = (TextView) tableRow.findViewById(R.id.event_display_no);
                    TextView event_display_date = (TextView) tableRow.findViewById(R.id.event_display_date);
                    TextView event_display_title = (TextView) tableRow.findViewById(R.id.event_display_title);

                    event_display_no.setText(String.valueOf(i));
                    event_display_date.setText(formatData(event.getDate()));
                    event_display_title.setText(event.getTitle());
                    tableLayout.addView(tableRow);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String formatData(String data) {
        return data.length() < 8
                ? data
                : data.substring(0, 4) + "/" + data.substring(4, 6) + "/" + data.substring(6, 8);
    }
}