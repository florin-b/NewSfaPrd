package my.logon.screen.beans;

import java.util.List;

public class DepoziteUl {

	private List<String> listDepozite;

	private static DepoziteUl instance = null;

	private DepoziteUl() {

	}

	public static DepoziteUl getInstance() {
		if (instance == null)
			instance = new DepoziteUl();

		return instance;
	}

	public List<String> getListDepozite() {
		return listDepozite;
	}

	public void setListDepozite(List<String> listDepozite) {
		this.listDepozite = listDepozite;
	}

}
