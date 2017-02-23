package es.cesalberca.contactsapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

public class LocationUtils {
    public static Location getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> addresses;
        Location location = null;

        try {
            addresses = coder.getFromLocationName(strAddress, 5);
            if (addresses.size() == 0) {
                return null;
            }

            Address address = addresses.get(0);

            location = new Location("");
            location.setLatitude(address.getLatitude());
            location.setLongitude(address.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return location;
    }

    public static String getDistanceInMeters(Location locationA, Location locationB) {
        return String.format("%.2f", locationA.distanceTo(locationB));
    }
}
