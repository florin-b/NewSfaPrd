package my.logon.screen.model;

import my.logon.screen.enums.EnumTipComanda;

public interface ClientReturListener {
	public void clientSelected(String codClient, String numeClient, String interval, EnumTipComanda tipComanda);

}
