package my.logon.screen.enums;

public enum EnumOperatiiFurnizor {

	GET_FURNIZORI_MARFA("cautaFurnizorAndroid"), GET_FURNIZORI_PRODUSE("cautaFurnizorProduseAndroid");

	String numeComanda;

	EnumOperatiiFurnizor(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
