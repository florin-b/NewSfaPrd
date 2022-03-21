package my.logon.screen.beans;

public class SalarizareDetaliiMalusLite {

	private String numeClient;
	private String codClient;
	private double valoareFactura;
	private double penalizare;

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public double getValoareFactura() {
		return valoareFactura;
	}

	public void setValoareFactura(double valoareFactura) {
		this.valoareFactura = valoareFactura;
	}

	public double getPenalizare() {
		return penalizare;
	}

	public void setPenalizare(double penalizare) {
		this.penalizare = penalizare;
	}

}
