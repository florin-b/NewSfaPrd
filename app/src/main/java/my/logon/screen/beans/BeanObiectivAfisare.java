package my.logon.screen.beans;

public class BeanObiectivAfisare {

	private String id;
	private String nume;
	private String data;
	private String beneficiar;
	private String codStatus;
	private String numeAgent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getBeneficiar() {
		return beneficiar;
	}

	public void setBeneficiar(String beneficiar) {
		this.beneficiar = beneficiar;
	}

	public String getCodStatus() {
		return codStatus;
	}

	public void setCodStatus(String codStatus) {
		this.codStatus = codStatus;
	}

	public String getNumeAgent() {
		return numeAgent;
	}

	public void setNumeAgent(String numeAgent) {
		this.numeAgent = numeAgent;
	}

	@Override
	public String toString() {
		return "BeanObiectivAfisare [id=" + id + ", nume=" + nume + ", data=" + data + ", beneficiar=" + beneficiar + ", codStatus=" + codStatus
				+ ", numeAgent=" + numeAgent + "]";
	}

}
