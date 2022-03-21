package my.logon.screen.beans;

public class AntetComandaCLP {

	private String codClient;
	private String codJudet;
	private String localitate;
	private String strada;
	private String persCont;
	private String telefon;
	private String codFilialaDest;
	private String dataLivrare;
	private String tipPlata;
	private String tipTransport;
	private String depozDest;
	private String selectedAgent;
	private boolean cmdFasonate;
	private String numeClientCV;
	private String observatiiCLP;
	private String tipMarfa;
	private String masaMarfa;
	private String tipCamion;
	private String tipIncarcare;
	private String tonaj;
	private String prelucrare;

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getCodJudet() {
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}

	public String getLocalitate() {
		return localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public String getPersCont() {
		return persCont;
	}

	public void setPersCont(String persCont) {
		this.persCont = persCont;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getCodFilialaDest() {
		return codFilialaDest;
	}

	public void setCodFilialaDest(String codFilialaDest) {
		this.codFilialaDest = codFilialaDest;
	}

	public String getDataLivrare() {
		return dataLivrare;
	}

	public void setDataLivrare(String dataLivrare) {
		this.dataLivrare = dataLivrare;
	}

	public String getTipPlata() {
		return tipPlata;
	}

	public void setTipPlata(String tipPlata) {
		this.tipPlata = tipPlata;
	}

	public String getTipTransport() {
		return tipTransport;
	}

	public void setTipTransport(String tipTransport) {
		this.tipTransport = tipTransport;
	}

	public String getDepozDest() {
		return depozDest;
	}

	public void setDepozDest(String depozDest) {
		this.depozDest = depozDest;
	}

	public String getSelectedAgent() {
		return selectedAgent;
	}

	public void setSelectedAgent(String selectedAgent) {
		this.selectedAgent = selectedAgent;
	}

	public boolean isCmdFasonate() {
		return cmdFasonate;
	}

	public void setCmdFasonate(boolean cmdFasonate) {
		this.cmdFasonate = cmdFasonate;
	}

	public String getNumeClientCV() {
		return numeClientCV;
	}

	public void setNumeClientCV(String numeClientCV) {
		this.numeClientCV = numeClientCV;
	}

	public String getObservatiiCLP() {
		return observatiiCLP;
	}

	public void setObservatiiCLP(String observatiiCLP) {
		this.observatiiCLP = observatiiCLP;
	}

	public String getTipMarfa() {
		return tipMarfa;
	}

	public void setTipMarfa(String tipMarfa) {
		this.tipMarfa = tipMarfa;
	}

	public String getMasaMarfa() {
		return masaMarfa;
	}

	public void setMasaMarfa(String masaMarfa) {
		this.masaMarfa = masaMarfa;
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

	public String getTonaj() {
		return tonaj;
	}

	public void setTonaj(String tonaj) {
		this.tonaj = tonaj;
	}

	public String getPrelucrare() {
		return prelucrare;
	}

	public void setPrelucrare(String prelucrare) {
		this.prelucrare = prelucrare;
	}

	@Override
	public String toString() {
		return "AntetComandaCLP [codClient=" + codClient + ", codJudet=" + codJudet + ", localitate=" + localitate + ", strada=" + strada
				+ ", persCont=" + persCont + ", telefon=" + telefon + ", codFilialaDest=" + codFilialaDest + ", dataLivrare=" + dataLivrare
				+ ", tipPlata=" + tipPlata + ", tipTransport=" + tipTransport + ", depozDest=" + depozDest + ", selectedAgent=" + selectedAgent
				+ ", cmdFasonate=" + cmdFasonate + ", numeClientCV=" + numeClientCV + ", observatiiCLP=" + observatiiCLP + ", tipMarfa=" + tipMarfa
				+ ", masaMarfa=" + masaMarfa + ", tipCamion=" + tipCamion + ", tipIncarcare=" + tipIncarcare + ", tonaj=" + tonaj + "]";
	}

}
