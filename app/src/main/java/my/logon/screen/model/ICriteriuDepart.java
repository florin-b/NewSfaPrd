package my.logon.screen.model;

import java.util.List;

import my.logon.screen.beans.BeanObiectiveConstructori;

public interface ICriteriuDepart {
	List<BeanObiectiveConstructori> constructoriDepart(List<BeanObiectiveConstructori> listConstructori, String codDepart);

}
