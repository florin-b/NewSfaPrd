package my.logon.screen.beans;

public class VenitTCF {

	private String venitGrInc;
	private String targetPropus;
	private String targetRealizat;
	private String coefAfectare;
	private String venitTcf;

	public String getVenitGrInc() {
		return venitGrInc;
	}

	public void setVenitGrInc(String venitGrInc) {
		this.venitGrInc = venitGrInc;
	}

	public String getTargetPropus() {
		return targetPropus;
	}

	public void setTargetPropus(String targetPropus) {
		this.targetPropus = targetPropus;
	}

	public String getTargetRealizat() {
		return targetRealizat;
	}

	public void setTargetRealizat(String targetRealizat) {
		this.targetRealizat = targetRealizat;
	}

	public String getCoefAfectare() {
		return coefAfectare;
	}

	public void setCoefAfectare(String coefAfectare) {
		this.coefAfectare = coefAfectare;
	}

	public String getVenitTcf() {
		return venitTcf;
	}

	public void setVenitTcf(String venitTcf) {
		this.venitTcf = venitTcf;
	}

	@Override
	public String toString() {
		return "VenitTCF [venitGrInc=" + venitGrInc + ", targetPropus=" + targetPropus + ", targetRealizat=" + targetRealizat + ", coefAfectare="
				+ coefAfectare + ", venitTcf=" + venitTcf + "]";
	}

}
