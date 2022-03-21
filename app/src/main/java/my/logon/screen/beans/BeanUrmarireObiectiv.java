package my.logon.screen.beans;

public class BeanUrmarireObiectiv {

	private String idObiectiv;
	private String codClient;
	private int codEveniment;
	private String codDepart;
	private String data;
	private String observatii;

	public String getIdObiectiv() {
		return idObiectiv;
	}

	public void setIdObiectiv(String idObiectiv) {
		this.idObiectiv = idObiectiv;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public int getCodEveniment() {
		return codEveniment;
	}

	public void setCodEveniment(int codEveniment) {
		this.codEveniment = codEveniment;
	}

	public String getData() {
		if (data.contains(".") || data.trim().length() == 0)
			return data;
		else
			return data.substring(6, 8) + "." + data.substring(4, 6) + "." + data.substring(0, 4);

	}

	public void setData(String data) {
		this.data = data;
	}

	public String getObservatii() {
		if (observatii == null)
			return " ";
		return observatii.trim();
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public String getCodDepart() {
		return codDepart;
	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
	}

	public String toString() {
		return "BeanUrmarireObiectiv [idObiectiv=" + idObiectiv + ", codClient=" + codClient + ", codEveniment="
				+ codEveniment + ", codDepart=" + codDepart + ", data=" + data + ", observatii=" + observatii + "]";
	}

}
