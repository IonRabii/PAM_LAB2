package utm.pam.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static utm.pam.services.Impl.SchedulerServiceImpl.TITLE;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String activities = intent.getStringExtra(TITLE);
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(TITLE, activities);
        context.startForegroundService(intentService);
    }
}
