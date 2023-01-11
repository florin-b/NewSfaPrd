package my.logon.screen.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumFilialeLivrare {

	ARGES("Arges - distributie", "AG10"), ARGES2("Arges - Mathaus", "AG20"), ANDRONACHE("Andronache", "BU13"), BACAU("Bacau", "BC10"), BIHOR("Bihor", "BH10"), BUZAU("Buzau", "BZ10"), GLINA("Glina - distributie", "BU10"), GLINA2("Glina - Mathaus", "BU20"), MILITARI(
			"Militari", "BU11"), OTOPENI("Otopeni", "BU12"), BRASOV("Brasov", "BV10"), CLUJ("Cluj", "CJ10"), CONSTANTA("Constanta", "CT10"), DOLJ("Dolj",
			"DJ10"), GALATI("Galati", "GL10"), HUNEDOARA("Hunedoara", "HD10"), IASI("Iasi - distributie", "IS10"), IASI2("Iasi - Mathaus", "IS20"), MARAMURES("Maramures", "MM10"), MURES("Mures", "MS10"), PIATRA_NEAMT(
			"Neamt", "NT10"), PRAHOVA("Prahova", "PH10"), SIBIU("Sibiu", "SB10"),SUCEAVA("Suceava", "SV10"),TIMISOARA("Timisoara", "TM10"), VRANCEA("Vrancea", "VN10");

	private String nume;
	private String cod;

	EnumFilialeLivrare(String nume, String cod) {
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

		for (EnumFilialeLivrare enumF : EnumFilialeLivrare.values()) {
			if (enumF.getNume().equals(numeFiliala))
				return enumF.cod;
		}

		return "";
	}

	public static String getNumeFiliala(String codFiliala) {

		for (EnumFilialeLivrare enumF : EnumFilialeLivrare.values()) {
			if (enumF.getCod().equals(codFiliala))
				return enumF.nume;
		}

		return "";
	}

	public static List<String> getFiliale() {
		List<String> listFiliale = new ArrayList<String>();

		listFiliale.add("Selectati filiala");
		for (EnumFilialeLivrare enumF : EnumFilialeLivrare.values())
			listFiliale.add(enumF.nume);

		return listFiliale;
	}

	public static int getItemPosition(String codFiliala) {
		int pos = 0;
		for (EnumFilialeLivrare enumF : EnumFilialeLivrare.values()) {
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
