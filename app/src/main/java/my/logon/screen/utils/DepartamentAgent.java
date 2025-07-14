package my.logon.screen.utils;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.UserInfo;

public class DepartamentAgent {

	public static String getDepartamentImplicit(String codDepartament, String tipUser) {

		if (tipUser.equals("KA")) {
			return "Distributie";
		} else {
			return getNumeDepartament(codDepartament);
		}
	}

	public static String getDepartamentAlternativ() {
		return "Magazin";
	}

	private static String getNumeDepartament(String codDepartament) {
		String numeDepart = "";

		if (codDepartament.equals("01")) {
			numeDepart = "Lemnoase";
		}

		if (codDepartament.equals("02")) {
			numeDepart = "Feronerie";
		}

		if (codDepartament.equals("03")) {
			numeDepart = "Parchet";
		}

		if (codDepartament.equals("04")) {
			numeDepart = "Materiale grele";
		}

		if (codDepartament.equals("05")) {
			numeDepart = "Electrice";
		}

		if (codDepartament.equals("06")) {
			numeDepart = "Gips";
		}

		if (codDepartament.equals("07")) {
			numeDepart = "Chimice";
		}

		if (codDepartament.equals("08")) {
			numeDepart = "Instalatii";
		}

		if (codDepartament.equals("09")) {
			numeDepart = "Hidroizolatii";
		}

		if (codDepartament.equals("11")) {
			numeDepart = "Magazin";
		}

		if (codDepartament.equals("00")) {
			numeDepart = "Distributie";
		}

		return numeDepart;
	}

	private static boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	private static boolean isAG() {
		return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("SD");
	}

	public static List<String> getDepartamenteAgent() {

		ArrayList<String> depart = new ArrayList<String>();

		if (isKA() || UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD()
				|| UtilsUser.isUserSDKA() || UtilsUser.isUserCVOB()) {

			for (EnumDepartExtra depKA : EnumDepartExtra.values()) {
				if (depKA.getCod().equals("00") || depKA.getCod().equals("11"))
					depart.add(depKA.getNume());
			}
		}

		else if (isAG()) {

			if (isExtraDiviziiAgent() ){
				depart.add(EnumDepartExtra.getNumeDepart("00"));
			}
			else {

				if (UserInfo.getInstance().getCodDepart().equals("16"))
					depart.add(EnumDepartExtra.getNumeDepart("00"));
				else
					depart.add(EnumDepartExtra.getNumeDepart(UserInfo.getInstance().getCodDepart()));

				if (UserInfo.getInstance().getDepartExtra().length() > 0) {
					String[] depExtra = UserInfo.getInstance().getDepartExtra().split(";");

					for (int i = 0; i < depExtra.length; i++) {

						if (isConditiiAdaugaDepart(depExtra[i]))
							depart.add(EnumDepartExtra.getNumeDepart(depExtra[i]));
					}
				}
			}
			depart.add(EnumDepartExtra.getNumeDepart("11"));

		}

		depart.add("Catalog site");
		return depart;
	}

	private static boolean isConditiiAdaugaDepart(String departExtra) {
		boolean isConditii = false;

		if (departExtra.equals("01") || departExtra.equals("02"))
			isConditii = true;
		else {
			String diviziiClient = DateLivrare.getInstance().getDiviziiClient();

			if (diviziiClient != null && diviziiClient.contains(departExtra.substring(0, 2)))
				isConditii = true;

		}

		return isConditii;
	}

	public static boolean isExtraDiviziiAgent(){

		String diviziiClient = DateLivrare.getInstance().getDiviziiClient();

		return diviziiClient != null && (diviziiClient.split(";").length > 1 || diviziiClient.equals("11;"));
	}

	public static String getDepartArticole(String departImplicit){

		if (UtilsUser.isAV() && DepartamentAgent.isExtraDiviziiAgent()){
			return "EXTRA:" + DateLivrare.getInstance().getDiviziiClient();
		}
		else
			return departImplicit;

	}

	public static List<String> getDepartamenteAgentNerestr() {

		ArrayList<String> depart = new ArrayList<String>();

		if (isKA() || UtilsUser.isInfoUser() || UtilsUser.isUserSDKA() || UtilsUser.isUserSK()) {

			for (EnumDepartExtra depKA : EnumDepartExtra.values()) {
				if (depKA.getCod().equals("00") || depKA.getCod().equals("11"))
					depart.add(depKA.getNume());
			}
		}

		else if (isAG()) {
			depart.add(EnumDepartExtra.getNumeDepart(UserInfo.getInstance().getCodDepart()));

			if (UserInfo.getInstance().getDepartExtra().length() > 0) {
				String[] depExtra = UserInfo.getInstance().getDepartExtra().split(";");

				for (int i = 0; i < depExtra.length; i++) {
					depart.add(EnumDepartExtra.getNumeDepart(depExtra[i]));
				}
			}
			depart.add(EnumDepartExtra.getNumeDepart("11"));
		}

		depart.add("Catalog site");


		return depart;
	}


	public static List<String> getDepartamenteAgentGED() {

		ArrayList<String> depart = new ArrayList<String>();

		if (isKA() || UtilsUser.isInfoUser()) {

			for (EnumDepartExtra depKA : EnumDepartExtra.values()) {
				if (depKA.getCod().equals("00") || depKA.getCod().equals("11"))
					depart.add(depKA.getNume());
			}
		}

		else if (isAG()) {

			if (isExtraDiviziiAgent() ){
				depart.add(EnumDepartExtra.getNumeDepart("00"));
			}
			else {

				if (UserInfo.getInstance().getCodDepart().equals("16"))
					depart.add(EnumDepartExtra.getNumeDepart("00"));
				else
					depart.add(EnumDepartExtra.getNumeDepart(UserInfo.getInstance().getCodDepart()));

				if (UserInfo.getInstance().getCodDepart().startsWith("04") && UserInfo.getInstance().getDepartExtra().length() > 0) {
					String[] depExtra = UserInfo.getInstance().getDepartExtra().split(";");

					for (int i = 0; i < depExtra.length; i++) {
						depart.add(EnumDepartExtra.getNumeDepart(depExtra[i]));
					}
				}
			}

			depart.add(EnumDepartExtra.getNumeDepart("11"));
			depart.add("Catalog site");

		}else {
			depart.add(EnumDepartExtra.getNumeDepart("11"));
			depart.add("Catalog site");
		}

		return depart;
	}


	public static List<String> getDepartamenteAgentCLP(String diviziiClient) {

		ArrayList<String> depart = new ArrayList<String>();

		depart.add(EnumDepartExtra.getNumeDepart(UserInfo.getInstance().getCodDepart()));

		if (UserInfo.getInstance().getDepartExtra().length() > 0) {
			String[] depExtra = UserInfo.getInstance().getDepartExtra().split(";");

			for (int i = 0; i < depExtra.length; i++) {
				//if (diviziiClient != null && diviziiClient.contains(depExtra[i].substring(0, 2)))
					depart.add(EnumDepartExtra.getNumeDepart(depExtra[i]));
			}
		}
		depart.add(EnumDepartExtra.getNumeDepart("11"));

		return depart;
	}

}
