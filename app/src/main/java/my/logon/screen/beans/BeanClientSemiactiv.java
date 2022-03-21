package my.logon.screen.beans;

import java.util.Comparator;

public class BeanClientSemiactiv {

	private String numeClient;
	private String codClient;
	private String judet;
	private String localitate;
	private String strada;
	private String numePersContact;
	private String telPersContact;
	private String vanzMedie;
	private String vanz03;
	private String vanz06;
	private String vanz07;
	private String vanz09;
	private String vanz040;
	private String vanz041;

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getJudet() {
		return judet;
	}

	public void setJudet(String judet) {
		this.judet = judet;
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

	public String getNumePersContact() {
		return numePersContact;
	}

	public void setNumePersContact(String numePersContact) {
		this.numePersContact = numePersContact;
	}

	public String getTelPersContact() {
		return telPersContact;
	}

	public void setTelPersContact(String telPersContact) {
		this.telPersContact = telPersContact;
	}

	public String getVanzMedie() {
		return vanzMedie;
	}

	public void setVanzMedie(String vanzMedie) {
		this.vanzMedie = vanzMedie;
	}

	public String getVanz06() {
		return vanz06;
	}

	public void setVanz06(String vanz06) {
		this.vanz06 = vanz06;
	}

	public String getVanz07() {
		return vanz07;
	}

	public void setVanz07(String vanz07) {
		this.vanz07 = vanz07;
	}

	public String getVanz09() {
		return vanz09;
	}

	public void setVanz09(String vanz09) {
		this.vanz09 = vanz09;
	}

	public String getVanz040() {
		return vanz040;
	}

	public void setVanz040(String vanz040) {
		this.vanz040 = vanz040;
	}

	public String getVanz041() {
		return vanz041;
	}

	public void setVanz041(String vanz041) {
		this.vanz041 = vanz041;
	}

	public String getVanz03() {
		return vanz03;
	}

	public void setVanz03(String vanz03) {
		this.vanz03 = vanz03;
	}

	public static class CompareNumeClient implements Comparator<BeanClientSemiactiv> {

		private int mod = -1;

		public CompareNumeClient(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(BeanClientSemiactiv client1, BeanClientSemiactiv client2) {
			return mod * client1.getNumeClient().compareToIgnoreCase(client2.getNumeClient());

		}

	}

	public static class CompareJudet implements Comparator<BeanClientSemiactiv> {

		private int mod = -1;

		public CompareJudet(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(BeanClientSemiactiv client1, BeanClientSemiactiv client2) {
			return mod * client1.getJudet().compareToIgnoreCase(client2.getJudet());

		}

	}

	public static class CompareLocalitate implements Comparator<BeanClientSemiactiv> {

		private int mod = -1;

		public CompareLocalitate(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(BeanClientSemiactiv client1, BeanClientSemiactiv client2) {
			return mod * client1.getLocalitate().compareToIgnoreCase(client2.getLocalitate());

		}

	}

	public static class CompareVanzMed implements Comparator<BeanClientSemiactiv> {

		private int mod = -1;

		public CompareVanzMed(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(BeanClientSemiactiv client1, BeanClientSemiactiv client2) {
			return mod * (int) (Double.valueOf(client1.getVanzMedie()) - Double.valueOf(client1.getVanzMedie()));

		}

	}

}
