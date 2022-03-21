package my.logon.screen.beans;

public class BeanNewPretConcurenta {

	private String cod;
	private String concurent;
	private String valoare;
	private String observatii;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getConcurent() {
		return concurent;
	}

	public void setConcurent(String concurent) {
		this.concurent = concurent;
	}

	public String getValoare() {
		return valoare;
	}

	public void setValoare(String valoare) {
		this.valoare = valoare;
	}

	public String getObservatii() {

		if (observatii != null) {
			if (observatii.length() <= 200)
				return observatii;
			else
				return observatii.substring(0, 200);
		} else
			return " ";

	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

}
