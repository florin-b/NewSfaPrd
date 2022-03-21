package my.logon.screen.enums;

public enum EnumConcurenta {
	CONCURENT_0("Concurent", "");

	private String concName;
	private String concCode;

	private EnumConcurenta(String name, String code) {
		this.concName = name;
		this.concCode = code;
	}

	public String getCode() {
		return concCode;
	}

	public String getName() {
		return concName;
	}
}
