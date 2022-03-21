package my.logon.screen.enums;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.model.UserInfo;

public enum EnumFilialeKA {

	NN10("Selectati o filiala", "00"), ARGES("Arges", "AG10"), ANDRONACHE("Andronache", "BU13"), BACAU("Bacau", "BC10"), BIHOR("Bihor", "BH10"), BUZAU("Buzau",
			"BZ10"), GLINA("Glina", "BU10"), MILITARI("Militari", "BU11"), OTOPENI("Otopeni", "BU12"), BRASOV("Brasov", "BV10"), CLUJ("Cluj", "CJ10"), CONSTANTA(
			"Constanta", "CT10"), DOLJ("Dolj", "DJ10"), GALATI("Galati", "GL10"), HUNEDOARA("Hunedoara", "HD10"), IASI("Iasi", "IS10"), MARAMURES("Maramures",
			"MM10"), MURES("Mures", "MS10"), PIATRA_NEAMT("Neamt", "NT10"), PRAHOVA("Prahova", "PH10"), TIMISOARA("Timisoara", "TM10"), VRANCEA("Vrancea",
			"VN10");

	private String nume;
	private String cod;

	EnumFilialeKA(String nume, String cod) {
		this.nume = nume;
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public static String getCodFiliala(String numeFiliala) {

		for (EnumFilialeKA enumF : EnumFilialeKA.values()) {
			if (enumF.getNume().equals(numeFiliala))
				return enumF.cod;
		}

		return "";
	}

	public static String getNumeFiliala(String codFiliala) {

		for (EnumFilialeKA enumF : EnumFilialeKA.values()) {
			if (enumF.getCod().equals(codFiliala))
				return enumF.nume;
		}

		return "";
	}

	public static EnumFilialeKA[] getFilialeSK() {

		if (UserInfo.getInstance().getUnitLog().contains("BU")) {
			return new EnumFilialeKA[] { EnumFilialeKA.NN10, EnumFilialeKA.ANDRONACHE, EnumFilialeKA.GLINA, EnumFilialeKA.OTOPENI, EnumFilialeKA.MILITARI };
		}

		return new EnumFilialeKA[] { getFiliala(UserInfo.getInstance().getUnitLog()) };

	}

	private static EnumFilialeKA getFiliala(String unitLog) {
		for (EnumFilialeKA enumF : EnumFilialeKA.values()) {
			if (enumF.getCod().equals(unitLog))
				return enumF;

		}
		return null;
	}

	public static List<String> getFiliale() {
		List<String> listFiliale = new ArrayList<String>();

		listFiliale.add("Selectati filiala");
		for (EnumFilialeKA enumF : EnumFilialeKA.values())
			listFiliale.add(enumF.nume);

		return listFiliale;
	}

	public static int getItemPosition(String codFiliala) {
		int pos = 0;
		for (EnumFilialeKA enumF : EnumFilialeKA.values()) {
			if (enumF.getCod().equals(codFiliala)) {
				return pos;
			}
			pos++;
		}

		return -1;
	}

	@Override
	public String toString() {
		return this.nume;
	}

}
