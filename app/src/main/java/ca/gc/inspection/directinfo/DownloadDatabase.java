package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static ca.gc.inspection.directinfo.DirectInfoDbContract.*;

public class DownloadDatabase extends Activity {

    private static final String TAG = "DownloadDatabase";

    Context context;
    DirectInfoDbHelper dbHelper;
    SQLiteDatabase db;

    File gedsOpenData;
    String destination;
    String filePath;
    public TextView downloadingTv;

    ProgressBar progressBar;
    Button search;

    public static final String DI_CSV_FILE_URL = "http://directinfo.agr.gc.ca/directInfo/extracts/searchResults-2fjctouo2svup1rjsb3ijl38f5.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_database);

        context = getApplicationContext();
        dbHelper = new DirectInfoDbHelper(context);
        db = dbHelper.getWritableDatabase();
        db.execSQL(DirectInfo.SQL_DELETE_ENTRIES);
        db.execSQL(DirectInfo.SQL_CREATE_ENTRIES);

        downloadingTv = findViewById(R.id.downloadingTv);
        progressBar = findViewById(R.id.progressBar);

        search = findViewById(R.id.searchBtn2);

        gedsOpenData = new File(getApplicationContext().getFilesDir() + "/searchResults-2fjctouo2svup1rjsb3ijl38f5.csv" );
        filePath = gedsOpenData.getPath() + "/";
        destination = getApplicationContext().getFilesDir().getPath() + "/";


        downloadingTv.setText("DOWNLOADING....");

        DownloadGEDSZipFile downloadGEDSZipFile = new DownloadGEDSZipFile();
        downloadGEDSZipFile.execute(DI_CSV_FILE_URL);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });

    }

    public class DownloadGEDSZipFile extends AsyncTask<String , Integer, Void>{

        public Void doInBackground(String...urls) {

            try {
                URL u = new URL(urls[0]);
                URLConnection conn = u.openConnection();
                progressBar.setProgress(10);
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());
                progressBar.setProgress(25);

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();
                progressBar.setProgress(40);

                DataOutputStream fos = new DataOutputStream(new FileOutputStream(gedsOpenData));
                fos.write(buffer);
                fos.flush();
                fos.close();
                progressBar.setProgress(50);

            } catch(FileNotFoundException e) {

            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setProgress(65);
            hanldeDirectory(filePath);
            populateDatabase();
            progressBar.setProgress(100);
            downloadingTv.setText("DATABASE CREATED.");
        }
    }

    public void hanldeDirectory(String dir) {
        File f = new File(this.destination + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public void populateDatabase(){

        db.beginTransaction();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setNumberOfRowsToSkip(1); // skips the first row of the file as it contains the column names
        settings.setSkipEmptyLines(true);

        CsvParser parser = new CsvParser(settings);

        String[] row;
        parser.beginParsing(gedsOpenData);

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

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
} // end of class
