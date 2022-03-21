package my.logon.screen.model;

import java.util.HashMap;

import my.logon.screen.listeners.OperatiiVenitListener;
import my.logon.screen.beans.VenitAG;
import my.logon.screen.beans.VenituriNTCF;

public interface CalculVenit {

	public void getVenitTPR_TCF(HashMap<String, String> params);

	public void setOperatiiVenitListener(OperatiiVenitListener listener);

	public VenitAG getVenit(Object venitData);
	
	public void getVenitNTCF(HashMap<String, String> params);
	
	public VenituriNTCF deserializeDateNTCF(Object dateNTCF);
}
