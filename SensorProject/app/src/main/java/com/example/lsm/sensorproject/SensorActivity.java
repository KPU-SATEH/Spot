package com.example.lsm.sensorproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    TextView Accel,Direction,Judge;
    private SensorManager sensorManager;
    private Sensor Accel_sensor,Direction_sensor;

    // 실수의 출력 자리수를 지정하는 포맷 객체
    DecimalFormat m_format;

    float[] m_gravity_data = new float[3];
    float[] m_accel_data = new float[3];
    float[] m_direction_data = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);



        // 포맷 객체를 생성한다.
        m_format = new DecimalFormat();
        // 소수점 두자리까지 출력될 수 있는 형식을 지정한다.
        m_format.applyLocalizedPattern("0.##");


        Accel=(TextView)findViewById(R.id.Accel);
        Direction=(TextView)findViewById(R.id.Direction);
        Judge=(TextView)findViewById(R.id.Judge);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Accel_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Direction_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,Direction_sensor,Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this,Accel_sensor,sensorManager.SENSOR_DELAY_UI);
    }
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        String str_Direction;
        String str_Accel;
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ORIENTATION){
            m_direction_data[0]=sensorEvent.values[0];
            m_direction_data[1]=sensorEvent.values[1];
            m_direction_data[2]=sensorEvent.values[2];
            str_Direction="방향센서값\n\n방위각 : "+m_format.format(m_direction_data[0])+"\n피치 : "+ m_format.format(m_direction_data[1])+"\n방위각 : "+ m_format.format(m_direction_data[2]);
            Direction.setText(str_Direction);
        }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            final float alpha = (float)0.8;

            // 저속 통과 필터를 적용한 중력 데이터를 구한다.
            // 직전 중력 값에 alpha 를 곱하고, 현재 데이터에 0.2 를 곱하여 두 값을 더한다.
            m_gravity_data[0] = alpha * m_gravity_data[0] + (1 - alpha) * sensorEvent.values[0];
            m_gravity_data[1] = alpha * m_gravity_data[1] + (1 - alpha) * sensorEvent.values[1];
            m_gravity_data[2] = alpha * m_gravity_data[2] + (1 - alpha) * sensorEvent.values[2];

            // 현재 값에 중력 데이터를 빼서 가속도를 계산한다.
            m_accel_data[0] = sensorEvent.values[0] - m_gravity_data[0];
            m_accel_data[1] = sensorEvent.values[1] - m_gravity_data[1];
            m_accel_data[2] = sensorEvent.values[2] - m_gravity_data[2];

            str_Accel = "x : " + m_format.format(m_accel_data[0]) + "\n y : " + m_format.format(m_accel_data[1]) +
                    "\n z : " + m_format.format(m_accel_data[2]);

            Accel.setText(str_Accel);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
