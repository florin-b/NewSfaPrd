package my.logon.screen.listeners;

import java.util.List;

import my.logon.screen.beans.BeanArticolRetur;
import my.logon.screen.enums.EnumTipOp;

public interface ListaDocReturListener {
	public void setListDocRetur(String numeClient, Object listDocumente);
	public void setListArtDocRetur(String nrDocument, List<BeanArticolRetur> listaArticole, EnumTipOp tipOp, String codClient, String numeClient, ListaArtReturListener artReturListener);
}
