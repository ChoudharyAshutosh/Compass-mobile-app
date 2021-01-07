package com.example.ashutosh.compass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity implements SensorEventListener{
float azimuth_angle;
    private SensorManager compass;
    Sensor ac;
    Sensor ma;
    TextView ttt;
    ImageView img;
    private float c_d=0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass=(SensorManager)getSystemService(SENSOR_SERVICE);
        ac=compass.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        ma=compass.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }
    protected void onResume()
    {
        super.onResume();
        compass.registerListener(this, ac, SensorManager.SENSOR_DELAY_UI);
        compass.registerListener(this, ma, SensorManager.SENSOR_DELAY_UI);
    }
    protected void onPause()
    {
        super.onPause();
        compass.unregisterListener(this);
    }
    float [] acc;
    float[] mag;


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
    ttt=(TextView)findViewById(R.id.tt);
        img=(ImageView)findViewById(R.id.im);
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            acc= sensorEvent.values;
        if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
            mag=sensorEvent.values;
        if(acc!=null && mag!=null)
        {
            float R[]=new float[9];
            float I[]=new float[9];
            boolean s_r=SensorManager.getRotationMatrix(R, I, acc, mag);
            if(s_r)
            {float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            azimuth_angle=orientation[0];
            float d=((azimuth_angle*180f)/3.14f);
            int deg=Math.round(d);
            ttt.setText(Integer.toString(deg) + (char) 0x00B0 + "to absolute north.");
            RotateAnimation rotate= new RotateAnimation(c_d, -deg, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(100);
            rotate.setFillAfter(true);
            img.startAnimation(rotate);
            c_d=-deg;
        }}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
