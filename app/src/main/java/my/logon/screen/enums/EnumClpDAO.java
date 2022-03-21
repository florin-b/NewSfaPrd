package my.logon.screen.enums;

public enum EnumClpDAO {

	GET_LIST_COMENZI("getListClp"), GET_ARTICOLE_COMANDA("getListArtClp"), OPERATIE_COMANDA("operatiiClp"), SALVEAZA_COMANDA(
			"saveNewClp"), GET_ARTICOLE_COMANDA_JSON("getListArtClpJSON");

	private String numeComanda;

	EnumClpDAO(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
