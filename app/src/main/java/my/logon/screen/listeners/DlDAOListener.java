package my.logon.screen.listeners;

import my.logon.screen.enums.EnumDlDAO;

public interface DlDAOListener {
	public void operationDlComplete(EnumDlDAO methodName, Object result);
}
