package my.logon.screen.patterns;

import java.util.Comparator;

import my.logon.screen.beans.FacturaNeincasataLite;

public class FactNeincOrdComparator implements Comparator<FacturaNeincasataLite> {

	@Override
	public int compare(FacturaNeincasataLite fact1, FacturaNeincasataLite fact2) {
		return fact1.getOrdineSelectie() - fact2.getOrdineSelectie();

	}

}
