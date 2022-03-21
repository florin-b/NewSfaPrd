package my.logon.screen.patterns;

import java.util.Comparator;

import my.logon.screen.beans.ArticolCLP;

public class ClpDepartComparator implements Comparator<ArticolCLP> {

	public int compare(ArticolCLP art1, ArticolCLP art2) {
		return art1.getDepart().compareTo(art2.getDepart());
	}

}
