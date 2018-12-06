package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    Context context;

    Button downloadBtn;
//    Button searchBtn;
    SharedPreferences sharedPreferences;
    boolean check;
    long previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        downloadBtn = findViewById(R.id.downloadBtn);
//        searchBtn = findViewById(R.id.searchBtn);
        sharedPreferences = getApplication().getSharedPreferences("check", MODE_PRIVATE);


        previous = sharedPreferences.getLong("time", System.currentTimeMillis());

        check = sharedPreferences.getBoolean("checkboolean", true);
        long currentTime = System.currentTimeMillis();
        Log.d("TIME", "onCreate: " + currentTime);



        if (check) {
            startActivity(new Intent(context, DownloadDatabase.class));
            Log.d(TAG, "onCreate: TIME FIRST RUN");
            sharedPreferences.edit().putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();
            finish();


        } else {
            if (System.currentTimeMillis() - previous >= 60000) {
                startActivity(new Intent(context, DownloadDatabase.class));
                sharedPreferences.edit().putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();
                Log.d(TAG, "onCreate: TIME > 1 MIN");
                finish();
            } else {
                startActivity(new Intent(context, SearchActivity.class));
                finish();

                Log.d(TAG, "onCreate: TIME < 1 MIN");
            }

        }



    }
}
