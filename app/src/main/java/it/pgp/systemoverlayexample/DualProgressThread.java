package it.pgp.systemoverlayexample;

import android.app.Service;
import android.widget.ProgressBar;

/**
 * Created by pgp on 29/06/17
 */

public class DualProgressThread extends Thread {

    ProgressBar pb1,pb2;
    Service ribbonService;
    public DualProgressThread(ProgressBar pb1, ProgressBar pb2, Service ribbonService) {
        this.pb1 = pb1;
        this.pb2 = pb2;
        this.ribbonService = ribbonService;
        setDaemon(true);
    }

    @Override
    public void run() {
        for (int i=0;i<=100;i+=5) {
            pb1.setProgress(i);
            for (int j=0;j<=100;j+=10) {
                pb2.setProgress(j);
                try {sleep(20);} catch(InterruptedException ignored) {}
            }
        }

        ribbonService.stopForeground(true);
        ribbonService.stopSelf();
    }
}
