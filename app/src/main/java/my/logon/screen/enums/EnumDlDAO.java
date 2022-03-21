package my.logon.screen.enums;

public enum EnumDlDAO {

	GET_LIST_COMENZI("getListDl"), GET_ARTICOLE_COMANDA("getListArtClp"), OPERATIE_COMANDA("operatiiDl"), SALVEAZA_COMANDA(
			"saveNewDl"), GET_ARTICOLE_COMANDA_JSON("getListArtClpJSON");

	private String numeComanda;

	EnumDlDAO(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
