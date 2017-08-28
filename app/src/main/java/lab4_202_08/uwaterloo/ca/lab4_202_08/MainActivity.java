package lab4_202_08.uwaterloo.ca.lab4_202_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    //LineGraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set relative layout here
        LinearLayout l = (LinearLayout) findViewById(R.id.Linear);
        l.setOrientation(LinearLayout.VERTICAL);

        //Set textviews and layout stuff here
        TextView title = new TextView(getApplicationContext());
        TextView accelText = new TextView(getApplicationContext());
        Button myButton = new Button (getApplicationContext());

        title.setText("TITLE TEST");
        title.setTextSize(40);

        myButton.setText("Make CSV");
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                FileWriter fw = null;
                PrintWriter pw = null;
                try {
                    fw = new FileWriter("filename.csv");
                    pw = new PrintWriter(fw);
                    //Write the coordinates to the csv here
                } catch (IOException e) {
                    Log.d("File Write Error: ", e.toString());
                } finally {
                    if (pw != null) {
                        //Probably redundant but include anyways to avoid further NPE
                        pw.flush();
                        pw.close();
                    }
                }
            }
        });

        //Add all views to linear layout to the linear layout
        l.addView(title);
        l.addView(accelText);
        l.addView(myButton);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accel = new AccelerometerEventListener(accelText);
        sensorManager.registerListener(accel, accelSensor, sensorManager.SENSOR_DELAY_GAME);

    }

}
class AccelerometerEventListener implements SensorEventListener {
    TextView output;
    //constructor
    public AccelerometerEventListener(TextView outputView) {
        output = outputView;
    }

    public void onAccuracyChanged(Sensor s, int i) {}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //put the stuff into the linked list and graph
            output.setText("CURRENT: X: " + String.valueOf(se.values[0]) + ", Y: " + String.valueOf(se.values[1])+ ", Z: " + String.valueOf(se.values[2]));
        }
    }
}