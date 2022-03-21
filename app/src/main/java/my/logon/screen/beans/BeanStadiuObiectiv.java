package my.logon.screen.beans;

public class BeanStadiuObiectiv implements Comparable<BeanStadiuObiectiv> {

	private String codDepart;
	private int codStadiu;
	private String numeStadiu;

	public String getCodDepart() {
		return codDepart;
	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
	}

	public int getCodStadiu() {
		return codStadiu;
	}

	public void setCodStadiu(int codStadiu) {
		this.codStadiu = codStadiu;
	}

	public String getNumeStadiu() {
		return numeStadiu;
	}

	public void setNumeStadiu(String numeStadiu) {
		this.numeStadiu = numeStadiu;
	}

	public int compareTo(BeanStadiuObiectiv another) {
		return this.numeStadiu.compareTo(another.numeStadiu) > 0 ? -1 : 0;
	}

	@Override
	public String toString() {
		return "BeanStadiuObiectiv [codDepart=" + codDepart + ", codStadiu=" + codStadiu + ", numeStadiu=" + numeStadiu + "]";
	}

}
