package my.logon.screen.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumMotivRespArticol {
	M900("1", "Lipsuri"), M901("2", "Deteriorate"), M902("3", "Incurcate"), M903("4", "Viciu furnizor"), M904("5", "Caracteristici site diferite"), M905(
			"6", "Exceptie DZ");

	private String cod;
	private String nume;

	EnumMotivRespArticol(String cod, String nume) {
		this.cod = cod;
		this.nume = nume;
	}

	public String getCod() {
		return cod;
	}

	public String getNume() {
		return nume;
	}

	public static List<String> getStringList() {
		List<String> listMotive = new ArrayList<String>();

		for (EnumMotivRespArticol enumM : EnumMotivRespArticol.values()) {
			listMotive.add(enumM.nume);
		}

		return listMotive;

	}

	public static String getCodRetur(String numeRetur) {

		for (EnumMotivRespArticol enumM : EnumMotivRespArticol.values()) {
			if (enumM.nume.equals(numeRetur))
				return enumM.cod;

		}

		return "";
	}

	public static String getNumeRetur(String codRetur) {

		for (EnumMotivRespArticol enumM : EnumMotivRespArticol.values()) {
			if (enumM.cod.equals(codRetur))
				return enumM.nume;

		}

		return "";
	}

}
