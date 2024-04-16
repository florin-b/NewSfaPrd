package my.logon.screen.listeners;

import java.util.List;

import my.logon.screen.beans.BeanArticolRetur;
import my.logon.screen.screens.DocumenteReturPaleti;

public interface ListaArtReturListener {
	public void setListArtRetur(String nrDocument, List<BeanArticolRetur> listArticole, String codClient, String numeClient);
	public void setDocumentReturPaletiInstance(DocumenteReturPaleti instance);
}
