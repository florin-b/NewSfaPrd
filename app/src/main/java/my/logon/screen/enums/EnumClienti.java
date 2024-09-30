package my.logon.screen.enums;

public enum EnumClienti {

	GET_LISTA_CLIENTI("getListClientiCautare"), GET_DETALII_CLIENT("getDetaliiClient"), GET_LISTA_CLIENTI_CV("getListClientiCV"), GET_ADRESE_LIVRARE(
			"getAdreseLivrareClient"), GET_LISTA_MESERIASI("getListMeseriasi"), GET_STARE_TVA("getStareTVA"), GET_MESERIASI("getMeseriasi"), GET_CNP_CLIENT(
			"getDatePersonale"), GET_CLIENTI_INST_PUB("getListClientiInstPublice"), GET_CLIENTI_ALOCATI("getClientiAlocati"), GET_AGENT_COMANDA(
			"getAgentClientInfo"), GET_TERMEN_PLATA("getTermenPlataClientDepart"), GET_INFO_CREDIT("getInfoCreditClient"),
			GET_DATE_CLIENT_ANAF("getDateClientANAF"), CREEAZA_CLIENT_PJ("creeazaClientPJ"), GET_LISTA_CLIENTI_CUI("getListClientiCUI");

	String numeComanda;

	EnumClienti(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
