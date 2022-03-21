package my.logon.screen.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.screens.CreareComanda;

public class ListaArticoleModificareComanda extends Observable {

	private int articolIndex = -1;
	private static ListaArticoleModificareComanda instance = new ListaArticoleModificareComanda();
	private ArrayList<ArticolComanda> listArticoleComanda;
	private List<BeanConditiiArticole> conditiiComandaArticole;

	private ListaArticoleModificareComanda() {

	}

	public static ListaArticoleModificareComanda getInstance() {
		return instance;
	}

	public void setListaArticole(ArrayList<ArticolComanda> listaArticole) {
		this.listArticoleComanda = listaArticole;
	}

	public void addArticolComanda(ArticolComanda articolComanda) {
		if (!articolExists(articolComanda))
			listArticoleComanda.add(articolComanda);
		else {
			listArticoleComanda.remove(articolIndex);
			listArticoleComanda.add(articolIndex, articolComanda);
		}

		triggerObservers();

	}

	private boolean articolExists(ArticolComanda articolComanda) {
		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

		int pos = 0;
		articolIndex = -1;
		while (iterator.hasNext()) {

			if (isSameArticol(iterator.next(), articolComanda)) {
				articolIndex = pos;
				return true;
			}
			pos++;
		}

		return false;
	}

	private boolean isSameArticol(ArticolComanda articol1, ArticolComanda articol2) {
		return articol1.getCodArticol().equals(articol2.getCodArticol())
				&& articol1.getDepozit().equals(articol2.getDepozit());
	}

	public void removeArticolComanda(String codArticol) {

		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

		while (iterator.hasNext()) {

			if (iterator.next().getCodArticol().equals(codArticol)) {
				iterator.remove();
				break;
			}
		}

		triggerObservers();

	}

	public void removeArticolComanda(int articolIndex) {
		listArticoleComanda.remove(articolIndex);
		triggerObservers();

	}

	public ArrayList<ArticolComanda> getListArticoleComanda() {
		return listArticoleComanda;
	}

	public double getTotalComanda() {

		double totalComanda = 0;
		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

		while (iterator.hasNext()) {
			totalComanda += iterator.next().getPret();
		}

		return totalComanda;

	}

	public int getNrArticoleComanda() {
		return listArticoleComanda.size();
	}

	public void setConditiiArticole(List<BeanConditiiArticole> conditiiComandaArticole) {
		this.conditiiComandaArticole = conditiiComandaArticole;
	}

	public List<BeanConditiiArticole> getConditiiArticole() {
		return this.conditiiComandaArticole;
	}

	public void clearArticoleComanda() {
		if (listArticoleComanda != null)
			listArticoleComanda.clear();

		if (conditiiComandaArticole != null)
			conditiiComandaArticole.clear();
	}

	public double calculPondereB() {

		double totalArtB = 0, procentB = 0, localTotalComanda = 0;

		Number tokPretArt, tokCantArt, tokMultArt;

		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

		ArticolComanda articol;
		while (iterator.hasNext()) {
			articol = iterator.next();

			tokPretArt = articol.getPretUnit();
			tokCantArt = articol.getCantitate();
			tokMultArt = articol.getMultiplu();

			if (articol.getTipArt().equals("B")) {
				totalArtB += tokPretArt.doubleValue() * tokCantArt.doubleValue() / tokMultArt.doubleValue();
			}

			localTotalComanda += tokPretArt.doubleValue() * tokCantArt.doubleValue() / tokMultArt.doubleValue();
		}

		if (localTotalComanda == 0) {
			procentB = 0;
		} else {
			procentB = totalArtB / localTotalComanda * 100;
		}

		return procentB;

	}

	public double calculTaxaVerde() {

		double totalTaxaVerde = 0;

		String[] tokVal;
		Double valCondPret = 0.0;
		String[] tokInfoArt;

		if (CreareComanda.canalDistrib.equals("10")) {

			Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

			ArticolComanda articol;
			while (iterator.hasNext()) {
				articol = iterator.next();
				tokInfoArt = articol.getInfoArticol().split(";");

				for (int ii = 0; ii < tokInfoArt.length; ii++) {
					tokVal = tokInfoArt[ii].split(":");
					valCondPret = Double.valueOf(tokVal[1].replace(',', '.').trim());
					if (valCondPret != 0) {
						if (tokVal[0].toUpperCase(Locale.getDefault()).contains("VERDE")) {
							totalTaxaVerde += valCondPret;
						}

					}
				}
			}

		}// sf. if

		return totalTaxaVerde;

	}

	private void triggerObservers() {

		setChanged();
		notifyObservers(listArticoleComanda);
	}

}
