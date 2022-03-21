package my.logon.screen.beans;

import java.util.HashMap;

public class VenituriNTCF {

	private HashMap<String, Object> clientFactAnAnterior;
	private HashMap<String, Object> targetAnCurent;
	private HashMap<String, Object> clientFactAnCurent;
	private HashMap<String, Object> coefAfectare;

	public HashMap<String, Object> getClientFactAnAnterior() {
		return clientFactAnAnterior;
	}

	public void setClientFactAnAnterior(HashMap<String, Object> clientFactAnAnterior) {
		this.clientFactAnAnterior = clientFactAnAnterior;
	}

	public HashMap<String, Object> getTargetAnCurent() {
		return targetAnCurent;
	}

	public void setTargetAnCurent(HashMap<String, Object> targetAnCurent) {
		this.targetAnCurent = targetAnCurent;
	}

	public HashMap<String, Object> getClientFactAnCurent() {
		return clientFactAnCurent;
	}

	public void setClientFactAnCurent(HashMap<String, Object> clientFactAnCurent) {
		this.clientFactAnCurent = clientFactAnCurent;
	}

	public HashMap<String, Object> getCoefAfectare() {
		return coefAfectare;
	}

	public void setCoefAfectare(HashMap<String, Object> coefAfectare) {
		this.coefAfectare = coefAfectare;
	}

	@Override
	public String toString() {
		return "VenituriNTCF [clientFactAnAnterior=" + clientFactAnAnterior + ", targetAnCurent=" + targetAnCurent + ", clientFactAnCurent="
				+ clientFactAnCurent + ", coefAfectare=" + coefAfectare + "]";
	}

}
