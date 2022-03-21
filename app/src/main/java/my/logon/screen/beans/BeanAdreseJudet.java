package my.logon.screen.beans;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.beans.BeanLocalitate;

public class BeanAdreseJudet {

	private List<BeanLocalitate> listLocalitati;
	private List<String> listStrazi;

	public List<BeanLocalitate> getListLocalitati() {
		return listLocalitati;
	}

	public void setListLocalitati(List<BeanLocalitate> listLocalitati) {
		this.listLocalitati = listLocalitati;
	}

	public List<String> getListStrazi() {
		return listStrazi;
	}

	public void setListStrazi(List<String> listStrazi) {
		this.listStrazi = listStrazi;
	}

	public List<String> getListStringLocalitati() {

		List<String> listStr = new ArrayList<String>();

		for (BeanLocalitate loc : listLocalitati) {
			listStr.add(loc.getLocalitate());
		}

		return listStr;
	}

}
