package my.logon.screen.listeners;

import java.util.List;

import my.logon.screen.beans.BeanArticolRetur;

public interface ListaArtReturListener {
	public void setListArtRetur(String nrDocument, List<BeanArticolRetur> listArticole, String codClient, String numeClient);
}
