package my.logon.screen.listeners;

import java.util.HashMap;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class FilialaSelection implements OnItemSelectedListener {

	private FilialaSelectionListener filialaListener;

	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

		String selectedFiliala = "0";
		HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);

		selectedFiliala = artMap.get("codFiliala");

		if (selectedFiliala.trim().equals(""))
			selectedFiliala = "-1";
		if (selectedFiliala.equals("00000000"))
			selectedFiliala = "0";

		if (filialaListener != null) {
			filialaListener.selectedFiliala(selectedFiliala);
		}

	}

	public void onNothingSelected(AdapterView<?> parent) {
		// TODO
	}

	public void setFilialaListener(FilialaSelectionListener listener) {
		this.filialaListener = listener;
	}

}
