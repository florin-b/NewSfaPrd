package my.logon.screen.enums;

public enum EnumCategorieObiectiv {

	CIVIL("Civil", "01"), INDISTRIAL("Industrial", "02"), INFRASTRUCTURA("Infrastructura", "03"), SPECIAL("Special",
			"04");

	private String numeCategorie;
	private String codCategorie;

	EnumCategorieObiectiv(String numeCategorie, String codCategorie) {
		this.numeCategorie = numeCategorie;
		this.codCategorie = codCategorie;
	}

	public String getNumeCategorie() {
		return numeCategorie;
	}

	public String getCodCategorie() {
		return codCategorie;
	}

	public static String getNumeCategorie(String codCategorie) {
		String numeCategorie = "";

		for (EnumCategorieObiectiv cat : EnumCategorieObiectiv.values()) {
			if (cat.getCodCategorie().equals(codCategorie)) {
				numeCategorie = cat.getNumeCategorie();
				break;
			}

		}

		return numeCategorie;
	}

}
