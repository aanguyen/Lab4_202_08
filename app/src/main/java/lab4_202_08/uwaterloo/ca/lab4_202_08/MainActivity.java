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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;

public class MainActivity extends AppCompatActivity {
    final String APP_NAME = "LAB4_202_08";
    LineGraphView graph;

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
        graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
        title.setText("lorem ipsum");
        title.setTextSize(40);

        myButton.setText("Make CSV");


        //Add all views to linear layout to the linear layout
        l.addView(title);
        l.addView(accelText);
        l.addView(myButton);
        l.addView(graph);
        graph.setVisibility(View.VISIBLE);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        final Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //final SensorEventListener accelHandler = new AccelerometerEventListener(accelText, graph);
        final AccelerometerEventListener accelHandler = new AccelerometerEventListener(accelText, graph);
        sensorManager.registerListener(accelHandler, accelSensor, sensorManager.SENSOR_DELAY_GAME);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                PrintWriter pw = null;
                try {
                    //CHANGE CSV NAME BELOW to reflect which direction data it is!!
                    File file = new File(getExternalFilesDir(APP_NAME), "DATA.csv");
                    pw = new PrintWriter(file);
                    for (int i = 0; i < 100; i++) {
                        float[] readingToPrint = accelHandler.getReadingAtIndex(i);
                        String strToPrint = readingToPrint[0] + "," + readingToPrint[1] + "," + readingToPrint[2];
                        pw.println(strToPrint);
                    }
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
    }
}
class AccelerometerEventListener implements SensorEventListener {
    TextView output;
    LineGraphView outGraph;
    public float[][] storedVals = new float[3][100];
    //constructor
    public AccelerometerEventListener(TextView outputView, LineGraphView graph) {
        output = outputView;
        outGraph = graph;
    }

    public void onAccuracyChanged(Sensor s, int i) {}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Filter point here
            float[] filteredReading = new float[3];


            storeValue(se.values[0], se.values[1], se.values[2]);
            outGraph.addPoint(se.values);
            output.setText(String.format("X:%.3f\nY:%.3f \nZ:%.3f", se.values[0], se.values[1], se.values[2]));
        }
    }
    private void storeValue(float x, float y, float z) {
        for (int i = 0; i < 99; i++) {
            storedVals[0][i] = storedVals[0][i+1];
            storedVals[1][i] = storedVals[1][i+1];
            storedVals[2][i] = storedVals[2][i+1];
        }
        storedVals[0][99] = x;
        storedVals[1][99] = y;
        storedVals[2][99] = z;
    }
    public float[] getReadingAtIndex(int i) {
        return new float[]{storedVals[0][i], storedVals[1][i], storedVals[2][i]};
    }
}
