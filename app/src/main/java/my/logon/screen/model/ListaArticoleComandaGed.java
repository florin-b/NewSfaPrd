package my.logon.screen.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import my.logon.screen.beans.BeanConditiiArticole;

public class ListaArticoleComandaGed extends Observable {

	private int articolIndex = -1;
	private static ListaArticoleComandaGed instance = new ListaArticoleComandaGed();
	private ArrayList<ArticolComanda> listArticoleComanda = new ArrayList<ArticolComanda>();
	private List<BeanConditiiArticole> conditiiComandaArticole;
	private double valoareNegociata;

	private ListaArticoleComandaGed() {

	}

	public static ListaArticoleComandaGed getInstance() {
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
			removeConditiiArticol(articolComanda);
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

	// pentru total negociat
	public void calculProcentReducere() {

		
		double totalComanda = getTotalNegociatComanda();

		double procentGlobal = 0;

		if (valoareNegociata <= totalComanda) {
			procentGlobal = (1 - valoareNegociata / totalComanda) * 100;
		} else
			procentGlobal = 0;

		if (procentGlobal > 0) {

			double procentLocal = 0;

			String tipAlert = "";
			ArticolComanda articol = null;
			Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();
			while (iterator.hasNext()) {
				articol = iterator.next();

				articol.setPretUnitarClient(articol.getPretUnitarClient() * (1 - procentGlobal / 100));
				articol.setPretUnit(articol.getPretUnitarClient());
				articol.setPret(articol.getPretUnitarClient() * articol.getCantUmb());

				procentLocal = (1 - articol.getPretUnitarClient() / articol.getPretUnitarGed()) * 100;

				if (procentLocal > articol.getDiscountAg()) {
					tipAlert = "SD";
				}
				if (procentLocal > articol.getDiscountSd()) {
					tipAlert += ";DV";
				}

				articol.setProcent(procentLocal);
				articol.setProcAprob(procentLocal);
				
				articol.setValTransport((articol.getPretUnitarClient() * articol.getCantUmb()) * (articol.getProcTransport() / 100));
				articol.setTipAlert(tipAlert);
				articol.setPonderare(0);

			}
		}

	}

	private boolean isSameArticol(ArticolComanda articol1, ArticolComanda articol2) {
		return articol1.getCodArticol().equals(articol2.getCodArticol()) && articol1.getDepozit().equals(articol2.getDepozit());
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

	private void removeConditiiArticol(ArticolComanda articolComanda) {
		if (conditiiComandaArticole != null) {
			Iterator<BeanConditiiArticole> iterator = conditiiComandaArticole.iterator();

			while (iterator.hasNext()) {
				if (iterator.next().getCod().equals(articolComanda.getCodArticol())) {
					iterator.remove();
					break;
				}

			}

		}
	}

	public void removeArticolComanda(int articolIndex) {
		listArticoleComanda.remove(articolIndex);
		triggerObservers();

	}

	public ArrayList<ArticolComanda> getListArticoleComanda() {
		return listArticoleComanda;
	}

	public ArticolComanda getArticolComanda(int position) {
		return listArticoleComanda.get(position);
	}

	public double getTotalNegociatComanda() {

		double totalComanda = 0;
		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();
		ArticolComanda articol = null;
		while (iterator.hasNext()) {
			articol = iterator.next();
			totalComanda += articol.getPretUnitarClient() * articol.getCantUmb();
		}

		return totalComanda;

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

	public double getValoareNegociata() {
		return valoareNegociata;
	}

	public void setValoareNegociata(double valoareNegociata) {
		this.valoareNegociata = valoareNegociata;
	}

	public void clearArticoleComanda() {
		if (listArticoleComanda != null)
			listArticoleComanda.clear();

		if (conditiiComandaArticole != null)
			conditiiComandaArticole.clear();

		valoareNegociata = 0;
	}

	private void triggerObservers() {

		setChanged();
		notifyObservers(listArticoleComanda);
	}

}
