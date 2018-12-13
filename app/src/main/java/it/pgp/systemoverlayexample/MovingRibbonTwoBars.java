package it.pgp.systemoverlayexample;

import android.app.Service;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by pgp on 10/07/17
 */

public class MovingRibbonTwoBars implements View.OnTouchListener {
    LinearLayout oView;
    ProgressBar pb1,pb2;

    View topLeftView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;

    final WindowManager wm;

    MovingRibbonTwoBars(final Service service, final WindowManager wm) {
        this.wm = wm;

        LayoutInflater inflater = (LayoutInflater) service.getSystemService(LAYOUT_INFLATER_SERVICE);
        oView = (LinearLayout) inflater.inflate(R.layout.ribbon_two, null);

        pb1 = oView.findViewById(R.id.pb1);
        pb2 = oView.findViewById(R.id.pb2);

        pb1.setMax(100);
        pb1.setIndeterminate(false);
        pb1.setBackgroundColor(0x880000ff);

        pb2.setMax(100);
        pb2.setIndeterminate(false);
        pb2.setBackgroundColor(0x8800ff00);

        oView.setOnTouchListener(this);

        wm.addView(oView, ViewType.CONTAINER.getParams());

        topLeftView = new View(service);

        wm.addView(topLeftView,ViewType.ANCHOR.getParams());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            moving = false;

            int[] location = new int[2];
            v.getLocationOnScreen(location);

            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;

        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
            System.out.println("originalY="+originalYPos);

            float x = event.getRawX();
            float y = event.getRawY();

            WindowManager.LayoutParams params = (WindowManager.LayoutParams) v.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            wm.updateViewLayout(v, params);
            moving = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }

    void destroy() {
        if(oView!=null) wm.removeView(oView);
    }
}
