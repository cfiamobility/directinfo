package ca.gc.inspection.directinfo;

import android.provider.BaseColumns;

public final class DirectInfoDbContract {

    // constructor is private to prevent instantiation of the contract class
    private DirectInfoDbContract(){}

    //inner class that defines the table contents
    public static final class DirectInfo implements BaseColumns {
        public static final String TABLE_NAME = "gedsOpenData";


        public static final String COLUMN_NAME_FIRST_NAME = "agofficialname";
        public static final String COLUMN_NAME_LAST_NAME = "sn";
        public static final String COLUMN_NAME_PREFIX_EN = "gcprefixenglish";
        public static final String COLUMN_NAME_TITLE_EN = "gctitleenglish";
        public static final String COLUMN_NAME_TELEPHONE_NUMBER = "telephonenumber";
        public static final String COLUMN_NAME_MOBILE_NUMBER = "mobile";
        public static final String COLUMN_NAME_EMAIL = "mail";
        public static final String COLUMN_NAME_POSTAL_STREET_NUMBER = "agpostalstreetnumber";
        public static final String COLUMN_NAME_POSTAL_STREET_NAME = "agpostalstreetnameenglish";
        public static final String COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE = "agpostalbuildingunittypeenglish";
        public static final String COLUMN_NAME_POSTAL_BUILDING_UNIT_ID = "agpostalbuildingunitid";
        public static final String COLUMN_NAME_PO_BOX_EN = "gcpostofficeboxenglish";
        public static final String COLUMN_NAME_POSTAL_CITY_EN = "gccityenglish";
        public static final String COLUMN_NAME_POSTAL_PROVINCE_EN = "gcprovincenameenglish";
        public static final String COLUMN_NAME_C = "c";
        public static final String COLUMN_NAME_POSTAL_CODE = "postalcode";
        public static final String COLUMN_NAME_BUILDING_NAME_EN = "gcbuildingnameenglish";
        public static final String COLUMN_NAME_FLOOR = "agfloorenglish";
        public static final String COLUMN_NAME_ROOM = "agroomnumberenglish";
        public static final String COLUMN_NAME_PHYSICAL_STREET_NUMBER = "agphysicalstreetnumber";
        public static final String COLUMN_NAME_PHYSICAL_STREET_NAME = "agphysicalstreetnameenglish";
        public static final String COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE = "agphysicalbuildingunittypeenglish";
        public static final String COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID = "agphysicalbuildingunitid";
        public static final String COLUMN_NAME_PHYSICAL_CITY_EN = "agphysicalcityenglish";
        public static final String COLUMN_NAME_PHYSICAL_PROVINCE_EN = "agphysicalprovincenameenglish";






        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DirectInfo.TABLE_NAME + " (" +
                        DirectInfo._ID + " INTEGER PRIMARY KEY, " +
                        DirectInfo.COLUMN_NAME_FIRST_NAME + " TEXT, " +
                        DirectInfo.COLUMN_NAME_LAST_NAME + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PREFIX_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_TITLE_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_MOBILE_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_EMAIL + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_STREET_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_STREET_NAME + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_TYPE + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_BUILDING_UNIT_ID + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PO_BOX_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_CITY_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_PROVINCE_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_C + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_CODE + " TEXT, " +
                        DirectInfo.COLUMN_NAME_BUILDING_NAME_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_FLOOR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ROOM + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_STREET_NAME + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_TYPE + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_BUILDING_UNIT_ID + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_CITY_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PHYSICAL_PROVINCE_EN + " TEXT )" ;

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DirectInfo.TABLE_NAME;
    }
}
