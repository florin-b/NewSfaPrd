package my.logon.screen.beans;

import java.util.List;

import my.logon.screen.model.ArticolComanda;

public class RezumatComanda {

	private List<ArticolComanda> listArticole;
	private String filialaLivrare;

	public List<ArticolComanda> getListArticole() {
		return listArticole;
	}

	public void setListArticole(List<ArticolComanda> listArticole) {
		this.listArticole = listArticole;
	}

	public String getFilialaLivrare() {
		return filialaLivrare;
	}

	public void setFilialaLivrare(String filialaLivrare) {
		this.filialaLivrare = filialaLivrare;
	}

}
