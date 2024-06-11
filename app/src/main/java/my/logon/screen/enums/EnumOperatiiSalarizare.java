package my.logon.screen.enums;

public enum EnumOperatiiSalarizare {

	GET_SALARIZARE_AGENT("getSalarizareAgent"), GET_SALARIZARE_DEPART("getSalarizareDepartament"), GET_SALARIZARE_SD("getSalarizareSd"), GET_SALARIZARE_KA(
			"getSalarizareKA"), GET_SALARIZARE_DEPART_KA("getSalarizareDepartamentKA"), GET_SALARIZARE_SDKA("getSalarizareSDKA"),
			GET_SALARIZARE_CVA("getSalarizareCVA"), GET_SALARIZARE_CVIP("getSalarizareCVIP"),
			GET_SALARIZARE_DEPART_CVA("getSalarizareDepartCVA"), GET_SALARIZARE_DEPART_CVIP("getSalarizareDepartCVIP");

	private String numeComanda;

	EnumOperatiiSalarizare(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
