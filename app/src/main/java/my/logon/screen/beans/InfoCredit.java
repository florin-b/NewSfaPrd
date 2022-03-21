package my.logon.screen.beans;

public class InfoCredit {

	private double limitaCredit;
	private double restCredit;
	private boolean isBlocat;
	private String motivBlocat;

	public double getLimitaCredit() {
		return limitaCredit;
	}

	public void setLimitaCredit(double limitaCredit) {
		this.limitaCredit = limitaCredit;
	}

	public double getRestCredit() {
		return restCredit;
	}

	public void setRestCredit(double restCredit) {
		this.restCredit = restCredit;
	}

	public boolean isBlocat() {
		return isBlocat;
	}

	public void setBlocat(boolean isBlocat) {
		this.isBlocat = isBlocat;
	}

	public String getMotivBlocat() {
		return motivBlocat;
	}

	public void setMotivBlocat(String motivBlocat) {
		this.motivBlocat = motivBlocat;
	}

}
