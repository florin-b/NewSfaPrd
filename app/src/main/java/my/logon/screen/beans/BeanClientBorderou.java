package my.logon.screen.beans;

import my.logon.screen.utils.UtilsFormatting;

public class BeanClientBorderou {

	private String codClient;
	private int pozitie;
	private String stareLivrare;

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getStareLivrare() {

		if (stareLivrare.trim().length() > 0)
			return "Livrat la " + UtilsFormatting.formatTime(stareLivrare);
		else
			return stareLivrare;
	}

	public void setStareLivrare(String stareLivrare) {
		this.stareLivrare = stareLivrare;
	}

	public int getPozitie() {
		return pozitie;
	}

	public void setPozitie(int pozitie) {
		this.pozitie = pozitie;
	}

	@Override
	public String toString() {
		return "BeanClientBorderou [codClient=" + codClient + ", pozitie=" + pozitie + ", stareLivrare=" + stareLivrare + "]";
	}

}
