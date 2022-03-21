package my.logon.screen.beans;

import java.util.ArrayList;
import java.util.List;

public class VanzariAgentiParam {

	private String tipArticol;
	private String agent;
	private String filiala;
	private String departament;
	private String startInterval;
	private String stopInterval;
	private String tipComanda;
	private List<String> articole;
	private List<String> clienti;

	public VanzariAgentiParam() {

	}

	public String getTipArticol() {
		return tipArticol;
	}

	public void setTipArticol(String tipArticol) {
		this.tipArticol = tipArticol;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getStartInterval() {
		return startInterval;
	}

	public void setStartInterval(String startInterval) {
		this.startInterval = startInterval;
	}

	public String getStopInterval() {
		return stopInterval;
	}

	public void setStopInterval(String stopInterval) {
		this.stopInterval = stopInterval;
	}

	public String getTipComanda() {
		return tipComanda;
	}

	public void setTipComanda(String tipComanda) {
		this.tipComanda = tipComanda;
	}

	public List<String> getArticole() {
		return articole;
	}

	public void setArticole(ArrayList<String> articole) {
		this.articole = articole;
	}

	public List<String> getClienti() {
		return clienti;
	}

	public void setClienti(ArrayList<String> clienti) {
		this.clienti = clienti;
	}

}
