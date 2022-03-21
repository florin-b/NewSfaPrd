package my.logon.screen.enums;

public enum EnumTipReducere {

	FACT_RED_SEP("1 fact (red separat)", "R"), FACT_2("2 facturi", "X"), FACT_RED_PRET("1 fact (red in pret)", " ");

	String codReducere;
	String numeReducere;

	EnumTipReducere(String numeReducere, String codReducere) {
		this.numeReducere = numeReducere;
		this.codReducere = codReducere;
	}

	public String getNumeReducere() {
		return numeReducere;
	}

	public String getCodReducere() {
		return codReducere;
	}

	public static String getCodReducere(String numeReducere) {
		String codReducere = "";
		for (EnumTipReducere enumRed : EnumTipReducere.values()) {
			if (enumRed.getNumeReducere().equals(numeReducere)) {
				codReducere = enumRed.getCodReducere();
				break;
			}
		}

		return codReducere;
	}
	
	public static EnumTipReducere getEnumReducere(String numeReducere) {
		for (EnumTipReducere enumRed : EnumTipReducere.values())
			if (enumRed.numeReducere == numeReducere)
				return enumRed;

		return null;
	}

}
