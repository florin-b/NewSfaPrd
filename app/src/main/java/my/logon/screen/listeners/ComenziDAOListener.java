package my.logon.screen.listeners;

import my.logon.screen.enums.EnumComenziDAO;

public interface ComenziDAOListener {
	public void operationComenziComplete(EnumComenziDAO methodName, Object result);
}
