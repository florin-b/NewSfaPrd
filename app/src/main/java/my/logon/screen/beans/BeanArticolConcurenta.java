package my.logon.screen.beans;

public class BeanArticolConcurenta {

	private String cod;
	private String nume;
	private String umVanz;
	private String valoare;
	private String dataValoare;
	private String observatii;

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

	public String getValoare() {
		if (valoare == null)
			return "";
		return valoare.trim();
	}

	public void setValoare(String valoare) {
		this.valoare = valoare;
	}

	public String getDataValoare() {
		return dataValoare;
	}

	public void setDataValoare(String dataValoare) {
		this.dataValoare = dataValoare;
	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

}
