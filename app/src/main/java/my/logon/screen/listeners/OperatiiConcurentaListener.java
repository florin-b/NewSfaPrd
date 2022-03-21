package my.logon.screen.listeners;

import my.logon.screen.enums.EnumOperatiiConcurenta;

public interface OperatiiConcurentaListener {
	public void operationComplete(EnumOperatiiConcurenta numeComanda, Object result);

}
