package my.logon.screen.helpers;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanLocalitate;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.UserInfo;
import my.logon.screen.screens.CreareComanda;
import my.logon.screen.screens.ModificareComanda;

public class HelperAdreseLivrare {

	private static final String livrareRapida = "TERR - Curier rapid";
	private static final double DIST_MIN_ADR_KM = 0.5;
	private static final double DIST_MIN_ADR_NOUA_KM = 0.2;

	private static String localitatiAcceptate;

	public static boolean isConditiiCurierRapid() {

		if (!UserInfo.getInstance().getCodDepart().equals("02"))
			return false;

		if (CreareComanda.filialaAlternativa != null)
			return CreareComanda.filialaAlternativa.equalsIgnoreCase("BV90") && UserInfo.getInstance().getUnitLog().startsWith("BU");
		else if (ModificareComanda.filialaAlternativaM != null)
			return ModificareComanda.filialaAlternativaM.equalsIgnoreCase("BV90") && UserInfo.getInstance().getUnitLog().startsWith("BU");

		return false;

	}

	public static String[] adaugaTransportCurierRapid(String[] tipTransport) {

		String[] arrayTransport;

		List<String> listTransport = new ArrayList<String>(Arrays.asList(tipTransport));

		listTransport.add(livrareRapida);

		arrayTransport = (String[]) listTransport.toArray(new String[listTransport.size()]);

		return arrayTransport;

	}

	public static String[] eliminaElement(String[] arrayElem, String element) {
		List<String> listArray = new ArrayList<>(Arrays.asList(arrayElem));
		listArray.removeIf(item -> item.startsWith(element));

		return listArray.stream().toArray(n -> new String[n]);
	}

	public static boolean isAdresaLivrareRapida() {
		return localitatiAcceptate.toUpperCase().contains(DateLivrare.getInstance().getOras().toUpperCase());
	}

	public static String getLocalitatiAcceptate() {
		return localitatiAcceptate;
	}

	public static void setLocalitatiAcceptate(String localitatiAcceptate) {
		HelperAdreseLivrare.localitatiAcceptate = localitatiAcceptate;
	}

	public static int getTonajSpinnerPos(String tonaj) {
		if (tonaj == null || tonaj.equals("0"))
			return 0;

		if (tonaj.equals("3.5"))
			return 1;
		else if (tonaj.equals("10"))
			return 2;
		else if (tonaj.equals("20"))
			return 3;

		return 0;
	}

	public static String getTonajSpinnerValue(int selectedPos) {

		switch (selectedPos) {
		case 1:
			return "3.5";
		case 2:
			return "10";
		case 3:
		default:
			return "20";

		}
	}

	public static String getTonajAdresaNoua(List<BeanAdresaLivrare> listAdrese, LatLng coordAdresa) {

		String tonajAdresa = "0";

		for (BeanAdresaLivrare adresa : listAdrese) {

			String coords[] = adresa.getCoords().split(",");

			double distAdresa = distanceXtoY(coordAdresa.latitude, coordAdresa.longitude, Double.valueOf(coords[0]), Double.valueOf(coords[1]), "K");

			if (distAdresa <= DIST_MIN_ADR_NOUA_KM) {
				tonajAdresa = adresa.getTonaj();
				break;
			}

		}

		return tonajAdresa;
	}

	public static int verificaDistantaAdresaNoua(List<BeanAdresaLivrare> listAdrese, LatLng coordAdresa) {

		int selectedPos = -1;

		if (listAdrese == null || coordAdresa == null)
			return -1;

		int pos = 0;
		for (BeanAdresaLivrare adresa : listAdrese) {
			String coords[] = adresa.getCoords().split(",");

			double distAdresa = distanceXtoY(coordAdresa.latitude, coordAdresa.longitude, Double.valueOf(coords[0]), Double.valueOf(coords[1]), "K");
			if (distAdresa < DIST_MIN_ADR_NOUA_KM) {
				selectedPos = pos;
				break;
			}

			pos++;
		}

		return selectedPos;
	}
	
	public static int adresaExista(List<BeanAdresaLivrare> listAdrese){
		int adresaExista  = -1;
		
		for (BeanAdresaLivrare adresa : listAdrese){
			if (adresa.getCodJudet().equals(DateLivrare.getInstance().getCodJudet()) && adresa.getOras().toLowerCase().equals(DateLivrare.getInstance().getOras().toLowerCase()) && adresa.getStrada().replace(" ", "").toLowerCase().equals(DateLivrare.getInstance().getStrada().replace(" ","").toLowerCase())) {
				adresaExista = listAdrese.indexOf(adresa);
				break;
			}
			
		}
		
		return adresaExista;
	}

	public static double distanceXtoY(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
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

	public static boolean isDistantaCentruOk(Context context, BeanLocalitate localitate, LatLng coordAdresa) {

		boolean distantaOk = true;

		String[] arrayLoc = localitate.getCoordonate().split(",");
		int distanta = (int) distanceXtoY(Double.parseDouble(arrayLoc[0]), Double.parseDouble(arrayLoc[1]), coordAdresa.latitude, coordAdresa.longitude, "K");

		if (distanta > localitate.getRazaKm())
			distantaOk = false;

		return distantaOk;

	}

	public static BeanLocalitate getDateLocalitate(List<BeanLocalitate> listLocalitati, String localitate) {

		BeanLocalitate beanLocalitate = null;

		for (BeanLocalitate loc : listLocalitati) {

			if (loc.getLocalitate().equalsIgnoreCase(localitate)) {
				beanLocalitate = loc;
				break;
			}

		}

		return beanLocalitate;
	}
	
}
