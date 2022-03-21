package my.logon.screen.enums;

public enum EnumTipInterval {

	NIMIC("Selectati un interval", -1), ASTAZI("Astazi", 0), ULT_7_ZILE("Ultimele 7 zile", 1), ULT_30_ZILE("Ultimele 30 de zile", 2), PESTE_30_ZILE(
			"Mai mult de 30 de zile", 3);

	private String nume;
	private int cod;

	EnumTipInterval(String nume, int cod) {
		this.nume = nume;
		this.cod = cod;
	}

	public int getCod() {
		return cod;
	}

	@Override
	public String toString() {
		return nume;
	}

}
