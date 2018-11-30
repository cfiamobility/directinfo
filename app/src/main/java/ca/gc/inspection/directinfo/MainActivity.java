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
    Button searchBtn;
    SharedPreferences sharedPreferences;
    boolean check;
    long previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        downloadBtn = findViewById(R.id.downloadBtn);
        searchBtn = findViewById(R.id.searchBtn);
        sharedPreferences = getApplication().getSharedPreferences("check", MODE_PRIVATE);


        previous = sharedPreferences.getLong("time", System.currentTimeMillis());

        check = sharedPreferences.getBoolean("checkboolean", true);
        long currentTime = System.currentTimeMillis();
        Log.d("TIME", "onCreate: " + currentTime);

        long previousUpdate = 01;
//        long minute = 60 * 1000;
//
//        Intent intent = new Intent(getApplicationContext(), DownloadDatabase.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime + minute, minute, pendingIntent);


        if (check) {

            // cyrrent time    1543522235540
            //if (currentTime - previousUpdate >= 60000){

            startActivity(new Intent(context, DownloadDatabase.class));
            Log.d(TAG, "onCreate: TIME FIRST RUN");


            previousUpdate = System.currentTimeMillis();
            long current = System.currentTimeMillis();

            sharedPreferences.edit().putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();

            //}
        } else {
            if (System.currentTimeMillis() - previous >= 60000) {
                startActivity(new Intent(context, DownloadDatabase.class));
                sharedPreferences.edit().putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();
                Log.d(TAG, "onCreate: TIME > 1 MIN");
            } else {


                startActivity(new Intent(context, SearchActivity.class));

                Log.d(TAG, "onCreate: TIME < 1 MIN");
            }

        }


//        downloadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(context, DownloadDatabase.class));
////                downloadBtn.setVisibility(View.INVISIBLE);
//            }
//        });

//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(context, SearchActivity.class));
//            }
//        });
//    }
    }
}
