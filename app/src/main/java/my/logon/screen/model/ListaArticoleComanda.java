package my.logon.screen.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import my.logon.screen.screens.CreareComanda;


public class ListaArticoleComanda extends Observable {

	private int articolIndex = -1;
	private static ListaArticoleComanda instance = new ListaArticoleComanda();
	private List<ArticolComanda> listArticoleComanda = new ArrayList<ArticolComanda>();

	private ListaArticoleComanda() {

	}

	public static ListaArticoleComanda getInstance() {
		return instance;
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
		return articol1.getCodArticol().equals(articol2.getCodArticol()) && articol1.getDepozit().equals(articol2.getDepozit());
	}

	public void removeArticolComanda(String codArticol) {

		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

		while (iterator.hasNext()) {
			iterator.next().getCodArticol().equals(codArticol);
			iterator.remove();
			break;
		}

		triggerObservers();

	}

	public void removeArticolComanda(int articolIndex) {
		listArticoleComanda.remove(articolIndex);
		triggerObservers();

	}

	public List<ArticolComanda> getListArticoleComanda() {
		return listArticoleComanda;
	}

	public double getTotalComanda() {

		double totalComanda = 0;
		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();
		ArticolComanda articol = null;
		while (iterator.hasNext()) {
			articol = iterator.next();
			totalComanda += articol.getPretUnit() / articol.getMultiplu() * Double.valueOf(articol.getCantUmb());

		}

		return totalComanda;

	}

	public int getNrArticoleComanda() {
		return listArticoleComanda.size();
	}

	public void clearArticoleComanda() {
		listArticoleComanda.clear();
	}

	// calcul procent reducere pentru comenzi cu valoare totala negociata
	public void calculProcReducere() {

		NumberFormat nf2 = new DecimalFormat("#0.00");

		double totalFaraDisc = 0;
		String localPretLista = "";
		String localpretArt[];

		Iterator<ArticolComanda> iterator = listArticoleComanda.iterator();

		ArticolComanda articol;
		while (iterator.hasNext()) {
			articol = iterator.next();

			if (articol.getAlteValori() == null || articol.getAlteValori().trim().isEmpty())
				continue;

			localPretLista = articol.getAlteValori();
			localpretArt = localPretLista.split("!");
			totalFaraDisc += Double.parseDouble(localpretArt[0]);
		}

		double newProcRed = 0;

		if (CreareComanda.valNegociat <= totalFaraDisc) {
			newProcRed = (1 - CreareComanda.valNegociat / totalFaraDisc) * 100;
		} else
			newProcRed = 0;

		String roundedProcRed = nf2.format(newProcRed);

		double valTotArt = 0;
		double newUnitPrice = 0, newTotalPrice = 0, procRedFact = 0, procDiscClient = 0, procentAprob = 0;

		String tokCantArticol = "", tokNumeArticol = "", tokMonedaArticol = "", tokCodArticol = "", tokDepozArticol = "", tokUmArticol = "";
		String tokObs = "", tokMultiplu = "", tokInfoArticol = "", tokCantUmb = "", tokUmb = "", tokAlteValori = "", tokDepart = "", tokTipArt = "";
		String artPretPromo = "", strProcRedFact = "", strProcDiscClient = "", strProcentAprob = "", localTipAlert = "";

		boolean conditie = false;

		String preturiArt[];

		int counter = 1;

		int i = 0;
		for (i = 0; i < listArticoleComanda.size(); i++) {
			articol = listArticoleComanda.get(i);

			if (articol.getAlteValori() == null || articol.getAlteValori().trim().isEmpty())
				continue;

			tokCantArticol = String.valueOf(articol.getCantitate());
			tokNumeArticol = articol.getNumeArticol();
			tokMonedaArticol = articol.getMoneda();
			tokCodArticol = articol.getCodArticol();
			tokDepozArticol = articol.getDepozit();
			tokUmArticol = articol.getUm();
			tokObs = articol.getObservatii();
			tokMultiplu = String.valueOf(articol.getMultiplu());
			tokInfoArticol = articol.getInfoArticol();
			tokCantUmb = String.valueOf(articol.getCantUmb());
			tokUmb = articol.getUmb();
			tokAlteValori = articol.getAlteValori();
			tokDepart = articol.getDepart();
			tokTipArt = articol.getTipArt();
			conditie = tokObs.contains("conditie");

			listArticoleComanda.remove(i);

			preturiArt = tokAlteValori.split("!");
			valTotArt = Double.parseDouble(preturiArt[0]);

			artPretPromo = preturiArt[5];

			if (artPretPromo.equals("1"))
				roundedProcRed = "0";

			// pret unitar
			newUnitPrice = (valTotArt / articol.getCantitate()) * articol.getMultiplu() - (valTotArt / articol.getCantitate()) * articol.getMultiplu()
					* (Double.valueOf(roundedProcRed) / 100);

			// valoare articol
			newTotalPrice = valTotArt - valTotArt * (Double.valueOf(roundedProcRed) / 100);

			// factura de reducere
			procRedFact = 0;
			if (Double.parseDouble(preturiArt[1]) != 0)
				procRedFact = (Double.parseDouble(preturiArt[2]) / articol.getCantitate() * articol.getMultiplu() - newUnitPrice)
						/ (Double.parseDouble(preturiArt[1]) / articol.getCantitate() * articol.getMultiplu()) * 100;

			strProcRedFact = nf2.format(procRedFact);

			// procent discount pt. aprobare
			procDiscClient = 0;
			if (Double.parseDouble(preturiArt[1]) > 0)
				procDiscClient = 100 - (Double.parseDouble(preturiArt[2]) / Double.parseDouble(preturiArt[1])) * 100;

			strProcDiscClient = nf2.format(procDiscClient);

			procentAprob = (1 - newUnitPrice / (Double.parseDouble(preturiArt[1]) / articol.getCantitate() * articol.getMultiplu())) * 100;

			strProcentAprob = nf2.format(procentAprob);

			// tip alerta SD/DV
			localTipAlert = "";

			if (procentAprob > Double.parseDouble(preturiArt[3])) {
				localTipAlert = "SD";
			}

			if (procentAprob > Double.parseDouble(preturiArt[4])) {
				localTipAlert = "DV";
			}

			articol.setNrCrt(counter);
			articol.setNumeArticol(tokNumeArticol);
			articol.setCodArticol(tokCodArticol);
			articol.setCantitate(Double.parseDouble(tokCantArticol));
			articol.setUm(tokUmArticol);
			articol.setPret(newTotalPrice);
			articol.setPretUnit(newUnitPrice);
			articol.setMoneda(tokMonedaArticol);
			articol.setDepozit(tokDepozArticol);
			articol.setProcent(Double.valueOf(roundedProcRed));
			articol.setConditie(conditie);
			articol.setPromotie(-1);
			articol.setProcentFact(Double.parseDouble(strProcRedFact));
			articol.setDiscClient(Double.parseDouble(strProcDiscClient));
			articol.setObservatii(localTipAlert);
			articol.setProcAprob(Double.parseDouble(strProcentAprob));
			articol.setMultiplu(Double.parseDouble(tokMultiplu));
			articol.setInfoArticol(tokInfoArticol);
			articol.setCantUmb(Double.valueOf(tokCantUmb));
			articol.setUmb(tokUmb);
			articol.setAlteValori(tokAlteValori);
			articol.setDepart(tokDepart);
			articol.setTipArt(tokTipArt);

			if (tokObs.contains("Pret"))
				articol.setPromotie(1); // Pret promotional
			if (tokObs.contains("Articol"))
				articol.setPromotie(2); // Articol cu promotie

			listArticoleComanda.add(--counter, articol);

			counter++;

		}

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

				if (articol.getInfoArticol() == null || !articol.getInfoArticol().contains(";"))
					continue;

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
