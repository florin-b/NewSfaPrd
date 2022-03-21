package my.logon.screen.listeners;

import my.logon.screen.enums.EnumOperatiiPierdereVanz;

public interface OperatiiPierdVanzListener {
	public void operationPierduteComplete(EnumOperatiiPierdereVanz methodName, Object result);
}
