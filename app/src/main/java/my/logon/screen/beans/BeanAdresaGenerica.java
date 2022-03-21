package my.logon.screen.beans;

public class BeanAdresaGenerica {

	private String codJudet;
	private String numeJudet;
	private String oras;
	private String strada;

	public String getCodJudet() {
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}

	public String getNumeJudet() {
		return numeJudet;
	}

	public void setNumeJudet(String numeJudet) {
		this.numeJudet = numeJudet;
	}

	public String getOras() {
		return oras;
	}

	public void setOras(String oras) {
		this.oras = oras;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public void clear() {
		codJudet = "";
		numeJudet = "";
		oras = "";
		strada = "";
	}

}
