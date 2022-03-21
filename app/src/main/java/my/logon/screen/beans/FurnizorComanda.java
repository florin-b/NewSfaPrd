package my.logon.screen.beans;

public class FurnizorComanda {

	private String codFurnizorMarfa;
	private String numeFurnizorMarfa;
	private String codFurnizorProduse;
	private String numeFurnizorProduse;

	public String getCodFurnizorMarfa() {
		return codFurnizorMarfa;
	}

	public void setCodFurnizorMarfa(String codFurnizorMarfa) {
		this.codFurnizorMarfa = codFurnizorMarfa;
	}

	public String getNumeFurnizorMarfa() {
		return numeFurnizorMarfa;
	}

	public void setNumeFurnizorMarfa(String numeFurnizorMarfa) {
		this.numeFurnizorMarfa = numeFurnizorMarfa;
	}

	public String getCodFurnizorProduse() {
		return codFurnizorProduse;
	}

	public void setCodFurnizorProduse(String codFurnizorProduse) {
		this.codFurnizorProduse = codFurnizorProduse;
	}

	public String getNumeFurnizorProduse() {
		return numeFurnizorProduse;
	}

	public void setNumeFurnizorProduse(String numeFurnizorProduse) {
		this.numeFurnizorProduse = numeFurnizorProduse;
	}

	@Override
	public String toString() {
		return "FurnizorComanda [codFurnizorMarfa=" + codFurnizorMarfa + ", numeFurnizorMarfa=" + numeFurnizorMarfa + ", codFurnizorProduse="
				+ codFurnizorProduse + ", numeFurnizorProduse=" + numeFurnizorProduse + "]";
	}

	
	
}
