package my.logon.screen.model;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.BeanDateLivrareClient;
import my.logon.screen.enums.EnumLocalitate;

public interface OperatiiAdresa {
	public void getLocalitatiJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate);

	public void getAdreseJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate);

	public BeanAdreseJudet deserializeListAdrese(Object values);

	public List<String> deserializeListLocalitati(Object values);

	public void setOperatiiAdresaListener(OperatiiAdresaListener listener);

	public void isAdresaValida(HashMap<String, String> params, EnumLocalitate tipLocalitate);

	public void getDateLivrare(HashMap<String, String> params);

	public void getAdreseLivrareClient(HashMap<String, String> params);

	public void getLocalitatiLivrareRapida();

	public void getDateLivrareClient(HashMap<String, String> params);

	public BeanDateLivrareClient deserializeDateLivrareClient(String result);
	
	public void getFilialaLivrareMathaus(HashMap<String, String> params);

	public void getAdresaFiliala(HashMap<String, String> params);

}
