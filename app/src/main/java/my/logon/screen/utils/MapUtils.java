package my.logon.screen.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Geocoder;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.GeocodeAddress;

import com.google.android.gms.maps.model.LatLng;

public class MapUtils {

	public static GeocodeAddress geocodeAddress(Address address, Context context) {

		GeocodeAddress geoAddress = new GeocodeAddress();

		double latitude = 0;
		double longitude = 0;

		StringBuilder strAddress = new StringBuilder();

		if (address.getStreet() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getStreet());
			strAddress.append(",");
		}

		if (address.getNumber() != null && !address.getNumber().equals("")) {
			strAddress.append(address.getNumber());
			strAddress.append(",");
		}

		if (address.getSector() != null && !address.getSector().equals("")) {
			strAddress.append(address.getSector());
			strAddress.append(",");
		}

		if (address.getCity() != null && !address.getCity().equals("")) {
			if (address.getCity().contains("SECTOR") && address.getSector().equals("BUCURESTI"))
				strAddress.append("BUCURESTI");
			else
				strAddress.append(address.getCity());
			strAddress.append(",");
		}

		strAddress.append(address.getCountry());
		

		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		List<android.location.Address> addresses = new ArrayList<android.location.Address>();
		try {
			addresses = geoCoder.getFromLocationName(strAddress.toString(), 1);

		} catch (IOException e) {
			geoAddress.setErrorMessage(e.toString());
		}

		if (addresses.size() > 0) {
			latitude = addresses.get(0).getLatitude();
			longitude = addresses.get(0).getLongitude();
			geoAddress.setGeoStatus(true);
			geoAddress.setErrorMessage("Succes");

		}

		geoAddress.setCoordinates(new LatLng(latitude, longitude));

		return geoAddress;
	}

	public static android.location.Address getAddressFromCoordinate(LatLng coords, Context context) {
		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

		android.location.Address address = new android.location.Address(null);

		try {

			List<android.location.Address> tempList = geoCoder.getFromLocation(coords.latitude, coords.longitude, 1);

			if (!tempList.isEmpty())
				address = tempList.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return address;
	}

	public static double distanceXtoY(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static Address getAddress(android.location.Address googleAddress) {
		Address address = new Address();

		String tempString = "";

		if (googleAddress.getAdminArea() != null)
			tempString = UtilsFormatting.flattenToAscii(googleAddress.getAdminArea()).toUpperCase();
		else
			tempString = "";

		tempString = tempString.replace("JUDETUL", "").trim();
		tempString = tempString.replace("MUNICIPIUL", "").trim();
		address.setSector(tempString);

		if (googleAddress.getLocality() != null) {
			tempString = UtilsFormatting.flattenToAscii(googleAddress.getLocality()).toUpperCase();
			if (tempString.equalsIgnoreCase("bucharest"))
				tempString = "BUCURESTI";
		} else
			tempString = "";

		address.setCity(tempString);

		if (googleAddress.getThoroughfare() != null)
			tempString = UtilsFormatting.flattenToAscii(googleAddress.getThoroughfare()).toUpperCase().replace("STRADA", "");
		else
			tempString = "";

		address.setStreet(tempString);

		if (googleAddress.getSubThoroughfare() != null)
			tempString = UtilsFormatting.flattenToAscii(googleAddress.getSubThoroughfare()).toUpperCase();
		else
			tempString = "";

		address.setNumber(tempString);

		return address;

	}

}
