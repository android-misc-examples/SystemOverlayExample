package it.pgp.systemoverlayexample;

import android.app.Service;
import android.widget.ProgressBar;

/**
 * Created by pgp on 29/06/17
 */

public class ProgressThread extends Thread {

    ProgressBar pb;
    Service ribbonService;
    public ProgressThread(ProgressBar pb, Service ribbonService) {
        this.pb = pb;
        this.ribbonService = ribbonService;
    }

    @Override
    public void run() {
        for(int i=0;i<=100;i++) {
            pb.setProgress(i);
//            Log.d(this.getClass().getName(),"Current progress: "+i);
            try {sleep(100);} catch(InterruptedException ignored) {}
        }
        ribbonService.stopForeground(true);
        ribbonService.stopSelf();
    }
}
