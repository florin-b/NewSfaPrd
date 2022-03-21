package my.logon.screen.enums;

public enum EnumOperatiiObiective {
	SALVEAZA_OBIECTIV("salveazaObiectiv"), GET_LIST_OBIECTIVE("getListObiectiveKA"), GET_LIST_OBIECTIVE_AV("getListObiectiveAgenti"), GET_DETALII_OBIECTIV(
			"getDetaliiObiectiv"), GET_STARE_CLIENT("getStareClient"), GET_CLIENTI_OBIECTIV("getClientiObiectiv"), SALVEAZA_EVENIMENT(
			"salveazaEvenimentObiectiv"), GET_EVENIMENTE_CLIENT("getEvenimenteObiectiv"), GET_OBIECTIVE_DEPARTAMENT("getObiectiveDepartament"), GET_OBIECTIVE_HARTA(
			"getListObiectiveKAHarta"), GET_OBIECTIVE_CONSILIERI("getObiectiveConsilieri");

	private String numeObiectiv;

	EnumOperatiiObiective(String numeObiectiv) {
		this.numeObiectiv = numeObiectiv;
	}

	public String getNume() {
		return numeObiectiv;
	}

}
