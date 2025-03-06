package my.logon.screen.beans;

import java.util.List;

public class LivrareMathaus {

	private ComandaMathaus comandaMathaus;
	private List<CostTransportMathaus> costTransport;
	private List<TaxaMasina> taxeMasini;
	private List<ArticolPalet> listPaleti;

	public ComandaMathaus getComandaMathaus() {
		return comandaMathaus;
	}

	public void setComandaMathaus(ComandaMathaus comandaMathaus) {
		this.comandaMathaus = comandaMathaus;
	}

	public List<CostTransportMathaus> getCostTransport() {
		return costTransport;
	}

	public void setCostTransport(List<CostTransportMathaus> costTransport) {
		this.costTransport = costTransport;
	}

	public List<TaxaMasina> getTaxeMasini() {
		return taxeMasini;
	}

	public void setTaxeMasini(List<TaxaMasina> taxeMasini) {
		this.taxeMasini = taxeMasini;
	}

	public List<ArticolPalet> getListPaleti() {
		return listPaleti;
	}

	public void setListPaleti(List<ArticolPalet> listPaleti) {
		this.listPaleti = listPaleti;
	}
}
