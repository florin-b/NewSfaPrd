package my.logon.screen.enums;

public enum EnumRetur {

	GET_LISTA_DOCUMENTE("getDocumenteRetur"), GET_ARTICOLE_DOCUMENT("getArticoleRetur"), SAVE_COMANDA_RETUR("saveComandaRetur"), GET_COMENZI_SALVATE(
			"getListDocumenteSalvate"), GET_ARTICOLE_COMANDA_SALVATA("getArticoleDocumentSalvat"), OPEREAZA_COMANDA("opereazaComandaRetur"), GET_ARTICOLE_COMANDA_SAP(
			"getArticoleDocumentReturSAP"), GET_STOC_RETUR_AVANSAT("getStocReturAvansat"), SAVE_LIST_COMENZI_RETUR("saveListComenziRetur");

	String numeComanda;

	EnumRetur(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
