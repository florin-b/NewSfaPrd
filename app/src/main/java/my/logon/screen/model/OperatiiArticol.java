package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.AntetCmdMathaus;
import my.logon.screen.beans.ArticolCant;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.BeanArticolCautare;
import my.logon.screen.beans.BeanArticolSimulat;
import my.logon.screen.beans.BeanArticolStoc;
import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.beans.BeanGreutateArticol;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DatePoligonLivrare;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.listeners.OperatiiArticolListener;

public interface OperatiiArticol {
	public void getPret(HashMap<String, String> params);

	public void getPretGed(HashMap<String, String> params);

	public void getPretGedJson(HashMap<String, String> params);

	public void getStocDepozit(HashMap<String, String> params);

	public void getStocDisponibil(HashMap<String, String> params);

	public void getFactorConversie(HashMap<String, String> params);

	public void setListener(OperatiiArticolListener listener);

	public void getArticoleDistributie(HashMap<String, String> params);

	public ArrayList<ArticolDB> deserializeArticoleVanzare(String serializedListArticole);

	public PretArticolGed deserializePretGed(Object result);

	public void getArticoleComplementare(List<ArticolComanda> listaArticole);

	public void getArticoleFurnizor(HashMap<String, String> params);

	public void getSinteticeDistributie(HashMap<String, String> params);

	public void getNivel1Distributie(HashMap<String, String> params);

	public String serializeParamPretGed(BeanParametruPretGed param);

	public String serializeArticolePretTransport(List<ArticolComanda> listArticole);

	public BeanGreutateArticol deserializeGreutateArticol(Object result);

	public Object getDepartBV90(String codArticol);

	public void getStocArticole(HashMap<String, String> params);

	public String serializeListArtStoc(List<BeanArticolStoc> listArticole);

	public String serializeListArtSim(List<BeanArticolSimulat> listArticole);

	public List<BeanArticolStoc> derializeListArtStoc(String listArticole);

	public void getCodBare(HashMap<String, String> params);

	public void getArticoleStatistic(HashMap<String, String> params);

	public void getStocCustodie(HashMap<String, String> params);

	public void getArticoleCustodie(HashMap<String, String> params);

	public void getStocMathaus(HashMap<String, String> params);

	public void getInfoPretMathaus(HashMap<String, String> params);

	public void getArticoleCant(HashMap<String, String> params);

	public ArrayList<ArticolCant> deserializeArticoleCant(String listArticole);

	public ComandaMathaus deserializeStocMathaus(String result);
	
	public LivrareMathaus deserializeLivrareMathaus(String result);

	public String serializeComandaMathaus(ComandaMathaus comandaMathaus);
	
	public String serializeAntetCmdMathaus(AntetCmdMathaus antetcmdMathaus);

	public void getCabluri05(HashMap<String, String> params);
	
	public ArrayList<BeanCablu05> deserializeCabluri05(String listArticole);
	
	public String serializeCabluri05(List<BeanCablu05> listCabluri);
	
	public String serializeCostTransportMathaus(List<CostTransportMathaus> costTransport);

	public void getArticoleACZC(HashMap<String, String> params);

	public ArrayList<BeanArticolCautare> deserializeArtRecom(String serListArticole) ;

	public String serializeDatePoligon(DatePoligonLivrare datePoligonLivrare);

	public void getStocDisponibilTCLI(HashMap<String, String> params);

	public void getStocSap(HashMap<String, String> params);

}


