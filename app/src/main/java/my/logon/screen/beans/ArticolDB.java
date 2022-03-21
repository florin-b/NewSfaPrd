package my.logon.screen.beans;

public class ArticolDB {

	private String cod;
	private String nume;
	private String umVanz;
	private String depart;
	private String tipAB;
	private String sintetic;
	private String umVanz10;
	private String nivel1;
	private String departAprob;
	private boolean umPalet;
	private String stoc;
	private String categorie;
	private double lungime;

	public ArticolDB() {

	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getUmVanz() {
		return umVanz;
	}

	public void setUmVanz(String umVanz) {
		this.umVanz = umVanz;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getTipAB() {
		if (tipAB == null || tipAB.equals("null"))
			return " ";
		return tipAB;
	}

	public void setTipAB(String tipAB) {
		this.tipAB = tipAB;
	}

	public String getSintetic() {
		return sintetic;
	}

	public void setSintetic(String sintetic) {
		this.sintetic = sintetic;
	}

	public String getUmVanz10() {
		return umVanz10;
	}

	public void setUmVanz10(String umVanz10) {
		this.umVanz10 = umVanz10;
	}

	public String getNivel1() {
		return nivel1;
	}

	public void setNivel1(String nivel1) {
		this.nivel1 = nivel1;
	}

	public String getDepartAprob() {
		return departAprob;
	}

	public void setDepartAprob(String departAprob) {
		this.departAprob = departAprob;
	}

	public boolean isUmPalet() {
		return umPalet;
	}

	public void setUmPalet(boolean umPalet) {
		this.umPalet = umPalet;
	}

	public String getStoc() {
		return stoc;
	}

	public void setStoc(String stoc) {
		this.stoc = stoc;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public double getLungime() {
		return lungime;
	}

	public void setLungime(double lungime) {
		this.lungime = lungime;
	}

	@Override
	public String toString() {
		return "ArticolDB [cod=" + cod + ", nume=" + nume + ", umVanz=" + umVanz + ", depart=" + depart + ", tipAB=" + tipAB + ", sintetic="
				+ sintetic + ", umVanz10=" + umVanz10 + ", nivel1=" + nivel1 + ", departAprob=" + departAprob + ", umPalet=" + umPalet + ", stoc="
				+ stoc + "]";
	}

}
