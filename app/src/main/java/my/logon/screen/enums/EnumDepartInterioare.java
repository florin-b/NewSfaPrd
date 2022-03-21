package my.logon.screen.enums;

public enum EnumDepartInterioare {

	Parchet("03"), Gips("06"), Chimice("07");

	private String codDepart;

	EnumDepartInterioare(String codDepart) {
		this.codDepart = codDepart;
	}

	public String getCodDepart() {
		return codDepart;
	}

}
