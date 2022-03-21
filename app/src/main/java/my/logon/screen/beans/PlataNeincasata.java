package my.logon.screen.beans;

import java.util.List;

import my.logon.screen.beans.IncasareDocument;

public class PlataNeincasata {
	private String tipDocument;
	private String codClient;
	private double sumaPlata;
	private String seriaDocument;
	private String dataEmitere;
	private String dataScadenta;
	private String codAgent;
	private List<IncasareDocument> listaDocumente;
	private String girant;

	public String getTipDocument() {
		return tipDocument;
	}

	public void setTipDocument(String tipDocument) {
		this.tipDocument = tipDocument;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public double getSumaPlata() {
		return sumaPlata;
	}

	public void setSumaPlata(double sumaPlata) {
		this.sumaPlata = sumaPlata;
	}

	public String getSeriaDocument() {
		return seriaDocument;
	}

	public void setSeriaDocument(String seriaDocument) {
		this.seriaDocument = seriaDocument;
	}

	public String getDataEmitere() {
		return dataEmitere;
	}

	public void setDataEmitere(String dataEmitere) {
		this.dataEmitere = dataEmitere;
	}

	public String getDataScadenta() {
		return dataScadenta;
	}

	public void setDataScadenta(String dataScadenta) {
		this.dataScadenta = dataScadenta;
	}

	public String getCodAgent() {
		return codAgent;
	}

	public void setCodAgent(String codAgent) {
		this.codAgent = codAgent;
	}

	public List<IncasareDocument> getListaDocumente() {
		return listaDocumente;
	}

	public void setListaDocumente(List<IncasareDocument> listaDocumente) {
		this.listaDocumente = listaDocumente;
	}

	public String getGirant() {
		return girant;
	}

	public void setGirant(String girant) {
		this.girant = girant;
	}

}
