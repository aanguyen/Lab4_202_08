package motion_2048.uwaterloo.ca.lab4_202_08;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //These resolutions specifically made for Galaxy S7 FHD setting
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_layout);
        rl.getLayoutParams().height = 1000;
        rl.getLayoutParams().width = 1000;
        rl.setBackgroundResource(R.drawable.gameboard);

        //Set textviews and layout stuff here
        TextView dirView = new TextView(getApplicationContext());
        dirView.setTextSize(40);

        //Add all views to layout
        rl.addView(dirView);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        final Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final AccelerometerEventListener accelHandler = new AccelerometerEventListener(dirView);
        sensorManager.registerListener(accelHandler, accelSensor, sensorManager.SENSOR_DELAY_GAME);

        Timer myGameLoop = new Timer();
        GameLoopTask myGameLoopTask = new GameLoopTask(this, getApplicationContext(), rl);

        myGameLoop.schedule(myGameLoopTask, 50, 50);
    }
}
