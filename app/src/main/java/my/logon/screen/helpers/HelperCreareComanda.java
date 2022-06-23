package my.logon.screen.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.logon.screen.beans.ArticolTaxaVerde;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.UserInfo;

public class HelperCreareComanda {

	private static final double LUNGIME_MIN_FIER_BETON_CM = 600;

	public static boolean isComandaAmbalaje(List<ArticolComanda> listArticole) {

		if (DateLivrare.getInstance().isClientFurnizor())
			return false;

		for (ArticolComanda articol : listArticole) {

			if (articol.getCategorie() == null || articol.getCategorie().trim().isEmpty())
				return false;

		}

		return true;

	}

	public static boolean isConditiiAlertaIndoire(List<ArticolComanda> listArticole) {
		boolean isArt04Lung = false;

		for (ArticolComanda articol : listArticole) {
			if (articol.getDepart().contains("04")) {

				if (articol.getLungime() > LUNGIME_MIN_FIER_BETON_CM)
					isArt04Lung = true;

			}
		}

		boolean isTonajMic = DateLivrare.getInstance().getTonaj().equals("3.5") || DateLivrare.getInstance().getTonaj().equals("10");

		boolean alertaIndoire;

		if (isFilialaRemorca() && DateLivrare.getInstance().getTonaj().equals("3.5"))
			alertaIndoire = false;
		else
			alertaIndoire = true;

		return DateLivrare.getInstance().getPrelucrare().equals("-1") && isArt04Lung && isTonajMic && alertaIndoire;

	}

	public static boolean isFilialaRemorca() {
		String filiala = UserInfo.getInstance().getUnitLog().substring(0, 2);
		String filialeRem = "BU, BV, CT, MS, TM";

		return filialeRem.contains(filiala);
	}

	public static List<ArticolTaxaVerde> getArticoleTVerde(List<ArticolTaxaVerde> listArticole) {

		Set<String> filialeArt = getFilialeComanda(listArticole);

		List<ArticolTaxaVerde> listArticoleTVerde = new ArrayList<>();

		for (String filiala : filialeArt) {

			ArticolTaxaVerde articolNou = new ArticolTaxaVerde();
			articolNou.setFiliala(filiala);

			double valTVerde = 0;
			String depart = "";
			String depozit = "";
			String transp = "";
			for (ArticolTaxaVerde articol : listArticole) {
				if (articol.getFiliala().equals(filiala)) {
					valTVerde += articol.getValoare();
					depart = articol.getDepart();
					depozit = articol.getDepozit();
					transp = articol.getTipTransp();
				}
			}

			articolNou.setTipTransp(transp);
			articolNou.setDepozit(depozit);
			articolNou.setDepart(depart);
			articolNou.setValoare(valTVerde);
			listArticoleTVerde.add(articolNou);

		}


		return listArticoleTVerde;

	}

	private static Set<String> getFilialeComanda(List<ArticolTaxaVerde> listArticole) {

		Set<String> filiale = new HashSet<String>();
		for (ArticolTaxaVerde articol : listArticole) {
			filiale.add(articol.getFiliala());
		}
		return filiale;

	}


}
