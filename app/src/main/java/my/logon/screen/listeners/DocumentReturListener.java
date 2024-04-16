package my.logon.screen.listeners;

import my.logon.screen.beans.BeanDocumentRetur;
import my.logon.screen.enums.EnumTipOp;

public interface DocumentReturListener {
	public void documentSelected(String nrDocument);
	public void documentSelected(BeanDocumentRetur documentRetur);
	public void documentSelected(String nrDocument, EnumTipOp tipOp);

}
