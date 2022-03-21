package my.logon.screen.listeners;

import java.util.HashMap;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class GenericSpinnerSelection implements OnItemSelectedListener {

	private SpinnerSelectionListener itemListener;

	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long arg3) {
		HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);

		Spinner spinner = (Spinner) parent;

		String objName = artMap.get("stringName");
		String objId = artMap.get("stringId");

		if (itemListener != null) {
			itemListener.selectedItem(spinner, view, objId, objName);
		}

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		return;

	}

	public void setItemSelectionListener(SpinnerSelectionListener itemListener) {
		this.itemListener = itemListener;
	}

}
