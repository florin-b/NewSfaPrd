package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.utils.UtilsGeneral;

public class OperatiiFiliala {

	private ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();

	private OperatiiFiliala() {

	}

	public static OperatiiFiliala getInstance() {
		return new OperatiiFiliala();
	}

	public ArrayList<HashMap<String, String>> getListToateFiliale() {

		HashMap<String, String> temp;
		temp = new HashMap<String, String>();
		temp.put("numeFiliala", "Filiala");
		temp.put("codFiliala", " ");
		listFiliale.add(temp);

		for (int i = 0; i < UtilsGeneral.numeFiliale.length; i++) {
			temp = new HashMap<String, String>();

			temp.put("numeFiliala", UtilsGeneral.numeFiliale[i]);
			temp.put("codFiliala", UtilsGeneral.codFiliale[i]);

			listFiliale.add(temp);
		}

		return listFiliale;
	}

	public ArrayList<HashMap<String, String>> getListFilialeDV(String filialeDV) {
		HashMap<String, String> temp;
		String[] tokenLinie = filialeDV.split(";");

		if (UserInfo.getInstance().getFilialeDV().trim().length() > 0) {
			for (int i = 0; i < tokenLinie.length; i++) {

				temp = new HashMap<String, String>(30, 0.75f);
				temp.put("numeFiliala", ClientiGenericiGedInfoStrings.getNumeUL(tokenLinie[i]));
				temp.put("codFiliala", tokenLinie[i]);

				listFiliale.add(temp);
			}

		} else {
			temp = new HashMap<String, String>(1, 0.75f);
			temp.put("numeFiliala", "Nedefinit");
			temp.put("codFiliala", "ND10");

			listFiliale.add(temp);
		}

		return listFiliale;
	}

	public ArrayList<HashMap<String, String>> getListFilialeBuc() {

		HashMap<String, String> temp;

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("numeFiliala", "BUC. GLINA");
		temp.put("codFiliala", "BU10");
		listFiliale.add(temp);

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("numeFiliala", "BUC. MILITARI");
		temp.put("codFiliala", "BU11");
		listFiliale.add(temp);

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("numeFiliala", "BUC. OTOPENI");
		temp.put("codFiliala", "BU12");
		listFiliale.add(temp);

		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("numeFiliala", "BUC. ANDRONACHE");
		temp.put("codFiliala", "BU13");
		listFiliale.add(temp);

		return listFiliale;
	}

}
