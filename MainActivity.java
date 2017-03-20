package com.example.lsm.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    SensorManager m_sensor_manager;
    Sensor m_accelerometer;

    // 출력용 텍스트뷰
    TextView m_gravity_view;
    TextView m_accel_view;

    // 실수의 출력 자리수를 지정하는 포맷 객체
    DecimalFormat m_format;

    // 데이터를 저장할 변수들
    float[] m_gravity_data = new float[3];
    float[] m_accel_data = new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 포맷 객체를 생성한다.
        m_format = new DecimalFormat();
        // 소수점 두자리까지 출력될 수 있는 형식을 지정한다.
        m_format.applyLocalizedPattern("0.##");

        // 출력용 텍스트뷰를 얻는다.
        m_gravity_view = (TextView)findViewById(R.id.Gravity_Accel);
        m_accel_view = (TextView)findViewById(R.id.Current_Accel);

        // 시스템서비스로부터 SensorManager 객체를 얻는다.
        m_sensor_manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // SensorManager 를 이용해서 가속센서 객체를 얻는다.
        m_accelerometer = m_sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    protected void onResume()
    {
        super.onResume();
        // 센서 값을 이 컨텍스트에서 받아볼 수 있도록 리스너를 등록한다.
        m_sensor_manager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    // 해당 액티비티가 포커스를 잃으면 가속 데이터를 얻어도 소용이 없으므로 리스너를 해제한다.
    protected void onPause()
    {
        super.onPause();
        // 센서 값이 필요하지 않는 시점에 리스너를 해제해준다.
        m_sensor_manager.unregisterListener(this);
    }

    // 정확도 변경시 호출되는 메소드. 센서의 경우 걋?호출되지 않는다.
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    // 측정한 값을 전달해주는 메소드.
    public void onSensorChanged(SensorEvent event)
    {
        // 가속 센서가 전달한 데이터인 경우
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // 중력 데이터를 구하기 위해서 저속 통과 필터를 적용할 때 사용하는 비율 데이터.
            // t : 저속 통과 필터의 시정수. 시정수란 센서가 가속도의 63% 를 인지하는데 걸리는 시간
            // dT : 이벤트 전송율 혹은 이벤트 전송속도.
            // alpha = t / (t + Dt)
            final float alpha = (float)0.8;

            // 저속 통과 필터를 적용한 중력 데이터를 구한다.
            // 직전 중력 값에 alpha 를 곱하고, 현재 데이터에 0.2 를 곱하여 두 값을 더한다.
            m_gravity_data[0] = alpha * m_gravity_data[0] + (1 - alpha) * event.values[0];
            m_gravity_data[1] = alpha * m_gravity_data[1] + (1 - alpha) * event.values[1];
            m_gravity_data[2] = alpha * m_gravity_data[2] + (1 - alpha) * event.values[2];

            // 현재 값에 중력 데이터를 빼서 가속도를 계산한다.
            m_accel_data[0] = event.values[0] - m_gravity_data[0];
            m_accel_data[1] = event.values[1] - m_gravity_data[1];
            m_accel_data[2] = event.values[2] - m_gravity_data[2];

            String str;
            // 문자열을 구성하여 텍스트뷰에 출력한다.
            str = "x : " + m_format.format(m_gravity_data[0]) + ", y : " + m_format.format(m_gravity_data[1]) +
                    ", z : " + m_format.format(m_gravity_data[2]);
            m_gravity_view.setText(str);

            // 문자열을 구성하여 텍스트뷰에 출력한다.
            str = "x : " + m_format.format(m_accel_data[0]) + ", y : " + m_format.format(m_accel_data[1]) +
                    ", z : " + m_format.format(m_accel_data[2]);
            m_accel_view.setText(str);
        }
    }
}
