package my.logon.screen.beans;

import com.google.android.gms.maps.model.LatLng;

public class GeocodeAddress {

	private LatLng coordinates;
	private boolean geoStatus;
	private String errorMessage;

	public LatLng getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
	}

	public boolean isAdresaValida() {
		return geoStatus;
	}

	public void setGeoStatus(boolean geoStatus) {
		this.geoStatus = geoStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
