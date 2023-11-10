package my.logon.screen.beans;

import java.io.Serializable;

public class ArticolMathaus extends ArticolDB implements Serializable {
	private String cod;
	private String nume;
	private String adresaImg;
	private String adresaImgMare;
	private String descriere;
	private String catMathaus;
	private String pretUnitar;
	private boolean isLocal;
	private boolean isArticolSite;
	private String tip1;
	private String tip2;
	private String planificator;

	public ArticolMathaus(){

	}

	public ArticolMathaus(ArticolMathaus articolMathaus){
		this.cod = articolMathaus.getCod();
		this.nume = articolMathaus.getNume();
		this.adresaImg = articolMathaus.getAdresaImg();
		this.adresaImgMare = articolMathaus.getAdresaImgMare();
		this.descriere = articolMathaus.getDescriere();
		this.catMathaus = articolMathaus.getCatMathaus();
		this.pretUnitar = articolMathaus.getPretUnitar();
		this.isLocal = articolMathaus.isLocal();
		this.isArticolSite = articolMathaus.isArticolSite;
		this.tip1 = articolMathaus.getTip1();
		this.tip2 = articolMathaus.getTip2();
		this.planificator = articolMathaus.getPlanificator();
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getAdresaImg() {
		return adresaImg;
	}

	public void setAdresaImg(String adresaImg) {
		this.adresaImg = adresaImg;
	}

	public String getAdresaImgMare() {
		return adresaImgMare;
	}

	public void setAdresaImgMare(String adresaImgMare) {
		this.adresaImgMare = adresaImgMare;
	}

	public String getDescriere() {
		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public String getCatMathaus() {
		return catMathaus;
	}

	public void setCatMathaus(String catMathaus) {
		this.catMathaus = catMathaus;
	}

	public String getPretUnitar() {
		return pretUnitar;
	}

	public void setPretUnitar(String pretUnitar) {
		this.pretUnitar = pretUnitar;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public boolean isArticolSite() {
		return isArticolSite;
	}

	public void setArticolSite(boolean isArticolSite) {
		this.isArticolSite = isArticolSite;
	}

	public String getTip1() {
		return tip1;
	}

	public void setTip1(String tip1) {
		this.tip1 = tip1;
	}

	public String getTip2() {
		return tip2;
	}

	public void setTip2(String tip2) {
		this.tip2 = tip2;
	}

	public String getPlanificator() {
		return planificator;
	}

	public void setPlanificator(String planificator) {
		this.planificator = planificator;
	}
}
