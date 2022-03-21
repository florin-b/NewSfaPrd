package my.logon.screen.beans;

public class BeanConditiiArticole {
	private String cod;
	private String nume;
	private double cantitate;
	private String um;
	private double valoare;
	private double multiplu;

	public BeanConditiiArticole() {

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

	public double getCantitate() {
		return cantitate;
	}

	public void setCantitate(double cantitate) {
		this.cantitate = cantitate;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public double getValoare() {
		return valoare;
	}

	public void setValoare(double valoare) {
		this.valoare = valoare;
	}

	public double getMultiplu() {
		return multiplu;
	}

	public void setMultiplu(double multiplu) {
		this.multiplu = multiplu;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof BeanConditiiArticole)) {
			return false;
		}

		BeanConditiiArticole other = (BeanConditiiArticole) obj;
		return this.cod.equals(other.cod);

	}

}
