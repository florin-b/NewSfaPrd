package my.logon.screen.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import my.logon.screen.beans.StatusIntervalLivrare;
import my.logon.screen.model.Constants;

public class UtilsDates {

	public static String getDateMonthString(int addMonth) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, addMonth);
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ro"));
	}

	public static int getYearMonthDate(int addMonth) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, addMonth);

		String date = String.valueOf(cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.MONTH) + 1);

		return Integer.parseInt(date);

	}

	public static String getMonthName(String strDate) {

		if (strDate == null || strDate.trim().length() == 0)
			return " ";

		int monthNumber = Integer.parseInt(strDate.substring(4, 6));

		String monthName = "";
		switch (monthNumber) {
		case 1:
			monthName = "ianuarie";
			break;

		case 2:
			monthName = "februarie";
			break;

		case 3:
			monthName = "martie";
			break;

		case 4:
			monthName = "aprilie";
			break;

		case 5:
			monthName = "mai";
			break;

		case 6:
			monthName = "iunie";
			break;

		case 7:
			monthName = "iulie";
			break;

		case 8:
			monthName = "august";
			break;

		case 9:
			monthName = "septembrie";
			break;

		case 10:
			monthName = "octombrie";
			break;

		case 11:
			monthName = "noiembrie";
			break;

		case 12:
			monthName = "decembrie";
			break;

		}

		return monthName;

	}

	public static String getCurrentDate() {
		return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
	}

	private static Date getDateMidnight() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static int dateDiffinDays(Date dateStop) {

		long diff = dateStop.getTime() - getDateMidnight().getTime() + 1000;

		if (diff < 0)
			return -1;

		return (int) (diff / (24 * 60 * 60 * 1000));

	}
	
	
	
	public static int dateDiffinDays2(Date dateStop) {

		long diff = getDateMidnight().getTime() + 1000 - dateStop.getTime();

		if (diff < 0)
			return -1;

		return (int) (diff / (24 * 60 * 60 * 1000));

	}
	
	
	

	public static StatusIntervalLivrare getStatusIntervalLivrare(Date dateStop) {

		StatusIntervalLivrare statusInterval = new StatusIntervalLivrare();

		int dateDiff = dateDiffinDays(dateStop);

		if (dateDiff < 0) {
			statusInterval.setValid(false);
			statusInterval.setMessage("Data livrare incorecta.");
			return statusInterval;
		}

		if (dateDiff > getNrZileLivrare()) {
			statusInterval.setValid(false);
			statusInterval.setMessage("Livrarea trebuie sa se faca in cel mult " + getNrZileLivrare() + " zile de la data curenta.");
			return statusInterval;
		}

		statusInterval.setValid(true);

		return statusInterval;
	}

	private static int getNrZileLivrare() {
		if (UtilsUser.isAV() || UtilsUser.isKA())
			return Constants.NR_ZILE_LIVRARE_AG;
		else
			return Constants.NR_ZILE_LIVRARE_CVA;
	}

	public static Date addDaysToDate(Date date, int days) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();

	}



	public static List<String> getZileLivrare() {
		List<String> datesInRange = new ArrayList<>();
		Calendar calendar = getCalendarWithoutTime(new Date());
		Calendar endCalendar = getCalendarWithoutTime(addDaysToDate(new Date(), 6));

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

		while (calendar.before(endCalendar)) {
			Date result = calendar.getTime();
			datesInRange.add(simpleDateFormat.format(result));
			calendar.add(Calendar.DATE, 1);
		}

		return datesInRange;
	}

	private static Calendar getCalendarWithoutTime(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static int dateDiffDays(Date dateStart, Date dateStop) {

		if (dateStart == null || dateStop == null)
			return -1;

		try {

			long diff = dateStop.getTime() - dateStart.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				return (int) diffDays;
			}

		} catch (Exception e) {

		}

		return -1;

	}

	public static String formatDataExp(String dataExp) {
		String[] blocks = dataExp.split("-");
		return blocks[2] + "-" + blocks[1] + "-" + blocks[0];
	}
	
	public static String formatDateFromSap(String strDate) {

		String formatted = "";

		try {

			String pattern = "yyyymmdd";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, new Locale("ro"));
			Date date = formatInit.parse(strDate);

			SimpleDateFormat formatFinal = new SimpleDateFormat("dd-mm-yyyy");

			formatted = formatFinal.format(date);
		} catch (Exception p) {
			
		}

		return formatted;

	}

}
