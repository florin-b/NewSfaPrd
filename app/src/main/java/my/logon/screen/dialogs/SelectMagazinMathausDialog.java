package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.MagazinMathausListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectMagazinMathausDialog extends Dialog {

	Context context;
	String magazin;
	MagazinMathausListener listener;

	public SelectMagazinMathausDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.select_mathaus_dialog);
		setTitle("Magazin Mathaus");

		setUpLayout();
	}

	public void showDialog() {
		this.show();
	}

	private void dismissThisDialog() {
		this.dismiss();
	}

	void setUpLayout() {
		Spinner spinnerDivizii = (Spinner) findViewById(R.id.spinnerMagazine);

		ArrayList<HashMap<String, String>> listDivizii = getListMagazine();
		final SimpleAdapter adapterDivizii = new SimpleAdapter(context, listDivizii, R.layout.generic_rowlayout, new String[] { "numeDivizie", "codDivizie" },
				new int[] { R.id.textName, R.id.textId });

		spinnerDivizii.setAdapter(adapterDivizii);

		spinnerDivizii.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> artMap = (HashMap<String, String>) adapterDivizii.getItem(position);
				magazin = artMap.get("codDivizie");

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		Button btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (listener != null) {

					if (!magazin.isEmpty()) {
						listener.magazinMathausSelected(magazin);
						dismissThisDialog();
					}
				}

			}
		});

		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismissThisDialog();

			}
		});

		TextView textViewInfo = (TextView) findViewById(R.id.textViewInfo);
		textViewInfo.setText(getInfoText());

	}

	private String getInfoText() {
		return "Selectati o filiala cu magazin Mathaus pentru aceste articole.";
	}

	public ArrayList<HashMap<String, String>> getListMagazine() {

		String[] numeMagazin = { "Arges", "Bihor", "Bucuresti", "Constanta", "Dolj", "Galati", "Iasi" };
		String[] codMagazin = { "AG10", "BH10", "BU10", "CT10", "DJ10", "GL10", "IS10" };

		HashMap<String, String> temp;
		temp = new HashMap<String, String>();
		temp.put("numeDivizie", "Selectati o filiala");
		temp.put("codDivizie", "");
		ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
		listFiliale.add(temp);

		for (int i = 0; i < numeMagazin.length; i++) {
			temp = new HashMap<String, String>();

			temp.put("numeDivizie", numeMagazin[i]);
			temp.put("codDivizie", codMagazin[i]);

			listFiliale.add(temp);
		}

		return listFiliale;
	}

	public void setMagazinMathausDialogListener(MagazinMathausListener listener) {
		this.listener = listener;
	}

}
