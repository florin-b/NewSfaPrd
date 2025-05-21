package my.logon.screen.listeners;

import java.util.List;

import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.TaxaTransport;

public interface TaxeMasiniListener {
    void tipMasinaFilialaSelected(LivrareMathaus livrareMathaus, CostDescarcare costDescarcare, List<TaxaTransport> taxeTransport);
}
