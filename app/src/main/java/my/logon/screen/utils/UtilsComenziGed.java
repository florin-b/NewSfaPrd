package my.logon.screen.utils;

import java.util.ArrayList;

import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.UserInfo;

public class UtilsComenziGed {

	private static final double VALOARE_COMANDA = 400;
	private static final double VALOARE_TRANSPORT = 25;

	public static double getValoareTransportSap(ArrayList<ArticolComanda> listArticole) {

		double totalTransp = 0;

		for (ArticolComanda art : listArticole) {
			totalTransp += art.getValTransport();
		}

		if (userCannotModifyPrice())
			totalTransp += getValoareAdaugataTransport(listArticole);

		return totalTransp;
	}

	private static double getValoareAdaugataTransport(ArrayList<ArticolComanda> listArticole) {
		double valTranspAdaugat = 0;

		if (!UtilsUser.isAgentOrSDorKA()) {
			double valoareMarfa = getValoareMarfa(listArticole);

			if (valoareMarfa > 0 && valoareMarfa < VALOARE_COMANDA)
				valTranspAdaugat = VALOARE_TRANSPORT;

			if (valoareMarfa > 0 && valoareMarfa > VALOARE_COMANDA) {
				valTranspAdaugat = 0;
			}
		}

		return valTranspAdaugat;
	}

	public static double getValoareScazutaTransport(ArrayList<ArticolComanda> listArticole) {
		double valTranspScazut = 0;

		if (!UtilsUser.isAgentOrSDorKA()) {
			double valoareMarfa = getValoareMarfa(listArticole);

			if (valoareMarfa > 0 && valoareMarfa < VALOARE_COMANDA)
				valTranspScazut = 0;

			if (valoareMarfa > 0 && valoareMarfa > VALOARE_COMANDA) {
				valTranspScazut = VALOARE_TRANSPORT;
			}
		}

		return valTranspScazut;
	}

	public static double getValoareMarfa(ArrayList<ArticolComanda> listArticole) {
		double valoareMarfa = 0;

		for (ArticolComanda articol : listArticole) {
			if (articol.getCmp() > 0) {
				valoareMarfa += articol.getPretUnitarClient() * articol.getCantUmb();
			}

		}

		return valoareMarfa;
	}

	public static double getValoareTransportComanda(ArrayList<ArticolComanda> listArticole) {

		double totalTranspCmd = 0;

		for (ArticolComanda art : listArticole) {
			if (art.getNumeArticol().toLowerCase().contains("servicii") && art.getNumeArticol().toLowerCase().contains("transport"))
				totalTranspCmd += art.getPretUnit();
		}

		return totalTranspCmd;
	}

	public static boolean isArticolTransport(ArticolComanda articol) {
		return articol.getNumeArticol().toLowerCase().contains("servicii") && articol.getNumeArticol().toLowerCase().contains("transport");
	}

	public static void setValoareArticolTransport(ArrayList<ArticolComanda> listArticole, double valoareTransport) {

		boolean pretTransportSet = false;

		for (ArticolComanda art : listArticole) {

			if (isArticolTransport(art)) {
				art.setPretUnit(valoareTransport);
				pretTransportSet = true;
				break;
			}

		}

		if (!pretTransportSet && !listArticole.isEmpty()) {

			String filiala = " ";
			String depozit = " ";
			String depart = " ";

			if (!listArticole.isEmpty()) {
				filiala = listArticole.get(0).getFilialaSite();
				depozit = listArticole.get(0).getDepozit();
				depart = listArticole.get(0).getDepart();
			}

			ArticolComanda articol = new ArticolComanda();
			articol.setCodArticol("30101050");
			articol.setNumeArticol("PRESTARI SERVICII TRANSPORT");
			articol.setCantitate(1.0);
			articol.setDepozit(depozit);
			articol.setPretUnit(valoareTransport);
			articol.setProcent(0);
			articol.setUm("BUC");
			articol.setProcentFact(0);
			articol.setConditie(false);
			articol.setDiscClient(0);
			articol.setProcAprob(0);
			articol.setMultiplu(1);
			articol.setPret(valoareTransport);
			articol.setInfoArticol(" ");
			articol.setCantUmb(1);
			articol.setUmb("BUC");
			articol.setPonderare(2);
			articol.setFilialaSite(filiala);
			articol.setTipArt(" ");
			articol.setDepart(depart);
			articol.setDepartSintetic(depart);
			articol.setAlteValori(" ");
			articol.setTipAlert(" ");
			articol.setCmp(0);

			listArticole.add(0, articol);

		}

	}

	private static boolean userCannotModifyPrice() {
		return UserInfo.getInstance().getTipUserSap().equals("CONS-GED") || UserInfo.getInstance().getTipUserSap().equals("CVR");
	}

}
