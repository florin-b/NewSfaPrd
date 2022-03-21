package my.logon.screen.enums;

public enum EnumDepartExtra {

	DISTRIBUTIE("Distributie", "00"), LEMNOASE("Lemnoase", "01"), FERONERIE("Feronerie", "02"), PARCHET("Parchet", "03"), MATERIALE_GRELE("Materiale grele",
			"04"), FEROASE("Feroase", "040"), PRAFOASE("Prafoase", "041"), ELECTRICE("Electrice", "05"), GIPS("Gips", "06"), CHIMICE("Chimice", "07"), INSTALATII(
			"Instalatii", "08"), HIDROIZOLATII("Hidroizolatii", "09"), MAGAZIN("Magazin", "11");

	private String nume;
	private String cod;

	EnumDepartExtra(String nume, String cod) {
		this.nume = nume;
		this.cod = cod;
	}

	public static String getNumeDepart(String codDepart) {
		for (EnumDepartExtra enumDep : EnumDepartExtra.values()) {
			if (enumDep.cod.equals(codDepart))
				return enumDep.nume;
		}
		return "";
	}

	public static String getCodDepart(String numeDepart) {
		for (EnumDepartExtra enumDep : EnumDepartExtra.values()) {
			if (enumDep.nume.equals(numeDepart))
				return enumDep.cod;
		}
		return "";
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

}
