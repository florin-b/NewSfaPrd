package my.logon.screen.beans;

public class BeanCategorieObiectiv {

	private String numeCategorie;
	private String codCategorie;

	public String getNumeCategorie() {
		return numeCategorie;
	}

	public void setNumeCategorie(String numeCategorie) {
		this.numeCategorie = numeCategorie;
	}

	public String getCodCategorie() {
		return codCategorie;
	}

	public void setCodCategorie(String codCategorie) {
		this.codCategorie = codCategorie;
	}

	public void clear() {
		numeCategorie = "";
		codCategorie = "";
	}

}
