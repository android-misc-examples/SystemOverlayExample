package it.pgp.systemoverlayexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.empty);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Alert permission not granted, please enable" +
                        "\"Permit drawing over other apps\" option in app settings", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this,SettingsLauncherActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return;
            }
        }

//        Intent svc = new Intent(this, OverlayShowingServiceWithProgressBar.class);
//        Intent svc = new Intent(this, OverlayRibbonService.class);
        Intent svc = new Intent(this, OverlayRibbonServiceLite.class);
        startService(svc);
//        finish();
    }
}
