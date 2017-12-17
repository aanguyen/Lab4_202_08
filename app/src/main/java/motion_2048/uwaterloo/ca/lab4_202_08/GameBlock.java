package motion_2048.uwaterloo.ca.lab4_202_08;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Andrew on 2017-12-17.
 */

public class GameBlock extends ImageView {
    private final float IMAGE_SCALE = 1.0f;
    //TODO: figure out max and min bounds
    private final float MAX_BOUND = 0;
    private final float MIN_BOUND = 0;
    private int curCoordX;
    private int curCoordY;
    private int targetX;
    private int targetY;
    private int velocity;
    private int value = 2;

    public GameBlock(Context c, int x, int y, int initValue) {
        super(c);
        this.setX(x);
        this.setY(y);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.value = initValue;
        curCoordX = x;
        curCoordY = y;
    }
}
