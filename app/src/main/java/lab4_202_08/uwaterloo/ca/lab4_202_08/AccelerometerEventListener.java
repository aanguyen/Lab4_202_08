package lab4_202_08.uwaterloo.ca.lab4_202_08;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by Andrew on 2017-09-21.
 */

public class AccelerometerEventListener implements SensorEventListener {
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
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
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

