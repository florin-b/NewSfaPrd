package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.utils.UtilsGeneral;

import my.logon.screen.listeners.DivizieDialogListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class SelectDivizieDialog extends Dialog {

	Context context;
	String divizie = "00";
	DivizieDialogListener listener;

	public SelectDivizieDialog(Context context, String divizie) {
		super(context);
		this.context = context;
		this.divizie = divizie;

		setContentView(R.layout.select_divizie_dialog);
		setTitle("Divizie");

		setUpLayout();
	}

	public void showDialog() {
		this.show();
	}

	private void dismissThisDialog() {
		this.dismiss();
	}

	void setUpLayout() {
		Spinner spinnerDivizii = (Spinner) findViewById(R.id.spinnerDivizii);

		ArrayList<HashMap<String, String>> listDivizii = getListToateDiviziile();
		final SimpleAdapter adapterDivizii = new SimpleAdapter(context, listDivizii, R.layout.generic_rowlayout,
				new String[] { "numeDivizie", "codDivizie" }, new int[] { R.id.textName, R.id.textId });

		spinnerDivizii.setAdapter(adapterDivizii);

		spinnerDivizii.setSelection(Integer.valueOf(divizie));

		spinnerDivizii.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> artMap = (HashMap<String, String>) adapterDivizii.getItem(position);
				divizie = artMap.get("codDivizie");

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		Button btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (listener != null) {
					listener.divizieSelected(divizie);
					dismissThisDialog();
				}

			}
		});

		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismissThisDialog();

			}
		});

	}

	public ArrayList<HashMap<String, String>> getListToateDiviziile() {

		HashMap<String, String> temp;
		temp = new HashMap<String, String>();
		temp.put("numeDivizie", "Toate diviziile");
		temp.put("codDivizie", "00");
		ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
		listFiliale.add(temp);

		for (int i = 0; i < UtilsGeneral.numeDivizii.length; i++) {
			temp = new HashMap<String, String>();

			temp.put("numeDivizie", UtilsGeneral.numeDivizii[i]);
			temp.put("codDivizie", UtilsGeneral.codDivizii[i]);

			listFiliale.add(temp);
		}

		return listFiliale;
	}

	public void setDivizieDialogListener(DivizieDialogListener listener) {
		this.listener = listener;
	}

}
