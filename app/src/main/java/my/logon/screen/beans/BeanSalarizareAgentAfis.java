package my.logon.screen.beans;

import my.logon.screen.beans.SalarizareDatePrincipale;

public class BeanSalarizareAgentAfis {

	private String codAgent;
	private String numeAgent;
	private SalarizareDatePrincipale datePrincipale;

	public SalarizareDatePrincipale getDatePrincipale() {
		return datePrincipale;
	}

	public void setDatePrincipale(SalarizareDatePrincipale datePrincipale) {
		this.datePrincipale = datePrincipale;
	}

	public String getCodAgent() {
		return codAgent;
	}

	public void setCodAgent(String codAgent) {
		this.codAgent = codAgent;
	}

	public String getNumeAgent() {
		return numeAgent;
	}

	public void setNumeAgent(String numeAgent) {
		this.numeAgent = numeAgent;
	}

}
