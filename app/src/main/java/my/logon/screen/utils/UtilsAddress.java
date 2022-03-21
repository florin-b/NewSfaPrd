package my.logon.screen.utils;

public class UtilsAddress {
	public static String getStreetNoNumber(String street) {

		if (street.toLowerCase().contains("nr"))
			return street.substring(0, street.toLowerCase().indexOf("nr")).trim();
		else
			return street;

	}

	public static String getStreetFromAddress(String address) {

		if (address == null)
			return " ";

		if (address.toLowerCase().contains("nr"))
			return address.substring(0, address.toLowerCase().indexOf("nr") - 2).trim();
		else if (address.toLowerCase().contains("nr."))
			return address.substring(0, address.toLowerCase().indexOf("nr.") - 3).trim();
		else
			return address;

	}

	public static String getStreetNumber(String street) {

		if (street == null)
			return " ";

		if (street.toLowerCase().contains("nr"))
			return street.substring(street.toLowerCase().indexOf("nr") + 2, street.length()).trim();
		if (street.toLowerCase().contains("nr."))
			return street.substring(street.toLowerCase().indexOf("nr.") + 3, street.length()).trim();
		else
			return " ";
	}

}
