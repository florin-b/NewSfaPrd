package my.logon.screen.beans;

public class BeanObiectiveConstructori {
	private String codClient;
	private String numeClient;
	private String codDepart;
	private String stare;

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCodDepart() {
		return codDepart;
	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
	}

	public String getStare() {
		if (stare == null)
			return "";
		return stare;
	}

	public void setStare(String stare) {
		this.stare = stare;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codClient == null) ? 0 : codClient.hashCode());
		result = prime * result + ((codDepart == null) ? 0 : codDepart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanObiectiveConstructori other = (BeanObiectiveConstructori) obj;
		if (codClient == null) {
			if (other.codClient != null)
				return false;
		} else if (!codClient.equals(other.codClient))
			return false;
		if (codDepart == null) {
			if (other.codDepart != null)
				return false;
		} else if (!codDepart.equals(other.codDepart))
			return false;
		return true;
	}

	public String toString() {
		return "BeanObiectiveConstructori [codClient=" + codClient + ", numeClient=" + numeClient + ", codDepart="
				+ codDepart + ", stare=" + stare + "]";
	}

}
