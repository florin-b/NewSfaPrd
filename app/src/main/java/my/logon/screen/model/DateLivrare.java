/**
 * @author florinb
 *
 */
package my.logon.screen.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import enums.EnumZona;
import my.logon.screen.beans.BeanFilialaLivrare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateLivrareAfisare;
import my.logon.screen.beans.DatePoligonLivrare;
import my.logon.screen.beans.Delegat;
import my.logon.screen.beans.FurnizorComanda;
import my.logon.screen.beans.ObiectivConsilier;
import my.logon.screen.beans.TaxaComanda;
import my.logon.screen.beans.TranspComenzi;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.enums.TipCmdGed;

public class DateLivrare {

	private static DateLivrare instance = new DateLivrare();

	private DateLivrare() {
	}

	public static DateLivrare getInstance() {
		return instance;
	}

	private String codJudet = "";
	private String numeJudet = "";
	private String Oras = "";
	private String Strada = "";
	private String persContact = "";
	private String nrTel = "";
	private String redSeparat = "R";
	private String Cantar = "";
	private String tipPlata = "";
	private String Transport = "";
	private String dateLivrare = "";
	private String termenPlata = "";
	private String obsLivrare = "";
	private String dataLivrare = "";
	private boolean adrLivrNoua = false;
	private String tipDocInsotitor = "1"; // 1 = factura, 2 = aviz
	private String obsPlata = " ";
	private String addrNumber = " ";
	private String valoareIncasare = "0";
	private boolean isValIncModif = false;
	private String mail = " ";
	private String totalComanda;
	private String unitLog;
	private String codAgent;
	private String factRed;
	private String tipPersClient;
	private double valTransport;
	private double valTransportSAP;
	private String adresaD;
	private String orasD;
	private String codJudetD;
	private boolean masinaMacara;
	private boolean altaAdresa;
	private String numeClient;
	private String cnpClient;
	private String idObiectiv = " ";
	private boolean isAdresaObiectiv;
	private LatLng coordonateAdresa;
	private String tonaj;
	private String prelucrare;
	private boolean isClientRaft;
	private boolean isFacturaCmd;
	private EnumZona zonaBucuresti;
	private String codMeserias = "";
	private boolean isClientFurnizor;

	private boolean isFactPaletSeparat = false;
	private FurnizorComanda furnizorComanda;
	private TipCmdDistrib tipComandaDistrib = TipCmdDistrib.COMANDA_VANZARE;
	private boolean isCamionDescoperit;
	private String diviziiClient;
	private String programLivrare = "0";
	private String livrareSambata = "";
	private String blocScara = "";
	private String codFilialaCLP = "";
	private Delegat delegat;
	private ObiectivConsilier obiectivConsilier;
	private String refClient;
	private TipCmdGed tipComandaGed = TipCmdGed.COMANDA_VANZARE;
	private String filialeExtraMathaus = "";
	private List<CostTransportMathaus> costTransportMathaus;
	private boolean isClientBlocat;
	private double limitaCredit;
	private String prelucrareLemn;
	private String tipPersAgent;
	private BeanFilialaLivrare filialaLivrareTCLI;
	private List<TranspComenzi> transpComenzi;
	private String filialaPlata;
	private DatePoligonLivrare datePoligonLivrare;

	private DateLivrareAfisare dateLivrareAfisare;
	private String tipMasina = "";
	private String codFilialaFasonate;
	private boolean isComandaCustodie;
	private String codPostal;
	private List<TaxaComanda> taxeComanda;
	private String transpInit = "";
	private List<ArticolComanda> comandaInit;

	public DateLivrareAfisare getDateLivrareAfisare() {
		return dateLivrareAfisare;
	}

	public void setDateLivrareAfisare(DateLivrareAfisare dateLivrareAfisare) {
		this.codJudet = dateLivrareAfisare.getCodJudet();
		this.numeJudet = dateLivrareAfisare.getNumeJudet();
		this.Oras = dateLivrareAfisare.getOras();
		this.Strada = dateLivrareAfisare.getDateLivrare();
		this.persContact = dateLivrareAfisare.getPersContact();
		this.nrTel = dateLivrareAfisare.getNrTel();
		this.redSeparat = dateLivrareAfisare.getRedSeparat();
		this.Cantar = dateLivrareAfisare.getCantar();
		this.tipPlata = dateLivrareAfisare.getTipPlata();
		this.Transport = dateLivrareAfisare.getTransport();
		this.dateLivrare = dateLivrareAfisare.getDateLivrare();
		this.termenPlata = dateLivrareAfisare.getTermenPlata();
		this.obsLivrare = dateLivrareAfisare.getObsLivrare();
		this.dataLivrare = dateLivrareAfisare.getDataLivrare();
		this.adrLivrNoua = dateLivrareAfisare.isAdrLivrNoua();
		this.tipDocInsotitor = dateLivrareAfisare.getTipDocInsotitor();
		this.obsPlata = dateLivrareAfisare.getObsPlata();
		this.addrNumber = dateLivrareAfisare.getAddrNumber();
		this.valoareIncasare = dateLivrareAfisare.getValoareIncasare();
		this.isValIncModif = dateLivrareAfisare.isValIncModif();
		this.mail = dateLivrareAfisare.getMail();
		this.totalComanda = dateLivrareAfisare.getTotalComanda();
		this.unitLog = dateLivrareAfisare.getUnitLog();
		this.codAgent = dateLivrareAfisare.getCodAgent();
		this.factRed = dateLivrareAfisare.getFactRed();
		this.redSeparat = dateLivrareAfisare.getFactRed();
		this.tipPersClient = dateLivrareAfisare.getTipPersClient();
		this.valTransport = dateLivrareAfisare.getValTransport();
		this.valTransportSAP = dateLivrareAfisare.getValTransportSAP();
		this.adresaD = dateLivrareAfisare.getAdresaD();
		this.orasD = dateLivrareAfisare.getOrasD();
		this.codJudetD = dateLivrareAfisare.getCodJudetD();
		this.masinaMacara = dateLivrareAfisare.getMacara().equalsIgnoreCase("X") ? true : false;

		this.numeClient = dateLivrareAfisare.getNumeClient();
		this.cnpClient = dateLivrareAfisare.getCnpClient();
		this.idObiectiv = dateLivrareAfisare.getIdObiectiv();
		this.isAdresaObiectiv = dateLivrareAfisare.isAdresaObiectiv();
		this.coordonateAdresa = dateLivrareAfisare.getCoordonateAdresa();
		this.tonaj = dateLivrareAfisare.getTonaj();
		this.isClientRaft = dateLivrareAfisare.isClientRaft();
		this.codMeserias = dateLivrareAfisare.getCodMeserias();
		this.isFactPaletSeparat = dateLivrareAfisare.isFactPaletSeparat();
		this.furnizorComanda = dateLivrareAfisare.getFurnizorComanda();
		this.isCamionDescoperit = dateLivrareAfisare.isCamionDescoperit();
		this.diviziiClient = dateLivrareAfisare.getDiviziiClient();
		this.programLivrare = dateLivrareAfisare.getProgramLivrare();
		this.livrareSambata = dateLivrareAfisare.getLivrareSambata();
		this.blocScara = dateLivrareAfisare.getBlocScara();
		this.codFilialaCLP = dateLivrareAfisare.getCodFilialaCLP();
		this.delegat = dateLivrareAfisare.getDelegat();
		this.refClient = dateLivrareAfisare.getRefClient();
		this.limitaCredit = dateLivrareAfisare.getLimitaCredit();
		this.tipPersAgent = dateLivrareAfisare.getTipPersAgent();
		this.filialaPlata = dateLivrareAfisare.getFilialaPlata();

		this.isComandaCustodie = dateLivrareAfisare.isComandaCustodie();
		this.codPostal = dateLivrareAfisare.getCodPostal();
		this.transpInit = dateLivrareAfisare.getTransport();


	}

	public boolean isClientRaft() {
		return isClientRaft;
	}

	public void setClientRaft(boolean isClientRaft) {
		this.isClientRaft = isClientRaft;
	}

	public String getCodJudet() {
		return codJudet;
	}

	public void setCodJudet(String codJudet) {
		this.codJudet = codJudet;
	}

	public String getNumeJudet() {
		return numeJudet;
	}

	public void setNumeJudet(String numeJudet) {
		this.numeJudet = numeJudet;
	}

	public String getOras() {
		return Oras;
	}

	public void setOras(String oras) {
		Oras = oras;
	}

	public String getStrada() {
		return Strada;
	}

	public void setStrada(String strada) {
		Strada = strada;
	}

	public String getPersContact() {
		return persContact;
	}

	public void setPersContact(String persContact) {
		this.persContact = persContact;
	}

	public String getNrTel() {
		return nrTel;
	}

	public void setNrTel(String nrTel) {
		this.nrTel = nrTel;
	}

	public String getRedSeparat() {
		return redSeparat;
	}

	public void setRedSeparat(String redSeparat) {
		this.redSeparat = redSeparat;
	}

	public String getCantar() {
		return Cantar;
	}

	public void setCantar(String cantar) {
		Cantar = cantar;
	}

	public String getTipPlata() {
		return tipPlata;
	}

	public void setTipPlata(String tipPlata) {
		this.tipPlata = tipPlata;
	}

	public String getTransport() {
		return Transport;
	}

	public void setTransport(String transport) {
		Transport = transport;
	}

	public String getDateLivrare() {
		return dateLivrare;
	}

	public void setDateLivrare(String dateLivrare) {
		this.dateLivrare = dateLivrare;
	}

	public String getTermenPlata() {
		return termenPlata;
	}

	public void setTermenPlata(String termenPlata) {
		this.termenPlata = termenPlata;
	}

	public String getObsLivrare() {
		return obsLivrare;
	}

	public void setObsLivrare(String obsLivrare) {
		this.obsLivrare = obsLivrare;
	}

	public boolean isAdrLivrNoua() {
		return adrLivrNoua;
	}

	public void setAdrLivrNoua(boolean adrLivrNoua) {
		this.adrLivrNoua = adrLivrNoua;
	}

	public String getTipDocInsotitor() {
		return tipDocInsotitor;
	}

	public void setTipDocInsotitor(String tipDocInsotitor) {
		this.tipDocInsotitor = tipDocInsotitor;
	}

	public String getObsPlata() {
		return obsPlata;
	}

	public void setObsPlata(String obsPlata) {
		this.obsPlata = obsPlata;
	}

	public String getAddrNumber() {
		return addrNumber;
	}

	public void setAddrNumber(String addrNumber) {
		this.addrNumber = addrNumber;
	}

	public String getValoareIncasare() {
		return valoareIncasare;
	}

	public void setValoareIncasare(String valoareIncasare) {
		this.valoareIncasare = valoareIncasare;
	}

	public boolean isValIncModif() {
		return isValIncModif;
	}

	public void setValIncModif(boolean isValIncModif) {
		this.isValIncModif = isValIncModif;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTotalComanda() {
		if (totalComanda == null)
			return "0";
		return totalComanda;
	}

	public void setTotalComanda(String totalComanda) {
		this.totalComanda = totalComanda;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public String getCodAgent() {
		return codAgent;
	}

	public void setCodAgent(String codAgent) {
		this.codAgent = codAgent;
	}

	public String getFactRed() {
		return factRed;
	}

	public void setFactRed(String factRed) {
		this.factRed = factRed;
	}

	public double getValTransport() {
		return valTransport;
	}

	public void setValTransport(double valTransport) {
		this.valTransport = valTransport;
	}

	public double getValTransportSAP() {
		return valTransportSAP;
	}

	public void setValTransportSAP(double valTransportSAP) {
		this.valTransportSAP = valTransportSAP;
	}

	public String getTipPersClient() {
		return tipPersClient;
	}

	public void setTipPersClient(String tipPersClient) {
		this.tipPersClient = tipPersClient;
	}

	public String getAdresaD() {
		if (adresaD == null)
			return " ";
		return adresaD;
	}

	public void setAdresaD(String adresaD) {
		this.adresaD = adresaD;
	}

	public String getOrasD() {
		if (orasD == null)
			return " ";
		return orasD;
	}

	public void setOrasD(String orasD) {
		this.orasD = orasD;
	}

	public String getCodJudetD() {

		if (codJudetD == null)
			return " ";
		return codJudetD;
	}

	public void setCodJudetD(String codJudetD) {
		this.codJudetD = codJudetD;
	}

	public boolean isMasinaMacara() {
		return masinaMacara;
	}

	public void setMasinaMacara(boolean masinaMacara) {
		this.masinaMacara = masinaMacara;
	}

	public boolean isAltaAdresa() {
		return altaAdresa;
	}

	public void setAltaAdresa(boolean altaAdresa) {
		this.altaAdresa = altaAdresa;
	}

	public String getNumeClient() {
		if (numeClient == null)
			return " ";
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getCnpClient() {
		if (cnpClient == null)
			return " ";
		return cnpClient;
	}

	public void setCnpClient(String cnpClient) {
		this.cnpClient = cnpClient;
	}

	public String getIdObiectiv() {
		return idObiectiv;
	}

	public void setIdObiectiv(String idObiectiv) {
		this.idObiectiv = idObiectiv;
	}

	public boolean isAdresaObiectiv() {
		return isAdresaObiectiv;
	}

	public void setAdresaObiectiv(boolean isAdresaObiectiv) {
		this.isAdresaObiectiv = isAdresaObiectiv;
	}

	public LatLng getCoordonateAdresa() {
		return coordonateAdresa;
	}

	public void setCoordonateAdresa(LatLng coordonateAdresa) {
		this.coordonateAdresa = coordonateAdresa;
	}

	public String getTonaj() {
		return tonaj;
	}

	public void setTonaj(String tonaj) {
		this.tonaj = tonaj;
	}

	public String getPrelucrare() {
		return prelucrare;
	}

	public void setPrelucrare(String prelucrare) {
		this.prelucrare = prelucrare;
	}

	public boolean isFacturaCmd() {
		return isFacturaCmd;
	}

	public void setFacturaCmd(boolean isFacturaCmd) {
		this.isFacturaCmd = isFacturaCmd;
	}

	public String getDataLivrare() {
		return dataLivrare;
	}

	public void setDataLivrare(String dataLivrare) {
		this.dataLivrare = dataLivrare;
	}

	public EnumZona getZonaBucuresti() {
		return zonaBucuresti;
	}

	public void setZonaBucuresti(EnumZona zonaBucuresti) {
		this.zonaBucuresti = zonaBucuresti;
	}

	public String getCodMeserias() {
		return codMeserias;
	}

	public void setCodMeserias(String codMeserias) {
		this.codMeserias = codMeserias;
	}

	public boolean isClientFurnizor() {
		return isClientFurnizor;
	}

	public void setClientFurnizor(boolean isClientFurnizor) {
		this.isClientFurnizor = isClientFurnizor;
	}

	public boolean isFactPaletSeparat() {
		return isFactPaletSeparat;
	}

	public void setFactPaletSeparat(boolean isFactPaletSeparat) {
		this.isFactPaletSeparat = isFactPaletSeparat;
	}

	public FurnizorComanda getFurnizorComanda() {
		return furnizorComanda;
	}

	public void setFurnizorComanda(FurnizorComanda furnizorComanda) {
		this.furnizorComanda = furnizorComanda;
	}

	public TipCmdDistrib getTipComandaDistrib() {
		return tipComandaDistrib;
	}

	public void setTipComandaDistrib(TipCmdDistrib tipComandaDistrib) {
		this.tipComandaDistrib = tipComandaDistrib;
	}

	public boolean isCamionDescoperit() {
		return isCamionDescoperit;
	}

	public void setCamionDescoperit(boolean isCamionDescoperit) {
		this.isCamionDescoperit = isCamionDescoperit;
	}

	public String getDiviziiClient() {
		return diviziiClient;
	}

	public void setDiviziiClient(String diviziiClient) {
		this.diviziiClient = diviziiClient;
	}

	public String getProgramLivrare() {
		return programLivrare;
	}

	public void setProgramLivrare(String programLivrare) {
		this.programLivrare = programLivrare;
	}

	public String getLivrareSambata() {
		return livrareSambata;
	}

	public void setLivrareSambata(String livrareSambata) {
		this.livrareSambata = livrareSambata;
	}

	public String getBlocScara() {
		return blocScara;
	}

	public void setBlocScara(String blocScara) {
		this.blocScara = blocScara;
	}

	public String getCodFilialaCLP() {
		return codFilialaCLP;
	}

	public void setCodFilialaCLP(String codFilialaCLP) {
		this.codFilialaCLP = codFilialaCLP;
	}

	public Delegat getDelegat() {
		return delegat;
	}

	public void setDelegat(Delegat delegat) {
		this.delegat = delegat;
	}

	public ObiectivConsilier getObiectivConsilier() {
		return obiectivConsilier;
	}

	public void setObiectivConsilier(ObiectivConsilier obiectivConsilier) {
		this.obiectivConsilier = obiectivConsilier;
	}

	public String getRefClient() {
		return refClient;
	}

	public void setRefClient(String refClient) {
		this.refClient = refClient;
	}

	public TipCmdGed getTipComandaGed() {
		return tipComandaGed;
	}

	public void setTipComandaGed(TipCmdGed tipComandaGed) {
		this.tipComandaGed = tipComandaGed;
	}

	public String getFilialeExtraMathaus() {
		return filialeExtraMathaus;
	}

	public void setFilialeExtraMathaus(String filialeExtraMathaus) {
		this.filialeExtraMathaus = filialeExtraMathaus;
	}

	public List<CostTransportMathaus> getCostTransportMathaus() {
		return costTransportMathaus;
	}

	public void setCostTransportMathaus(List<CostTransportMathaus> costTransportMathaus) {
		this.costTransportMathaus = costTransportMathaus;
	}

	public boolean isClientBlocat() {
		return isClientBlocat;
	}

	public void setClientBlocat(boolean isClientBlocat) {
		this.isClientBlocat = isClientBlocat;
	}

	public double getLimitaCredit() {
		return limitaCredit;
	}

	public void setLimitaCredit(double limitaCredit) {
		this.limitaCredit = limitaCredit;
	}

	public String getPrelucrareLemn() {
		return prelucrareLemn;
	}

	public void setPrelucrareLemn(String prelucrareLemn) {
		this.prelucrareLemn = prelucrareLemn;
	}

	public String getTipPersAgent() {
		return tipPersAgent;
	}

	public void setTipPersAgent(String tipPersAgent) {
		this.tipPersAgent = tipPersAgent;
	}

	public BeanFilialaLivrare getFilialaLivrareTCLI() {
		return filialaLivrareTCLI;
	}

	public void setFilialaLivrareTCLI(BeanFilialaLivrare filialaLivrareTCLI) {
		this.filialaLivrareTCLI = filialaLivrareTCLI;
	}

	public List<TranspComenzi> getTranspComenzi() {
		return transpComenzi;
	}

	public void setTranspComenzi(List<TranspComenzi> transpComenzi) {
		this.transpComenzi = transpComenzi;
	}

	public String getFilialaPlata() {
		return filialaPlata;
	}

	public void setFilialaPlata(String filialaPlata) {
		this.filialaPlata = filialaPlata;
	}

	public DatePoligonLivrare getDatePoligonLivrare() {
		return datePoligonLivrare;
	}

	public void setDatePoligonLivrare(DatePoligonLivrare datePoligonLivrare) {
		this.datePoligonLivrare = datePoligonLivrare;
	}

	public String getTipMasina() {
		return tipMasina;
	}

	public void setTipMasina(String tipMasina) {
		this.tipMasina = tipMasina;
	}

	public String getCodFilialaFasonate() {

		if (codFilialaFasonate == null)
			return "";
		return codFilialaFasonate;
	}

	public void setCodFilialaFasonate(String codFilialaFasonate) {
		this.codFilialaFasonate = codFilialaFasonate;
	}

	public boolean isComandaCustodie() {
		return isComandaCustodie;
	}

	public void setComandaCustodie(boolean comandaCustodie) {
		isComandaCustodie = comandaCustodie;
	}

	public String getCodPostal() {
		return codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}

	public List<TaxaComanda> getTaxeComanda() {
		return taxeComanda;
	}

	public void setTaxeComanda(List<TaxaComanda> taxeComanda) {
		this.taxeComanda = taxeComanda;
	}

	public String getTranspInit() {
		return transpInit;
	}

	public void setTranspInit(String transpInit) {
		this.transpInit = transpInit;
	}

	public List<ArticolComanda> getComandaInit() {
		return comandaInit;
	}

	public void setComandaInit(List<ArticolComanda> comandaInit) {
		this.comandaInit = comandaInit;
	}

	public void resetAll() {
		codJudet = "";
		numeJudet = "";
		Oras = "";
		Strada = "";
		persContact = "";
		nrTel = "";
		redSeparat = "R";
		Cantar = "";
		tipPlata = "";
		Transport = "";
		dateLivrare = "";
		termenPlata = "";
		obsLivrare = "";
		adrLivrNoua = false;
		tipDocInsotitor = "1";
		obsPlata = " ";
		addrNumber = " ";
		valoareIncasare = "0";
		isValIncModif = false;
		mail = " ";
		tipPersClient = "";
		valTransport = 0;
		valTransportSAP = 0;
		masinaMacara = false;
		altaAdresa = false;
		adresaD = " ";
		orasD = " ";
		codJudetD = " ";
		idObiectiv = " ";
		isAdresaObiectiv = false;
		coordonateAdresa = null;
		tonaj = "";
		prelucrare = "";
		isClientRaft = false;
		isFacturaCmd = false;
		dataLivrare = "";
		zonaBucuresti = null;
		codMeserias = "";
		isClientFurnizor = false;
		isFactPaletSeparat = false;
		furnizorComanda = null;
		tipComandaDistrib = TipCmdDistrib.COMANDA_VANZARE;
		isCamionDescoperit = false;
		diviziiClient = "";
		programLivrare = "0";
		livrareSambata = "";
		blocScara = "";
		codFilialaCLP = "";
		delegat = new Delegat();
		obiectivConsilier = null;
		refClient = "";
		tipComandaGed = TipCmdGed.COMANDA_VANZARE;
		filialeExtraMathaus = "";
		costTransportMathaus = null;
		isClientBlocat = false;
		limitaCredit = 0;
		prelucrareLemn = "";
		tipPersAgent = "";
		filialaLivrareTCLI = null;
		transpComenzi = null;
		filialaPlata = "";
		datePoligonLivrare = null;
		tipMasina = "";
		codFilialaFasonate = "";
		isComandaCustodie = false;
		codPostal = "";
		taxeComanda = null;
		transpInit = "";
		comandaInit = null;
	}

}
