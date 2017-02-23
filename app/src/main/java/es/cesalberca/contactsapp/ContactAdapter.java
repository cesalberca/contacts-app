package es.cesalberca.contactsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import es.cesalberca.contactsapp.data.Contact;
import es.cesalberca.contactsapp.utils.ImageUtils;

public class ContactAdapter extends ArrayAdapter<Contact> {
    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_card, parent, false);
        }

        TextView contactFullname = (TextView) convertView.findViewById(R.id.tvFullname);
        TextView contactPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        ImageView contactImage = (ImageView) convertView.findViewById(R.id.ivContact);

        if (contact != null) {
            contactFullname.setText(contact.getName() + " " + contact.getLastname());
            contactPhone.setText(contact.getPhone());
            try {
                Bitmap bitmap = ImageUtils.decodeBitmapUri(getContext(), Uri.parse(contact.getAvatar()));
                contactImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                contactImage.setBackgroundResource(R.drawable.ic_person_black_24dp);
            }
        }

        return convertView;
    }

}
