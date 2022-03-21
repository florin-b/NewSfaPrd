package my.logon.screen.filters;

import android.text.InputFilter;
import android.text.Spanned;

public class PaletiFilter implements InputFilter {

	private int mIntMin, mIntMax;

	public PaletiFilter(int minValue, int maxValue) {
		this.mIntMin = minValue;
		this.mIntMax = maxValue;
	}
	
	public PaletiFilter(String minValue, String maxValue) {
        this.mIntMin = Integer.parseInt(minValue);
        this.mIntMax = Integer.parseInt(maxValue);
    }

	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		try {
			int input = Integer.parseInt(dest.toString() + source.toString());
			if (isInRange(mIntMin, mIntMax, input))
				return null;
		} catch (NumberFormatException nfe) {
		}
		return "";
	}

	private boolean isInRange(int a, int b, int c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}

}
