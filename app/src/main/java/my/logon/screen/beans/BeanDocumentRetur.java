package my.logon.screen.beans;

import java.util.List;

public class BeanDocumentRetur {
	private String numar;
	private String data;
	private String tipTransport;
	private String dataLivrare;
	private List<BeanAdresaLivrare> listAdrese;
	private List<BeanPersoanaContact> listPersoane;
	private boolean isCmdACZC;

	public BeanDocumentRetur() {

	}

	public String getNumar() {
		return numar;
	}

	public void setNumar(String numar) {
		this.numar = numar;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTipTransport() {
		return tipTransport;
	}

	public void setTipTransport(String tipTransport) {
		this.tipTransport = tipTransport;
	}

	public String getDataLivrare() {
		return dataLivrare;
	}

	public void setDataLivrare(String dataLivrare) {
		this.dataLivrare = dataLivrare;
	}

	public List<BeanAdresaLivrare> getListAdrese() {
		return listAdrese;
	}

	public void setListAdrese(List<BeanAdresaLivrare> listAdrese) {
		this.listAdrese = listAdrese;
	}

	public List<BeanPersoanaContact> getListPersoane() {
		return listPersoane;
	}

	public void setListPersoane(List<BeanPersoanaContact> listPersoane) {
		this.listPersoane = listPersoane;
	}

	public boolean isCmdACZC() {
		return isCmdACZC;
	}

	public void setCmdACZC(boolean cmdACZC) {
		isCmdACZC = cmdACZC;
	}
}
