package it.pgp.systemoverlayexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

public class SettingsLauncherActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.empty);

        Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivityForResult(i,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Settings.canDrawOverlays(this)) {
            Intent i = new Intent(SettingsLauncherActivity.this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Alert permissions must be granted, exiting...", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }
}
