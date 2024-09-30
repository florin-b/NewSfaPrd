package my.logon.screen.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

public class ScreenUtils {

	public static void hideSoftKeyboard(Context context, EditText editText) {
		InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public static void showSoftKeyboard(Context context, EditText editText) {
		InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void disableEditText(EditText editText) {
		editText.setFocusable(false);
		editText.setCursorVisible(false);
		editText.setKeyListener(null);
	}

	public static void enableEditText(EditText editText) {
		editText.setFocusableInTouchMode(true);
		editText.setCursorVisible(true);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL );
		editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
	}

	public static boolean isBundleArticolModificat(Bundle bundle){
		return bundle != null && bundle.getString("indexArticolModificat") != null;
	}

	public static boolean isIntentArticolModificat(Intent intent){
		return intent != null && intent.getStringExtra("indexArticolModificat") != null;
	}

	public static void setCheckBoxVisibility(CheckBox checkBox, boolean isVisible){

		if (checkBox == null)
			return;

		if (isVisible)
			checkBox.setVisibility(View.VISIBLE);
		else
			checkBox.setVisibility(View.INVISIBLE);

		checkBox.setChecked(false);

	}

}
