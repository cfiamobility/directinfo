package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    Context context;


    SharedPreferences sharedPreferences;
    boolean check;
    long previous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        previous = sharedPreferences.getLong("time", System.currentTimeMillis());
        check = sharedPreferences.getBoolean("checkboolean", true);
        long currentTime = System.currentTimeMillis();
        Log.d("TIME", "onCreate: " + currentTime);


        if (check) {

            if (hasInternetConnection(this)) {
                editor.putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();
                Log.d(TAG, "onCreate: TIME FIRST RUN");
                startActivity(new Intent(context, DownloadDatabase.class));
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.checknetworkconnection)
                        .setMessage(R.string.internetisworking)
                        .setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }


        } /*else {
            if (System.currentTimeMillis() - previous >= 60000) {

                if (hasInternetConnection(this)) {
                    editor.putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();
                    Log.d(TAG, "onCreate: TIME > 1 MIN");
                    startActivity(new Intent(context, DownloadDatabase.class));
                    finish();
                } else if (!hasInternetConnection(this) && !check) {
                    startActivity(new Intent(context, SearchActivity.class));
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.checknetworkconnection)
                            .setMessage(R.string.internetisworking)
                            .setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }

            } */else {
                Log.d(TAG, "onCreate: TIME < 1 MIN");
                startActivity(new Intent(context, SearchActivity.class));
                finish();
            }
        //}

    }

    public static boolean hasInternetConnection(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }


} // end of class
