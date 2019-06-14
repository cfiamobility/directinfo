package ca.gc.inspection.directinfo;

import android.provider.BaseColumns;

final class DirectInfoDbContract {

    // constructor is private to prevent instantiation of the contract class
    private DirectInfoDbContract(){}

    /** Inner class that defines the table contents
     *  DirectInfo Schema has two tables:
     *  "people" table that holds information about employees
     *  "updateDate" table that holds the time that the database was last updated
     *      - This is the trigger used by the application to check if it needs to download
     *      the new data set from the app
     */
    static final class DirectInfo implements BaseColumns {

        /** people table, fields, and functions */
        static final String TABLE_NAME = "people";

        static final String COLUMN_NAME_SURNAME = "surname";
        static final String COLUMN_NAME_GIVEN_NAME = "givenname";
        static final String COLUMN_NAME_INITIALS = "initials";
        static final String COLUMN_NAME_PREFIX_EN = "prefix_en";
        static final String COLUMN_NAME_PREFIX_FR = "prefix_fr";
        static final String COLUMN_NAME_SUFFIX_EN = "suffix_en";
        static final String COLUMN_NAME_SUFFIX_FR = "suffix_fr";
        static final String COLUMN_NAME_TITLE_EN = "title_en";
        static final String COLUMN_NAME_TITLE_FR = "title_fr";
        static final String COLUMN_NAME_TELEPHONE_NUMBER = "telephonenumber";
        static final String COLUMN_NAME_FAX_NUMBER = "faxnumber";
        static final String COLUMN_NAME_TDD_NUMBER = "tddnumber";
        static final String COLUMN_NAME_EMAIL = "email";
        static final String COLUMN_NAME_STREET_ADDRESS_EN = "streetaddress_en";
        static final String COLUMN_NAME_STREET_ADDRESS_FR = "streetaddress_fr";
        static final String COLUMN_NAME_COUNTRY_EN = "country_en";
        static final String COLUMN_NAME_COUNTRY_FR = "country_fr";
        static final String COLUMN_NAME_PROVINCE_EN = "province_en";
        static final String COLUMN_NAME_PROVINCE_FR = "province_fr";
        static final String COLUMN_NAME_CITY_EN = "city_en";
        static final String COLUMN_NAME_CITY_FR = "city_fr";
        static final String COLUMN_NAME_POSTAL_CODE = "postalcode";
        static final String COLUMN_NAME_DEPARTMENT_ACRONYM = "departmentacronym";
        static final String COLUMN_NAME_DEPARTMENT_NAME_EN = "departmentname_en";
        static final String COLUMN_NAME_DEPARTMENT_NAME_FR = "departmentname_fr";
        static final String COLUMN_NAME_ORGANIZATION_ACRONYM = "organizationacronym";
        static final String COLUMN_NAME_ORGANIZATION_NAME_EN = "organizationname_en";
        static final String COLUMN_NAME_ORGANIZATION_NAME_FR = "organizationname_fr";

        static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DirectInfo.TABLE_NAME + " (" +
                        DirectInfo._ID + " INTEGER PRIMARY KEY, " +
                        DirectInfo.COLUMN_NAME_SURNAME + " TEXT, " +
                        DirectInfo.COLUMN_NAME_GIVEN_NAME + " TEXT, " +
                        DirectInfo.COLUMN_NAME_INITIALS + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PREFIX_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PREFIX_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_SUFFIX_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_SUFFIX_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_TITLE_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_TITLE_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_TELEPHONE_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_FAX_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_TDD_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_EMAIL + " TEXT, " +
                        DirectInfo.COLUMN_NAME_STREET_ADDRESS_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_STREET_ADDRESS_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_COUNTRY_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_COUNTRY_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PROVINCE_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PROVINCE_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_CITY_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_CITY_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_POSTAL_CODE + " TEXT, " +
                        DirectInfo.COLUMN_NAME_DEPARTMENT_ACRONYM + " TEXT, " +
                        DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_ACRONYM + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_FR + " TEXT )" ;

        static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DirectInfo.TABLE_NAME;



        /** updateDate table, fields, and functions */
        static final String TABLE_DATE_NAME = "updateDate";
        static final String COLUMN_NAME_DATE = "date";

        static String SQL_DATE_CREATE =
                "CREATE TABLE " + DirectInfo.TABLE_DATE_NAME + " (" +
                        DirectInfo.COLUMN_NAME_DATE + " TEXT )";

        static final String SQL_DELETE_DATE =
                "DROP TABLE IF EXISTS " + DirectInfo.TABLE_DATE_NAME;
    }
}
