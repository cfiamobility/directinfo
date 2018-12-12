package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


    final String DI_CSV_FILE_URL = "http://directinfo.agr.gc.ca/directInfo/extracts/searchResults-80a9p09u60ajoql7d0jg6iluu6.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_database);
        ThreeBounce wave = new ThreeBounce();
        progressBar = findViewById(R.id.SpinKit);
        progressBar.setIndeterminateDrawable(wave);
        context = getApplicationContext();
//        dbHelper = new DirectInfoDbHelper(context);
//        db = dbHelper.getWritableDatabase();
//        db.execSQL(DirectInfo.SQL_DELETE_ENTRIES);
//        db.execSQL(DirectInfo.SQL_CREATE_ENTRIES);

        // animation
        logo = findViewById(R.id.logo);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down);
        logo.setAnimation(animation);

        DownloadGEDSZipFile downloadGEDSZipFile = new DownloadGEDSZipFile();
        downloadGEDSZipFile.execute(DI_CSV_FILE_URL);

    }


    private class DownloadGEDSZipFile extends AsyncTask<String, Integer, Void> {

        public Void doInBackground(String... urls) {
            Log.d(TAG, "doInBackground: come into doInBackGround");
            try {

                dbHelper = new DirectInfoDbHelper(context);
                db = dbHelper.getWritableDatabase();
                db.execSQL(DirectInfo.SQL_DELETE_ENTRIES);
                db.execSQL(DirectInfo.SQL_CREATE_ENTRIES);

                gedsOpenData = new File(getApplicationContext().getFilesDir() + "/directinfo.csv");
                gedsOpenData.createNewFile();

                URL u = new URL(urls[0]);
                URLConnection conn = u.openConnection();

                int contentLength = conn.getContentLength();
                DataInputStream stream = new DataInputStream(u.openStream());


                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                DataOutputStream fos = new DataOutputStream(new FileOutputStream(gedsOpenData));
                fos.write(buffer);
                fos.flush();
                fos.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            db.beginTransaction();

            CsvParserSettings settings = new CsvParserSettings();
            settings.setNumberOfRowsToSkip(1); // skips the first row of the file as it contains the column names
            settings.setSkipEmptyLines(true);

            CsvParser parser = new CsvParser(settings);

        /*
        The downloaded CSV file is encoded in ANSI which did not process properly with the parser used.
        As there are names and addresses with accents in the CSV file which do not show up properly under the default
        encoding, The encoding was changed to Cp1252 as per the information found on the java documentation from the
        Oracle website @ https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
        This encoding now shows the names and addresses with the proper format.
        */
            try {
                parser.beginParsing(gedsOpenData, "Cp1252");
                String[] row;
                while ((row = parser.parseNext()) != null) {

                    ContentValues values = new ContentValues();
                    values.put(DirectInfo.COLUMN_NAME_FIRST_NAME, row[0]);
                    values.put(DirectInfo.COLUMN_NAME_LAST_NAME, row[1]);
                    values.put(DirectInfo.COLUMN_NAME_PREFIX_EN, row[2]);
                    values.put(DirectInfo.COLUMN_NAME_TITLE_EN, row[3]);
                    values.put(DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER, row[4]);
                    values.put(DirectInfo.COLUMN_NAME_MOBILE_NUMBER, row[5]);
                    values.put(DirectInfo.COLUMN_NAME_EMAIL, row[6]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_STREET_NUMBER, row[7]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_STREET_NAME, row[8]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE, row[9]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_ID, row[10]);
                    values.put(DirectInfo.COLUMN_NAME_PO_BOX_EN, row[11]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_CITY_EN, row[12]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_PROVINCE_EN, row[13]);
                    values.put(DirectInfo.COLUMN_NAME_C, row[14]);
                    values.put(DirectInfo.COLUMN_NAME_POSTAL_CODE, row[15]);
                    values.put(DirectInfo.COLUMN_NAME_BUILDING_NAME_EN, row[16]);
                    values.put(DirectInfo.COLUMN_NAME_FLOOR, row[17]);
                    values.put(DirectInfo.COLUMN_NAME_ROOM, row[18]);
                    values.put(DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER, row[19]);
                    values.put(DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME, row[20]);
                    values.put(DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE, row[21]);
                    values.put(DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID, row[22]);
                    values.put(DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN, row[23]);
                    values.put(DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN, row[24]);
                    db.insert(DirectInfo.TABLE_NAME, null, values);
                }

            } catch (IllegalArgumentException e) {
                Log.d(TAG, "doInBackground: ERROR: " + e.getMessage());
            } finally {
                parser.stopParsing();
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
} // end of class
