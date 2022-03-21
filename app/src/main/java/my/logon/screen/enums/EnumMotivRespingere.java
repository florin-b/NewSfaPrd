package my.logon.screen.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumMotivRespingere {

	M001("001", "Refuz total client"), M002("002", "Refuz partial client"), M003("003", "Client-Sediu client inchis"), M004(
			"004", "Client-Comanda eronata"), M005("005", "Agent-Corectie discount"), M006("006",
			"Agent-Comanda gresita"), M007("007", "Agent-Cheie articol eronata"), M008("008",
			"Gestionar-Marfa nelivrata"), M009("009", "Gestionar-Marfa incarcata eronat"), M010("010",
			"Gestionar-Lipsa stoc"), M011("011", "Facturare-Client gresit"), M012("012", "Facturare-Reper gresit"), M013(
			"013", "Facturare-Cantitate gresita"), M014("014", "Marfa defecta in palet sigilat"), M015("015",
			"Lipsa in cutii sigilate"), M016("016", "Cantitate cmd difera cantitate livrata"), M017("017",
			"Reducere ulterioara"), M018("018", "Retur paleti"), M019("019", "Card de fidelitate"), M020("020",
			"RETUR Garantie"), M021("021", "FARA CARD"), M022("022", "RETUR SITE"), M023("023", "Cupon Reducere"), M100(
			"100", "Diferenta pret: pretul a fost prea mare"), M101("101", "Calitate slaba"), M102("102",
			"Deteriorat in tranzit"), M103("103", "Diferenta cantitate"), M104("104", "Marfa deteriorata"), M105("105",
			"Mostra gratuita"), M200("200", "Diferenta pret: pretul a fost prea mic");

	private String cod;
	private String nume;

	EnumMotivRespingere(String cod, String nume) {
		this.cod = cod;
		this.nume = nume;
	}

	public String getNume() {
		return nume;
	}

	public String getCod() {
		return cod;
	}

	public static List<String> getStringList() {
		List<String> listMotive = new ArrayList<String>();

		for (EnumMotivRespingere enumM : EnumMotivRespingere.values()) {
			listMotive.add(enumM.nume);
		}

		return listMotive;

	}

	public static String getCodRetur(String numeRetur) {

		for (EnumMotivRespingere enumM : EnumMotivRespingere.values()) {
			if (enumM.nume.equals(numeRetur))
				return enumM.cod;

		}

		return "";
	}

}
