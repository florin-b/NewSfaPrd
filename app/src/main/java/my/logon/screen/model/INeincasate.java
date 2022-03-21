package my.logon.screen.model;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.ArticolNeincasatListener;
import my.logon.screen.beans.BeanArticolVanzari;

public interface INeincasate {
	void getArtDocNeincasat(HashMap<String, String> params);
	 void setArtNeincasatListener(ArticolNeincasatListener listener);
	 public List<BeanArticolVanzari> getArticole(Object result);
	
}
