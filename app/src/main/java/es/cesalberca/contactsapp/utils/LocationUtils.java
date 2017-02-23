package es.cesalberca.contactsapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

/**
 * General purpose location utils related to geocoding
 */
public class LocationUtils {

    /**
     * Returns a location with it's latitude and longitude form an address
     * @param context Context of the app
     * @param strAddress The address to retrieve the latitude and longitude
     * @return The location found
     */
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

            // No need to give name to location
            location = new Location("");
            location.setLatitude(address.getLatitude());
            location.setLongitude(address.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return location;
    }

    /**
     * Get the distance in meters between two locations
     * @param locationA Location A
     * @param locationB Location B
     * @return Distance in meters
     */
    public static String getDistanceInMeters(Location locationA, Location locationB) {
        return String.format("%.2f", locationA.distanceTo(locationB));
    }
}
