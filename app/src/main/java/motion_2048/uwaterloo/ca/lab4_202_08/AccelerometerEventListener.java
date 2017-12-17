package motion_2048.uwaterloo.ca.lab4_202_08;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Andrew on 2017-09-21.
 */

public class AccelerometerEventListener implements SensorEventListener {
    TextView directionView;
    private myFSM[] myFSMs = new myFSM[2]; //x, y/z
    private int myFSMCounter;
    private final int FSM_COUNTER_DEFAULT = 35; //number of readings to take for determining state
    private final int READINGS_SAVED = 100;
    public float[][] storedVals = new float[3][READINGS_SAVED];
    public float[] filteredReading = {0,0,0};
    private final float FILTER_CONSTANT = 6.0f;
    //constructor
    public AccelerometerEventListener(TextView dir) {
        directionView = dir;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < READINGS_SAVED; j++)
                storedVals[i][j] = 0.0f;

        myFSMs[0] = new myFSM();
        myFSMs[1] = new myFSM();

        myFSMCounter = FSM_COUNTER_DEFAULT;
    }

    public void onAccuracyChanged(Sensor s, int i) {}

    private void determineGesture() {
        myFSM.mySig[] sigs = new myFSM.mySig[2];

        for (int i = 0; i < 2; i++) {
            sigs[i] = myFSMs[i].getSignature();
            myFSMs[i].resetFSM();
        }
        if (sigs[0] == myFSM.mySig.SIG_A && sigs[1] == myFSM.mySig.SIG_X) {
            directionView.setText("RIGHT");
            Log.e("Gesture: ", "right");
        } else if (sigs[0] == myFSM.mySig.SIG_B && sigs[1] == myFSM.mySig.SIG_X) {
            directionView.setText("LEFT");
            Log.e("Gesture: ", "left");
        } else if (sigs[0] == myFSM.mySig.SIG_X && sigs[1] == myFSM.mySig.SIG_A) {
            directionView.setText("DOWN");
            Log.e("Gesture: ", "down");
        } else if (sigs[0] == myFSM.mySig.SIG_X && sigs[1] == myFSM.mySig.SIG_B) {
            directionView.setText("UP");
            Log.e("Gesture: ", "up");
        } else {
            directionView.setText("N/A");
            Log.e("Gesture: ", "break");
        }
    }
    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            //Filtering reading based on filter constant
            for (int i = 0; i < 3; i++) {
                filteredReading[i] += (se.values[i] - filteredReading[i]) / FILTER_CONSTANT;
            }

            storeValue(filteredReading[0], filteredReading[1], filteredReading[2]);

            if (myFSMCounter > 0) {

                boolean reductionFlag = false;
                /*
                for (int i = 0; i < 2; i++) {
                    myFSMs[i].supplyInput(storedVals[i][99]);
                    if (myFSMs[i].getState() != myFSM.FSMState.WAIT)
                        reductionFlag = true;
                }
                */
                myFSMs[0].supplyInput(storedVals[0][99]);
                myFSMs[1].supplyInput(storedVals[2][99]);
                if (myFSMs[0].getState() != myFSM.FSMState.WAIT || myFSMs[1].getState() != myFSM.FSMState.WAIT)
                    reductionFlag = true;

                if (reductionFlag)
                    myFSMCounter--;
            } else if (myFSMCounter <= 0) {
                determineGesture();
                myFSMCounter = FSM_COUNTER_DEFAULT;
            }

            if (myFSMs[0].isReady() && myFSMs[1].isReady())
                directionView.setTextColor(Color.GREEN);
            else
                directionView.setTextColor(Color.RED);
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