package motion_2048.uwaterloo.ca.lab4_202_08;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set relative layout here
        final RelativeLayout r = (RelativeLayout) findViewById(R.id.relative_layout);

        //Set textviews and layout stuff here
        TextView dirView = new TextView(getApplicationContext());
        dirView.setText("dir");
        dirView.setTextSize(40);

        //Add all views to layout
        r.addView(dirView);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        final Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final AccelerometerEventListener accelHandler = new AccelerometerEventListener(dirView);
        sensorManager.registerListener(accelHandler, accelSensor, sensorManager.SENSOR_DELAY_GAME);
    }
}
