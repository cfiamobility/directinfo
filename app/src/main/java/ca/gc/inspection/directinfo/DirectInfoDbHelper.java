package ca.gc.inspection.directinfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ca.gc.inspection.directinfo.DirectInfoDbContract.DirectInfo;


public class DirectInfoDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DirectInfoDbHelper";
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "DirectInfo.db";


    public  DirectInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        Log.d(TAG, "onCreate: " + DirectInfo.SQL_CREATE_ENTRIES);

        db.execSQL(DirectInfo.SQL_CREATE_ENTRIES);
        db.execSQL(DirectInfo.SQL_DATE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DirectInfo.SQL_DELETE_ENTRIES);
        db.execSQL(DirectInfo.SQL_DELETE_DATE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
