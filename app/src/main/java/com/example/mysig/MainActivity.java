package com.example.mysig;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private TextView t,t1,t2;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float x,y,z,last_x,last_y,last_z;
    private boolean isFirstValue = false;
    private float shakeThreshold = 3f,shakeThreshold1 = 5f, shakeThreshold2 = 8f;
    private MediaPlayer mMediaPlayer,mMediaPlayer1,mMediaPlayer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t= findViewById(R.id.T);
        t1= findViewById(R.id.T1);
        t2= findViewById(R.id.T2);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kalho);
        mMediaPlayer1 = MediaPlayer.create(getApplicationContext(), R.raw.tuhi);
        mMediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.terimitti);

    }
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener( this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener( this, mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        t.setText(event.values[0]+"m/s2");
        t1.setText(event.values[1]+"m/s2");
        t2.setText(event.values[2]+"m/s2");
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        if(isFirstValue) {
            float deltaX = Math.abs(last_x - x);
            float deltaY = Math.abs(last_y - y);
            float deltaZ = Math.abs(last_z - z);
            // If the values of acceleration have changed on at least two axes, then we assume that we are in
            // a shake motion
            if((deltaX > shakeThreshold && deltaY > shakeThreshold) && (deltaX < shakeThreshold1 && deltaY < shakeThreshold1)
                    || ((deltaX > shakeThreshold && deltaZ > shakeThreshold) && (deltaX < shakeThreshold1 && deltaZ < shakeThreshold1))
                    || ((deltaY > shakeThreshold && deltaZ > shakeThreshold) && (deltaY < shakeThreshold1 && deltaZ < shakeThreshold1))) {
                //Don't play sound, if it is already being played
                if(!mMediaPlayer.isPlaying()) {
                    //Play the sound, when Phone is Shaking
                    mMediaPlayer.start();
                    //mMediaPlayer1.stop();
                    //mMediaPlayer2.stop();
                }
            }
            else if ((deltaX > shakeThreshold1 && deltaY > shakeThreshold1) && (deltaX < shakeThreshold2 && deltaY < shakeThreshold2)
                    || ((deltaX > shakeThreshold1 && deltaZ > shakeThreshold1) && (deltaX < shakeThreshold2 && deltaZ < shakeThreshold2))
                    || ((deltaY > shakeThreshold1 && deltaZ > shakeThreshold2) && (deltaY < shakeThreshold2 && deltaZ < shakeThreshold2))) {
                if( (mMediaPlayer != null && mMediaPlayer.isPlaying()) || (mMediaPlayer2 != null && mMediaPlayer2.isPlaying()))
                //mMediaPlayer.stop();
                mMediaPlayer1.start();
                //mMediaPlayer2.stop();
            }

            else if((deltaX > shakeThreshold2 && deltaY > shakeThreshold2)
                    || (deltaX>shakeThreshold2 && deltaZ > shakeThreshold2)
                    || (deltaY >shakeThreshold2 && deltaZ > shakeThreshold2))
            {
                if((mMediaPlayer != null && mMediaPlayer.isPlaying()) || (mMediaPlayer1 != null && mMediaPlayer1.isPlaying()))
                //mMediaPlayer.stop();
                //mMediaPlayer1.stop();
                mMediaPlayer2.start();

            }
        }
        last_x = x;
        last_y = y;
        last_z = z;
        isFirstValue = true;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}