package my.logon.screen.listeners;

import com.google.android.gms.maps.model.LatLng;

public interface MapListener {

	public void addressSelected(LatLng coord, android.location.Address address);
	
}
