package my.logon.screen.beans;

import java.util.Comparator;

public class PierdereTipClient {

	public String codTipClient;
	public String numeClient;
	public double venitLC;
	public double venitLC1;
	public double venitLC2;

	public String getCodTipClient() {
		return codTipClient;
	}

	public void setCodTipClient(String codTipClient) {
		this.codTipClient = codTipClient;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public double getVenitLC() {
		return venitLC;
	}

	public void setVenitLC(double venitLC) {
		this.venitLC = venitLC;
	}

	public double getVenitLC1() {
		return venitLC1;
	}

	public void setVenitLC1(double venitLC1) {
		this.venitLC1 = venitLC1;
	}

	public double getVenitLC2() {
		return venitLC2;
	}

	public void setVenitLC2(double venitLC2) {
		this.venitLC2 = venitLC2;
	}

	public static class CompareNumeClient implements Comparator<PierdereTipClient> {

		private int mod = -1;

		public CompareNumeClient(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereTipClient client1, PierdereTipClient client2) {
			return mod * client1.getNumeClient().compareToIgnoreCase(client2.getNumeClient());
		}

	}

	public static class ComparePierderiLC implements Comparator<PierdereTipClient> {

		private int mod = -1;

		public ComparePierderiLC(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereTipClient client1, PierdereTipClient client2) {
			return mod * (int) (Double.valueOf(client1.getVenitLC()) - Double.valueOf(client2.getVenitLC()));
		}

	}

	
	
	public static class ComparePierderiLC1 implements Comparator<PierdereTipClient> {

		private int mod = -1;

		public ComparePierderiLC1(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereTipClient client1, PierdereTipClient client2) {
			return mod * (int) (Double.valueOf(client1.getVenitLC1()) - Double.valueOf(client2.getVenitLC1()));
		}

	}	
	
	public static class ComparePierderiLC2 implements Comparator<PierdereTipClient> {

		private int mod = -1;

		public ComparePierderiLC2(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereTipClient client1, PierdereTipClient client2) {
			return mod * (int) (Double.valueOf(client1.getVenitLC2()) - Double.valueOf(client2.getVenitLC2()));
		}

	}	
	
}
