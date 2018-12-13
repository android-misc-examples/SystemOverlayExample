package it.pgp.systemoverlayexample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/*
 * Web source: https://gist.github.com/bjoernQ/6975256
 */

public class OverlayShowingServiceWithProgressBar extends Service implements OnTouchListener, OnClickListener {

    private Button overlaidButton;
    private ProgressBar overlaidProgressBar;

    private View topLeftView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;



        overlaidButton = new Button(this);
        overlaidButton.setText("Overlay button");
        overlaidButton.setAlpha(0.0f);
        overlaidButton.setBackgroundColor(0x55fe4444);
        overlaidButton.setOnTouchListener(this);
        overlaidButton.setOnClickListener(this);


        wm.addView(overlaidButton, params);

        overlaidProgressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        overlaidProgressBar.setMax(100);
        overlaidProgressBar.setIndeterminate(false);
        overlaidProgressBar.setAlpha(0.0f);
        overlaidProgressBar.setBackgroundColor(0x55fe4444);
        overlaidProgressBar.setOnTouchListener(this);

        Thread progressIncrement = new ProgressThread(overlaidProgressBar,this);
        wm.addView(overlaidProgressBar,params);

        topLeftView = new View(this);
        LayoutParams topLeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
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
        if (overlaidButton != null) {
            wm.removeView(overlaidButton);
            wm.removeView(topLeftView);
            overlaidButton = null;
            topLeftView = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
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

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show();
    }

}