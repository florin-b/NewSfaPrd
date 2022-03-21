package my.logon.screen.listeners;

import my.logon.screen.enums.EnumOperatiiVenit;

public interface OperatiiVenitListener {
	public void operatiiVenitComplete(EnumOperatiiVenit numeComanda, Object result);
}
