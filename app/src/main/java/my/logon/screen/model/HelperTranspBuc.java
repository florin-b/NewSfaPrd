package my.logon.screen.model;

import java.util.List;

import enums.EnumZona;
import my.logon.screen.utils.UtilsUser;

public class HelperTranspBuc {

	public static void adaugaTransportBucuresti(List<ArticolComanda> listArticole, EnumZona zonaBuc) {

		eliminaCostTransportZoneBuc(listArticole);


		if (zonaBuc == EnumZona.ZONA_A || zonaBuc == EnumZona.ZONA_B1 || zonaBuc == EnumZona.ZONA_B2) {
			String filialaArt = getFilialaArt(listArticole);
			ArticolComanda articolTransport = generateArticolTransport(zonaBuc, filialaArt);
			listArticole.add(articolTransport);
		}

	}

	private static ArticolComanda generateArticolTransport(EnumZona zonaBuc, String filialaArt) {

		ArticolComanda articolComanda = new ArticolComanda();

		String codArticol;
		String numeArticol;
		String codDepart = getDepartArtTransp();
		if (zonaBuc == EnumZona.ZONA_A) {
			codArticol = transportZonaA(codDepart);
			numeArticol = "Servicii livrare zona A";
		} else {
			codArticol = transportZonaB(codDepart);
			numeArticol = "Serv.descarc.zona B";
		}

		articolComanda.setCodArticol(codArticol);
		articolComanda.setNumeArticol(numeArticol);
		articolComanda.setCantitate(1);
		articolComanda.setCantUmb(1);
		articolComanda.setPretUnit(10);
		articolComanda.setPret(10);
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
		articolComanda.setDepozit(codDepart.substring(0, 2) + "V1");
		articolComanda.setTipArt("");
		articolComanda.setDepart(codDepart);
		articolComanda.setDepartSintetic(codDepart);
		articolComanda.setFilialaSite(filialaArt);

		return articolComanda;

	}

	private static String transportZonaATest(String depart) {
		String codArticol = "";

		if (depart.equals("01"))
			codArticol = "30101750";

		if (depart.equals("02"))
			codArticol = "30101752";

		if (depart.equals("03"))
			codArticol = "30101754";

		if (depart.equals("04"))
			codArticol = "30101756";

		if (depart.equals("05"))
			codArticol = "30101758";

		if (depart.equals("06"))
			codArticol = "30101760";

		if (depart.equals("07"))
			codArticol = "30101762";

		if (depart.equals("08"))
			codArticol = "30101764";

		if (depart.equals("09"))
			codArticol = "30101766";

		return codArticol;
	}

	private static String transportZonaA(String depart) {
		String codArticol = "";

		if (depart.equals("01"))
			codArticol = "30101742";

		if (depart.equals("02"))
			codArticol = "30101745";

		if (depart.equals("03"))
			codArticol = "30101747";

		if (depart.equals("040"))
			codArticol = "30101926";

		if (depart.equals("041"))
			codArticol = "30101749";

		if (depart.equals("05"))
			codArticol = "30101751";

		if (depart.equals("06"))
			codArticol = "30101753";

		if (depart.equals("07"))
			codArticol = "30101755";

		if (depart.equals("08"))
			codArticol = "30101757";

		if (depart.equals("09"))
			codArticol = "30101759";

		return codArticol;
	}

	private static String transportZonaBTest(String depart) {
		String codArticol = "";

		if (depart.equals("01"))
			codArticol = "30101751";

		if (depart.equals("02"))
			codArticol = "30101753";

		if (depart.equals("03"))
			codArticol = "30101755";

		if (depart.equals("04"))
			codArticol = "30101757";

		if (depart.equals("05"))
			codArticol = "30101759";

		if (depart.equals("06"))
			codArticol = "30101761";

		if (depart.equals("07"))
			codArticol = "30101763";

		if (depart.equals("08"))
			codArticol = "30101765";

		if (depart.equals("09"))
			codArticol = "30101767";

		return codArticol;
	}

	private static String transportZonaB(String depart) {
		String codArticol = "";

		if (depart.equals("01"))
			codArticol = "30101744";

		if (depart.equals("02"))
			codArticol = "30101746";

		if (depart.equals("03"))
			codArticol = "30101748";

		if (depart.equals("040"))
			codArticol = "30101927";

		if (depart.equals("041"))
			codArticol = "30101750";

		if (depart.equals("05"))
			codArticol = "30101752";

		if (depart.equals("06"))
			codArticol = "30101754";

		if (depart.equals("07"))
			codArticol = "30101756";

		if (depart.equals("08"))
			codArticol = "30101758";

		if (depart.equals("09"))
			codArticol = "30101760";

		return codArticol;
	}

	public static void eliminaCostTransportZoneBuc(List<ArticolComanda> listArticole) {
		ArticolComanda articolTransportA = generateArticolTransport(EnumZona.ZONA_A, "");

		if (listArticole.contains(articolTransportA))
			listArticole.remove(articolTransportA);

		ArticolComanda articolTransportB = generateArticolTransport(EnumZona.ZONA_B1, "");

		if (listArticole.contains(articolTransportB))
			listArticole.remove(articolTransportB);

	}

	private static String getFilialaArt(List<ArticolComanda> listArticole) {

		String filialaArt = UserInfo.getInstance().getUnitLog();

		for (ArticolComanda articol : listArticole) {
			if (articol.getFilialaSite() != null && !articol.getFilialaSite().isEmpty()) {
				filialaArt = articol.getFilialaSite();
				break;
			}

		}

		return filialaArt;

	}

	private static String getDepartArtTransp() {
		String codDepart = "";
		if (UtilsUser.isAgentOrSD())
			codDepart = UserInfo.getInstance().getCodDepart();

		if (UtilsUser.isUserKA() || UtilsUser.isUserSK() || UtilsUser.isInfoUser() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM()
				|| UtilsUser.isCGED() || UtilsUser.isOIVPD()) {

			if (!ListaArticoleComanda.getInstance().getListArticoleComanda().isEmpty())
				codDepart = ListaArticoleComanda.getInstance().getListArticoleComanda().get(0).getDepart();
			else if (ListaArticoleModificareComanda.getInstance().getListArticoleComanda() != null && !ListaArticoleModificareComanda.getInstance().getListArticoleComanda().isEmpty())
				codDepart = ListaArticoleModificareComanda.getInstance().getListArticoleComanda().get(0).getDepart();

		}

		return codDepart;
	}

	public static boolean isCondTranspZonaBuc(DateLivrare dateLivrare, EnumZona zona) {

		if (!dateLivrare.getTransport().equalsIgnoreCase("TRAP"))
			return false;

		if (zona == EnumZona.ZONA_A)
			return true;
		else
			return dateLivrare.isMasinaMacara() && (zona == EnumZona.ZONA_B1 || zona == EnumZona.ZONA_B2);
	}

}
