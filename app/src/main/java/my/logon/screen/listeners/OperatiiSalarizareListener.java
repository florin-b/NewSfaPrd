package my.logon.screen.listeners;

import my.logon.screen.enums.EnumOperatiiSalarizare;

public interface OperatiiSalarizareListener {
	public void operatiiSalarizareComplete(EnumOperatiiSalarizare numeComanda, Object result);
	public void detaliiAgentSelected(String codAgent, String numeAgent);

}
