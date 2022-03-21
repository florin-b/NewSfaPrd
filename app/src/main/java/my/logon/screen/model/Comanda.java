package my.logon.screen.model;

public class Comanda {
	private String codClient;
	private String numeClient;
	private String cnpClient;
	private String persoanaContact;
	private String telefon;
	private String cantarire;
	private String metodaPlata;
	private String tipTransport;
	private String comandaBlocata;
	private String nrCmdSap;
	private String alerteKA;
	private String factRedSeparat;
	private String filialaAlternativa;
	private String userSite;
	private String userSiteMail;
	private String isValIncModif;
	private String codJ;
	private String adresaLivrareGed;
	private String adresaLivrare;
	private String valoareIncasare;
	private String conditieID;
	private String necesarAprobariCV;
	private String valTransportSap;
	private String nrDocumentClp;

	public Comanda() {

	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getPersoanaContact() {
		return persoanaContact;
	}

	public void setPersoanaContact(String persoanaContact) {
		this.persoanaContact = persoanaContact;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getCantarire() {
		return cantarire;
	}

	public void setCantarire(String cantarire) {
		this.cantarire = cantarire;
	}

	public String getMetodaPlata() {
		return metodaPlata;
	}

	public void setMetodaPlata(String metodaPlata) {
		this.metodaPlata = metodaPlata;
	}

	public String getTipTransport() {
		return tipTransport;
	}

	public void setTipTransport(String tipTransport) {
		this.tipTransport = tipTransport;
	}

	public String getComandaBlocata() {
		return comandaBlocata;
	}

	public void setComandaBlocata(String comandaBlocata) {
		this.comandaBlocata = comandaBlocata;
	}

	public String getNrCmdSap() {
		return nrCmdSap;
	}

	public void setNrCmdSap(String nrCmdSap) {
		this.nrCmdSap = nrCmdSap;
	}

	public String getAlerteKA() {
		return alerteKA;
	}

	public void setAlerteKA(String alerteKA) {
		this.alerteKA = alerteKA;
	}

	public String getFactRedSeparat() {
		return factRedSeparat;
	}

	public void setFactRedSeparat(String factRedSeparat) {
		this.factRedSeparat = factRedSeparat;
	}

	public String getFilialaAlternativa() {
		return filialaAlternativa;
	}

	public void setFilialaAlternativa(String filialaAlternativa) {
		this.filialaAlternativa = filialaAlternativa;
	}

	public String getUserSite() {
		return userSite;
	}

	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}

	public String getUserSiteMail() {
		return userSiteMail;
	}

	public void setUserSiteMail(String userSiteMail) {
		this.userSiteMail = userSiteMail;
	}

	public String getIsValIncModif() {
		return isValIncModif;
	}

	public void setIsValIncModif(String isValIncModif) {
		this.isValIncModif = isValIncModif;
	}

	public String getCodJ() {
		return codJ;
	}

	public void setCodJ(String codJ) {
		this.codJ = codJ;
	}

	public String getAdresaLivrareGed() {
		return adresaLivrareGed;
	}

	public void setAdresaLivrareGed(String adresaLivrareGed) {
		this.adresaLivrareGed = adresaLivrareGed;
	}

	public String getAdresaLivrare() {
		return adresaLivrare;
	}

	public void setAdresaLivrare(String adresaLivrare) {
		this.adresaLivrare = adresaLivrare;
	}

	public String getValoareIncasare() {
		return valoareIncasare;
	}

	public void setValoareIncasare(String valoareIncasare) {
		this.valoareIncasare = valoareIncasare;
	}

	public String getConditieID() {
		return conditieID;
	}

	public void setConditieID(String conditieID) {
		this.conditieID = conditieID;
	}

	public String getNumeClient() {
		if (numeClient == null)
			return " ";
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCnpClient() {
		return cnpClient;
	}

	public void setCnpClient(String cnpClient) {
		this.cnpClient = cnpClient;
	}

	public String getNecesarAprobariCV() {
		return necesarAprobariCV;
	}

	public void setNecesarAprobariCV(String aprobariCV) {
		this.necesarAprobariCV = aprobariCV;
	}

	public String getValTransportSap() {
		return valTransportSap;
	}

	public void setValTransportSap(String valTransportSap) {
		this.valTransportSap = valTransportSap;
	}

	public String getNrDocumentClp() {
		return nrDocumentClp;
	}

	public void setNrDocumentClp(String nrDocumentClp) {
		this.nrDocumentClp = nrDocumentClp;
	}

}
