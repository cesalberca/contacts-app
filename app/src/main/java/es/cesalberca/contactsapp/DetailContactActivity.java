package es.cesalberca.contactsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

import es.cesalberca.contactsapp.data.Contact;
import es.cesalberca.contactsapp.data.local.ContactsRepository;
import es.cesalberca.contactsapp.utils.ImageUtils;
import es.cesalberca.contactsapp.utils.LocationUtils;

public class DetailContactActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private String contactId;
    private Contact contact;
    private TextView contactName, contactLastname, contactPhone, contactAddress, contactPostalCode, contactEmail, contactDistance;
    private ImageView contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        contactId = extras.getString(MainActivity.CONTACT_ID);

        contactName = (TextView) findViewById(R.id.etName);
        contactLastname = (TextView) findViewById(R.id.etLastname);
        contactPhone = (TextView) findViewById(R.id.etPhone);
        contactAddress = (TextView) findViewById(R.id.etAddress);
        contactImage = (ImageView) findViewById(R.id.ivContact);
        contactPostalCode = (TextView) findViewById(R.id.tvPostalCode);
        contactEmail = (TextView) findViewById(R.id.tvEmail);
        contactImage = (ImageView) findViewById(R.id.ivContact);
        contactDistance = (TextView) findViewById(R.id.tvDistance);

        this.loadContactData(contactId);
        buildGoogleApiClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteContact:
                deleteContact();
                return true;
            case R.id.shareContact:
                shareContact();
                return true;
            case R.id.locateContact:
                locateContact();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void locateContact() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + contact.getAddress()));
        startActivity(intent);
    }

    private void deleteContact() {
        ContactsRepository.getInstance(getApplicationContext()).delete(contactId);
        Toast.makeText(this, "Contact deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loadContactData(String contactId) {
        contact = ContactsRepository.getInstance(getApplicationContext()).findOne(contactId);
        contactName.setText(contact.getName());
        contactLastname.setText(contact.getLastname());
        contactAddress.setText(contact.getAddress());
        contactPhone.setText(contact.getPhone());
        contactEmail.setText(contact.getEmail());
        contactPostalCode.setText(contact.getPostalCode());

        try {
            Bitmap bitmap = ImageUtils.decodeBitmapUri(this, Uri.parse(contact.getAvatar()));
            contactImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            contactImage.setImageDrawable(getResources().getDrawable(R.drawable.default_contact_image));
        }
    }

    public void callContact(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contact.getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "We don't have permissions to call!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    public void shareContact() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contact.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void previousContact(View view) {
        List<Contact> contactList = ContactsRepository.getInstance(getApplicationContext()).findAll();
        int previousIndex = contactList.indexOf(contact) - 1;

        String nextContactId;

        if (previousIndex >= 0) {
            nextContactId = contactList.get(previousIndex).getId();
        } else {
            nextContactId = contactList.get(contactList.size() - 1).getId();
        }

        Intent intent = new Intent(this, DetailContactActivity.class);
        intent.putExtra(MainActivity.CONTACT_ID, nextContactId);
        startActivity(intent);
    }

    public void nextContact(View view) {
        List<Contact> contactList = ContactsRepository.getInstance(getApplicationContext()).findAll();
        int nextIndex = contactList.indexOf(contact) + 1;

        String nextContactId;

        if (nextIndex < contactList.size()) {
            nextContactId = contactList.get(nextIndex).getId();
        } else {
            nextContactId = contactList.get(0).getId();
        }

        Intent intent = new Intent(this, DetailContactActivity.class);
        intent.putExtra(MainActivity.CONTACT_ID, nextContactId);
        startActivity(intent);
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "We need gps permissions!", Toast.LENGTH_SHORT).show();
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Location contactLocation = LocationUtils.getLocationFromAddress(this, contact.getAddress());

        if (lastLocation != null) {
            if (contactLocation != null) {
                contactDistance.setText(LocationUtils.getDistanceInMeters(lastLocation, contactLocation) + " meters");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
