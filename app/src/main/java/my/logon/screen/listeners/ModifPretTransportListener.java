package my.logon.screen.listeners;

import my.logon.screen.adapters.AdapterRezumatComanda;
import my.logon.screen.beans.RezumatComanda;

public interface ModifPretTransportListener {
    void pretTransportModificat(RezumatComanda rezumat, String pretTransport, AdapterRezumatComanda.ViewHolder viewHolder);
}
