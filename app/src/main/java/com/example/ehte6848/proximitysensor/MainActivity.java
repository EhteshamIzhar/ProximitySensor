package com.example.ehte6848.proximitysensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.R.attr.type;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends Activity implements SensorEventListener{
    TextView proxText;
    TextView counterText;
    SensorManager sm;
    Sensor proxSensor;
    Ringtone r ;
    int counter;
    CountDownTimer count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        proxText = (TextView) findViewById(R.id.textView);
        counterText = (TextView) findViewById(R.id.textView2);

        //register sensor event listener
        sm.registerListener(this,proxSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.values[0] > -4 && event.values[0]< 4)
        {
            counter = 10;
            count = new CountDownTimer(10000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    counterText.setText(" " + millisUntilFinished/1000);

                }

                @Override
                public void onFinish() {

                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                        r = RingtoneManager.getRingtone(getApplicationContext(),notification);
                        r.play();


                }
            }.start();
            counterText.setVisibility(View.VISIBLE);
            proxText.setText("NEAR");

              /*  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                r = RingtoneManager.getRingtone(getApplicationContext(),notification);
                r.play();*/

        }
        else
        {    if (count!= null)
             count.cancel();
            proxText.setText("FAR");
           counterText.setVisibility(View.GONE);


            if(r!= null) {
                r.stop();
            }


        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
