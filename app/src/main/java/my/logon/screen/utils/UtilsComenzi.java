package my.logon.screen.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.DatePoligonLivrare;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.model.ListaArticoleModificareComanda;

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
		String distribUL = ClientiGenericiGedInfoStrings.getDistribUnitLog(unitLog);

		if (ClientiGenericiGedInfoStrings.getClientGenericGed(distribUL, "PF").equals(codClient) || ClientiGenericiGedInfoStrings.getClientGenericGed(distribUL, "PJ").equals(codClient)
				|| ClientiGenericiGedInfoStrings.getClientGenericGedWood(distribUL, "PF").equals(codClient)
				|| ClientiGenericiGedInfoStrings.getClientGenericGedWood(distribUL, "PJ").equals(codClient)
				|| ClientiGenericiGedInfoStrings.getClientGenericGedWood_faraFact(distribUL, "PF").equals(codClient)
				|| ClientiGenericiGedInfoStrings.getClientGenericGed_CONSGED_faraFactura(distribUL, "PF").equals(codClient)
				|| ClientiGenericiGedInfoStrings.getClientCVO_cuFact_faraCnp(distribUL, "").equals(codClient)
				|| ClientiGenericiGedInfoStrings.getClientGed_FaraFactura(distribUL).equals(codClient))

			return true;
		else
			return false;
	}

	public static String[] tipPlataGed_obsolete(boolean isRestrictie) {
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

	public static boolean isDepozitTCLI(String depozit, String departament){
		String depozitDistrib = departament.substring(0,2) + "V1";

		if (depozit.equals(depozitDistrib) || depozit.equals("MAV1"))
			return true;

		return false;
	}

	public static boolean isDepozitUnitLog(String depozitStoc, List<String> listDepozite){

		for (String depozit : listDepozite){
			if (depozit.toLowerCase().equals(depozitStoc.toLowerCase()))
				return true;
		}

		return false;
	}

	public static BeanStocTCLI getStocMaxim(List<BeanStocTCLI> listStocuri){

		if (listStocuri == null || listStocuri.isEmpty()) {
			return null;
		}

		BeanStocTCLI stocMaxim = listStocuri.get(0);

		for (BeanStocTCLI stoc : listStocuri){
			if (stoc.getCantitate() > stocMaxim.getCantitate())
				stocMaxim = stoc;
		}


		return stocMaxim;
	}

	public static double getGreutateKgArticole(List<ArticolComanda> listArticole){
		double greutate = 0;

		for (ArticolComanda articol : listArticole) {
			greutate += articol.getGreutateBruta();

		}

		return greutate;
	}

	public static boolean isComandaEnergofaga(List<ArticolComanda> listArticole){

		for (ArticolComanda articol : listArticole) {
			if (articol.getTipMarfa() != null && articol.getTipMarfa().equals(Constants.TIP_ARTICOL_ENERGOFAG))
				return true;
		}

		return false;
	}

	public static boolean isComandaExtralungi(List<ArticolComanda> listArticole){

		for (ArticolComanda articol : listArticole) {
			if (articol.getLungimeArt() != null && articol.getLungimeArt().toLowerCase().equals("extralungi"))
				return true;
		}

		return false;
	}



	public static ArrayList<HashMap<String, String>> fillFilialeFasonate() {

		ArrayList<HashMap<String, String>> listFilialeFasonate = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> temp;
		int i = 0;

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Selectati filiala");
		temp.put("codJudet", "");
		listFilialeFasonate.add(temp);

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Buc. Glina");
		temp.put("codJudet", "BU10");
		listFilialeFasonate.add(temp);

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Brasov");
		temp.put("codJudet", "BV10");
		listFilialeFasonate.add(temp);

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Iasi");
		temp.put("codJudet", "IS10");
		listFilialeFasonate.add(temp);

		return listFilialeFasonate;

	}

	public static String getFilialaDistrib(String filiala) {

		if (filiala == null || filiala.trim().isEmpty())
			return "";

		return filiala.substring(0, 2) + "1" + filiala.substring(3, 4);

	}

	public static boolean isComandaDl() {
		return DateLivrare.getInstance().getFurnizorComanda() != null && !DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().isEmpty()
				&& DateLivrare.getInstance().getFurnizorComanda().getCodFurnizorMarfa().length() > 4;
	}


	public static boolean isAdresaUnitLogModifCmd(Context context, String filialaModifComanda, DatePoligonLivrare poligonLivrareon){

		if (filialaModifComanda == null || filialaModifComanda.trim().isEmpty())
			return true;

		if (poligonLivrareon == null)
			return true;

		if (isComandaDl())
			return true;

		if (UtilsUser.isUserSite() || UtilsUser.isUserCVO())
			return true;

		if (!DateLivrare.getInstance().getTransport().equals("TRAP"))
			return true;

		if (isComandaModifBV90())
			return true;

		if (!UtilsComenzi.getFilialaDistrib(filialaModifComanda).equals(poligonLivrareon.getFilialaPrincipala()) &&
				!UtilsComenzi.getFilialaDistrib(filialaModifComanda).equals(poligonLivrareon.getFilialaSecundara())) {

			StringBuilder infoMsg = new StringBuilder();
			infoMsg.append("\n");
			infoMsg.append("Completati o adresa de livrare arondata filialei " + filialaModifComanda + ".");
			infoMsg.append("\n");

			UtilsComenzi.showAlertDialog(context, infoMsg.toString());

			return false;
		}

		return true;

	}

	private static boolean isComandaModifBV90() {

		List<ArticolComanda> listArtCmdModif = null;

		if (ListaArticoleComandaGed.getInstance() != null && ListaArticoleComandaGed.getInstance().getListArticoleComanda()!= null &&
				ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() > 0)
			listArtCmdModif = ListaArticoleComandaGed.getInstance().getListArticoleComanda();
		else
		if (ListaArticoleModificareComanda.getInstance() != null && ListaArticoleModificareComanda.getInstance().getListArticoleComanda()!= null &&
				ListaArticoleModificareComanda.getInstance().getListArticoleComanda().size() > 0)
			listArtCmdModif = ListaArticoleModificareComanda.getInstance().getListArticoleComanda();

		if (listArtCmdModif == null)
			return false;

		boolean isBV90 = false;

		for (ArticolComanda articol : listArtCmdModif) {
			if (articol.getFilialaSite().equals("BV90")) {
				isBV90 = true;
				break;
			}
		}

		return isBV90;
	}



	public static void showFilialaLivrareDialog(Context context, String filiala){
		StringBuilder infoMsg = new StringBuilder();
		infoMsg.append("\n");
		infoMsg.append("Completati o adresa de livrare arondata filialei " + filiala + ".");
		infoMsg.append("\n");

		UtilsComenzi.showAlertDialog(context, infoMsg.toString());
	}



	public static void showAlertDialog(Context context, String message){

		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
		dlgAlert.setMessage(message);
		dlgAlert.setTitle("Atentie!");
		dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	public static boolean isArticolCustodieDistrib(ArticolComanda articolComanda){

		if (!DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.LIVRARE_CUSTODIE))
			return false;

		if (!articolComanda.isUmPalet() && !articolComanda.getCodArticol().replaceFirst("^0*", "").startsWith("30"))
			return true;

		return false;



	}

	public static boolean isModifTCLIinTRAP(DatePoligonLivrare poligonLivrare){
		return DateLivrare.getInstance().getTransport().equals("TRAP") && DateLivrare.getInstance().getFilialaLivrareTCLI() != null &&
				!DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog().equals(poligonLivrare.getFilialaPrincipala());

	}

	public static String getSpinnerTipTransp(String tipTransport){
		if (tipTransport.toLowerCase().contains("livrare"))
			return "TRAP";

		return "TCLI";
	}

	public static boolean comandaAreArticole(String canal){
		if (canal.equals("10"))
			return ListaArticoleComanda.getInstance().getListArticoleComanda() != null && ListaArticoleComanda.getInstance().getListArticoleComanda().size() > 0;
		else
			return ListaArticoleComandaGed.getInstance().getListArticoleComanda() != null && ListaArticoleComandaGed.getInstance().getListArticoleComanda().size() > 0;
	}

}
