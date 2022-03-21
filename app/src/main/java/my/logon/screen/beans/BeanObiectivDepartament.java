package my.logon.screen.beans;

public class BeanObiectivDepartament {

	private String id = "";
	private String nume = "";
	private String beneficiar = "";
	private String dataCreare = "";
	private String adresa = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getBeneficiar() {
		return beneficiar;
	}

	public void setBeneficiar(String beneficiar) {
		this.beneficiar = beneficiar;
	}

	public String getDataCreare() {
		return dataCreare;
	}

	public void setDataCreare(String dataCreare) {
		this.dataCreare = dataCreare;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String toString() {
		return "BeanObiectivDepartament [id=" + id + ", nume=" + nume + ", beneficiar=" + beneficiar + ", dataCreare=" + dataCreare + ", adresa="
				+ adresa + "]";
	}

}
