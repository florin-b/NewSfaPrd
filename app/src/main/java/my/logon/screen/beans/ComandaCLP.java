package my.logon.screen.beans;

import java.util.List;

import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.DateLivrareCLP;

public class ComandaCLP {
	private DateLivrareCLP dateLivrare;
	private List<ArticolCLP> articole;

	public ComandaCLP() {

	}

	public DateLivrareCLP getDateLivrare() {
		return dateLivrare;
	}

	public void setDateLivrare(DateLivrareCLP dateLivrare) {
		this.dateLivrare = dateLivrare;
	}

	public List<ArticolCLP> getArticole() {
		return articole;
	}

	public void setArticole(List<ArticolCLP> articole) {
		this.articole = articole;
	}

}
