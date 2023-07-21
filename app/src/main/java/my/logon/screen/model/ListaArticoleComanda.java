package my.logon.screen.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import my.logon.screen.beans.ArticolMathaus;
import my.logon.screen.screens.CreareComanda;

public class ListaArticoleComanda extends Observable {

	private int articolIndex = -1;
	private static ListaArticoleComanda instance = new ListaArticoleComanda();
	private List<ArticolComanda> listArticoleComanda = new ArrayList<ArticolComanda>();

	private ArrayList<ArticolComanda> listArticoleLivrare = new ArrayList<>();

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

	public void addArticolLivrareComanda(ArticolComanda articolComanda) {
		if (!articolExists(articolComanda))
			listArticoleLivrare.add(articolComanda);
		else {
			listArticoleLivrare.remove(articolIndex);
			listArticoleLivrare.add(articolIndex, articolComanda);
		}

	}

	public ArrayList<ArticolComanda> getListArticoleLivrare() {
		return listArticoleLivrare;
	}

	public void reseteazaArticoleLivrare(){
		this.listArticoleLivrare = new ArrayList<>();
	}

	public ArticolComanda genereazaArticolLivrare(ArticolComanda articolComanda){

		ArticolComanda articolLivrare = new ArticolComandaGed();

		articolLivrare.setNrCrt(articolComanda.getNrCrt());
		articolLivrare.setNumeArticol(articolComanda.getNumeArticol());
		articolLivrare.setCodArticol(articolComanda.getCodArticol());
		articolLivrare.setDepozit(articolComanda.getDepozit());
		articolLivrare.setCantitate(articolComanda.getCantitate());
		articolLivrare.setUm(articolComanda.getUm());
		articolLivrare.setPret(articolComanda.getPret());
		articolLivrare.setMoneda(articolComanda.getMoneda());
		articolLivrare.setProcent(articolComanda.getProcent());
		articolLivrare.setObservatii(articolComanda.getObservatii());
		articolLivrare.setConditie(articolComanda.getConditie());
		articolLivrare.setPromotie(articolComanda.getPromotie());
		articolLivrare.setProcentFact(articolComanda.getProcentFact());
		articolLivrare.setPretUnit(articolComanda.getPretUnit());
		articolLivrare.setDiscClient(articolComanda.getDiscClient());
		articolLivrare.setTipAlert(articolComanda.getTipAlert());
		articolLivrare.setProcAprob(articolComanda.getProcAprob());
		articolLivrare.setMultiplu(articolComanda.getMultiplu());
		articolLivrare.setInfoArticol(articolComanda.getInfoArticol());
		articolLivrare.setCantUmb(articolComanda.getCantUmb());
		articolLivrare.setUmb(articolComanda.getUmb());
		articolLivrare.setAlteValori(articolComanda.getAlteValori());
		articolLivrare.setDepart(articolComanda.getDepart());
		articolLivrare.setTipArt(articolComanda.getTipArt());
		articolLivrare.setTaxaVerde(articolComanda.getTaxaVerde());
		articolLivrare.setPretUnitarPonderat(articolComanda.getPretUnitarPonderat());
		articolLivrare.setPretUnitarClient(articolComanda.getPretUnitarClient());
		articolLivrare.setUnitLogAlt(articolComanda.getUnitLogAlt());
		articolLivrare.setStatus(articolComanda.getStatus());
		articolLivrare.setCmp(articolComanda.getCmp());
		articolLivrare.setAddCond(articolComanda.getAddCond());
		articolLivrare.setPretUnitarGed(articolComanda.getPretUnitarGed());
		articolLivrare.setMarjaClient(articolComanda.getMarjaClient());
		articolLivrare.setMarjaCorectata(articolComanda.getMarjaCorectata());
		articolLivrare.setReducerePonderata(articolComanda.getReducerePonderata());
		articolLivrare.setMarjaGed(articolComanda.getMarjaGed());
		articolLivrare.setTipAlertPret(articolComanda.getTipAlertPret());
		articolLivrare.setAdaosMinimArticol(articolComanda.getAdaosMinimArticol());
		articolLivrare.setAdaosClientCorectat(articolComanda.getAdaosClientCorectat());
		articolLivrare.setAdaosMinimAcceptat(articolComanda.getAdaosMinimAcceptat());
		articolLivrare.setPonderare(articolComanda.getPonderare());
		articolLivrare.setDiscountAg(articolComanda.getDiscountAg());
		articolLivrare.setDiscountSd(articolComanda.getDiscountSd());
		articolLivrare.setDiscountDv(articolComanda.getDiscountDv());
		articolLivrare.setPermitSubCmp(articolComanda.getPermitSubCmp());
		articolLivrare.setCoefCorectie(articolComanda.getCoefCorectie());
		articolLivrare.setPretMediu(articolComanda.getPretMediu());
		articolLivrare.setAdaosMediu(articolComanda.getAdaosMediu());
		articolLivrare.setUnitMasPretMediu(articolComanda.getUnitMasPretMediu());
		articolLivrare.setDepartSintetic(articolComanda.getDepartSintetic());
		articolLivrare.setConditii(articolComanda.hasConditii());
		articolLivrare.setRespins(articolComanda.isRespins());
		articolLivrare.setDeficit(articolComanda.getDeficit());
		articolLivrare.setValTransport(articolComanda.getValTransport());
		articolLivrare.setProcTransport(articolComanda.getProcTransport());
		articolLivrare.setDepartAprob(articolComanda.getDepartAprob());
		articolLivrare.setUmPalet(articolComanda.isUmPalet());
		articolLivrare.setFilialaSite(articolComanda.getFilialaSite());
		articolLivrare.setIstoricPret(articolComanda.getIstoricPret());
		articolLivrare.setVechime(articolComanda.getVechime());
		articolLivrare.setCategorie(articolComanda.getCategorie());
		articolLivrare.setLungime(articolComanda.getLungime());
		articolLivrare.setProcT1(articolComanda.getProcT1());
		articolLivrare.setValT1(articolComanda.getValT1());
		articolLivrare.setDataExpPret(articolComanda.getDataExpPret());
		if (articolComanda.getArticolMathaus() != null)
			articolLivrare.setArticolMathaus(new ArticolMathaus(articolComanda.getArticolMathaus()));

		articolLivrare.setListCabluri(articolComanda.getListCabluri());
		articolLivrare.setPretFaraTva(articolComanda.getPretFaraTva());
		articolLivrare.setAczcDeLivrat(articolComanda.getAczcDeLivrat());
		articolLivrare.setAczcLivrat(articolComanda.getAczcLivrat());
		articolLivrare.setTipTransport(articolComanda.getTipTransport());
		articolLivrare.setGreutate(articolComanda.getGreutate());
		articolLivrare.setGreutateBruta(articolComanda.getGreutateBruta());

		articolLivrare.setPonderare(articolComanda.getPonderare());

		return articolLivrare;

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

	public void eliminaArticolLivrare(String codArticol, String filiala){
		Iterator<ArticolComanda> iterator = listArticoleLivrare.iterator();

		while (iterator.hasNext()) {
			ArticolComanda art = iterator.next();
			if (art.getCodArticol().equals(codArticol) && art.getFilialaSite().equals(filiala)) {
				iterator.remove();
				break;
			}
		}
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

	public double getGreutateKgArticole(){

		double greutate = 0;

		for (ArticolComanda articol : listArticoleComanda) {
				greutate += articol.getGreutateBruta();

		}

		return greutate;
	}

	public boolean isComandaEnergofaga(){

		for (ArticolComanda articol : listArticoleComanda) {
			if (articol.getTipMarfa().equals(Constants.TIP_ARTICOL_ENERGOFAG))
				return true;
		}

		return false;
	}

	public boolean isComandaExtralungi(){

		for (ArticolComanda articol : listArticoleComanda) {
			if (articol.getLungimeArt().toLowerCase().equals("extralungi"))
				return true;
		}

		return false;
	}


	private void triggerObservers() {

		setChanged();
		notifyObservers(listArticoleComanda);
	}

}
