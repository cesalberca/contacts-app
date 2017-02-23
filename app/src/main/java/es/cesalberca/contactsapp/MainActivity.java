package es.cesalberca.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.cesalberca.contactsapp.data.Contact;
import es.cesalberca.contactsapp.data.local.ContactsRepository;

public class MainActivity extends AppCompatActivity {
    public static final String CONTACT_ID = "CONTACT_ID";

    private ListView lvContacts;
    private List<Contact> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContacts = (ListView) findViewById(R.id.lvContacts);

        contactsList = ContactsRepository.getInstance(getApplicationContext()).findAll();

        final ContactAdapter adapter = new ContactAdapter(this, (ArrayList<Contact>) contactsList);
        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = contactsList.get(i);
                Intent intent = new Intent(MainActivity.this, DetailContactActivity.class);
                intent.putExtra(CONTACT_ID, contact.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_contact:
                Intent intent = new Intent(this, CreateContactActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
