package my.logon.screen.beans;

import my.logon.screen.utils.UtilsFormatting;

public class DateLivrareCLP {

	private String persContact;
	private String telefon;
	private String adrLivrare;
	private String oras;
	private String judet;
	private String data;
	private String tipMarfa;
	private String masa;
	private String tipCamion;
	private String tipIncarcare;
	private String tipPlata;
	private String mijlocTransport;
	private String aprobatOC;
	private String deSters;
	private String statusAprov;
	private String valComanda;
	private String obsComanda;
	private String valTransp;
	private String procTransp;
	private String acceptDV;
	private String dataIncarcare;
	private String nrCT;

	public DateLivrareCLP() {

	}

	public String getPersContact() {
		return persContact;
	}

	public void setPersContact(String persContact) {
		this.persContact = persContact;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getAdrLivrare() {
		return adrLivrare;
	}

	public void setAdrLivrare(String adrLivrare) {
		this.adrLivrare = adrLivrare;
	}

	public String getOras() {
		return oras;
	}

	public void setOras(String oras) {
		this.oras = oras;
	}

	public String getJudet() {
		return judet;
	}

	public void setJudet(String judet) {
		this.judet = judet;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTipMarfa() {
		return tipMarfa;
	}

	public void setTipMarfa(String tipMarfa) {
		this.tipMarfa = tipMarfa;
	}

	public String getMasa() {
		return masa;
	}

	public void setMasa(String masa) {
		this.masa = masa;
	}

	public String getTipCamion() {
		return tipCamion;
	}

	public void setTipCamion(String tipCamion) {
		this.tipCamion = tipCamion;
	}

	public String getTipIncarcare() {
		return tipIncarcare;
	}

	public void setTipIncarcare(String tipIncarcare) {
		this.tipIncarcare = tipIncarcare;
	}

	public String getTipPlata() {
		return tipPlata;
	}

	public void setTipPlata(String tipPlata) {
		this.tipPlata = tipPlata;
	}

	public String getMijlocTransport() {
		return mijlocTransport;
	}

	public void setMijlocTransport(String mijlocTransport) {
		this.mijlocTransport = mijlocTransport;
	}

	public String getAprobatOC() {
		return aprobatOC;
	}

	public void setAprobatOC(String aprobatOC) {
		this.aprobatOC = aprobatOC;
	}

	public String getDeSters() {
		return deSters;
	}

	public void setDeSters(String deSters) {
		this.deSters = deSters;
	}

	public String getStatusAprov() {
		return statusAprov;
	}

	public void setStatusAprov(String statusAprov) {
		this.statusAprov = statusAprov;
	}

	public String getValComanda() {
		return valComanda;
	}

	public void setValComanda(String valComanda) {
		this.valComanda = valComanda;
	}

	public String getObsComanda() {
		return obsComanda;
	}

	public void setObsComanda(String obsComanda) {
		this.obsComanda = obsComanda;
	}

	public String getValTransp() {
		return valTransp;
	}

	public void setValTransp(String valTransp) {
		this.valTransp = valTransp;
	}

	public String getProcTransp() {
		return procTransp;
	}

	public void setProcTransp(String procTransp) {
		this.procTransp = procTransp;
	}

	public String getAcceptDV() {
		if (acceptDV.trim().length() > 0)
			return "Da";
		return "Nu";
	}

	public void setAcceptDV(String acceptDV) {
		this.acceptDV = acceptDV;
	}

	public String getDataIncarcare() {

		if (dataIncarcare == null || dataIncarcare.length() == 0 || dataIncarcare.equals("00000000"))
			return "";

		return UtilsFormatting.formatDateFromSap(dataIncarcare);
	}

	public void setDataIncarcare(String dataIncarcare) {
		this.dataIncarcare = dataIncarcare;
	}

	public String getNrCT() {
		return nrCT;
	}

	public void setNrCT(String nrCT) {
		this.nrCT = nrCT;
	}

}
