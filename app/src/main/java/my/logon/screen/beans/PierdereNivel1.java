package my.logon.screen.beans;

import java.util.Comparator;

public class PierdereNivel1 {
	private String numeClient;
	private String numeNivel1;
	private double venitLC;
	private double venitLC1;
	private double venitLC2;

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getNumeNivel1() {
		return numeNivel1;
	}

	public void setNumeNivel1(String numeNivel1) {
		this.numeNivel1 = numeNivel1;
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

	public static class CompareNumeNivel1 implements Comparator<PierdereNivel1> {

		private int mod = -1;

		public CompareNumeNivel1(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereNivel1 client1, PierdereNivel1 client2) {
			return mod * client1.getNumeNivel1().compareToIgnoreCase(client2.getNumeNivel1());
		}

	}

	public static class CompareVenitLC implements Comparator<PierdereNivel1> {

		private int mod = -1;

		public CompareVenitLC(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereNivel1 client1, PierdereNivel1 client2) {
			return mod * (int) (Double.valueOf(client1.getVenitLC()) - Double.valueOf(client2.getVenitLC()));
		}

	}

	public static class CompareVenitLC1 implements Comparator<PierdereNivel1> {

		private int mod = -1;

		public CompareVenitLC1(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereNivel1 client1, PierdereNivel1 client2) {
			return mod * (int) (Double.valueOf(client1.getVenitLC1()) - Double.valueOf(client2.getVenitLC1()));
		}

	}

	public static class CompareVenitLC2 implements Comparator<PierdereNivel1> {

		private int mod = -1;

		public CompareVenitLC2(boolean asc) {
			if (asc)
				mod = 1;
		}

		@Override
		public int compare(PierdereNivel1 client1, PierdereNivel1 client2) {
			return mod * (int) (Double.valueOf(client1.getVenitLC2()) - Double.valueOf(client2.getVenitLC2()));
		}

	}

}
