package es.cesalberca.contactsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.cesalberca.contactsapp.data.Contact;
import es.cesalberca.contactsapp.data.local.ContactsRepository;
import es.cesalberca.contactsapp.utils.FileUtils;

/**
 * Activity for the create contact screen
 */
public class CreateContactActivity extends AppCompatActivity {

    private EditText name, lastname, phone, address, postalCode, email;
    private ImageView contactImage;
    private Uri pathImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        name = (EditText) findViewById(R.id.etName);
        lastname = (EditText) findViewById(R.id.etLastname);
        phone = (EditText) findViewById(R.id.etPhone);
        address = (EditText) findViewById(R.id.etAddress);
        contactImage = (ImageView) findViewById(R.id.ivContact);
        postalCode = (EditText) findViewById(R.id.etPostalCode);
        email = (EditText) findViewById(R.id.etEmail);

        // Asks for permissions for camera and external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            contactImage.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    /**
     * Creates a contact
     * @param view View of the activity as required
     */
    public void createContact(View view) {
        String finalPathImage = "";

        // Check if there is a photo, if not, sets it to empty
        if (pathImage != null) {
                finalPathImage = pathImage.toString();
        }

        Contact contact = new Contact(
            name.getText().toString(),
            lastname.getText().toString(),
            phone.getText().toString(),
            address.getText().toString(),
            finalPathImage,
            postalCode.getText().toString(),
            email.getText().toString()
        );

        // Saves the contact
        ContactsRepository.getInstance(getApplicationContext()).save(contact);
        Toast.makeText(this, "Contact created!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Takes a picture for the contact's avatar
     * @param view View of the activity as required
     */
    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pathImage = Uri.fromFile(FileUtils.getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathImage);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                try {
                    // Sets the preview image
                    contactImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), pathImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                contactImage.setEnabled(true);
            }
        }
    }
}
