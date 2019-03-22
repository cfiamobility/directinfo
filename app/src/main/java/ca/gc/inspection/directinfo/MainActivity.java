package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    Context context;


    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static boolean check;
    long previous;

    SQLiteDatabase db;
    DirectInfoDbHelper dbHelper;


    static String newDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        dbHelper = new DirectInfoDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();


        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        previous = sharedPreferences.getLong("time", System.currentTimeMillis());
        check = sharedPreferences.getBoolean("checkboolean", true);
        long currentTime = System.currentTimeMillis();
        Log.d("TIME", "onCreate: " + currentTime);

        if (check) {

            if (hasInternetConnection(this)) {
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


        } else {
            if (System.currentTimeMillis() - previous >= 60000) {

                if (hasInternetConnection(this)) {
                    editor.putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).commit();
                    Log.d(TAG, "onCreate: TIME > 1 MIN");

                    grabDate();

                } else if (!hasInternetConnection(this) && !check) {
                    startActivity(new Intent(context, SearchActivity.class));
                    finish();
                }else {
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

            } else {
                Log.d(TAG, "onCreate: TIME < 1 MIN");
                startActivity(new Intent(context, SearchActivity.class));
                finish();
            }
        }
    }

    public static boolean hasInternetConnection(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public void grabDate() {
        String dateURL = "http://13.88.234.89:3000/users/update";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dateURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                        // grabbing the new date from the database
                        JSONObject jsonPart = response.getJSONObject(i);
                        newDate = jsonPart.getString("date");
                        Log.i("NEW DATE", newDate);

                        // transforming the new date into a date object
                        Date dateNew = simpleDateFormat.parse(newDate);

                        // grabbing the date from the local database
                        String dateQuery = "SELECT * FROM " + DirectInfo.TABLE_DATE_NAME + ";";
                        Cursor cursor = db.rawQuery(dateQuery, null);

                        if (cursor.moveToFirst()) {
                            cursor.moveToFirst();
                            String oldDate = cursor.getString(0);
                            Date date = null;
                            try {
                                // transforming the old date into a date object
                                date = simpleDateFormat.parse(oldDate);

                                // comparing the two dates
                                if (dateNew.after(date)) {
                                    // if the new date is later than the old date it will download the database
                                    startActivity(new Intent(context, DownloadDatabase.class));
                                    finish();
                                } else {
                                    // if the new date is before the old date or the same, it will go straight into the SearchActivity class
                                    startActivity(new Intent(context, SearchActivity.class));
                                    finish();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Server Error")
                        .setMessage("The Server is currently down.")
                        .setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(context, SearchActivity.class));
                                finish();
                            }
                        }).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequest);
        Log.i("RESPONSE", "DONE ALL GET REQUESTS");
    }


} // end of class
