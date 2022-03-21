/**
 * @author florinb
 * clasa pentru definirea articolelor din comanda ged
 */
package my.logon.screen.model;

import java.util.Comparator;
import java.util.Locale;

public class ArticolComandaGed extends ArticolComanda implements Comparable<ArticolComanda> {

	private double pretUnitarGed;
	private double marjaClient;
	private double marjaCorectata;
	private double reducerePonderata;
	private double marjaGed;
	private String tipAlertPret;
	private double adaosMinimArticol;
	private double adaosClientCorectat;
	private double adaosMinimAcceptat;
	private int ponderare;
	

	public double getPretUnitGed() {
		return pretUnitarGed;
	}

	public void setPretUnitGed(double pretUnitGed) {
		this.pretUnitarGed = pretUnitGed;
	}

	public double getMarjaClient() {
		return marjaClient;
	}

	public void setMarjaClient(double marjaClient) {
		this.marjaClient = marjaClient;
	}

	public double getMarjaCorectata() {
		return marjaCorectata;
	}

	public void setMarjaCorectata(double marjaCorectata) {
		this.marjaCorectata = marjaCorectata;
	}

	public double getReducerePonderata() {
		return reducerePonderata;
	}

	public void setReducerePonderata(double reducerePonderata) {
		this.reducerePonderata = reducerePonderata;
	}

	public double getMarjaGed() {
		return marjaGed;
	}

	public void setMarjaGed(double marjaGed) {
		this.marjaGed = marjaGed;
	}

	public String getTipAlertPret() {
		return tipAlertPret;
	}

	public void setTipAlertPret(String tipAlertPret) {
		this.tipAlertPret = tipAlertPret;
	}

	public double getAdaosMinimArticol() {
		return adaosMinimArticol;
	}

	public void setAdaosMinimArticol(double adaosMinimArticol) {
		this.adaosMinimArticol = adaosMinimArticol;
	}

	public double getAdaosClientCorectat() {
		return adaosClientCorectat;
	}

	public void setAdaosClientCorectat(double adaosClientCorectat) {
		this.adaosClientCorectat = adaosClientCorectat;
	}

	public double getAdaosMinimAcceptat() {
		return adaosMinimAcceptat;
	}

	public void setAdaosMinimAcceptat(double adaosMinimAcceptat) {
		this.adaosMinimAcceptat = adaosMinimAcceptat;
	}

	public int getPonderare() {
		return ponderare;
	}

	public void setPonderare(int ponderare) {
		this.ponderare = ponderare;
	}

	public double getPretUnitarGed() {
		return pretUnitarGed;
	}

	public void setPretUnitarGed(double pretUnitarGed) {
		this.pretUnitarGed = pretUnitarGed;
	}

	

	public int compareTo(ArticolComanda compareArtCom) {

		String comparaDepart = (compareArtCom).getDepart();
		return depart.compareTo(comparaDepart);

	}

	public static Comparator<ArticolComanda> DepartComparator = new Comparator<ArticolComanda>() {

		public int compare(ArticolComanda depart1, ArticolComanda depart2) {

			String departName1 = "";
			String departName2 = "";

			if (depart1 != null)
				departName1 = depart1.getDepart().toUpperCase(Locale.UK);

			if (depart2 != null)
				departName2 = depart2.getDepart().toUpperCase(Locale.UK);

			// descrescator
			// return departName2.compareTo(departName1);

			// crescator
			return departName1.compareTo(departName2);

		}

	};

	@Override
	public String toString() {
		return "ArticolComandaGed [pretUnitarGed=" + pretUnitarGed + ", " + "  marjaClient=" + marjaClient + ", marjaCorectata=" + marjaCorectata
				+ ", reducerePonderata=" + reducerePonderata + ", marjaGed=" + marjaGed + ", tipAlertPret=" + tipAlertPret + ", adaosMinimArticol="
				+ adaosMinimArticol + ", adaosClientCorectat=" + adaosClientCorectat + ", adaosMinimAcceptat=" + adaosMinimAcceptat + ", ponderare="
				+ ponderare + ", nrCrt=" + nrCrt + ", numeArticol=" + numeArticol + ", codArticol=" + codArticol + ", depozit=" + depozit + ", cantitate="
				+ cantitate + ", um=" + um + ", pret=" + pret + ", moneda=" + moneda + ", procent=" + procent + ", observatii=" + observatii + ", conditie="
				+ conditie + ", promotie=" + promotie + ", procentFact=" + procentFact + ", pretUnit=" + pretUnit + ", discClient=" + discClient
				+ ", tipAlert=" + tipAlert + ", procAprob=" + procAprob + ", multiplu=" + multiplu + ", infoArticol=" + infoArticol + ", cantUmb=" + cantUmb
				+ ", Umb=" + Umb + ", alteValori=" + alteValori + ", depart=" + depart + ", tipArt=" + tipArt + ", taxaVerde=" + taxaVerde
				+ ", pretUnitarPonderat=" + pretUnitarPonderat + ", pretUnitarClient=" + pretUnitarClient + ", deficit=" + deficit + "]\n\n";
	}

}
