package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;

public class DownloadDatabase extends Activity {
    Context context;
    DirectInfoDbHelper dbHelper;
    SQLiteDatabase db;

    ProgressBar progressBar;

    ImageView logo;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
        db.execSQL(DirectInfo.SQL_DELETE_DATE);
        db.execSQL(DirectInfo.SQL_DATE_CREATE);

        // animation
        logo = findViewById(R.id.logo);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down);
        logo.setAnimation(animation);
    }

    private void jsonParse() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MainActivity.IP_ADDRESS + "users/geds/";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        db.beginTransaction();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonPart = response.getJSONObject(i);
                            ContentValues values = new ContentValues();
                            values.put(DirectInfo.COLUMN_NAME_SURNAME, jsonPart.getString(DirectInfo.COLUMN_NAME_SURNAME));
                            values.put(DirectInfo.COLUMN_NAME_GIVEN_NAME, jsonPart.getString(DirectInfo.COLUMN_NAME_GIVEN_NAME));
                            values.put(DirectInfo.COLUMN_NAME_INITIALS, jsonPart.getString(DirectInfo.COLUMN_NAME_INITIALS));

                            values.put(DirectInfo.COLUMN_NAME_PREFIX_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_PREFIX_EN));
                            values.put(DirectInfo.COLUMN_NAME_PREFIX_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_PREFIX_FR));

                            values.put(DirectInfo.COLUMN_NAME_SUFFIX_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_SUFFIX_EN));
                            values.put(DirectInfo.COLUMN_NAME_SUFFIX_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_SUFFIX_FR));

                            values.put(DirectInfo.COLUMN_NAME_TITLE_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_TITLE_EN));
                            values.put(DirectInfo.COLUMN_NAME_TITLE_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_TITLE_FR));

                            values.put(DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER));
                            values.put(DirectInfo.COLUMN_NAME_FAX_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_FAX_NUMBER));
                            values.put(DirectInfo.COLUMN_NAME_TDD_NUMBER, jsonPart.getString(DirectInfo.COLUMN_NAME_TDD_NUMBER));

                            values.put(DirectInfo.COLUMN_NAME_EMAIL, jsonPart.getString(DirectInfo.COLUMN_NAME_EMAIL));

                            values.put(DirectInfo.COLUMN_NAME_STREET_ADDRESS_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_STREET_ADDRESS_EN));
                            values.put(DirectInfo.COLUMN_NAME_STREET_ADDRESS_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_STREET_ADDRESS_FR));

                            values.put(DirectInfo.COLUMN_NAME_COUNTRY_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_COUNTRY_EN));
                            values.put(DirectInfo.COLUMN_NAME_COUNTRY_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_COUNTRY_FR));

                            values.put(DirectInfo.COLUMN_NAME_PROVINCE_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_PROVINCE_EN));
                            values.put(DirectInfo.COLUMN_NAME_PROVINCE_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_PROVINCE_FR));

                            values.put(DirectInfo.COLUMN_NAME_CITY_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_CITY_EN));
                            values.put(DirectInfo.COLUMN_NAME_CITY_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_CITY_FR));

                            values.put(DirectInfo.COLUMN_NAME_POSTAL_CODE, jsonPart.getString(DirectInfo.COLUMN_NAME_POSTAL_CODE));

                            values.put(DirectInfo.COLUMN_NAME_DEPARTMENT_ACRONYM, jsonPart.getString(DirectInfo.COLUMN_NAME_DEPARTMENT_ACRONYM));
                            values.put(DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_EN));
                            values.put(DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_FR));

                            values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_ACRONYM, jsonPart.getString(DirectInfo.COLUMN_NAME_ORGANIZATION_ACRONYM));
                            values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_EN, jsonPart.getString(DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_EN));
                            values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_FR, jsonPart.getString(DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_FR));
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

            }
        });

        queue.add(request);

        String dateUrl = MainActivity.IP_ADDRESS + "users/updategeds";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dateUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        // grab the date from the date column on the server table
                        JSONObject jsonDate = response.getJSONObject(i);
                        String serverDate = jsonDate.getString("date");

                        // insert the new date into the sqlite database table
                        ContentValues date = new ContentValues();
                        date.put(DirectInfo.COLUMN_NAME_DATE, serverDate);
                        db.insert(DirectInfo.TABLE_DATE_NAME, null, date);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
} // end of class
