package my.logon.screen.beans;

import java.util.List;

public class BeanDatePersonale {

	private String cnp;
	private String nume;
	private String codjudet;
	private String localitate;
	private String strada;
	private List<String> termenPlata;
	private boolean clientBlocat;
	private String tipPlata;
	private String codClient;
	private String divizii;

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCodjudet() {
		return codjudet;
	}

	public void setCodjudet(String codjudet) {
		this.codjudet = codjudet;
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
	
	

	public List<String> getTermenPlata() {
		return termenPlata;
	}

	public void setTermenPlata(List<String> termenPlata) {
		this.termenPlata = termenPlata;
	}

	public boolean isClientBlocat() {
		return clientBlocat;
	}

	public void setClientBlocat(boolean clientBlocat) {
		this.clientBlocat = clientBlocat;
	}

	public String getTipPlata() {
		return tipPlata;
	}

	public void setTipPlata(String tipPlata) {
		this.tipPlata = tipPlata;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getDivizii() {
		return divizii;
	}

	public void setDivizii(String divizii) {
		this.divizii = divizii;
	}

	@Override
	public String toString() {
		return "BeanDatePersonale [cnp=" + cnp + ", nume=" + nume + ", codjudet=" + codjudet + ", localitate=" + localitate + ", strada=" + strada + "]";
	}

}
