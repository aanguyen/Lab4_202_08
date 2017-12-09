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
    TextView direction;
    LineGraphView outGraph;
    private final int READINGS_SAVED = 100;
    public float[][] storedVals = new float[3][READINGS_SAVED];
    public float[] filteredReading = {0,0,0};
    final float FILTER_CONSTANT = 6;
    final float X_THRESH = 0;
    final float Y_THRESH = 0;
    //constructor
    public AccelerometerEventListener(TextView outputView, LineGraphView graph, TextView directionView) {
        output = outputView;
        outGraph = graph;
        direction = directionView;
    }

    public void onAccuracyChanged(Sensor s, int i) {}

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            //Filtering reading based on filter constant
            for (int i = 0; i < 3; i++) {
                filteredReading[i] += (se.values[i] - filteredReading[i]) / FILTER_CONSTANT;
            }

            storeValue(filteredReading[0], filteredReading[1], filteredReading[2]);
            outGraph.addPoint(filteredReading);
            output.setText(String.format("X:%.3f\nY:%.3f \nZ:%.3f", filteredReading[0], filteredReading[1], filteredReading[2]));
        }
    }
    private void storeValue(float x, float y, float z) {
        for (int i = 0; i < (READINGS_SAVED)-1; i++) {
            for (int j = 0; j < 3; j++) {
                storedVals[j][i] = storedVals[j][i+1];
            }
        }
        storedVals[0][READINGS_SAVED-1] = x;
        storedVals[1][READINGS_SAVED-1] = y;
        storedVals[2][READINGS_SAVED-1] = z;
    }
    public float[] getReadingAtIndex(int i) {
        return new float[]{storedVals[0][i], storedVals[1][i], storedVals[2][i]};
    }
}

//TODO: Once FSM done, create gameloop here with a timer