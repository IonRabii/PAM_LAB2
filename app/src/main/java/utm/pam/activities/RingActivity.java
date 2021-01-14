package utm.pam.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import utm.pam.R;
import utm.pam.services.AlarmService;

import static utm.pam.services.Impl.SchedulerServiceImpl.TITLE;

public class RingActivity extends AppCompatActivity {
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        final Intent intent = getIntent();
        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(intent.getStringExtra(TITLE));
        final Button dismiss = (Button) findViewById(R.id.activity_ring_dismiss);

        dismiss.setOnClickListener(v -> {
            Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
            getApplicationContext().stopService(intentService);
            finish();
        });
        animateClock();
    }

    private void animateClock() {
        final ImageView clock = (ImageView) findViewById(R.id.activity_ring_clock);
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }
}
