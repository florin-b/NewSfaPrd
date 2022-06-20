package my.logon.screen.beans;

public class CategorieMathaus implements Comparable<CategorieMathaus> {
	private String cod;
	private String nume;
	private String codHybris;
	private String codParinte;

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

	public String getCodHybris() {
		return codHybris;
	}

	public void setCodHybris(String codHybris) {
		this.codHybris = codHybris;
	}

	public String getCodParinte() {
		return codParinte;
	}

	public void setCodParinte(String codParinte) {
		this.codParinte = codParinte;
	}

	@Override
	public int compareTo(CategorieMathaus another) {

		int returnValue = 0;

		int comparaCod = Integer.parseInt(another.getCod());

		if (Integer.parseInt(this.cod) == comparaCod)
			returnValue = 0;
		if (Integer.parseInt(this.cod) < comparaCod)
			returnValue = -1;
		if (Integer.parseInt(this.cod) > comparaCod)
			returnValue = 1;

		return returnValue;
	}

}
