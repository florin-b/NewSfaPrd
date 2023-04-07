package my.logon.screen.beans;

import java.util.List;

public class ComandaExtraMathaus {
	private List<StocMathaus> listArticole;
	private boolean succes;

	public List<StocMathaus> getListArticole() {
		return listArticole;
	}

	public void setListArticole(List<StocMathaus> listArticole) {
		this.listArticole = listArticole;
	}

	public boolean isSucces() {
		return succes;
	}

	public void setSucces(boolean succes) {
		this.succes = succes;
	}

}
