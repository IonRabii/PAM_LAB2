package utm.pam.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import utm.pam.R;
import utm.pam.activities.RingActivity;

import static utm.pam.services.Impl.SchedulerServiceImpl.CHANNEL_ID;
import static utm.pam.services.Impl.SchedulerServiceImpl.TITLE;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String activities = intent.getStringExtra(TITLE);
        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra(TITLE, activities);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = "List of activities";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText(activities)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        mediaPlayer.start();
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}