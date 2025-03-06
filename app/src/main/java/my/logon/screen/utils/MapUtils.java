package my.logon.screen.utils;

import android.content.Context;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import my.logon.screen.beans.Address;
import my.logon.screen.beans.GeocodeAddress;

public class MapUtils {

	public static GeocodeAddress geocodeAddress(Address address, Context context) {

		GeocodeAddress geoAddress = new GeocodeAddress();

		if (address.getSector() == null || address.getSector().equals("Nedefinit")){
			Toast.makeText(context, "Completati judetul.", Toast.LENGTH_LONG).show();
			return geoAddress;
		}

		if (address.getCity() == null || address.getCity().trim().isEmpty()){
			Toast.makeText(context, "Completati localitatea.", Toast.LENGTH_LONG).show();
			return geoAddress;
		}

		double latitude = 0;
		double longitude = 0;

		StringBuilder strAddress = new StringBuilder();

		if (address.getStreet() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getStreet().toUpperCase().replace(" NR ", " ").replace(" NR. ", " "));
			strAddress.append(",");
		}

		if (address.getNumber() != null && !address.getNumber().equals("")) {
			strAddress.append(address.getNumber());
			strAddress.append(",");
		}

		if (address.getSector() != null && !address.getSector().equals("")) {

			if (!UtilsFormatting.flattenToAscii(address.getSector()).toLowerCase().contains("bucuresti"))
				strAddress.append("Judetul " + address.getSector());
			else
				strAddress.append(address.getSector());
			strAddress.append(",");
		}

		if (address.getCity() != null && !address.getCity().equals("")) {
			strAddress.append(address.getCity());
			strAddress.append(",");
		}

		strAddress.append(address.getCountry());
		Locale aLocale = new Locale.Builder().setLanguage("ro").build();
		Geocoder geoCoder = new Geocoder(context, aLocale);
		List<android.location.Address> addresses = new ArrayList<android.location.Address>();
		try {
			addresses = geoCoder.getFromLocationName(strAddress.toString(), 1);
		} catch (IOException e) {
			geoAddress.setErrorMessage(e.toString());
			Toast.makeText(context, "GeocodeAddress: " + e.toString(), Toast.LENGTH_LONG).show();
		}


		if (addresses.size() > 0) {

			boolean isLocalitateCorecta = false;

			String judetResult = addresses.get(0).getAdminArea();
			String comunaResult = addresses.get(0).getSubAdminArea();
			String localitateResult = addresses.get(0).getLocality();
			String subLocalitateResult = addresses.get(0).getSubLocality() != null ? addresses.get(0).getSubLocality() : " ";

			String localitateAdresa = UtilsFormatting.flattenToAscii(address.getCity().trim().toLowerCase());
			String judetAdresa = UtilsFormatting.flattenToAscii(address.getSector().trim().toLowerCase());

			if (judetAdresa.equals("bucuresti"))
				localitateAdresa = judetAdresa;

			if (!UtilsFormatting.flattenToAscii(judetResult).toLowerCase().contains(UtilsFormatting.flattenToAscii(judetAdresa).toLowerCase()))
				isLocalitateCorecta = false;
			else if (localitateResult != null)
				isLocalitateCorecta = UtilsFormatting.flattenToAscii(localitateResult).toLowerCase().contains(UtilsFormatting.flattenToAscii(localitateAdresa.replaceAll("\\(.*?\\)","").trim()).toLowerCase()) ;
			else if (subLocalitateResult != null)
				isLocalitateCorecta = UtilsFormatting.flattenToAscii(subLocalitateResult).toLowerCase().contains(UtilsFormatting.flattenToAscii(localitateAdresa).toLowerCase());
			else if (comunaResult != null)
				isLocalitateCorecta = UtilsFormatting.flattenToAscii(comunaResult).toLowerCase().contains(UtilsFormatting.flattenToAscii(localitateAdresa).toLowerCase());
			else
				isLocalitateCorecta = false;


			if (isLocalitateCorecta) {
				latitude = addresses.get(0).getLatitude();
				longitude = addresses.get(0).getLongitude();
				geoAddress.setGeoStatus(true);
				geoAddress.setErrorMessage("Succes");
			} else {
				geoAddress.setGeoStatus(false);
				geoAddress.setErrorMessage("Adresa inexistenta.");
			}

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

		String localitateAddress = googleAddress.getSubLocality() != null ? googleAddress.getSubLocality() : googleAddress.getLocality();

		if (localitateAddress != null) {
			tempString = UtilsFormatting.flattenToAscii(localitateAddress).toUpperCase();
			if (tempString.equalsIgnoreCase("bucharest"))
				tempString = "BUCURESTI";
		} else
			tempString = "";

		if (tempString.equals("BUCURESTI"))
			tempString = UtilsAddress.getSectorBucuresti(googleAddress.getSubLocality());

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
