package mchehab.com.countdowntimerdemo;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button buttonCountDown;
    private Button buttonStopCountDown;
    private TextView textViewCountDown;

    private CountDownTimer countDownTimer;

    private long remainingTime;
    private final long INTERVAL = 1;

    private boolean didStartCountDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        buttonCountDown = findViewById(R.id.buttonCountDown);
        buttonStopCountDown = findViewById(R.id.buttonStopCountDown);
        textViewCountDown = findViewById(R.id.textViewCountDown);

        setButtonCountDownListener();
        setButtonStopCountDownListener();

        if(savedInstanceState != null){
            remainingTime = savedInstanceState.getLong("remainingTime");
            didStartCountDown = savedInstanceState.getBoolean("didStartCountDown");
            buttonStopCountDown.setText(savedInstanceState.getString("buttonStopCountDown"));
            textViewCountDown.setText(savedInstanceState.getString("textViewCountDown"));
            if(didStartCountDown){
                startCountDownTimer(remainingTime, INTERVAL);
                buttonStopCountDown.setEnabled(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("remainingTime", remainingTime);
        outState.putBoolean("didStartCountDown", didStartCountDown);
        outState.putString("buttonStopCountDown", buttonStopCountDown.getText().toString());
        outState.putString("textViewCountDown", textViewCountDown.getText().toString());
    }

    private void setButtonCountDownListener(){
        buttonCountDown.setOnClickListener(e -> {
            if(editText.getText().toString().length() == 0){
                return;
            }
            buttonStopCountDown.setEnabled(true);
            remainingTime = Integer.parseInt(editText.getText().toString());
            remainingTime *= 1000;
            if(countDownTimer != null){
                stopCountDownTimer();
            }
            startCountDownTimer(remainingTime, INTERVAL);
        });
    }

    private String formatNumber(long value){
        if(value < 10)
            return "0" + value;
        return value + "";
    }

    private void setButtonStopCountDownListener(){
        buttonStopCountDown.setOnClickListener(e -> {
            if(buttonStopCountDown.getText().toString().equalsIgnoreCase(getString(R.string.stopCountDown))){
                stopCountDownTimer();
                buttonStopCountDown.setText(getString(R.string.resumeCountDown));
            }else{
                startCountDownTimer(remainingTime, INTERVAL);
                buttonStopCountDown.setText(getString(R.string.stopCountDown));
            }
        });
    }

    private void startCountDownTimer(long duration, long interval){
        countDownTimer = new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                long seconds = millisUntilFinished/1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;

                if(minutes > 0)
                    seconds = seconds % 60;
                if(hours > 0)
                    minutes = minutes % 60;
                String time = formatNumber(hours) + ":" + formatNumber(minutes) + ":" +
                        formatNumber(seconds);
                textViewCountDown.setText(time);
            }

            @Override
            public void onFinish() {
                textViewCountDown.setText("00:00:00");
                flashAnimate(textViewCountDown, 500, 0, Animation.REVERSE, Animation.INFINITE);
            }
        };
        countDownTimer.start();
        didStartCountDown = true;
    }

    private void stopCountDownTimer(){
        countDownTimer.cancel();
        didStartCountDown = false;
    }

    private void flashAnimate(View view, long duration,int startOffset, int repeatMode, int
            repeatCount){
        Animation flashAnimation = new AlphaAnimation(0.0f, 1.0f);
        flashAnimation.setDuration(duration);
        flashAnimation.setStartOffset(startOffset);
        flashAnimation.setRepeatMode(repeatMode);
        flashAnimation.setRepeatCount(repeatCount);
        view.startAnimation(flashAnimation);
    }
}