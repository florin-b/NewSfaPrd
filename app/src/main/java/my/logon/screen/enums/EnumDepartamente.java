package my.logon.screen.enums;

public enum EnumDepartamente {

	D01("Lemnoase"), D02("Feronerie"), D03("Parchet"), D04("Materiale grele"), D05("Electrice"), D06("Gips"), D07(
			"Chimice"), D08("Instalatii"), D09("Hidroizolatii"), D11("Magazin");

	private String nume;

	private EnumDepartamente(String nume) {
		this.nume = nume;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Override
	public String toString() {
		return nume;
	}

}
