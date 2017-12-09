package lab4_202_08.uwaterloo.ca.lab4_202_08;

import android.hardware.Sensor;
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

        final Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final AccelerometerEventListener accelHandler = new AccelerometerEventListener(accelText, graph, title);
        sensorManager.registerListener(accelHandler, accelSensor, sensorManager.SENSOR_DELAY_GAME);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                PrintWriter pw = null;
                try {
                    //CHANGE CSV NAME BELOW to reflect which direction data it is!!
                    File file = new File(getExternalFilesDir(APP_NAME), "LEFT_DATA_FILTER_5.csv");
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
                        pw.flush();
                        pw.close();
                    }
                }
            }
        });
    }
}
