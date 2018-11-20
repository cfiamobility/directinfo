package ca.gc.inspection.directinfo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    File getGedsOpenDataUnzipped;
    String destination;
    String filePath;
    String unzippedLocation;
    public TextView downloadingTv;

    ProgressBar progressBar;
    Button search;

    public static final String GEDS_ZIP_FILE_URL = "https://api.geds-sage.gc.ca/GEDS20/dist/opendata/gedsOpenData.zip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_database);

        context = getApplicationContext();
        dbHelper = new DirectInfoDbHelper(context);
        db = dbHelper.getWritableDatabase();

        downloadingTv = findViewById(R.id.downloadingTv);
        progressBar = findViewById(R.id.progressBar);

        search = findViewById(R.id.searchBtn2);

//        gedsOpenData = new File(Environment.getDataDirectory() + "/gedsCSV.zip");
        gedsOpenData = new File(getApplicationContext().getFilesDir() + "/gedsOpenData.zip" );
//        gedsOpenData = new File(context.getAssets() + "/gedsCSV.zip");
        filePath = gedsOpenData.getPath() + "/";
        destination = getApplicationContext().getFilesDir().getPath() + "/";


        downloadingTv.setText("DOWNLOADING....");

        DownloadGEDSZipFile downloadGEDSZipFile = new DownloadGEDSZipFile();
        downloadGEDSZipFile.execute(GEDS_ZIP_FILE_URL);
//        downloadFile(GEDS_ZIP_FILE_URL, gedsOpenData);

//        hanldeDirectory(destination);
//        unzip();


//        getGedsOpenDataUnzipped = new File(unzippedLocation);

//        populateDatabase();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });

    }

    public class DownloadGEDSZipFile extends AsyncTask<String , Integer, Void>{

        int count = 0;

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
            unzip();
            progressBar.setProgress(65);
            getGedsOpenDataUnzipped = new File(unzippedLocation);
            populateDatabase();
            progressBar.setProgress(100);
            downloadingTv.setText("DATABASE CREATED.");
        }
    }

    public void unzip() {

        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                Log.d("Unzip", "Unzipping " + zEntry.getName() + " at "
                        + destination);
                unzippedLocation = destination + "/" + zEntry.getName();

                if (zEntry.isDirectory()) {
                    hanldeDirectory(zEntry.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(
                            this.destination + "/" + zEntry.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                   /* byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zipStream.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }*/
                    byte[] buffer = new byte[205000000];
                    int read = 0;
                    while ((read = zipStream.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }

                    zipStream.closeEntry();
                    bufout.close();
                    fout.close();
                }
            }
            zipStream.close();
            Log.d("Unzip", "Unzipping complete. path :  " + destination);
        } catch (Exception e) {
            Log.d("Unzip", "Unzipping failed" + e.getMessage());

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
        settings.setSkipEmptyLines(true);

        CsvParser parser = new CsvParser(settings);

        String[] row;
        parser.beginParsing(getGedsOpenDataUnzipped);

            while ((row = parser.parseNext()) != null) {

                ContentValues values = new ContentValues();
                values.put(DirectInfo.COLUMN_NAME_SURNAME, row[0]);
                values.put(DirectInfo.COLUMN_NAME_GIVEN_NAME, row[1]);
                values.put(DirectInfo.COLUMN_NAME_INITIALS, row[2]);
                values.put(DirectInfo.COLUMN_NAME_PREFIX_EN, row[3]);
                values.put(DirectInfo.COLUMN_NAME_PREFIX_FR, row[4]);
                values.put(DirectInfo.COLUMN_NAME_SUFFIX_EN, row[5]);
                values.put(DirectInfo.COLUMN_NAME_SUFFIX_FR, row[6]);
                values.put(DirectInfo.COLUMN_NAME_TITLE_EN, row[7]);
                values.put(DirectInfo.COLUMN_NAME_TITLE_FR, row[8]);
                values.put(DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER, row[9]);
                values.put(DirectInfo.COLUMN_NAME_FAX_NUMBER, row[10]);
                values.put(DirectInfo.COLUMN_NAME_TDD_NUMBER, row[11]);
                values.put(DirectInfo.COLUMN_NAME_SECURE_TELEPHONE_NUMBER, row[12]);
                values.put(DirectInfo.COLUMN_NAME_SECURE_FAX_NUMBER, row[13]);
                values.put(DirectInfo.COLUMN_NAME_ALTERNATE_TELEPHONE_NUMBER, row[14]);
                values.put(DirectInfo.COLUMN_NAME_EMAIL, row[15]);
                values.put(DirectInfo.COLUMN_NAME_STREET_ADDRESS_EN, row[16]);
                values.put(DirectInfo.COLUMN_NAME_STREET_ADDRESS_FR, row[17]);
                values.put(DirectInfo.COLUMN_NAME_COUNTRY_EN, row[18]);
                values.put(DirectInfo.COLUMN_NAME_COUNTRY_FR, row[19]);
                values.put(DirectInfo.COLUMN_NAME_PROVINCE_EN, row[20]);
                values.put(DirectInfo.COLUMN_NAME_PROVINCE_FR, row[21]);
                values.put(DirectInfo.COLUMN_NAME_CITY_EN, row[22]);
                values.put(DirectInfo.COLUMN_NAME_CITY_FR, row[23]);
                values.put(DirectInfo.COLUMN_NAME_POSTAL_CODE, row[24]);
                values.put(DirectInfo.COLUMN_NAME_PO_BOX_EN, row[25]);
                values.put(DirectInfo.COLUMN_NAME_PO_BOX_FR, row[26]);
                values.put(DirectInfo.COLUMN_NAME_MAILSTOP, row[27]);
                values.put(DirectInfo.COLUMN_NAME_BUILDING_EN, row[28]);
                values.put(DirectInfo.COLUMN_NAME_BUILDING_FR, row[29]);
                values.put(DirectInfo.COLUMN_NAME_FLOOR, row[30]);
                values.put(DirectInfo.COLUMN_NAME_ROOM, row[31]);
                values.put(DirectInfo.COLUMN_NAME_ADMINISTRATIVE_ASSISTANT, row[32]);
                values.put(DirectInfo.COLUMN_NAME_ADMINISTRATIVE_ASSISTANT_TELEPHONE_NUMBER, row[33]);
                values.put(DirectInfo.COLUMN_NAME_EXECUTIVE_ASSISTANT, row[34]);
                values.put(DirectInfo.COLUMN_NAME_EXECUTIVE_ASSISTANT_TELEPHONE_NUMBER, row[35]);
                values.put(DirectInfo.COLUMN_NAME_DEPARTMENT_ACRONYM, row[36]);
                values.put(DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_EN, row[37]);
                values.put(DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_FR, row[38]);
                values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_ACRONYM, row[39]);
                values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_EN, row[40]);
                values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_FR, row[41]);
                values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_STRUCTURE_EN, row[42]);
//                values.put(DirectInfo.COLUMN_NAME_ORGANIZATION_STRUCTURE_FR, row[43]);
                db.insert(DirectInfo.TABLE_NAME, null, values);
            }

        db.setTransactionSuccessful();
        db.endTransaction();
    }
} // end of class
