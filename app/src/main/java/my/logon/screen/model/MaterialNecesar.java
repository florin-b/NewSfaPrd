package my.logon.screen.model;

import java.util.Comparator;

public class MaterialNecesar {
	private String codArticol;
	private String numeArticol;
	private String codSintetic;
	private String numeSintetic;
	private String consum30;
	private String stoc;
	private String propunereNecesar;
	private String CA;
	private String interval1;
	private String interval2;
	private String interval3;

	public MaterialNecesar() {

	}

	public String getCodArticol() {
		return codArticol;
	}

	public void setCodArticol(String codArticol) {
		this.codArticol = codArticol;
	}

	public String getNumeArticol() {
		return numeArticol;
	}

	public void setNumeArticol(String numeArticol) {
		this.numeArticol = numeArticol;
	}

	public String getConsum30() {
		return consum30;
	}

	public void setConsum30(String consum30) {
		this.consum30 = consum30;
	}

	public String getStoc() {
		return stoc;
	}

	public void setStoc(String stoc) {
		this.stoc = stoc;
	}

	public String getPropunereNecesar() {
		return propunereNecesar;
	}

	public void setPropunereNecesar(String propunereNecesar) {
		this.propunereNecesar = propunereNecesar;
	}

	public String getCA() {
		return CA;
	}

	public void setCA(String cA) {
		CA = cA;
	}

	public String getInterval1() {
		return interval1;
	}

	public void setInterval1(String interval1) {
		this.interval1 = interval1;
	}

	public String getInterval2() {
		return interval2;
	}

	public void setInterval2(String interval2) {
		this.interval2 = interval2;
	}

	public String getInterval3() {
		return interval3;
	}

	public void setInterval3(String interval3) {
		this.interval3 = interval3;
	}

	public String getCodSintetic() {
		return codSintetic;
	}

	public void setCodSintetic(String codSintetic) {
		this.codSintetic = codSintetic;
	}

	public String getNumeSintetic() {
		return numeSintetic;
	}

	public void setNumeSintetic(String numeSintetic) {
		this.numeSintetic = numeSintetic;
	}

	public static class CompareNumeArt implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public CompareNumeArt(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod * material1.getNumeArticol().compareToIgnoreCase(material2.getNumeArticol());
		}

	}

	public static class CompareCodArt implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public CompareCodArt(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod * material1.getCodArticol().compareToIgnoreCase(material2.getCodArticol());
		}

	}

	public static class CompareNumeSintetic implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public CompareNumeSintetic(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod * material1.getNumeSintetic().compareToIgnoreCase(material2.getNumeSintetic());
		}

	}

	public static class CompareCodSintetic implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public CompareCodSintetic(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod * material1.getCodSintetic().compareToIgnoreCase(material2.getCodSintetic());
		}

	}

	public static class CompareCons30 implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public CompareCons30(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod * (int) (Double.valueOf(material1.getConsum30()) - Double.valueOf(material2.getConsum30()));
		}

	}

	public static class CompareStoc implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public CompareStoc(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod * (int) (Double.valueOf(material1.getStoc()) - Double.valueOf(material2.getStoc()));
		}

	}

	public static class ComparePropunere implements Comparator<MaterialNecesar> {

		private int mod = -1;

		public ComparePropunere(boolean asc) {
			if (asc)
				mod = 1;
		}

		public int compare(MaterialNecesar material1, MaterialNecesar material2) {
			return mod
					* (int) (Double.valueOf(material1.getPropunereNecesar()) - Double.valueOf(material2
							.getPropunereNecesar()));
		}

	}

}
