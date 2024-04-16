package my.logon.screen.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.logon.screen.enums.EnumJudete;
import my.logon.screen.enums.EnumTipComanda;
import my.logon.screen.model.Constants;

public class UtilsFormatting {

	private static NumberFormat nf2 = new DecimalFormat("#0.00");

	public static String formatDate(String dateString) {

		String formattedDate = "";

		if (dateString.trim().equals(""))
			return "";

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("ro"));

		try {
			formattedDate = df.format(dateFormat.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedDate;

	}

	public static String formatDateFromSap(String target) {

		String formattedDate = "";

		try {
			SimpleDateFormat formatFinal = new SimpleDateFormat("yyyyMMdd", Locale.US);
			Date date = formatFinal.parse(target);

			String pattern = "dd-MM-yyyy";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern);

			formattedDate = formatInit.format(date);

		} catch (ParseException ex) {
			System.out.println(ex.toString());
		}
		return formattedDate;

	}

	public static boolean isNumeric(String stringToCheck) {
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(stringToCheck.trim());
		return matcher.find();

	}

	public static String removeComma(String stringNumber) {
		return stringNumber.replaceAll(",", "");
	}

	public static String format2Decimals(double doubleValue, boolean groupSeparator) {

		NumberFormat nf = NumberFormat.getInstance();

		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(groupSeparator);

		return nf.format(doubleValue);

	}

	public static String addSpace(String text, int length) {
		StringBuilder spaceString = new StringBuilder();

		for (int i = 0; i < length - text.length(); i++)
			spaceString.append(" ");

		return spaceString.toString();
	}

	public static String flattenToAscii(String string) {
		StringBuilder sb = new StringBuilder(string.length());
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		for (char c : string.toCharArray()) {
			if (c <= '\u007F')
				sb.append(c);
		}
		return sb.toString();
	}

	public static boolean streetHasNumber(String street) {
		String regex = "[\\s]*[\\d]+[\\w]*";

		Pattern patern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patern.matcher(street);

		return matcher.find();
	}

	public static String formatTime(String stringDateTime) {
		DateFormat dfSap = new SimpleDateFormat("HHmmss");

		Date d = new Date();

		try {
			d = dfSap.parse(stringDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		DateFormat dfFin = new SimpleDateFormat("HH:mm:ss");

		return dfFin.format(d);
	}

	public static Bitmap writeTextOnDrawable(Context context, int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

		Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTypeface(tf);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(convertToPixels(context, 21));

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		Canvas canvas = new Canvas(bm);

		if (textRect.width() >= (canvas.getWidth() - 2))
			paint.setTextSize(convertToPixels(context, 7));

		int xPos = (canvas.getWidth() / 2) - 2;

		int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 4));

		canvas.drawText(text, xPos, yPos, paint);

		return bm;
	}

	public static int convertToPixels(Context context, int nDP) {
		final float conversionScale = context.getResources().getDisplayMetrics().density;

		return (int) ((nDP * conversionScale) + 0.5f);

	}

	public static String formatAddress(String address) {
		String formatted = address;

		if (address != null && address.contains("/")) {
			String[] adrTokens = address.split("/");

			for (int i = 0; i < adrTokens.length; i++)
				if (i == 0)
					formatted = EnumJudete.getNumeJudet(Integer.parseInt(adrTokens[i]));
				else
					formatted += ", " + adrTokens[i];

		}

		return formatted;
	}

	public static String getMonthNameFromDate(String dateString, int formatStyle) {

		if (dateString.trim().equals(""))
			return "";

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
		;

		if (formatStyle == Calendar.LONG)
			dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

		if (formatStyle == Calendar.SHORT)
			dateFormat = new SimpleDateFormat("dd-mm-yy", Locale.ENGLISH);

		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(dateFormat.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return cal.getDisplayName(Calendar.MONTH, formatStyle, new Locale("ro"));

	}

	public static String getIstoricPret(String istoric, EnumTipComanda tipComanda) {
		StringBuilder formatted = new StringBuilder();

		double valTva = 1;

		if (tipComanda != null && tipComanda == EnumTipComanda.GED)
			valTva = Constants.TVA;

		if (istoric != null && istoric.contains(":")) {
			String[] arrayIstoric = istoric.split(":");

			for (String item : arrayIstoric) {
				String[] token = item.split("@");

				if (!formatted.toString().isEmpty())
					formatted.append("#");

				formatted.append(nf2.format(Double.valueOf(token[0].trim()) * valTva));
				formatted.append(" / ");
				formatted.append(token[2]);
				formatted.append(" ");
				formatted.append(getMonthNameFromDate(token[3], Calendar.SHORT));

			}

		}

		return formatted.toString();
	}

	public static String addSpace(int nrCars) {
		String retVal = "";

		for (int i = 0; i < nrCars; i++)
			retVal += " ";

		return retVal;
	}
}
