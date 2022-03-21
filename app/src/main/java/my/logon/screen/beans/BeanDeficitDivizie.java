package my.logon.screen.beans;

public class BeanDeficitDivizie implements Comparable<BeanDeficitDivizie> {
	private String codDivizie;
	private double valDeficit;
	private boolean isAcoperit;

	public String getCodDivizie() {
		return codDivizie;
	}

	public void setCodDivizie(String codDivizie) {
		this.codDivizie = codDivizie;
	}

	public double getValDeficit() {
		return valDeficit;
	}

	public void setValDeficit(double valDeficit) {
		this.valDeficit = valDeficit;
	}

	public boolean isAcoperit() {
		return isAcoperit;
	}

	public void setAcoperit(boolean isAcoperit) {
		this.isAcoperit = isAcoperit;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codDivizie == null) ? 0 : codDivizie.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanDeficitDivizie other = (BeanDeficitDivizie) obj;
		if (codDivizie == null) {
			if (other.codDivizie != null)
				return false;
		} else if (!codDivizie.equals(other.codDivizie))
			return false;
		return true;
	}

	public int compareTo(BeanDeficitDivizie deficitDivizie) {
		int returnValue = 0;

		if (Math.abs(this.valDeficit) == Math.abs(deficitDivizie.valDeficit))
			returnValue = 0;
		if (Math.abs(this.valDeficit) < Math.abs(deficitDivizie.valDeficit))
			returnValue = -1;
		if (Math.abs(this.valDeficit) > Math.abs(deficitDivizie.valDeficit))
			returnValue = 1;

		return returnValue;
	}

	public String toString() {
		return "BeanDeficitDivizie [codDivizie=" + codDivizie + ", valDeficit=" + valDeficit + ", isAcoperit="
				+ isAcoperit + "]";
	}

}
