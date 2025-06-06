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

		try {
			if (address.toLowerCase().contains("nr"))
				return address.substring(0, address.toLowerCase().indexOf("nr") - 2).trim();
			else if (address.toLowerCase().contains("nr."))
				return address.substring(0, address.toLowerCase().indexOf("nr.") - 3).trim();
			else
				return address;
		} catch (Exception ex) {
			return " ";
		}

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

	public static String getSectorBucuresti(String googleSector){
		if (googleSector.toLowerCase().contains("sectorul 1"))
			return "SECTOR 1";
		else if (googleSector.toLowerCase().contains("sectorul 2"))
			return "SECTOR 2";
		else if (googleSector.toLowerCase().contains("sectorul 3"))
			return "SECTOR 3";
		else if (googleSector.toLowerCase().contains("sectorul 4"))
			return "SECTOR 4";
		else if (googleSector.toLowerCase().contains("sectorul 5"))
			return "SECTOR 5";
		else if (googleSector.toLowerCase().contains("sectorul 6"))
			return "SECTOR 6";
		else
			return "BUCURESTI";
	}

}
