package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;

public class DownloadDatabase extends Activity {

    private static final String TAG = "DownloadDatabase";

    Context context;
    DirectInfoDbHelper dbHelper;
    SQLiteDatabase db;
    File gedsOpenData;
    ProgressBar progressBar;

    ImageView logo;
    Animation animation;
    boolean checkUrl;

    RequestQueue mQueue;

    /*
     * The link for the download of the DirectInfo database can be found by going to the directInfo site at bec
     * http://directinfo.agr.gc.ca/directInfo/eng/index.php?fuseaction=agriInfo.orgUnit&dn=OU=CFIA-ACIA,o=gc,c=ca
     * then clicking on customizable list from the 'Lists' submenu on the left hand side. After that scroll down to the
     * 'Search Results' section and check the following boxes:
     *
     *          Under the Heading 'Employee Details' :
     *              Official given name, Surname, English prefix,  English title,
     *              Telephone number,  Mobile/cellular, Email
     *          Under the Heading 'Employee Postal Address':
     *              Street number, Street name, Building unit type, Building unit,
     *              P.O. Box, City, Province, Country, Postal code
     *          Under the Heading 'Employee Postal Address':
     *               Building name, Floor, Room, Street number, Street name
     *               Building unit type, Building unit, City, Province
     *
     * Also note that the link seems to change every month or so, even though it says on the site that it does not.
     * */

    //    final String DI_CSV_FILE_URL = "http://directinfo.agr.gc.ca/directInfo/extracts/searchResults-80a9p09u60ajoql7d0jg6iluu6.csv";
//    final String DI_CSV_FILE_URL = "http://directinfo.agr.gc.ca/directInfo/extracts/searchResults-7femegq9c7lbo91o7fpf4e06i4.csv";
    final String DI_CSV_FILE_URL = "http://directinfo.agr.gc.ca/directInfo/extracts/searchResults-veqkh5p8lbf8ckln30fhcf5357.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mQueue = Volley.newRequestQueue(this);
        jsonParse();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_database);
        ThreeBounce wave = new ThreeBounce();
        progressBar = findViewById(R.id.SpinKit);
        progressBar.setIndeterminateDrawable(wave);
        context = getApplicationContext();
        dbHelper = new DirectInfoDbHelper(context);
        db = dbHelper.getWritableDatabase();
        db.execSQL(DirectInfo.SQL_DELETE_ENTRIES);
        db.execSQL(DirectInfo.SQL_CREATE_ENTRIES);

        // animation
        logo = findViewById(R.id.logo);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down);
        logo.setAnimation(animation);

//        DownloadGEDSZipFile downloadGEDSZipFile = new DownloadGEDSZipFile();
//        downloadGEDSZipFile.execute(DI_CSV_FILE_URL);

    }

    private void jsonParse() {
        String url = "http://13.88.234.89:3000/user";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            db.beginTransaction();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonPart = response.getJSONObject(i);
                                ContentValues values = new ContentValues();
                                values.put(DirectInfo.COLUMN_NAME_FIRST_NAME, jsonPart.getString(DirectInfo.COLUMN_NAME_FIRST_NAME));
                                values.put(DirectInfo.COLUMN_NAME_LAST_NAME, jsonPart.getString(DirectInfo.COLUMN_NAME_LAST_NAME));
                                values.put(DirectInfo.COLUMN_NAME_PREFIX_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_PREFIX_EN));
                                values.put(DirectInfo.COLUMN_NAME_TITLE_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_TITLE_EN));
                                values.put(DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER));
                                values.put(DirectInfo.COLUMN_NAME_MOBILE_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_MOBILE_NUMBER));
                                values.put(DirectInfo.COLUMN_NAME_EMAIL, jsonPart.getString(DirectInfo.COLUMN_NAME_EMAIL));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_STREET_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_STREET_NUMBER));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_STREET_NAME, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_STREET_NAME));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_ID, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_ID));
                                values.put(DirectInfo.COLUMN_NAME_PO_BOX_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_PO_BOX_EN));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_CITY_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_CITY_EN));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_PROVINCE_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_PROVINCE_EN));
                                values.put(DirectInfo.COLUMN_NAME_C, jsonPart.getString(DirectInfo.COLUMN_NAME_C));
                                values.put(DirectInfo.COLUMN_NAME_POSTAL_CODE, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_CODE));
                                values.put(DirectInfo.COLUMN_NAME_BUILDING_NAME_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_BUILDING_NAME_EN));
                                values.put(DirectInfo.COLUMN_NAME_FLOOR, jsonPart.getString(DirectInfo.COLUMN_NAME_FLOOR));
                                values.put(DirectInfo.COLUMN_NAME_ROOM, jsonPart.getString(DirectInfo.COLUMN_NAME_ROOM));
                                values.put(DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER));
                                values.put(DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME, jsonPart.getString(DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME));
                                values.put(DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE, jsonPart.getString(DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE));
                                values.put(DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID, jsonPart.getString(DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID));
                                values.put(DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN));
                                values.put(DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN));
                                db.insert(DirectInfo.TABLE_NAME, null, values);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        db.setTransactionSuccessful();
                        db.endTransaction();
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
} // end of class
