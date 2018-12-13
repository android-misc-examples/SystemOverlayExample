package it.pgp.systemoverlayexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.view.WindowManager;

public class OverlayRibbonServiceLite extends Service {
//    private MovingRibbon mr;
    private MovingRibbonTwoBars mr;

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mr = new MovingRibbon(this,(WindowManager)getSystemService(WINDOW_SERVICE));
        mr = new MovingRibbonTwoBars(this,(WindowManager)getSystemService(WINDOW_SERVICE));
//        Thread progressIncrement = new ProgressThread(mr.pb,this);
        Thread progressIncrement = new DualProgressThread(mr.pb1,mr.pb2,this);
        progressIncrement.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mr.destroy();
    }
}
