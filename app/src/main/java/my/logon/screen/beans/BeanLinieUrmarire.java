package my.logon.screen.beans;

public class BeanLinieUrmarire {

	private int idEveniment;
	private String numeEveniment;
	private String data;
	private String observatii;

	public int getIdEveniment() {
		return idEveniment;
	}

	public void setIdEveniment(int idEveniment) {
		this.idEveniment = idEveniment;
	}

	public String getNumeEveniment() {
		return numeEveniment;
	}

	public void setNumeEveniment(String numeEveniment) {
		this.numeEveniment = numeEveniment;
	}

	public String getData() {
		if (data == null)
			return " ";
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getObservatii() {
		if (observatii == null)
			return " ";
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

}
