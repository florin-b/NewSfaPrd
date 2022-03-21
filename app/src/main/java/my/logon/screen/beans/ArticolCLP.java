package my.logon.screen.beans;

public class ArticolCLP {

	private String cod;
	private String nume;
	private String cantitate;
	private String umBaza;
	private String depozit;
	private String status;
	private String depart;

	public ArticolCLP() {

	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCantitate() {
		return cantitate;
	}

	public void setCantitate(String cantitate) {
		this.cantitate = cantitate;
	}

	public String getUmBaza() {
		return umBaza;
	}

	public void setUmBaza(String umBaza) {
		this.umBaza = umBaza;
	}

	public String getDepozit() {
		return depozit;
	}

	public void setDepozit(String depozit) {
		this.depozit = depozit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	@Override
	public String toString() {
		return "ArticolCLP [cod=" + cod + ", nume=" + nume + ", cantitate=" + cantitate + ", umBaza=" + umBaza + ", depozit=" + depozit + ", status=" + status
				+ ", depart=" + depart + "]";
	}

}
