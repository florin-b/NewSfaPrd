package my.logon.screen.listeners;

import my.logon.screen.enums.EnumOperatiiMeniu;

public interface OperatiiMeniuListener {
	public void pinCompleted(EnumOperatiiMeniu numeOp, boolean isSuccess);
}
