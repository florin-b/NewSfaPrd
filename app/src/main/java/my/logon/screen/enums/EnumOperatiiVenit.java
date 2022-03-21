package my.logon.screen.enums;

public enum EnumOperatiiVenit {

	GET_VENIT_AG("salarizareAV"), GET_VENIT_NTCF("getNTCF");

	private String numeComanda;

	EnumOperatiiVenit(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

	public void setNumeComanda(String numeComanda) {
		this.numeComanda = numeComanda;
	}

}
