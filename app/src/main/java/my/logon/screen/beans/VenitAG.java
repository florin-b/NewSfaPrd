package my.logon.screen.beans;

import java.util.List;

public class VenitAG {

	private List<VenitTCF> venitTcf;
	private List<VenitTPR> venitTpr;

	public List<VenitTCF> getVenitTcf() {
		return venitTcf;
	}

	public void setVenitTcf(List<VenitTCF> venitTcf) {
		this.venitTcf = venitTcf;
	}

	public List<VenitTPR> getVenitTpr() {
		return venitTpr;
	}

	public void setVenitTpr(List<VenitTPR> venitTpr) {
		this.venitTpr = venitTpr;
	}

	@Override
	public String toString() {
		return "VenitAG [venitTcf=" + venitTcf + ", venitTpr=" + venitTpr + "]";
	}

}
