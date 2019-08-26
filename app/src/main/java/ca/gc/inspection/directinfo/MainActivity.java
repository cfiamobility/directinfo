package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;

public class MainActivity extends Activity {
    //Use this as a toggle between testing locally vs testing the backend-server
    //static String IP_ADDRESS = "http://[IP_ADDRESS]:3000/";
    static String IP_ADDRESS = "http://10.0.2.2:3000/";
    static SharedPreferences sharedPreferences;

    SQLiteDatabase db;
    DirectInfoDbHelper dbHelper;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        dbHelper = new DirectInfoDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(hasInternetConnection()){
            checkServer();
        } else if (firstRun() || freshUpdate()) {
            /* If it's the app's first run - it needs to download the database to be functional
               If the app was freshly updated to the new database, it needs to download
               the new database to be functional.*/
            alertAndClose(getResources().getString(R.string.internetErrorTitle),
                    getResources().getString(R.string.internetErrorMessage));
        } else {
            goToSearch();
        }
    }

    // to check if the server is running
    private void checkServer() {
        // Request a string response from the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Status", "Got a response from middle tier");
                checkBeforeDownload();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (firstRun() || freshUpdate()) {
                /* If it's the app's first run - it needs to download the database to be functional
                If the app was freshly updated to the new database, it needs to download
                the new database to be functional.*/
                    alertAndClose(getResources().getString(R.string.serverErrorTitle),
                            getResources().getString(R.string.serverErrorMessage));
                } else {
                    goToSearch();
                }
            }
        });
        // add the request to the request queue
        queue.add(stringRequest);
    }
    private void checkBeforeDownload() {
        Log.i("Status", "Inside checkBeforeDownload");
        if (firstRun() || freshUpdate()) {
            Log.i("Status", "First Run, Downloading database");
            sharedPreferences.edit().putBoolean("appFirstRun", false).apply();
            downloadDatabase();
        } else {
            checkUpdate();
        }
    }

    private void downloadDatabase() {
        startActivity(new Intent(this, DownloadDatabase.class));
        finish();
    }

    private void checkUpdate() {
        Log.i("Status", "Inside checkUpdate");

        final Long localUpdateDate = getApplicationContext().getSharedPreferences("ca.gc.inspection.directinfo", MODE_PRIVATE).getLong("LocalUpdateDate", 0);

        String serverDateURL = IP_ADDRESS + "users/updategeds";
        StringRequest serverUpdate = new StringRequest(Request.Method.GET, serverDateURL,
            new Response.Listener<String>() {
            @Override
            public void onResponse(String serverDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            try {
                serverDate = serverDate.replaceAll("\"", "");
                Date serverDateFormat = simpleDateFormat.parse(serverDate);
                Long serverUpdateDate = serverDateFormat.getTime();
                Log.i("Status", "Comparing " + serverUpdateDate + " and " + localUpdateDate);

                if (serverUpdateDate.equals(localUpdateDate)) {
                    goToSearch();
                } else {
                    downloadDatabase();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                goToSearch();
            }
        });
        queue.add(serverUpdate);
    }

    private boolean hasInternetConnection() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Network network = connectivityManager.getActiveNetwork();
        final NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    boolean firstRun(){
        return sharedPreferences.getBoolean("appFirstRun", true);
    }

    public static boolean freshUpdate(){
        return sharedPreferences.getBoolean("freshUpdate", true);
    }

    private void goToSearch() {
        startActivity(new Intent(this, SearchActivity.class));
        finish();
    }

    private void alertAndClose(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.DialogPositiveBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        Dialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
    }
} // end of class
