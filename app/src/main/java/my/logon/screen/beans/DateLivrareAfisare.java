package my.logon.screen.beans;

import com.google.android.gms.maps.model.LatLng;

public class DateLivrareAfisare {

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
    private String macara;
    private String numeClient;
    private String cnpClient;
    private String idObiectiv = " ";
    private boolean isAdresaObiectiv;
    private LatLng coordonateAdresa;
    private String tonaj;
    private boolean isClientRaft;
    private String codMeserias = "";
    private boolean isFactPaletSeparat = false;
    private FurnizorComanda furnizorComanda;
    private boolean isCamionDescoperit;
    private String diviziiClient;
    private String programLivrare = "0";
    private String livrareSambata = "";
    private String blocScara = "";
    private String codFilialaCLP = "";
    private Delegat delegat;
    private double marjaT1 = 0;
    private double procentT1 = 0;
    private double marjaT1Tot = 0;
    private double procentT1Tot = 0;
    private double mCantCmd = 0;
    private double mCant30 = 0;
    private double marjaBrutaPalVal = 0;
    private double marjaBrutaCantVal = 0;
    private double marjaBrutaPalProc = 0;
    private double marjaBrutaCantProc = 0;
    private String refClient;
    private boolean isClientBlocat;
    private double limitaCredit = 0;
    private String nrCmdClp;

    private double marjaBruta = 0;
    private double procMarjaBruta = 0;

    private double marjaBrutaTot = 0;
    private double procMarjaBrutaTot = 0;

    private String tipPersAgent;
    private String filialaPlata;
    private boolean isComandaCustodie;
    private String codPostal;
    private String codJ;
    private String aprobariNecesare;
    private String aprobariPrimite;
    private String prelucrare;

    public DateLivrareAfisare() {

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

    public String getTipPersClient() {
        return tipPersClient;
    }

    public void setTipPersClient(String tipPersClient) {
        this.tipPersClient = tipPersClient;
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

    public String getAdresaD() {
        return adresaD;
    }

    public void setAdresaD(String adresaD) {
        this.adresaD = adresaD;
    }

    public String getOrasD() {
        return orasD;
    }

    public void setOrasD(String orasD) {
        this.orasD = orasD;
    }

    public String getCodJudetD() {
        return codJudetD;
    }

    public void setCodJudetD(String codJudetD) {
        this.codJudetD = codJudetD;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getCnpClient() {
        return cnpClient;
    }

    public void setCnpClient(String cnpClient) {
        this.cnpClient = cnpClient;
    }

    public String getMacara() {
        return macara;
    }

    public void setMacara(String macara) {
        this.macara = macara;
    }

    public String toString() {
        return "DateLivrareAfisare [codJudet=" + codJudet + ", numeJudet=" + numeJudet + ", Oras=" + Oras + ", Strada=" + Strada + ", persContact="
                + persContact + ", nrTel=" + nrTel + ", redSeparat=" + redSeparat + ", Cantar=" + Cantar + ", tipPlata=" + tipPlata + ", Transport="
                + Transport + ", dateLivrare=" + dateLivrare + ", termenPlata=" + termenPlata + ", obsLivrare=" + obsLivrare + ", dataLivrare=" + dataLivrare
                + ", adrLivrNoua=" + adrLivrNoua + ", tipDocInsotitor=" + tipDocInsotitor + ", obsPlata=" + obsPlata + ", addrNumber=" + addrNumber
                + ", valoareIncasare=" + valoareIncasare + ", isValIncModif=" + isValIncModif + ", mail=" + mail + ", totalComanda=" + totalComanda
                + ", unitLog=" + unitLog + ", codAgent=" + codAgent + ", factRed=" + factRed + ", tipPersClient=" + tipPersClient + "]";
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

    public boolean isClientRaft() {
        return isClientRaft;
    }

    public void setClientRaft(boolean isClientRaft) {
        this.isClientRaft = isClientRaft;
    }

    public String getDataLivrare() {
        return dataLivrare;
    }

    public void setDataLivrare(String dataLivrare) {
        this.dataLivrare = dataLivrare;
    }

    public String getCodMeserias() {
        return codMeserias;
    }

    public void setCodMeserias(String codMeserias) {
        this.codMeserias = codMeserias;
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

    public double getMarjaT1() {
        return marjaT1;
    }

    public void setMarjaT1(double marjaT1) {
        this.marjaT1 = marjaT1;
    }

    public double getProcentT1() {
        return procentT1;
    }

    public void setProcentT1(double procentT1) {
        this.procentT1 = procentT1;
    }

    public double getmCantCmd() {
        return mCantCmd;
    }

    public void setmCantCmd(double mCantCmd) {
        this.mCantCmd = mCantCmd;
    }

    public double getmCant30() {
        return mCant30;
    }

    public void setmCant30(double mCant30) {
        this.mCant30 = mCant30;
    }

    public double getMarjaBrutaPalVal() {
        return marjaBrutaPalVal;
    }

    public void setMarjaBrutaPalVal(double marjaBrutaPalVal) {
        this.marjaBrutaPalVal = marjaBrutaPalVal;
    }

    public double getMarjaBrutaCantVal() {
        return marjaBrutaCantVal;
    }

    public void setMarjaBrutaCantVal(double marjaBrutaCantVal) {
        this.marjaBrutaCantVal = marjaBrutaCantVal;
    }

    public double getMarjaBrutaPalProc() {
        return marjaBrutaPalProc;
    }

    public void setMarjaBrutaPalProc(double marjaBrutaPalProc) {
        this.marjaBrutaPalProc = marjaBrutaPalProc;
    }

    public double getMarjaBrutaCantProc() {
        return marjaBrutaCantProc;
    }

    public void setMarjaBrutaCantProc(double marjaBrutaCantProc) {
        this.marjaBrutaCantProc = marjaBrutaCantProc;
    }

    public String getRefClient() {
        return refClient;
    }

    public void setRefClient(String refClient) {
        this.refClient = refClient;
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

    public String getNrCmdClp() {
        return nrCmdClp;
    }

    public void setNrCmdClp(String nrCmdClp) {
        this.nrCmdClp = nrCmdClp;
    }

    public double getMarjaBruta() {
        return marjaBruta;
    }

    public void setMarjaBruta(double marjaBruta) {
        this.marjaBruta = marjaBruta;
    }

    public double getProcMarjaBruta() {
        return procMarjaBruta;
    }

    public void setProcMarjaBruta(double procMarjaBruta) {
        this.procMarjaBruta = procMarjaBruta;
    }

    public String getTipPersAgent() {
        return tipPersAgent;
    }

    public void setTipPersAgent(String tipPersAgent) {
        this.tipPersAgent = tipPersAgent;
    }

    public String getFilialaPlata() {
        return filialaPlata;
    }

    public void setFilialaPlata(String filialaPlata) {
        this.filialaPlata = filialaPlata;
    }

    public double getMarjaT1Tot() {
        return marjaT1Tot;
    }

    public void setMarjaT1Tot(double marjaT1Tot) {
        this.marjaT1Tot = marjaT1Tot;
    }

    public double getProcentT1Tot() {
        return procentT1Tot;
    }

    public void setProcentT1Tot(double procentT1Tot) {
        this.procentT1Tot = procentT1Tot;
    }

    public double getMarjaBrutaTot() {
        return marjaBrutaTot;
    }

    public void setMarjaBrutaTot(double marjaBrutaTot) {
        this.marjaBrutaTot = marjaBrutaTot;
    }

    public double getProcMarjaBrutaTot() {
        return procMarjaBrutaTot;
    }

    public void setProcMarjaBrutaTot(double procMarjaBrutaTot) {
        this.procMarjaBrutaTot = procMarjaBrutaTot;
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

    public String getCodJ() {
        return codJ;
    }

    public void setCodJ(String codJ) {
        this.codJ = codJ;
    }

    public String getAprobariNecesare() {
        return aprobariNecesare;
    }

    public void setAprobariNecesare(String aprobariNecesare) {
        this.aprobariNecesare = aprobariNecesare;
    }

    public String getAprobariPrimite() {
        return aprobariPrimite;
    }

    public void setAprobariPrimite(String aprobariPrimite) {
        this.aprobariPrimite = aprobariPrimite;
    }

    public String getPrelucrare() {
        return prelucrare;
    }

    public void setPrelucrare(String prelucrare) {
        this.prelucrare = prelucrare;
    }
}
