package ca.gc.inspection.directinfo;

import android.provider.BaseColumns;

public final class DirectInfoDbContract {

    // constructor is private to prevent instantiation of the contract class
    private DirectInfoDbContract(){}

    //inner class that defines the table contents
    public static final class DirectInfo implements BaseColumns {
        public static final String TABLE_NAME = "gedsOpenData";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_GIVEN_NAME = "given_name";
        public static final String COLUMN_NAME_INITIALS = "initials";
        public static final String COLUMN_NAME_PREFIX_EN = "prefix_en";
        public static final String COLUMN_NAME_PREFIX_FR = "prefix_fr";
        public static final String COLUMN_NAME_SUFFIX_EN = "suffix_en";
        public static final String COLUMN_NAME_SUFFIX_FR = "suffix_fr";
        public static final String COLUMN_NAME_TITLE_EN = "title_en";
        public static final String COLUMN_NAME_TITLE_FR = "title_fr";
        public static final String COLUMN_NAME_TELEPHONE_NUMBER = "telephone_number";
        public static final String COLUMN_NAME_FAX_NUMBER = "fax_number";
        public static final String COLUMN_NAME_TDD_NUMBER = "tdd_number";
        public static final String COLUMN_NAME_SECURE_TELEPHONE_NUMBER = "secure_telephone_number";
        public static final String COLUMN_NAME_SECURE_FAX_NUMBER = "secure_fax_number";
        public static final String COLUMN_NAME_ALTERNATE_TELEPHONE_NUMBER = "alternate_telephone_number";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_STREET_ADDRESS_EN = "street_address_en";
        public static final String COLUMN_NAME_STREET_ADDRESS_FR = "street_address_fr";
        public static final String COLUMN_NAME_COUNTRY_EN = "country_en";
        public static final String COLUMN_NAME_COUNTRY_FR = "country_fr";
        public static final String COLUMN_NAME_PROVINCE_EN = "province_en";
        public static final String COLUMN_NAME_PROVINCE_FR = "province_fr";
        public static final String COLUMN_NAME_CITY_EN = "city_en";
        public static final String COLUMN_NAME_CITY_FR = "city_fr";
        public static final String COLUMN_NAME_POSTAL_CODE = "postal_code";
        public static final String COLUMN_NAME_PO_BOX_EN = "po_box_en";
        public static final String COLUMN_NAME_PO_BOX_FR = "po_box_fr";
        public static final String COLUMN_NAME_MAILSTOP = "mailstop";
        public static final String COLUMN_NAME_BUILDING_EN = "building_en";
        public static final String COLUMN_NAME_BUILDING_FR = "building_fr";
        public static final String COLUMN_NAME_FLOOR = "floor";
        public static final String COLUMN_NAME_ROOM = "room";
        public static final String COLUMN_NAME_ADMINISTRATIVE_ASSISTANT = "administrative_assistant";
        public static final String COLUMN_NAME_ADMINISTRATIVE_ASSISTANT_TELEPHONE_NUMBER = "administrative_assistant_telephone_number";
        public static final String COLUMN_NAME_EXECUTIVE_ASSISTANT = "executive_assistant";
        public static final String COLUMN_NAME_EXECUTIVE_ASSISTANT_TELEPHONE_NUMBER = "executive_assistant_telephone_number";
        public static final String COLUMN_NAME_DEPARTMENT_ACRONYM = "department_acronym";
        public static final String COLUMN_NAME_DEPARTMENT_NAME_EN = "department_name_en";
        public static final String COLUMN_NAME_DEPARTMENT_NAME_FR = "department_name_fr";
        public static final String COLUMN_NAME_ORGANIZATION_ACRONYM = "organization_acronym";
        public static final String COLUMN_NAME_ORGANIZATION_NAME_EN = "organization_name_en";
        public static final String COLUMN_NAME_ORGANIZATION_NAME_FR = "organization_name_fr";
        public static final String COLUMN_NAME_ORGANIZATION_STRUCTURE_EN = "organization_structure_en";
        public static final String COLUMN_NAME_ORGANIZATION_STRUCTURE_FR = "organization_structure_fr";


        public static final String SQL_CREATE_ENTRIES =
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
                        DirectInfo.COLUMN_NAME_SECURE_TELEPHONE_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_SECURE_FAX_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ALTERNATE_TELEPHONE_NUMBER + " TEXT, " +
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
                        DirectInfo.COLUMN_NAME_PO_BOX_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_PO_BOX_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_MAILSTOP + " TEXT, " +
                        DirectInfo.COLUMN_NAME_BUILDING_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_BUILDING_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_FLOOR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ROOM + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ADMINISTRATIVE_ASSISTANT + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ADMINISTRATIVE_ASSISTANT_TELEPHONE_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_EXECUTIVE_ASSISTANT + " TEXT, " +
                        DirectInfo.COLUMN_NAME_EXECUTIVE_ASSISTANT_TELEPHONE_NUMBER + " TEXT, " +
                        DirectInfo.COLUMN_NAME_DEPARTMENT_ACRONYM + " TEXT, " +
                        DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_DEPARTMENT_NAME_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_ACRONYM + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_NAME_FR + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_STRUCTURE_EN + " TEXT, " +
                        DirectInfo.COLUMN_NAME_ORGANIZATION_STRUCTURE_FR + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DirectInfo.TABLE_NAME;
    }
}
