package my.logon.screen.listeners;

import my.logon.screen.enums.EnumClpDAO;

public interface ClpDAOListener {
	public void operationClpComplete(EnumClpDAO methodName, Object result);
}
