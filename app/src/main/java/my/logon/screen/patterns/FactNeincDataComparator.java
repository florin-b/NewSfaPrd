package my.logon.screen.patterns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import my.logon.screen.beans.FacturaNeincasataLite;

public class FactNeincDataComparator implements Comparator<FacturaNeincasataLite> {

	@Override
	public int compare(FacturaNeincasataLite fact1, FacturaNeincasataLite fact2) {

		DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

		int dateComp = 0;

		try {
			Date date1 = format.parse(fact1.getDataEmitere());
			Date date2 = format.parse(fact2.getDataEmitere());
			dateComp = date1.compareTo(date2);

		} catch (ParseException e) {

		}

		return dateComp;

	}

}
