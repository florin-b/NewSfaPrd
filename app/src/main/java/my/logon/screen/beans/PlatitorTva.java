package my.logon.screen.beans;

public class PlatitorTva {

	private boolean isPlatitor;
	private String numeClient;
	private String nrInreg;
	private String errMessage;
	private String codJudet;
	private String localitate;
	private String strada;
	private String stareInregistrare;
	private String diviziiClient;

	public boolean isPlatitor() {
		return isPlatitor;
	}

	public void setPlatitor(boolean isPlatitor) {
		this.isPlatitor = isPlatitor;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public String getNrInreg() {
		return nrInreg;
	}

	public void setNrInreg(String nrInreg) {
		this.nrInreg = nrInreg;
	}

	public String getCodJudet() {
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}

	public String getLocalitate() {
		return localitate;
	}

	public void setLocalitate(String localitate) {
		this.localitate = localitate;
	}

	public String getStrada() {
		return strada;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}

	public String getStareInregistrare() {
		return stareInregistrare;
	}

	public void setStareInregistrare(String stareInregistrare) {
		this.stareInregistrare = stareInregistrare;
	}

	public String getDiviziiClient() {
		return diviziiClient;
	}

	public void setDiviziiClient(String diviziiClient) {
		this.diviziiClient = diviziiClient;
	}
}
