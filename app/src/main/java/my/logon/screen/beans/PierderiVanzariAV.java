package my.logon.screen.beans;

import java.util.ArrayList;
import java.util.List;

public class PierderiVanzariAV {
	public ArrayList<PierdereVanz> pierderiHeader;
	public ArrayList<PierdereTipClient> listPierderiTipCl;
	public ArrayList<PierdereNivel1> listPierderiNivel1;

	public List<PierdereVanz> getPierderiHeader() {
		return pierderiHeader;
	}

	public void setPierderiHeader(ArrayList<PierdereVanz> pierderiHeader) {
		this.pierderiHeader = pierderiHeader;
	}

	public ArrayList<PierdereTipClient> getListPierderiTipCl() {
		return listPierderiTipCl;
	}

	public void setListPierderiTipCl(ArrayList<PierdereTipClient> listPierderiTipCl) {
		this.listPierderiTipCl = listPierderiTipCl;
	}

	public ArrayList<PierdereNivel1> getListPierderiNivel1() {
		return listPierderiNivel1;
	}

	public void setListPierderiNivel1(ArrayList<PierdereNivel1> listPierderiNivel1) {
		this.listPierderiNivel1 = listPierderiNivel1;
	}

}
