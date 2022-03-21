package my.logon.screen.listeners;

import my.logon.screen.enums.EnumClienti;

public interface OperatiiClientListener {
	public void operationComplete(EnumClienti methodName, Object result);
}
