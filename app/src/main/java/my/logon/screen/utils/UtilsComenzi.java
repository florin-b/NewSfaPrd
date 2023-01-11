package my.logon.screen.utils;

import android.widget.Spinner;

import my.logon.screen.enums.TipCmdDistrib;
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
			return new String[] { "E - Plata in numerar in filiala", "BRD - Card BRD", "ING - Card ING", "UNI - Card Unicredit",
					"CBTR - Card Transilvania", "CGRB - Card Garanti Bonus", "CRFZ - Card Raiffeisen", "CCTL - Card Cetelem", "CAVJ - Card Avantaj",
					"INS - Card online", "E1 - Numerar sofer" };
		else
			return new String[] { "E1 - Numerar sofer", "B - Bilet la ordin", "C - Cec", "E - Plata in numerar in filiala", "O - Ordin de plata",
					"BRD - Card BRD", "ING - Card ING", "UNI - Card Unicredit", "CBTR - Card Transilvania", "CGRB - Card Garanti Bonus",
					"CRFZ - Card Raiffeisen", "CCTL - Card Cetelem", "CAVJ - Card Avantaj", "INS - Card online" };

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

}
