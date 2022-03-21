package my.logon.screen.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumUrmarireObiective {

	CONTACTAT("Contactat", 0), OFERTAT("Ofertat", 1), NEGOCIAT("Negociat", 2), CONTRACTAT("Contractat", 3), FINALIZAT(
			"Finalizat", 4);

	private String nume;
	private int cod;

	EnumUrmarireObiective(String nume, int cod) {
		this.nume = nume;
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public int getCod() {
		return cod;
	}

	public static List<String> getNumeList() {
		List<String> listNumeObiective = new ArrayList<String>();

		for (EnumUrmarireObiective enumOb : EnumUrmarireObiective.values()) {
			listNumeObiective.add(enumOb.nume);
		}

		return listNumeObiective;

	}

	public static String getNumeEveniment(int cod) {
		String numeEveniment = "";
		for (EnumUrmarireObiective enumEv : EnumUrmarireObiective.values()) {
			if (enumEv.getCod() == cod) {
				numeEveniment = enumEv.getNume();
				break;
			}
		}

		return numeEveniment;
	}

}
