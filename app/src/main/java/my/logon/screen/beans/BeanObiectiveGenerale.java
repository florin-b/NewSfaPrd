package my.logon.screen.beans;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.adapters.AdapterObiectiveGeneral;
import my.logon.screen.enums.EnumCategorieObiectiv;
import my.logon.screen.enums.EnumMotiveSuspendareObKA;
import my.logon.screen.enums.EnumStadiuObiectivKA;
import my.logon.screen.enums.EnumStadiuSubantrep;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;


public class BeanObiectiveGenerale {

	private String codAgent = " ";
	private String numeAgent = " ";
	private String unitLog = " ";
	private String dataCreare = " ";
	private String nrObiectiv = " ";
	private String numeObiectiv = " ";
	private String codStadiuObiectiv = " ";
	private String codMotivSuspendare = " ";

	private BeanAdresaGenerica adresaObiectiv;
	private String numeBeneficiar = " ";
	private String numeAntreprenorGeneral = " ";
	private String codAntreprenorGeneral = " ";
	private String numeArhitect = " ";
	private BeanCategorieObiectiv categorieObiectiv;
	private String valoareObiectiv = " ";
	private String nrAutorizatieConstructie = " ";
	private String dataEmitereAutorizatie = " ";
	private String dataExpirareAutorizatie = " ";
	private BeanAdresaGenerica primariaEmitenta;
	private String valoareFundatie = " ";
	private List<BeanStadiuObiectiv> listStadii;
	private List<BeanObiectiveConstructori> listConstructori;
	private List<BeanStadiuObiectiv> stadiiDepart;
	private AdapterObiectiveGeneral adapterObiective;
	private String id = " ";
	private boolean inchis = false;
	private List<BeanUrmarireEveniment> listEvenimente;

	private static BeanObiectiveGenerale instance = new BeanObiectiveGenerale();

	private BeanObiectiveGenerale() {
		listStadii = new ArrayList<BeanStadiuObiectiv>();
		adresaObiectiv = new BeanAdresaGenerica();
		categorieObiectiv = new BeanCategorieObiectiv();
		primariaEmitenta = new BeanAdresaGenerica();
		listConstructori = new ArrayList<BeanObiectiveConstructori>();
		stadiiDepart = new ArrayList<BeanStadiuObiectiv>();
	}

	public static BeanObiectiveGenerale getInstance() {
		return instance;
	}

	public void updateObiectiv(int idObiectiv, Object valoareObiectiv, Object valoareObs) {

		switch (idObiectiv) {
		case 0:
			this.numeObiectiv = (String) valoareObiectiv;
			break;
		case 1:
			String strStadiu = String.valueOf(valoareObiectiv);
			this.codStadiuObiectiv = String.valueOf(EnumStadiuObiectivKA.valueOf(strStadiu).getCodStadiu());
			String strSuspendare = String.valueOf(valoareObs);
			this.setCodMotivSuspendare(String.valueOf(EnumMotiveSuspendareObKA.valueOf(strSuspendare).getCodMotiv()));
			break;
		case 2:
			this.dataCreare = (String) valoareObiectiv;
			break;
		case 3:
			addAdresa((BeanAdresaGenerica) valoareObiectiv);
			break;
		case 4:
			this.numeBeneficiar = (String) valoareObiectiv;
			break;
		case 5:
			this.numeAntreprenorGeneral = ((BeanClient) valoareObiectiv).getNumeClient();
			this.codAntreprenorGeneral = ((BeanClient) valoareObiectiv).getCodClient();
			break;
		case 6:
			this.numeArhitect = (String) valoareObiectiv;
			break;
		case 7:
			addCategorieObiectiv(valoareObiectiv);
			break;
		case 8:
			this.valoareObiectiv = (String) valoareObiectiv;
			break;
		case 9:
			this.nrAutorizatieConstructie = (String) valoareObiectiv;
			break;
		case 10:
			this.dataEmitereAutorizatie = (String) valoareObiectiv;
			break;
		case 11:
			this.dataExpirareAutorizatie = (String) valoareObiectiv;
			break;
		case 12:
			addPrimariaEmitenta((BeanAdresaGenerica) valoareObiectiv);
			break;
		case 13:
			this.valoareFundatie = (String) valoareObiectiv;
			break;
		case 14:
			addStadiu((EnumStadiuSubantrep) valoareObiectiv, "04");
			break;
		case 15:
			addStadiu((EnumStadiuSubantrep) valoareObiectiv, "09");
			break;
		case 16:
			addStadiu((EnumStadiuSubantrep) valoareObiectiv, "08");
			break;
		case 17:
			addStadiu((EnumStadiuSubantrep) valoareObiectiv, "05");
			break;
		case 18:
			addStadiu((EnumStadiuSubantrep) valoareObiectiv, "00");
			break;

		}

	}

	private void addStadiu(EnumStadiuSubantrep stadiuNou, String codDepart) {

		for (BeanStadiuObiectiv stadiu : listStadii) {
			if (stadiu.getCodDepart().equals(codDepart)) {
				listStadii.remove(stadiu);
				break;
			}

		}

		for (BeanStadiuObiectiv stadiu : stadiiDepart) {
			if (stadiu.getCodDepart().equals(codDepart)) {
				stadiiDepart.remove(stadiu);
				break;
			}

		}

		BeanStadiuObiectiv objStadiu = new BeanStadiuObiectiv();
		objStadiu.setNumeStadiu(stadiuNou.getNumeStadiu());
		objStadiu.setCodStadiu(stadiuNou.getCodStadiu());
		objStadiu.setCodDepart(codDepart);
		listStadii.add(objStadiu);

		stadiiDepart.add(objStadiu);

	}

	private void addCategorieObiectiv(Object valoareObiectiv) {
		categorieObiectiv.setCodCategorie(((BeanCategorieObiectiv) valoareObiectiv).getCodCategorie());
		categorieObiectiv.setNumeCategorie(((BeanCategorieObiectiv) valoareObiectiv).getNumeCategorie());

	}

	private void addAdresa(Object adresa) {
		this.adresaObiectiv.setCodJudet(((BeanAdresaGenerica) adresa).getCodJudet());
		this.adresaObiectiv.setNumeJudet(((BeanAdresaGenerica) adresa).getNumeJudet());
		this.adresaObiectiv.setOras(((BeanAdresaGenerica) adresa).getOras());
		this.adresaObiectiv.setStrada(((BeanAdresaGenerica) adresa).getStrada());

	}

	private void addPrimariaEmitenta(Object adresa) {
		this.primariaEmitenta.setCodJudet(((BeanAdresaGenerica) adresa).getCodJudet());
		this.primariaEmitenta.setNumeJudet(((BeanAdresaGenerica) adresa).getNumeJudet());
		this.primariaEmitenta.setOras(((BeanAdresaGenerica) adresa).getOras());

	}

	public String getValoareObiectiv(int idObiectiv) {

		switch (idObiectiv) {
		case 0:
			return this.numeObiectiv;
		case 1:
			return getStringStare();
		case 2:
			return getDataCreare();
		case 3:
			return getAdresa();
		case 4:
			return this.numeBeneficiar;
		case 5:
			return this.numeAntreprenorGeneral;
		case 6:
			return this.numeArhitect;
		case 7:
			return categorieObiectiv.getNumeCategorie();
		case 8:
			return UtilsFormatting.format2Decimals(Double.valueOf(getValoareObiectiv()), true);
		case 9:
			return this.nrAutorizatieConstructie;
		case 10:
			return this.dataEmitereAutorizatie;
		case 11:
			return this.dataExpirareAutorizatie;
		case 12:
			return getPrimariaEmitenta();
		case 13:
			return UtilsFormatting.format2Decimals(Double.valueOf(getValoareFundatie()), true);
		case 14:
			return getNumeStadiu("04");
		case 15:
			return getNumeStadiu("09");
		case 16:
			return getNumeStadiu("08");
		case 17:
			return getNumeStadiu("05");
		case 18:
			return getNumeStadiu("00");

		default:
			return "";

		}
	}

	private String getStringStare() {
		if (!getCodMotivSuspendare().equals("-1"))
			return EnumStadiuObiectivKA.getNumeStadiu(Integer.valueOf(getStadiuObiectiv())) + " / "
					+ EnumMotiveSuspendareObKA.getNumeMotiv(Integer.valueOf(getCodMotivSuspendare()));
		else
			return EnumStadiuObiectivKA.getNumeStadiu(Integer.valueOf(getStadiuObiectiv()));
	}

	private String getAdresa() {
		String adresa = "";
		if (adresaObiectiv.getNumeJudet() != null && !adresaObiectiv.getNumeJudet().equals("")) {
			adresa = adresaObiectiv.getNumeJudet() + " / " + adresaObiectiv.getOras();

			if (adresaObiectiv.getStrada().toString().trim().length() > 0)
				adresa += " / " + adresaObiectiv.getStrada();
		}

		return adresa;
	}

	public String getAdresaSer() {
		String adresa = " ";
		if (adresaObiectiv.getNumeJudet() != null && !adresaObiectiv.getNumeJudet().equals("")) {
			adresa = adresaObiectiv.getCodJudet() + "/" + adresaObiectiv.getOras();

			if (adresaObiectiv.getStrada().toString().trim().length() > 0)
				adresa += "/" + adresaObiectiv.getStrada();
		}

		return adresa;
	}

	private String getPrimariaEmitenta() {
		String primaria = "";
		if (primariaEmitenta.getNumeJudet() != null && !primariaEmitenta.getNumeJudet().equals(""))
			primaria = primariaEmitenta.getNumeJudet() + " / " + primariaEmitenta.getOras();

		return primaria;
	}

	public String getPrimariaEmitentaSer() {
		String primaria = " ";
		if (primariaEmitenta.getNumeJudet() != null && !primariaEmitenta.getNumeJudet().equals(""))
			primaria = primariaEmitenta.getCodJudet() + "/" + primariaEmitenta.getOras();

		return primaria;
	}

	public void setPrimariaEmitenta(String adresaPrimarie) {
		if (adresaPrimarie.contains("/")) {
			String[] tokenAdresa = adresaPrimarie.split("/");
			this.primariaEmitenta.setCodJudet(tokenAdresa[0]);
			this.primariaEmitenta.setNumeJudet(UtilsGeneral.getNumeJudet(tokenAdresa[0]));

			if (tokenAdresa.length > 1)
				this.primariaEmitenta.setOras(tokenAdresa[1]);

		}

	}

	private String getNumeStadiu(String codStadiu) {
		for (BeanStadiuObiectiv stadiu : stadiiDepart) {
			if (stadiu.getCodDepart().equals(codStadiu))
				return stadiu.getNumeStadiu();

		}

		return "";
	}

	public String getCodAgent() {
		return codAgent;
	}

	public void setCodAgent(String codAgent) {
		this.codAgent = codAgent;
	}

	public String getNumeAgent() {
		return numeAgent;
	}

	public void setNumeAgent(String numeAgent) {
		this.numeAgent = numeAgent;
	}

	public String getDataCreare() {

		if (dataCreare.contains(".") || dataCreare.trim().length() == 0)
			return dataCreare;
		else
			return dataCreare.substring(6, 8) + "." + dataCreare.substring(4, 6) + "." + dataCreare.substring(0, 4);
	}

	public void setDataCreare(String dataCreare) {
		this.dataCreare = dataCreare;
	}

	public String getNrObiectiv() {
		return nrObiectiv;
	}

	public void setNrObiectiv(String nrObiectiv) {
		this.nrObiectiv = nrObiectiv;
	}

	public String getNumeObiectiv() {
		return numeObiectiv;
	}

	public void setNumeObiectiv(String numeObiectiv) {
		this.numeObiectiv = numeObiectiv.trim();
	}

	public String getNumeBeneficiar() {
		return numeBeneficiar;
	}

	public void setNumeBeneficiar(String numeBeneficiar) {
		this.numeBeneficiar = numeBeneficiar.trim();
	}

	public String getNumeAntreprenorGeneral() {
		return numeAntreprenorGeneral;
	}

	public void setNumeAntreprenorGeneral(String numeAntreprenorGeneral) {
		this.numeAntreprenorGeneral = numeAntreprenorGeneral.trim();
	}

	public String getNumeArhitect() {
		return numeArhitect;
	}

	public void setNumeArhitect(String numeArhitect) {
		this.numeArhitect = numeArhitect.trim();
	}

	public String getValoareObiectiv() {

		try {
			Double.parseDouble(UtilsFormatting.removeComma(valoareObiectiv));
		} catch (Exception ex) {
			return "0";
		}

		return UtilsFormatting.removeComma(valoareObiectiv);
	}

	public void setValoareObiectiv(String valoareObiectiv) {
		this.valoareObiectiv = valoareObiectiv.trim();
	}

	public String getNrAutorizatieConstructie() {
		return nrAutorizatieConstructie;
	}

	public void setNrAutorizatieConstructie(String nrAutorizatieConstructie) {
		this.nrAutorizatieConstructie = nrAutorizatieConstructie.trim();
	}

	public String getDataEmitereAutorizatie() {
		return dataEmitereAutorizatie;
	}

	public void setDataEmitereAutorizatie(String dataEmitereAutorizatie) {
		this.dataEmitereAutorizatie = dataEmitereAutorizatie.trim();
	}

	public String getDataExpirareAutorizatie() {
		return dataExpirareAutorizatie;
	}

	public void setDataExpirareAutorizatie(String dataExpirareAutorizatie) {
		this.dataExpirareAutorizatie = dataExpirareAutorizatie.trim();
	}

	public String getCodAntreprenorGeneral() {
		return codAntreprenorGeneral;
	}

	public void setCodAntreprenorGeneral(String codAntreprenorGeneral) {
		this.codAntreprenorGeneral = codAntreprenorGeneral;
	}

	public List<BeanStadiuObiectiv> getListStadii() {
		return listStadii;
	}

	public String getValoareFundatie() {

		try {
			Double.parseDouble(UtilsFormatting.removeComma(valoareFundatie.trim()));
		} catch (Exception ex) {
			return "0";
		}

		return UtilsFormatting.removeComma(valoareFundatie.trim());
	}

	public BeanCategorieObiectiv getCategorieObiectiv() {
		if (categorieObiectiv.getCodCategorie() == null) {
			categorieObiectiv.setCodCategorie(" ");
			categorieObiectiv.setNumeCategorie(" ");
		}
		return categorieObiectiv;

	}

	public void setCategorieObiectiv(String codCategorie) {
		this.categorieObiectiv.setCodCategorie(codCategorie);
		this.categorieObiectiv.setNumeCategorie(EnumCategorieObiectiv.getNumeCategorie(codCategorie));

	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public BeanAdresaGenerica getAdresaObiectiv() {
		return adresaObiectiv;
	}

	public void setAdresaObiectiv(BeanAdresaGenerica adresaObiectiv) {
		this.adresaObiectiv = adresaObiectiv;
	}

	public void setAdresaObiectiv(String adresaObiectiv) {

		BeanAdresaGenerica adresa = new BeanAdresaGenerica();
		if (adresaObiectiv.contains("/")) {
			String[] tokenAdresa = adresaObiectiv.split("/");
			adresa.setCodJudet(tokenAdresa[0]);
			adresa.setNumeJudet(UtilsGeneral.getNumeJudet(tokenAdresa[0]));
			adresa.setOras(tokenAdresa[1]);
			if (tokenAdresa.length > 2)
				adresa.setStrada(tokenAdresa[2]);
			else
				adresa.setStrada("");

		}

		this.adresaObiectiv = adresa;
	}

	public void setCategorieObiectiv(BeanCategorieObiectiv categorieObiectiv) {
		this.categorieObiectiv = categorieObiectiv;
	}

	public void setPrimariaEmitenta(BeanAdresaGenerica primariaEmitenta) {
		this.primariaEmitenta = primariaEmitenta;
	}

	public void setValoareFundatie(String valoareFundatie) {
		this.valoareFundatie = valoareFundatie;
	}

	public void setListStadii(List<BeanStadiuObiectiv> listStadii) {
		this.listStadii = listStadii;
	}

	public List<BeanObiectiveConstructori> getListConstructori() {
		return listConstructori;
	}

	public void setListConstructori(List<BeanObiectiveConstructori> listConstructori) {
		this.listConstructori = listConstructori;
	}

	public void setAdapterObiective(AdapterObiectiveGeneral adapterObiective) {
		this.adapterObiective = adapterObiective;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isInchis() {
		return inchis;
	}

	public void setInchis(boolean inchis) {
		this.inchis = inchis;
	}

	public void clearInstanceData() {
		codAgent = "";
		id = " ";
		numeAgent = "";
		dataCreare = "";
		nrObiectiv = "";
		numeObiectiv = "";
		codStadiuObiectiv = "";
		setCodMotivSuspendare("");
		adresaObiectiv.clear();
		numeBeneficiar = "";
		numeAntreprenorGeneral = "";
		codAntreprenorGeneral = "";
		numeArhitect = "";
		categorieObiectiv.clear();
		valoareObiectiv = "";
		nrAutorizatieConstructie = "";
		dataEmitereAutorizatie = "";
		dataExpirareAutorizatie = "";
		primariaEmitenta.clear();
		valoareFundatie = "";
		listStadii.clear();
		listConstructori.clear();
		stadiiDepart.clear();
		inchis = false;
		if (this.adapterObiective != null)
			this.adapterObiective.notifyDataSetChanged();
	}

	public String getStadiuObiectiv() {
		if (codStadiuObiectiv.trim().length() == 0)
			return "-1";
		return codStadiuObiectiv;
	}

	public void setStadiuObiectiv(String stadiuObiectiv) {
		this.codStadiuObiectiv = stadiuObiectiv;
	}

	public String getCodMotivSuspendare() {
		if (codMotivSuspendare.trim().length() == 0 || codMotivSuspendare.equals("99"))
			return "-1";
		return codMotivSuspendare;
	}

	public void setCodMotivSuspendare(String codMotivSuspendare) {
		this.codMotivSuspendare = codMotivSuspendare;
	}

	public List<BeanStadiuObiectiv> getStadiiDepart() {
		return stadiiDepart;
	}

	public void setStadiiDepart(List<BeanStadiuObiectiv> stadiiDepart) {
		this.stadiiDepart = stadiiDepart;
	}

	public List<BeanUrmarireEveniment> getListEvenimente() {
		return listEvenimente;
	}

	public void setListEvenimente(List<BeanUrmarireEveniment> listEvenimente) {
		this.listEvenimente = listEvenimente;
	}

}
