package my.logon.screen.listeners;

import java.util.HashMap;

import android.view.View;
import android.widget.AdapterView;

public class CustomSpinnerClass implements
		AdapterView.OnItemSelectedListener {

	private CustomSpinnerListener listener;

	public void setListener(CustomSpinnerListener listener) {
		this.listener = listener;
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		@SuppressWarnings("unchecked")
		HashMap<String, String> artMap = (HashMap<String, String>) arg0
				.getSelectedItem();

		if (listener != null) {
			listener.onSelectedSpinnerItem(arg0.getId(), artMap, position);
		}

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		

	}

}
