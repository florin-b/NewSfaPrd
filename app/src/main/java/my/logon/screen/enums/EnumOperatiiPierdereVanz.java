package my.logon.screen.enums;

public enum EnumOperatiiPierdereVanz {
	GET_VANZ_PIERD_AG("getVanzariPierduteAv"), GET_VANZ_PIERD_DEP("getVanzariPierduteDepart"), GET_VANZ_PIERD_TOTAL("getVanzariPierduteTotal");

	private String numeOperatie;

	EnumOperatiiPierdereVanz(String numeOperatie) {
		this.numeOperatie = numeOperatie;
	}

	public String getNume() {
		return numeOperatie;
	}

}
