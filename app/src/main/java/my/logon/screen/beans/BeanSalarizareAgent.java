package my.logon.screen.beans;

import java.util.List;

public class BeanSalarizareAgent {
	private SalarizareDatePrincipale datePrincipale;
	private List<SalarizareDetaliiBaza> detaliiBaza;
	private SalarizareDetaliiTCF detaliiTCF;
	private SalarizareDetaliiCorectie detaliiCorectie;
	private List<SalarizareDetaliiInc08> detaliiInc08;
	private List<SalarizareDetaliiMalus> detaliiMalus;
	private List<BeanVanzariVS> detaliiVS;
	private List<BeanVanzariIncr> detaliiVanzariIncr;

	public SalarizareDatePrincipale getDatePrincipale() {
		return datePrincipale;
	}

	public void setDatePrincipale(SalarizareDatePrincipale datePrincipale) {
		this.datePrincipale = datePrincipale;
	}

	public List<SalarizareDetaliiBaza> getDetaliiBaza() {
		return detaliiBaza;
	}

	public void setDetaliiBaza(List<SalarizareDetaliiBaza> detaliiBaza) {
		this.detaliiBaza = detaliiBaza;
	}

	public SalarizareDetaliiTCF getDetaliiTCF() {
		return detaliiTCF;
	}

	public void setDetaliiTCF(SalarizareDetaliiTCF detaliiTCF) {
		this.detaliiTCF = detaliiTCF;
	}

	public SalarizareDetaliiCorectie getDetaliiCorectie() {
		return detaliiCorectie;
	}

	public void setDetaliiCorectie(SalarizareDetaliiCorectie detaliiCorectie) {
		this.detaliiCorectie = detaliiCorectie;
	}

	public List<SalarizareDetaliiInc08> getDetaliiInc08() {
		return detaliiInc08;
	}

	public void setDetaliiInc08(List<SalarizareDetaliiInc08> detaliiInc08) {
		this.detaliiInc08 = detaliiInc08;
	}

	public List<SalarizareDetaliiMalus> getDetaliiMalus() {
		return detaliiMalus;
	}

	public void setDetaliiMalus(List<SalarizareDetaliiMalus> detaliiMalus) {
		this.detaliiMalus = detaliiMalus;
	}

	public List<BeanVanzariVS> getDetaliiVS() {
		return detaliiVS;
	}

	public void setDetaliiVS(List<BeanVanzariVS> detaliiVS) {
		this.detaliiVS = detaliiVS;
	}

	public List<BeanVanzariIncr> getDetaliiVanzariIncr() {
		return detaliiVanzariIncr;
	}

	public void setDetaliiVanzariIncr(List<BeanVanzariIncr> detaliiVanzariIncr) {
		this.detaliiVanzariIncr = detaliiVanzariIncr;
	}
}
