package utm.pam.services.Impl;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import utm.pam.dao.EventRepository;
import utm.pam.model.Event;
import utm.pam.services.AlarmBroadcastReceiver;
import utm.pam.services.AlarmService;
import utm.pam.services.SchedulerService;

import static java.lang.Integer.parseInt;

public class SchedulerServiceImpl implements SchedulerService {
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";
    public static final String TITLE = "TITLE";

    private final EventRepository eventRepository;

    public SchedulerServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void schedule(final String date, final Context context) {
        final List<Event> events = eventRepository.getByDate(date);
        final String activities = events.stream().map(Event::getTitle).collect(Collectors.joining("\n"));

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(TITLE, activities);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 28000, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
        Toast.makeText(context, "Alarm scheduled at " + calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
    }
}
