package my.logon.screen.beans;

import java.util.List;

public class LivrareMathaus {

	private ComandaMathaus comandaMathaus;
	private List<CostTransportMathaus> costTransport;

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

}
