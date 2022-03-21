package my.logon.screen.beans;

public class BeanArticolStoc {

	private String cod;
	private String depozit;
	private String unitLog;
	private String depart;
	private double stoc;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDepozit() {
		return depozit;
	}

	public void setDepozit(String depozit) {
		this.depozit = depozit;
	}

	public double getStoc() {
		return stoc;
	}

	public void setStoc(double stoc) {
		this.stoc = stoc;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	@Override
	public String toString() {
		return "BeanArticolStoc [cod=" + cod + ", depozit=" + depozit + ", unitLog=" + unitLog + ", depart=" + depart + ", stoc=" + stoc + "]";
	}

}
