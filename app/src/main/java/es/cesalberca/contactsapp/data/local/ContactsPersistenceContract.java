package es.cesalberca.contactsapp.data.local;

import android.provider.BaseColumns;

/**
 * Contract that represents the schema of the Contacts table in the database
 */
public final class ContactsPersistenceContract {
    // Private constructor to keep from accidentally instantiating the contract class.
    private ContactsPersistenceContract() {}

    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_POSTAL_CODE = "postalCode";
    }
}
