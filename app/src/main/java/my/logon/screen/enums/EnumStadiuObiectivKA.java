package my.logon.screen.enums;

public enum EnumStadiuObiectivKA {

	IN_CONSTRUCTIE("In constructie", 0), NEINCEPUT("Neinceput", 1), SUSPENDAT("Suspendat", 2);

	private String numeStadiu;
	private int codStadiu;

	EnumStadiuObiectivKA(String numeStadiu, int codStadiu) {
		this.numeStadiu = numeStadiu;
		this.codStadiu = codStadiu;
	}

	public String getNumeStadiu() {
		return numeStadiu;
	}

	public int getCodStadiu() {
		return codStadiu;
	}

	public static String getNumeStadiu(int codStadiu) {

		for (EnumStadiuObiectivKA enumSt : EnumStadiuObiectivKA.values())
			if (enumSt.codStadiu == codStadiu)
				return enumSt.numeStadiu;

		return "";
	}
	
	
	public static String getCodStadiu(String numeStadiu) {

		for (EnumStadiuObiectivKA enumSt : EnumStadiuObiectivKA.values())
			if (enumSt.numeStadiu == numeStadiu)
				return String.valueOf(enumSt.codStadiu);

		return "";
	}	

	public static EnumStadiuObiectivKA getStadiu(int codStadiu) {
		for (EnumStadiuObiectivKA enumSt : EnumStadiuObiectivKA.values())
			if (enumSt.codStadiu == codStadiu)
				return enumSt;

		return null;
	}

}
