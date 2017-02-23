package es.cesalberca.contactsapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.cesalberca.contactsapp.data.Contact;
import es.cesalberca.contactsapp.data.local.ContactsPersistenceContract.ContactEntry;

/**
 * Repository class in charge of making the queries to the database
 */
public class ContactsRepository implements ContactsDataSource {
    private static ContactsRepository INSTANCE = null;
    private DbHelper dbHelper;

    /**
     * Private constructor to avoid instantiating by error
     * @param context Context of the application
     */
    private ContactsRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    /**
     * Singleton pattern, as the ContactsLocalDataSource shouldn't have more that one instance
     * @param context Database context
     * @return Returns itself
     */
    public static ContactsRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_NAME_NAME,
                ContactEntry.COLUMN_NAME_LASTNAME,
                ContactEntry.COLUMN_NAME_PHONE,
                ContactEntry.COLUMN_NAME_ADDRESS,
                ContactEntry.COLUMN_NAME_AVATAR,
                ContactEntry.COLUMN_NAME_POSTAL_CODE,
                ContactEntry.COLUMN_NAME_EMAIL
        };

        Cursor c = db.query(ContactEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(ContactEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_NAME));
                String lastname = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_LASTNAME));
                String phone = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_PHONE));
                String address = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_ADDRESS));
                String avatar = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_AVATAR));
                String postalCode = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_POSTAL_CODE));
                String email = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_EMAIL));
                Contact contact = new Contact(id, name, lastname, phone, address, avatar, postalCode, email);
                contacts.add(contact);
            }
        }

        if (c != null) {
            c.close();
        }

        db.close();

        return contacts;
    }

    @Override
    public Contact findOne(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ContactEntry._ID,
                ContactEntry.COLUMN_NAME_NAME,
                ContactEntry.COLUMN_NAME_LASTNAME,
                ContactEntry.COLUMN_NAME_PHONE,
                ContactEntry.COLUMN_NAME_ADDRESS,
                ContactEntry.COLUMN_NAME_AVATAR,
                ContactEntry.COLUMN_NAME_POSTAL_CODE,
                ContactEntry.COLUMN_NAME_EMAIL
        };

        String selection = ContactEntry._ID + " LIKE ?";
        String[] selectionArgs = { id };
        Cursor c = db.query(ContactEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Contact contact = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String name = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_NAME));
            String lastname = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_LASTNAME));
            String phone = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_PHONE));
            String address = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_ADDRESS));
            String avatar = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_AVATAR));
            String postalCode = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_POSTAL_CODE));
            String email = c.getString(c.getColumnIndexOrThrow(ContactEntry.COLUMN_NAME_EMAIL));
            contact = new Contact(id, name, lastname, phone, address, avatar, postalCode, email);
        }

        if (c != null) {
            c.close();
        }

        db.close();

        return contact;
    }

    @Override
    public void save(Contact contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactEntry._ID, contact.getId());
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_LASTNAME, contact.getLastname());
        values.put(ContactEntry.COLUMN_NAME_PHONE, contact.getPhone());
        values.put(ContactEntry.COLUMN_NAME_ADDRESS, contact.getAddress());
        values.put(ContactEntry.COLUMN_NAME_AVATAR, contact.getAvatar());
        values.put(ContactEntry.COLUMN_NAME_POSTAL_CODE, contact.getPostalCode());
        values.put(ContactEntry.COLUMN_NAME_EMAIL, contact.getEmail());

        db.insert(ContactEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void update(Contact contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(ContactEntry.COLUMN_NAME_LASTNAME, contact.getLastname());
        values.put(ContactEntry.COLUMN_NAME_PHONE, contact.getPhone());
        values.put(ContactEntry.COLUMN_NAME_ADDRESS, contact.getAddress());
        values.put(ContactEntry.COLUMN_NAME_AVATAR, contact.getAvatar());
        values.put(ContactEntry.COLUMN_NAME_POSTAL_CODE, contact.getPostalCode());
        values.put(ContactEntry.COLUMN_NAME_EMAIL, contact.getEmail());

        String selection = ContactEntry._ID + " LIKE ?";
        String[] selectionArgs = { contact.getId() };

        db.update(ContactEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public void delete(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = ContactEntry._ID + " LIKE ?";
        String[] selectionArgs = { id };

        db.delete(ContactEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
