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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            contactImage.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    public void createContact(View view) {
        String finalPathImage = "";

        if (pathImage.toString() != null) {
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

        ContactsRepository.getInstance(getApplicationContext()).save(contact);
        Toast.makeText(this, "Contact created!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pathImage = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathImage);

        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ExifInterface ei;
                Bitmap image;

                try {
                    ei = new ExifInterface(pathImage.getPath());
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pathImage);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch(orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotateImage(image, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotateImage(image, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotateImage(image, 270);
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            break;
                    }

                    contactImage.setImageBitmap(image);
                } catch (IOException e) {
                    contactImage.setImageURI(pathImage);
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
