package my.logon.screen.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import my.logon.screen.beans.BeanDeficitDivizie;
import my.logon.screen.utils.UtilsComenziGed;

public class AlgoritmComandaGed {

	private double marjaBrutaGed = 0, marjaBrutaClient = 0;
	private double totalAdaosClientCorectat = 0;
	private double totalAdaosMinimReper = 0;
	private double coefComision = UserInfo.getInstance().getComisionCV() / 100;
	private Set<BeanDeficitDivizie> deficitDivizii;
	private double marjaDisponibila;

	public void calculProcenteComanda(ArrayList<ArticolComanda> listArticole, boolean seAplicaAlgoritm) {

		double pretUnitarClient = 0, pretUnitarGed = 0, cantitate = 0, cmp = 0;

		double pretMediuDistrib = 0, adaosMediuDistrib = 0, coefZ = 0;
		double adaosMinimArticol = 0, marjaMedieDistrib = 0, adaosClientCorectat = 0, adaosMinimReper = 0;

		marjaBrutaGed = 0;
		marjaBrutaClient = 0;
		totalAdaosClientCorectat = 0;
		totalAdaosMinimReper = 0;

		ArticolComanda articol = null;

		if (seAplicaAlgoritm) {

			for (int i = 0; i < listArticole.size(); i++) {
				articol = listArticole.get(i);

				if (articol.getPromotie() <= 0) {

					pretUnitarClient = articol.getPretUnitarClient();

					if (pretUnitarClient == 0)
						continue;

					if (UtilsComenziGed.isArticolTransport(articol) || articol.isRespins())
						continue;

					pretUnitarGed = getPretGed(articol);

					// exceptie articol transport
					if (pretUnitarGed == 0)
						pretUnitarGed = pretUnitarClient;

					cantitate = articol.getCantitate();

					cmp = round(articol.getCmp() * Constants.TVA, 3); // cmp cu
																		// tva

					marjaBrutaClient += (pretUnitarClient - cmp) * cantitate;
					articol.setMarjaClient((pretUnitarClient - cmp) * cantitate);
					marjaBrutaGed += (pretUnitarGed - cmp) * cantitate;
					articol.setMarjaGed((pretUnitarGed - cmp) * cantitate);

					pretMediuDistrib = 0;
					adaosMediuDistrib = 0;

					if (!articol.getDepozit().equals("0000")) {
						if (!articol.getDepartSintetic().equals("11")) {

							pretMediuDistrib = articol.getPretMediu() * Constants.TVA;
							adaosMediuDistrib = articol.getAdaosMediu() * Constants.TVA;
						} else if (articol.getDepartSintetic().equals("11")) {

							double pretGed = getPretGed(articol);

							double marjaBruta = (pretGed) * articol.getMultiplu() - articol.getCmp() * Constants.TVA;

							pretMediuDistrib = (pretGed) * articol.getMultiplu() - marjaBruta * 0.19;
							adaosMediuDistrib = pretMediuDistrib - articol.getCmp() * Constants.TVA;

						}
					}

					if (pretMediuDistrib == 0) // se ia in calcul pretul ged
					{

						pretMediuDistrib = pretUnitarGed;
						adaosMediuDistrib = 0;
					}

					if (articol.getDepartSintetic().equals("11")) // articole
																	// din MAV1
					{
						coefZ = 1;
					} else {

						coefZ = articol.getCoefCorectie() != 0 ? articol.getCoefCorectie() : getCoefZDepart(articol.getDepartSintetic());
					}

					if (adaosMediuDistrib < 0) {
						adaosMinimArticol = Math.abs(adaosMediuDistrib) * (coefZ - 1);
					} else {
						adaosMinimArticol = adaosMediuDistrib * coefZ;
					}

					articol.setAdaosMinimArticol(adaosMinimArticol);

					marjaMedieDistrib = round((adaosMediuDistrib / pretMediuDistrib) * 100, 3);

					if (articol.getDepartSintetic().equals("11")) {
						adaosClientCorectat = round((round(pretUnitarClient, 3) - (pretMediuDistrib - pretMediuDistrib * (marjaMedieDistrib / 100)))
								* cantitate, 3);
					} else {
						adaosClientCorectat = round((round(pretUnitarClient, 3) - (pretMediuDistrib - pretMediuDistrib * (marjaMedieDistrib / 100)))
								* cantitate, 3);
					}

					articol.setAdaosClientCorectat(adaosClientCorectat);

					totalAdaosClientCorectat += adaosClientCorectat;

					adaosMinimReper = round(adaosMinimArticol * (cantitate / articol.getMultiplu()), 3);

					articol.setAdaosMinimAcceptat(adaosMinimReper);

					totalAdaosMinimReper += adaosMinimReper;

					articol.setMarjaCorectata(-1);
					articol.setTipAlertPret(" ");

				}
			}

		}

		if (marjaBrutaGed < 0)
			marjaBrutaGed = 0;

		if (marjaBrutaClient < 0)
			marjaBrutaClient = 0;

		double varProc = 0;
		double marjaTeoretica = 1;

		// recalculare procent si pret
		for (int i = 0; i < listArticole.size(); i++) {
			articol = listArticole.get(i);

			if (articol.getPromotie() <= 0 & articol.getPretUnitarGed() > 0) {
				articol.setReducerePonderata(articol.getMarjaCorectata() / marjaTeoretica * marjaBrutaClient);
				articol.setPretUnitarPonderat(articol.getPretUnitarClient());
				varProc = (articol.getPretUnitarGed() - articol.getPretUnitarPonderat()) / articol.getPretUnitarGed() * 100;
				articol.setProcent(varProc);
				articol.setProcAprob(varProc);
			}

		}

	}

	public void redistribuireMarja(ArrayList<ArticolComanda> listArticole, double valTransport) {

		double deficitArticol = 0;
		deficitDivizii = new TreeSet<BeanDeficitDivizie>();
		marjaDisponibila = valTransport;

		double tempMarja = 0;

		for (int i = 0; i < listArticole.size(); i++) {

			tempMarja = listArticole.get(i).getAdaosClientCorectat() - listArticole.get(i).getAdaosMinimAcceptat();

			if (tempMarja > 0)
				marjaDisponibila += tempMarja;

			deficitArticol = tempMarja;
			listArticole.get(i).setDeficit(deficitArticol);

			if (deficitArticol < 0)
				addDeficitDivizii(listArticole.get(i).getDepart(), deficitArticol);

		}

		if (marjaDisponibila > 0) {

			Iterator<BeanDeficitDivizie> iterator = deficitDivizii.iterator();

			BeanDeficitDivizie deficitDivizie = null;

			double tempMarjaDisponibila = marjaDisponibila;
			while (iterator.hasNext()) {
				deficitDivizie = iterator.next();

				if (tempMarjaDisponibila - Math.abs(deficitDivizie.getValDeficit()) > 0) {
					tempMarjaDisponibila -= Math.abs(deficitDivizie.getValDeficit());
					deficitDivizie.setAcoperit(true);
				} else {
					break;
				}

			}

		}

		setStareDeficitArticole(listArticole);

		return;

	}

	private void addDeficitDivizii(String divizie, double deficit) {

		BeanDeficitDivizie deficitDivizie = new BeanDeficitDivizie();
		deficitDivizie.setCodDivizie(divizie);
		deficitDivizie.setValDeficit(deficit);

		Iterator<BeanDeficitDivizie> iterator = deficitDivizii.iterator();

		BeanDeficitDivizie objDeficit = null;
		boolean objectDivizieExists = false;
		while (iterator.hasNext()) {
			objDeficit = iterator.next();
			if (objDeficit.getCodDivizie().equals(divizie)) {
				objDeficit.setValDeficit(deficit + objDeficit.getValDeficit());
				objectDivizieExists = true;
				break;
			}
		}

		if (!objectDivizieExists)
			deficitDivizii.add(deficitDivizie);

	}

	private void setStareDeficitArticole(List<ArticolComanda> listArticole) {

		for (int i = 0; i < listArticole.size(); i++) {

			if (listArticole.get(i).getPromotie() >= 1)
				listArticole.get(i).setPonderare(0);
			else {
				if (isDeficitDivizieAcoperit(listArticole.get(i).getDepart()) || listArticole.get(i).getDeficit() >= 0) {
					listArticole.get(i).setPonderare(2);
					listArticole.get(i).setTipAlertPret(" ");
				} else {
					listArticole.get(i).setPonderare(1);
					listArticole.get(i).setTipAlertPret("FM");
				}

			}

		}

	}

	private boolean isDeficitDivizieAcoperit(String divizie) {
		Iterator<BeanDeficitDivizie> iterator = deficitDivizii.iterator();
		boolean isAcoperit = true;

		BeanDeficitDivizie deficitDivizie = null;
		while (iterator.hasNext()) {
			deficitDivizie = iterator.next();
			if (deficitDivizie.getCodDivizie().equals(divizie)) {
				isAcoperit = deficitDivizie.isAcoperit();
				break;
			}

		}

		return isAcoperit;

	}

	private static double round(double value, int places) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	private static double getCoefZDepart(String codDepart) {
		double localCoef = 0;

		if (UserInfo.getInstance().getCoefCorectie().contains(codDepart)) {
			String[] arrayCoefs = UserInfo.getInstance().getCoefCorectie().split(";");

			for (String coef : arrayCoefs) {

				if (coef.contains(":")) {

					String[] oneCoef = coef.split(":");

					if (oneCoef[0].equals(codDepart)) {
						localCoef = Double.parseDouble(oneCoef[1]);
						break;
					}
				}

			}
		}

		return localCoef;
	}

	private double getPretGed(ArticolComanda articol) {
		double pretGed = 0.0;
		double valNETW = 0.0;
		double valMWSI = 0.0;

		// ZVK0:51.60;ZNET:51.60;MWSI:12.38;ZTAX:0.00;NETW:51.60;ZPRR:31.99;
		if (articol.getInfoArticol().contains(";")) {

			String[] arrayInfo = articol.getInfoArticol().split(";");
			String[] tokValue;

			for (int i = 0; i < arrayInfo.length; i++) {
				if (arrayInfo[i].toUpperCase().contains("NETW")) {
					tokValue = arrayInfo[i].split(":");
					valNETW = Double.valueOf(tokValue[1]);
					continue;
				}

				if (arrayInfo[i].toUpperCase().contains("MWSI")) {
					tokValue = arrayInfo[i].split(":");
					valMWSI = Double.valueOf(tokValue[1]);
				}

			}

			pretGed = (valNETW + valMWSI) / articol.getCantitate();

		}

		return pretGed;

	}

	// schimbare alerta din rosu in galben
	public void schimbaAlertaArticol(ArrayList<ArticolComanda> listArticole) {

		for (int i = 0; i < listArticole.size(); i++) {
			if (listArticole.get(i).getTipAlertPret() != null)
				if (listArticole.get(i).getPonderare() == 1) {
					listArticole.get(i).setTipAlertPret("M");
					listArticole.get(i).setPonderare(2);

				}

		}

	}

	public void inlaturaToateAlertelePret(ArrayList<ArticolComanda> listArticole) {

		for (int i = 0; i < listArticole.size(); i++) {
			listArticole.get(i).setTipAlertPret(" ");

		}

	}

	public double getMarjaBrutaGed() {
		return marjaBrutaGed;
	}

	public double getMarjaBrutaClient() {
		return marjaBrutaClient;
	}

	public double getTotalAdaosClientCorectat() {
		return totalAdaosClientCorectat;
	}

	public double getTotalAdaosMinimReper() {
		return totalAdaosMinimReper;
	}

	public double getCoefComision() {
		return coefComision;
	}

	public double getMarjaDisponibila() {
		return marjaDisponibila;
	}

	public double getTotalPretGedComanda(ArrayList<ArticolComanda> listArticole) {
		double totalGed = 0;

		for (int i = 0; i < listArticole.size(); i++) {
			totalGed += (listArticole.get(i).getPretUnitarGed() * listArticole.get(i).getCantUmb()) / listArticole.get(i).getMultiplu();
		}

		return totalGed;
	}

	public double getTotalPretClientComanda(ArrayList<ArticolComanda> listArticole) {
		double totalClient = 0;

		for (int i = 0; i < listArticole.size(); i++) {
			totalClient += (listArticole.get(i).getPretUnitarClient() * listArticole.get(i).getCantUmb()) / listArticole.get(i).getMultiplu();
		}

		return totalClient;
	}

	public Set<BeanDeficitDivizie> getDeficitDivizii() {
		return deficitDivizii;
	}

	public double getMarjaBrutaClient(ArrayList<ArticolComanda> listArticole){
		double marjaBrutaClient = 0;
		double cmp = 0;

		for (int i = 0; i < listArticole.size(); i++) {
			ArticolComanda articol = listArticole.get(i);
			cmp = round(articol.getCmp() * Constants.TVA, 3);
			marjaBrutaClient += (articol.getPretUnitarClient() - cmp) * articol.getCantitate();
		}

		return marjaBrutaClient;
	}

}
