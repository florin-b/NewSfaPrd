package my.logon.screen.beans;

import java.util.List;

import my.logon.screen.beans.BeanArticolRetur;
import my.logon.screen.utils.UtilsFormatting;

public class BeanComandaRetur {
	private String nrDocument;
	private String dataLivrare;
	private String tipTransport;
	private String codAgent;
	private String tipAgent;
	private String motivRespingere;
	private String listaArticole;
	private String numePersContact;
	private String telPersContact;
	private String adresaCodJudet;
	private String adresaOras;
	private String adresaStrada;
	private String adresaCodAdresa;
	private String observatii;
	private String codClient;
	private String numeClient;
	private List<BeanArticolRetur> arrayListArticole;
	private boolean transBack;

	public BeanComandaRetur() {

	}

	public String getNrDocument() {
		return nrDocument;
	}

	public void setNrDocument(String nrDocument) {
		this.nrDocument = nrDocument;
	}

	public String getDataLivrare() {
		return dataLivrare;
	}

	public void setDataLivrare(String dataLivrare) {
		this.dataLivrare = dataLivrare;
	}

	public String getTipTransport() {
		return tipTransport;
	}

	public void setTipTransport(String tipTransport) {
		this.tipTransport = tipTransport;
	}

	public String getListArticole() {
		return listaArticole;
	}

	public void setListArticole(String listArticole) {
		this.listaArticole = listArticole;
	}

	public String getCodAgent() {
		return codAgent;
	}

	public void setCodAgent(String codAgent) {
		this.codAgent = codAgent;
	}

	public String getTipAgent() {
		return tipAgent;
	}

	public void setTipAgent(String tipAgent) {
		this.tipAgent = tipAgent;
	}

	public String getMotivRespingere() {
		return motivRespingere;
	}

	public void setMotivRespingere(String motivRespingere) {
		this.motivRespingere = motivRespingere;
	}

	public String getNumePersContact() {
		return numePersContact;
	}

	public void setNumePersContact(String numePersContact) {
		this.numePersContact = numePersContact;
	}

	public String getTelPersContact() {
		return telPersContact;
	}

	public void setTelPersContact(String telPersContact) {
		this.telPersContact = telPersContact;
	}

	public String getAdresaCodJudet() {
		return adresaCodJudet;
	}

	public void setAdresaCodJudet(String adresaCodJudet) {
		this.adresaCodJudet = adresaCodJudet;
	}

	public String getAdresaOras() {
		return adresaOras;
	}

	public void setAdresaOras(String adresaOras) {
		this.adresaOras = adresaOras;
	}

	public String getAdresaStrada() {
		return adresaStrada;
	}

	public void setAdresaStrada(String adresaStrada) {
		this.adresaStrada = adresaStrada;
	}

	public String getAdresaCodAdresa() {
		return adresaCodAdresa;
	}

	public void setAdresaCodAdresa(String adresaCodAdresa) {
		this.adresaCodAdresa = adresaCodAdresa;
	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		if (UtilsFormatting.isNumeric(codClient))
			this.codClient = codClient;
		else
			this.codClient = " ";
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public List<BeanArticolRetur> getArrayListArticole() {
		return arrayListArticole;
	}

	public void setArrayListArticole(List<BeanArticolRetur> arrayListArticole) {
		this.arrayListArticole = arrayListArticole;
	}

	public boolean isTransBack() {
		return transBack;
	}

	public void setTransBack(boolean transBack) {
		this.transBack = transBack;
	}

}
