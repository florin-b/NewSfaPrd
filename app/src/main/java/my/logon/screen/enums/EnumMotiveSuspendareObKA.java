package my.logon.screen.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumMotiveSuspendareObKA {

	MOTIV_SUSPENDARE("Selectati motiv suspendare", 99), BLOCARE_FINANTARE("Blocare finantare", 0), PROBLEME_JURIDICE("Probleme juridice", 1), PROBLEME_TEHNICE(
			"Probleme tehnice", 2), ALTE_CAUZE("Alte cauze", 3);

	private String numeMotiv;
	private int codMotiv;

	EnumMotiveSuspendareObKA(String numeMotiv, int codMotiv) {
		this.numeMotiv = numeMotiv;
		this.codMotiv = codMotiv;
	}

	public String getNumeMotiv() {
		return numeMotiv;
	}

	public int getCodMotiv() {
		return codMotiv;
	}

	public static List<String> getListMotive() {

		List<String> listMotive = new ArrayList<String>();
		for (EnumMotiveSuspendareObKA motive : EnumMotiveSuspendareObKA.values()) {
			listMotive.add(motive.getNumeMotiv());
		}

		return listMotive;

	}

	public static String getNumeMotiv(int codMotiv) {

		for (EnumMotiveSuspendareObKA enumSt : EnumMotiveSuspendareObKA.values())
			if (enumSt.codMotiv == codMotiv)
				return enumSt.numeMotiv;

		return "";
	}

	public static String getCodMotiv(String numeMotiv) {

		for (EnumMotiveSuspendareObKA enumSt : EnumMotiveSuspendareObKA.values())
			if (enumSt.numeMotiv == numeMotiv)
				return String.valueOf(enumSt.codMotiv);

		return "";
	}

	public static EnumMotiveSuspendareObKA getMotiv(int codMotiv) {
		for (EnumMotiveSuspendareObKA enumSt : EnumMotiveSuspendareObKA.values())
			if (enumSt.codMotiv == codMotiv)
				return enumSt;

		return null;
	}

}
