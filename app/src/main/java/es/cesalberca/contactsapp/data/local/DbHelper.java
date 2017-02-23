package es.cesalberca.contactsapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.cesalberca.contactsapp.data.local.ContactsPersistenceContract.ContactEntry;

/**
 * Database helper which it's main task is to create the schemas of the database and update it
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactsApp.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                ContactEntry._ID + TEXT_TYPE  + " PRIMARY KEY" + COMMA_SEP +
                ContactEntry.COLUMN_NAME_NAME + TEXT_TYPE +  COMMA_SEP +
                ContactEntry.COLUMN_NAME_LASTNAME + TEXT_TYPE +  COMMA_SEP +
                ContactEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                ContactEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                ContactEntry.COLUMN_NAME_AVATAR + TEXT_TYPE + COMMA_SEP +
                ContactEntry.COLUMN_NAME_POSTAL_CODE + TEXT_TYPE + COMMA_SEP +
                ContactEntry.COLUMN_NAME_EMAIL + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
