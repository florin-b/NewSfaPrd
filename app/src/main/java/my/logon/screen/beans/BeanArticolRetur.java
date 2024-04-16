package my.logon.screen.beans;

import java.util.ArrayList;
import java.util.List;

public class BeanArticolRetur {
	private String nume;
	private String cod;
	private double cantitate;
	private String um;
	private double cantitateRetur;
	private double pretUnitPalet;
	private String motivRespingere;
	private boolean inlocuire;
	private List<PozaArticol> pozeArticol;
	private String nrDocument;

	public BeanArticolRetur() {

	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public double getCantitate() {
		return cantitate;
	}

	public void setCantitate(double cantitate) {
		this.cantitate = cantitate;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public double getCantitateRetur() {
		return cantitateRetur;
	}

	public void setCantitateRetur(double cantitateRetur) {
		this.cantitateRetur = cantitateRetur;
	}

	public double getPretUnitPalet() {
		return pretUnitPalet;
	}

	public void setPretUnitPalet(double pretUnitPalet) {
		this.pretUnitPalet = pretUnitPalet;
	}

	public String getMotivRespingere() {
		return motivRespingere;
	}

	public void setMotivRespingere(String motivRespingere) {
		this.motivRespingere = motivRespingere;
	}

	public boolean isInlocuire() {
		return inlocuire;
	}

	public void setInlocuire(boolean inlocuire) {
		this.inlocuire = inlocuire;
	}

	public List<PozaArticol> getPozeArticol() {
		if (pozeArticol == null)
			return new ArrayList<PozaArticol>();

		return pozeArticol;
	}

	public void setPozeArticol(List<PozaArticol> pozeArticol) {
		this.pozeArticol = pozeArticol;
	}

	public String getNrDocument() {
		return nrDocument;
	}

	public void setNrDocument(String nrDocument) {
		this.nrDocument = nrDocument;
	}

	@Override
	public String toString() {
		return "BeanArticolRetur [nume=" + nume + ", cod=" + cod + ", cantitate=" + cantitate + ", um=" + um + ", cantitateRetur=" + cantitateRetur + "]";
	}

}
