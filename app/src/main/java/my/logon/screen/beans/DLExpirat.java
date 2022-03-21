package my.logon.screen.beans;

public class DLExpirat {

	private String nrDocument;
	private String numeClient;
	private String dataDocument;
	private String nrDocumentSap;
	private String dataLivrare;
	private String furnizor;

	public String getNrDocument() {
		return nrDocument;
	}

	public void setNrDocument(String nrDocument) {
		this.nrDocument = nrDocument;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getDataDocument() {
		return dataDocument;
	}

	public void setDataDocument(String dataDocument) {
		this.dataDocument = dataDocument;
	}

	public String getNrDocumentSap() {
		return nrDocumentSap;
	}

	public void setNrDocumentSap(String nrDocumentSap) {
		this.nrDocumentSap = nrDocumentSap;
	}

	public String getDataLivrare() {
		return dataLivrare;
	}

	public void setDataLivrare(String dataLivrare) {
		this.dataLivrare = dataLivrare;
	}

	public String getFurnizor() {
		return furnizor;
	}

	public void setFurnizor(String furnizor) {
		this.furnizor = furnizor;
	}

	@Override
	public String toString() {
		return "DLExpirat [nrDocument=" + nrDocument + ", numeClient=" + numeClient + ", dataDocument=" + dataDocument + ", nrDocumentSap=" + nrDocumentSap
				+ ", dataLivrare=" + dataLivrare + ", furnizor=" + furnizor + "]";
	}

}
