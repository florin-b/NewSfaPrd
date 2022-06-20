package my.logon.screen.enums;

public enum EnumOperatiiMathaus {

	GET_CATEGORII("getCategoriiMathaus"), GET_ARTICOLE("getArticoleCategorieMathaus"), CAUTA_ARTICOLE("cautaArticoleMathaus"), CAUTA_ARTICOLE_LOCAL("cautaArticoleLocal");
	
	private String numeComanda;

	EnumOperatiiMathaus(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
