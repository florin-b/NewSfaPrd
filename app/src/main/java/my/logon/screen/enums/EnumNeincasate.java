package my.logon.screen.enums;

public enum EnumNeincasate {
	GET_ART_DOC_NEINCASAT("getArtDocNeincasat"), GET_FACTURI_NEINCASATE("getFacturiNeincasate"), SALVEAZA_PLATA("salveazaPlataNeincasata");

	private String numeComanda;

	EnumNeincasate(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
