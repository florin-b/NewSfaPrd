package my.logon.screen.enums;

public enum EnumOperatiiSite {
	GET_DEPOZ_UL("getDepoziteUL");

	private String numeOp;

	EnumOperatiiSite(String numeOp) {
		this.numeOp = numeOp;
	}

	public String getNumeOp() {
		return numeOp;
	}

}
