package it.pgp.systemoverlayexample;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class OverlayRibbonService extends Service implements View.OnTouchListener {
    LinearLayout oView;

    private View topLeftView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        oView = new LinearLayout(this);
        oView.setBackgroundColor(0x88ff0000); // The translucent red color
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                100,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        // add button (not clickable since parent layout is not clickable as well)
        Button innerButton = new Button(this);
        innerButton.setText("OK");
        oView.addView(innerButton);

        ProgressBar innerProgressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        innerProgressBar.setMax(100);
        innerProgressBar.setIndeterminate(false);
        innerProgressBar.setLayoutParams(params);
        oView.addView(innerProgressBar);
        Thread progressIncrement = new ProgressThread(innerProgressBar,this);

        oView.setOnTouchListener(this);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(oView, params);

        topLeftView = new View(this);
        LayoutParams topLeftParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.START | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        progressIncrement.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(oView!=null){
            wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(oView);
        }
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

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
            System.out.println("originalY="+originalYPos);

            float x = event.getRawX();
            float y = event.getRawY();

            LayoutParams params = (LayoutParams) v.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            wm.updateViewLayout(v, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }
}
