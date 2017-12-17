package motion_2048.uwaterloo.ca.lab4_202_08;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.TimerTask;

/**
 * Created by Andrew on 2017-12-17.
 */

public class GameLoopTask extends TimerTask {
    private Activity myActivity;
    private Context myContext;
    private RelativeLayout myRL;
    private long time = System.currentTimeMillis();

    public GameLoopTask (Activity a, Context c, RelativeLayout l) {
        this.myActivity = a;
        this.myContext = c;
        this.myRL = l;
    }
    public void run() {
        myActivity.runOnUiThread(
          new Runnable() {
              public void run() {
                  long curTime = System.currentTimeMillis() - time;
                  Log.d("Time:", String.valueOf(curTime));
              }
          }
        );
    }
}
