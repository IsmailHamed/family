package com.android.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountDownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        final TextView textView = (TextView) findViewById(R.id.count_down_text);
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final long[] pattern = {0, 500, 1000};
        final Button button = (Button) findViewById(R.id.cancel_button);

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000);
                v.vibrate(pattern, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onFinish();
                        Intent intent=new Intent(CountDownActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            public void onFinish() {
                v.cancel();
            }
        }.start();

    }

}
