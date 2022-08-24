package my.logon.screen.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;

public class HelperMathaus {

	public static void adaugaArticolTransport(List<CostTransportMathaus> costTransport, String canalDistrib) {

		List<ArticolComanda> listArticoleComanda;
		if (canalDistrib.equals("10"))
			listArticoleComanda = ListaArticoleComanda.getInstance().getListArticoleComanda();
		else
			listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleComanda();

		eliminaCostTransport(listArticoleComanda, costTransport);

		for (CostTransportMathaus cost : costTransport) {

			for (ArticolComanda articol : listArticoleComanda) {

				if (cost.getFiliala().equals(articol.getFilialaSite()) && !cost.getValTransp().equals("0")) {
					listArticoleComanda.add(genereazaArticolTransport(articol, cost));
					break;
				}

			}

		}

	}

	public static void eliminaCostTransport(List<ArticolComanda> listArticole, List<CostTransportMathaus> costTransport) {

		Iterator<ArticolComanda> iterator = listArticole.iterator();

		for (CostTransportMathaus cost : costTransport) {

			while (iterator.hasNext()) {

				ArticolComanda articol = iterator.next();

				if (cost.getCodArtTransp().replaceAll("^0+", "").equals(articol.getCodArticol().replaceAll("^0+", ""))) {
					iterator.remove();
				}

			}

			iterator = listArticole.iterator();
		}

	}

	private static ArticolComanda genereazaArticolTransport(ArticolComanda articol, CostTransportMathaus costTransport) {
		ArticolComanda articolComanda = new ArticolComanda();

		articolComanda.setCodArticol(costTransport.getCodArtTransp().replaceAll("^0+", ""));
		articolComanda.setNumeArticol("PREST.SERV.TRANSPORT");
		articolComanda.setCantitate(1);
		articolComanda.setCantUmb(1);
		articolComanda.setPretUnit(Double.valueOf(costTransport.getValTransp()));
		articolComanda.setPret(articolComanda.getPretUnit());
		articolComanda.setPretUnitarClient(articolComanda.getPretUnit());
		articolComanda.setPretUnitarGed(articolComanda.getPretUnit());
		articolComanda.setProcent(0);
		articolComanda.setUm("BUC");
		articolComanda.setUmb("BUC");
		articolComanda.setDiscClient(0);
		articolComanda.setProcentFact(0);
		articolComanda.setMultiplu(1);
		articolComanda.setConditie(false);
		articolComanda.setProcAprob(0);
		articolComanda.setInfoArticol(" ");
		articolComanda.setObservatii("");
		articolComanda.setDepartAprob("");
		articolComanda.setIstoricPret("");
		articolComanda.setAlteValori("");
		articolComanda.setDepozit(getDepozitDescarcare(articol.getDepart()));
		articolComanda.setTipArt("");
		articolComanda.setDepart(articol.getDepart());
		articolComanda.setDepartSintetic(articol.getDepart());
		articolComanda.setFilialaSite(costTransport.getFiliala());
		articolComanda.setTipTransport(costTransport.getTipTransp());

		return articolComanda;
	}

	private static String getDepozitDescarcare(String depart) {
		if (depart.substring(0, 2).equals("11"))
			return "MAV1";
		else
			return depart.substring(0, 2) + "V1";
	}

	public static List<RezumatComanda> getRezumatComanda(List<ArticolComanda> listArticole) {

		Set<String> filiale = getFilialeComanda(listArticole);

		List<RezumatComanda> listComenzi = new ArrayList<RezumatComanda>();

		for (String filiala : filiale) {

			RezumatComanda rezumat = new RezumatComanda();
			rezumat.setFilialaLivrare(filiala);
			List<ArticolComanda> listArtComanda = new ArrayList<ArticolComanda>();

			for (ArticolComanda articol : listArticole) {
				if (articol.getFilialaSite().equals(filiala)) {
					listArtComanda.add(articol);
				}
			}

			rezumat.setListArticole(listArtComanda);
			listComenzi.add(rezumat);
		}

		return listComenzi;
	}

	private static Set<String> getFilialeComanda(List<ArticolComanda> listArticole) {

		Set<String> filiale = new HashSet<String>();
		for (final ArticolComanda articol : listArticole) {
			filiale.add(articol.getFilialaSite());
		}
		return filiale;

	}

}
