package my.logon.screen.listeners;

import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.enums.EnumPaleti;

public interface PaletiListener {
	void paletiStatus(EnumPaleti status, ArticolPalet palet);
}
