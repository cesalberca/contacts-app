package es.cesalberca.contactsapp.data.local;

import java.util.List;

import es.cesalberca.contactsapp.data.Contact;

/**
 * Defines the data access object specifications for the Contacts table
 */
public interface ContactsDataSource {
    /**
     * Finds all {@link Contact}s
     */
    List<Contact> findAll();

    /**
     * Finds and retrieve a single {@link Contact}
     * @param id Id of the contact to look
     */
    Contact findOne(String id);

    /**
     * Saves a {@link Contact} to the database
     * @param contact Contact to save
     */
    void save(Contact contact);

    /**
     * Updates a given {@link Contact}
     * @param contact Contact to update
     */
    void update(Contact contact);

    /**
     * Deletes a {@link Contact}
     * @param id Id of the contact to delete
     */
    void delete(String id);
}
