package my.logon.screen.enums;

public enum EnumOperatiiMeniu {
	SAVE_PIN("savePinTableta"), DEBLOCHEAZA_MENIU("deblocheazaMeniu"), BLOCHEAZA_MENIU("blocheazaMeniu");

	private String numeOp;

	EnumOperatiiMeniu(String numeOp) {
		this.numeOp = numeOp;
	}

	public String getNumeOp() {
		return numeOp;
	}

}
