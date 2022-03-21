package my.logon.screen.enums;

public enum EnumTipAdresa {
	L_0("Alegeti tipul de adresa", ""), L_1("Sediul social", "01"), L_2("Punct de lucru", "02"), L_3(
			"Santier", "03");

	private String locatieName;
	private String locatieCode;

	private EnumTipAdresa(String name, String code) {
		this.locatieName = name;
		this.locatieCode = code;
	}

	public String getCode() {
		return locatieCode;
	}

	public String getName() {
		return locatieName;
	}
}
