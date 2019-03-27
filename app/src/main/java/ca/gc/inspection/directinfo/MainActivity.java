package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
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

    private boolean serverCheck;
    private String serverDate;
    private String sqliteDate;

    private RequestQueue queue;
    private SimpleDateFormat simpleDateFormat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        queue = Volley.newRequestQueue(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Log.i("CHECKING THE SERVER", "CHECKING");
        checkServer();

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
    }

    public void checkUpdate(Boolean serverStatus) {
        if (hasInternetConnection(this)) {
            // HAS INTERNET CONNECTION

            if (serverStatus) {
                // server is running
                Log.i("SERVER STATUS", "server is running");

                // grab date from sqlite
                String dateQuery = "SELECT * FROM " + DirectInfo.TABLE_DATE_NAME + ";";
                Cursor cursor = db.rawQuery(dateQuery, null);
                if (cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    sqliteDate = cursor.getString(0);


                    // grab date from server
                    String serverDateUrl = "http://10.0.2.2:3000/users/update";
                    JsonArrayRequest sqlDateArray = new JsonArrayRequest(Request.Method.GET, serverDateUrl, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonDate = response.getJSONObject(i);
                                    serverDate = jsonDate.getString("date");

                                    // change both of the dates into simple date format -> will allow us to compare them
                                    Date sqliteDateFormat = simpleDateFormat.parse(sqliteDate);
                                    Date serverDateFormat = simpleDateFormat.parse(serverDate);

                                    // comparing the two dates
                                    if (sqliteDateFormat.equals(serverDateFormat)) {
                                        // THE DATES ARE THE SAME
                                        Log.i("DATES", "The dates are same");
                                        startActivity(new Intent(context, SearchActivity.class));
                                        finish();
                                    } else {
                                        // THE DATES ARE DIFFERENT
                                        Log.i("DATES", "The dates are different");
                                        startActivity(new Intent(context, DownloadDatabase.class));
                                        finish();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(sqlDateArray);

                } else {
                    // FIRST RUN
                    editor.putBoolean("checkboolean", false).putLong("time", System.currentTimeMillis()).apply(); // changes the boolean so it is no longer "first run"
                    startActivity(new Intent(context, DownloadDatabase.class));
                    finish();
                }



            } else {
                // server is NOT running
                Log.i("SERVER STATUS", "server is NOT running");

                if (check) {
                    // FIRST RUN
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Server Error")
                            .setCancelable(false)
                            .setMessage("The Server is currently down.")
                            .setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });

                    Dialog dialog = builder.show();
                    dialog.setCanceledOnTouchOutside(false);

                } else {
                    // NOT FIRST RUN
                    startActivity(new Intent(context, SearchActivity.class));
                    finish();
                }

            }

        } else {
            // NO INTERNET CONNECTION
            if (check) {
                // FIRST RUN
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.checknetworkconnection)
                        .setMessage(R.string.internetisworking)
                        .setCancelable(false)
                        .setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                Dialog dialog = builder.show();
                dialog.setCanceledOnTouchOutside(false);

            } else {
                // NOT FIRST RUN
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

    // to check if the server is running
    public void checkServer() {

        // instantiate the RequestQueue
        String url = "http://10.0.2.2:3000";

        // Request a string response from the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // THE SERVER IS ON
                Log.i("BOOLEAN", "The server is on");
                checkUpdate(true);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // THE SERVER IS OFF
                Log.i("BOOLEAN", "The server is off");
                checkUpdate(false);
            }
        });

        // add the request to the request queue
        queue.add(stringRequest);

    }

} // end of class
