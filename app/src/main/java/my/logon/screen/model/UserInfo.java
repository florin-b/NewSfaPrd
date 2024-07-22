/**
 * @author florinb
 *
 */
package my.logon.screen.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInfo {

	private String nume = "";
	private String filiala = "";
	private String cod = "";
	private String numeDepart = "";
	private String codDepart = "";
	private String unitLog = "";
	private String initUnitLog = "";
	private String tipAcces = "";
	private String parentScreen = "";
	private String filialeDV = "";
	private boolean altaFiliala = false;
	private String tipUser = "";
	private String departExtra;
	private String tipUserSap = "";
	private List<String> extraFiliale;

	private double comisionCV = 0;
	private String coefCorectie = "";
	private String userSite = "";
	private boolean userWood = false;
	private boolean filHome = false;
	private String ftpIP = "";
	private String initDivizie = "";
	private String codSuperUser = "";
	private boolean isMeniuBlocat = false;
	private String codPinMeniu = "";

	private double maxNumerarPFizica = 0;
	private double maxNumerarPJuridica = 0;

	private String appVer;

	private static UserInfo instance = new UserInfo();

	private UserInfo() {
	}

	public static UserInfo getInstance() {
		return instance;
	}

	public String getNume() {
		return this.nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNumeDepart() {
		return numeDepart;
	}

	public void setNumeDepart(String numeDepart) {
		this.numeDepart = numeDepart;
	}

	public String getCodDepart() {
		return codDepart;
	}

	public void setCodDepart(String codDepart) {
		this.codDepart = codDepart;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public String getTipAcces() {
		return tipAcces;
	}

	public void setTipAcces(String tipAcces) {
		this.tipAcces = tipAcces;
	}

	public String getParentScreen() {
		return parentScreen;
	}

	public void setParentScreen(String parentScreen) {
		this.parentScreen = parentScreen;
	}

	public String getFilialeDV() {
		return filialeDV;
	}

	public void setFilialeDV(String filialeDV) {
		this.filialeDV = filialeDV;
	}

	public boolean isAltaFiliala() {
		return altaFiliala;
	}

	public void setAltaFiliala(boolean altaFiliala) {
		this.altaFiliala = altaFiliala;
	}

	public double getComisionCV() {
		return comisionCV;
	}

	public void setComisionCV(double comisionCV) {
		this.comisionCV = comisionCV;
	}

	public String getCoefCorectie() {
		return coefCorectie;
	}

	public void setCoefCorectie(String coefCorectie) {
		this.coefCorectie = coefCorectie;
	}

	public String getUserSite() {
		return userSite;
	}

	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}

	public String getTipUser() {
		return tipUser;
	}

	public void setTipUser(String tipUser) {
		this.tipUser = tipUser;
	}

	public String getDepartExtra() {
		return departExtra;
	}

	public void setDepartExtra(String departExtra) {
		this.departExtra = departExtra;
	}

	public static void setInstance(UserInfo instance) {
		UserInfo.instance = instance;
	}

	public String getTipUserSap() {
		return tipUserSap;
	}

	public void setTipUserSap(String tipUserSap) {
		this.tipUserSap = tipUserSap;
	}

	public List<String> getExtraFiliale() {
		return extraFiliale;
	}

	public String getInitUnitLog() {
		return initUnitLog;
	}

	public void setInitUnitLog(String initUnitLog) {
		this.initUnitLog = initUnitLog;
	}

	public boolean isUserWood() {
		return userWood;
	}

	public void setUserWood(boolean userWood) {
		this.userWood = userWood;
	}

	public void setExtraFiliale(String extraFiliale) {

		Scanner scanner = new Scanner(extraFiliale.trim());
		scanner.useDelimiter(",");

		List<String> listExtraFiliale = new ArrayList<String>();

		while (scanner.hasNext()) {
			listExtraFiliale.add(scanner.next());
		}

		this.extraFiliale = listExtraFiliale;
	}

	public void setExtraFiliale(List<String> extraFiliale) {
		this.extraFiliale = extraFiliale;
	}

	public boolean isFilHome() {
		return filHome;
	}

	public void setFilHome(boolean filHome) {
		this.filHome = filHome;
	}

	public String getFtpIP() {
		return ftpIP;
	}

	public void setFtpIP(String ftpIP) {
		this.ftpIP = ftpIP;
	}

	public String getInitDivizie() {
		return initDivizie;
	}

	public void setInitDivizie(String initDivizie) {
		this.initDivizie = initDivizie;
	}

	public String getCodSuperUser() {
		return codSuperUser;
	}

	public void setCodSuperUser(String codSuperUser) {
		this.codSuperUser = codSuperUser;
	}

	public boolean getIsMeniuBlocat() {
		return isMeniuBlocat;
	}

	public void setIsMeniuBlocat(boolean isMeniuBlocat) {
		this.isMeniuBlocat = isMeniuBlocat;
	}

	public String getCodPinMeniu() {
		return codPinMeniu;
	}

	public void setCodPinMeniu(String codPinMeniu) {
		this.codPinMeniu = codPinMeniu;
	}

	public double getMaxNumerarPFizica() {
		return maxNumerarPFizica;
	}

	public void setMaxNumerarPFizica(double maxNumerarPFizica) {
		this.maxNumerarPFizica = maxNumerarPFizica;
	}

	public double getMaxNumerarPJuridica() {
		return maxNumerarPJuridica;
	}

	public void setMaxNumerarPJuridica(double maxNumerarPJuridica) {
		this.maxNumerarPJuridica = maxNumerarPJuridica;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	@Override
	public String toString() {
		return "UserInfo [nume=" + nume + ", filiala=" + filiala + ", cod=" + cod + ", numeDepart=" + numeDepart + ", codDepart=" + codDepart + ", unitLog="
				+ unitLog + ", initUnitLog=" + initUnitLog + ", tipAcces=" + tipAcces + ", parentScreen=" + parentScreen + ", filialeDV=" + filialeDV
				+ ", altaFiliala=" + altaFiliala + ", tipUser=" + tipUser + ", departExtra=" + departExtra + ", tipUserSap=" + tipUserSap + ", extraFiliale="
				+ extraFiliale + ", comisionCV=" + comisionCV + ", coefCorectie=" + coefCorectie + ", userSite=" + userSite + ", userWood=" + userWood
				+ ", filHome=" + filHome + "]";
	}

}
