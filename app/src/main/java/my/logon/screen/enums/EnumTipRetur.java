package my.logon.screen.enums;

public enum EnumTipRetur {
	PALETI("PAL"), COMANDA("CMD");

	private String tipRetur;

	EnumTipRetur(String tipRetur) {
		this.tipRetur = tipRetur;
	}

	
	public String getTipRetur() {
		return tipRetur;
	}
	
	

}
