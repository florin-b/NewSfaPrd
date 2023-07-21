package my.logon.screen.enums;

public enum EnumComenziDAO {

    GET_LIST_COMENZI("getListComenzi"), GET_ARTICOLE_COMANDA("getCmdArt"), SALVEAZA_CONDITII_COMANDA("saveCondAndroid"), GET_LISTA_RESPINGERE(
            "getListMotiveRespingere"), OPERATIE_COMANDA("operatiiComenzi"), SALVEAZA_COMANDA_DISTRIB("saveNewCmdAndroid"), SALVEAZA_COMANDA_GED("saveCmdGED"), GET_ARTICOLE_COMANDA_JSON(
            "getArticoleComandaVanzare"), SALVEAZA_CONDITII_COMANDA_SER("salveazaConditiiComanda"), SEND_OFERTA_GED_MAIL("sendMailOfertaGed"), GET_COMENZI_DESCHISE(
            "getComenziDeschise"), GET_CLIENTI_BORD("getClientiBorderou"), GET_POZITIE_MASINA("getPozitieMasina"), GET_COST_MACARA("getCostMacara"), GET_STARE_COMANDA(
            "getStareComanda"), SALVEAZA_LIVRARE_CUSTODIE("saveLivrareCustodie"), GET_LIVRARI_CUSTODIE("getLivrariCustodie"), GET_ARTICOLE_CUSTODIE(
            "getArticoleCustodie"), SET_CUSTODIE_DATA_LIVRARE("setCustodieDataLivrare"), STERGE_LIVRARE_CUSTODIE("stergeLivrareCustodie"), SET_CUSTODIE_ADRESA_LIVRARE(
            "setCustodieAdresaLivrare"), GET_COMENZI_AMOB_AFIS("getListComenziAMOBAfis"), GET_ARTICOLE_AMOB("getArticoleComandaAMOB"), SET_CMD_VANZ_DATA_LIVRARE(
            "setCmdVanzDataLivrare"), UPDATE_COM_SIM("updateComandaSimulata"), GET_LIVRARI_MATHAUS("getLivrariMathaus"), GET_COST_MACARA_COMENZI("getCostMacaraComenzi"),
    GET_TOTAL_COMENZI_NUMERAR("getValoareComenziNumerar"), GET_LIST_DOCRETUR_STARE("getListDocReturStare"), GET_LIST_ARTICOLE_RETUR("getArticoleDocReturStare"), GET_STATUS_COMANDA_RETUR("getStareDocumentRetur"),
    GET_LIVRARI_MATHAUS_TCLI("getLivrariMathaus"), GET_PROC_MARJA_IP("getProcMarjaComenziIP"), GET_OPTIUNI_MASINI("getOptiuniMasini");

    private String numeComanda;

    EnumComenziDAO(String numeComanda) {
        this.numeComanda = numeComanda;
    }

    public String getComanda() {
        return numeComanda;
    }

}
