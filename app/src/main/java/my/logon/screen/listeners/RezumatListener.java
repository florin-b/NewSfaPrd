package my.logon.screen.listeners;

import java.util.List;

import my.logon.screen.model.ArticolComanda;

public interface RezumatListener {
	void comandaEliminata(List<String> listArticole, String filialaLivrare);
	void adaugaArticol(ArticolComanda articolComanda);
	void eliminaArticol(ArticolComanda articolComanda);
}
