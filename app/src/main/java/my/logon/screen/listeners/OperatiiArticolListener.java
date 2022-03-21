package my.logon.screen.listeners;


import my.logon.screen.enums.EnumArticoleDAO;

public interface OperatiiArticolListener {
	public void operationComplete(EnumArticoleDAO methodName, Object result);
}
