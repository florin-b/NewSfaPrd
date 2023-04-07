package my.logon.screen.utils;

import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.InfoStrings;

public class UtilsComenzi {

	public static String getStareComanda(int codStare) {
		String numeStare = "";

		switch (codStare) {

		case 1:
			numeStare = "Masina alocata";
			break;

		case 2:
			numeStare = "Borderou tiparit";
			break;

		case 3:
			numeStare = "Inceput incarcare";
			break;

		case 4:
			numeStare = "Terminat incarcare";
			break;

		case 6:
			numeStare = "Plecat in cursa";
			break;

		default:
			break;
		}

		return numeStare;
	}

	public static boolean isComandaGed(String unitLog, String codClient) {
		String distribUL = InfoStrings.getDistribUnitLog(unitLog);

		if (InfoStrings.getClientGenericGed(distribUL, "PF").equals(codClient) || InfoStrings.getClientGenericGed(distribUL, "PJ").equals(codClient)
				|| InfoStrings.getClientGenericGedWood(distribUL, "PF").equals(codClient)
				|| InfoStrings.getClientGenericGedWood(distribUL, "PJ").equals(codClient)
				|| InfoStrings.getClientGenericGedWood_faraFact(distribUL, "PF").equals(codClient)
				|| InfoStrings.getClientGenericGed_CONSGED_faraFactura(distribUL, "PF").equals(codClient)
				|| InfoStrings.getClientCVO_cuFact_faraCnp(distribUL, "").equals(codClient)
				|| InfoStrings.getClientGed_FaraFactura(distribUL).equals(codClient))

			return true;
		else
			return false;
	}

	public static String[] tipPlataGed(boolean isRestrictie) {
		if (isRestrictie)
			return new String[] { "E - Plata in numerar in filiala", "CB - Card bancar", "E1 - Numerar sofer" };
		else
			return new String[] { "E1 - Numerar sofer", "B - Bilet la ordin", "C - Cec", "E - Plata in numerar in filiala", "O - Ordin de plata",
					"CB - Card bancar" };

	}

	public static void setDefaultPlataMethod(Spinner spinnerPlata) {

		for (int ii = 0; ii < spinnerPlata.getAdapter().getCount(); ii++) {
			if (spinnerPlata.getAdapter().getItem(ii).toString().toUpperCase().contains("E1")) {
				spinnerPlata.setSelection(ii);
				break;
			}
		}

	}

	public static boolean isLivrareCustodie() {
		return DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.LIVRARE_CUSTODIE;
	}

	public static boolean isComandaInstPublica() {
		return DateLivrare.getInstance().getTipPersClient() != null && DateLivrare.getInstance().getTipPersClient().toUpperCase().equals("IP");
	}

	public static boolean isComandaExpirata(List<ArticolComanda> listArticole) {
		boolean isExpirata = false;

		Date dateCrt = new Date();

		try {

			for (ArticolComanda articol : listArticole) {
				if (articol.getDataExpPret().startsWith("00"))
					continue;

				Date dateArt = new SimpleDateFormat("yyyy-MM-dd").parse(articol.getDataExpPret());

				if (dateArt.compareTo(dateCrt) < 0) {
					isExpirata = true;
					break;
				}

			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return isExpirata;
	}

	public static String getFilialaGed(String filiala) {
		return filiala.substring(0, 2) + "2" + filiala.substring(3, 4);
	}

	public static String getTipPlataClient(String tipPlataSelect, String tipPlataContract) {

		if (tipPlataSelect.equals("LC"))
			return tipPlataContract;
		else if (tipPlataSelect.equals("N"))
			return "E";
		else if (tipPlataSelect.equals("OPA"))
			return "O";
		else if (tipPlataSelect.equals("R"))
			return "E1";
		else if (tipPlataSelect.equals("C"))
			return "CB";

		return tipPlataSelect;

	}

	public static String setTipPlataClient(String tipPlataClient) {

		if (tipPlataClient.equals("E"))
			return "N";
		else if (tipPlataClient.equals("O"))
			return "OPA";
		else if (tipPlataClient.equals("E1"))
			return "R";
		if (tipPlataClient.equals("CB"))
			return "C";

		return tipPlataClient;
	}

	public static boolean isComandaClp() {
		return !DateLivrare.getInstance().getCodFilialaCLP().trim().isEmpty() && DateLivrare.getInstance().getCodFilialaCLP().trim().length() == 4;
	}

	public static boolean isDespozitDeteriorate(String depozit){

		List<String> listDepozDeteriorate = Arrays.asList("01D1","01D2","02D1","02D2","03D1","03D2","04D1","04D2","05D1","05D2","06D1","06D2","07D1","07D2","08D1","08D2","09D1","09D2","MAD1","MAD2") ;

		for (String depoz : listDepozDeteriorate){
			if (depoz.equals(depozit))
				return true;
		}

		return false;

	}

}
