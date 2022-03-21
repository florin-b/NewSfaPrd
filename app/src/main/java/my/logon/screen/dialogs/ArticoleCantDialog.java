package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import my.logon.screen.listeners.ArticolCantListener;
import my.logon.screen.R;
import my.logon.screen.adapters.AdapterArticoleCant;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import my.logon.screen.beans.ArticolCant;

public class ArticoleCantDialog extends Dialog {

	private Context context;
	private Spinner spinnerCant, spinnerTipCant, spinnerFurnizor;
	private AdapterArticoleCant adapterPaleti;
	private ArticolCantListener listener;

	private List<ArticolCant> listArticole;
	private Button btnAdaugaArticol;
	private Button btnRenuntaPaleti;
	private ArticolCant cantSelectat;
	private String selectedTipCant = "";
	private String selectedFurnizor = "";

	public ArticoleCantDialog(Context context, List<ArticolCant> listArticole) {
		super(context);
		this.context = context;
		this.listArticole = listArticole;

		setContentView(R.layout.select_cant_dialog);
		setTitle("Selectie articole cant");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void setupSpinnerTipCantFurnizor() {

		TreeSet<String> tipCant = new TreeSet<String>();
		final TreeSet<String> furnizor = new TreeSet<String>();

		for (ArticolCant artCant : listArticole) {
			tipCant.add(artCant.getTipCant());
		}

		List<String> listTipCant = new ArrayList<String>(tipCant);
		listTipCant.add(0, "Selectati tipul de cant");

		ArrayAdapter<String> adapterCant = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,
				listTipCant.toArray(new String[listTipCant.size()]));
		spinnerTipCant.setAdapter(adapterCant);

		spinnerTipCant.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					selectedTipCant = (String) parent.getAdapter().getItem(position);

					furnizor.clear();

					for (ArticolCant artCant : listArticole) {
						if (artCant.getTipCant().equals(selectedTipCant))
							furnizor.add(artCant.getCaract());
					}

					List<String> listFurnizor = new ArrayList<String>(furnizor);
					listFurnizor.add(0, "Selectati furnizorul");

					ArrayAdapter<String> adapterFurnizor = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, listFurnizor
							.toArray(new String[listFurnizor.size()]));
					spinnerFurnizor.setAdapter(adapterFurnizor);

					if (listFurnizor.size() == 2)
						spinnerFurnizor.setSelection(1);

				} else
					selectedTipCant = "";

				afiseazaArticoleCant();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinnerFurnizor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					selectedFurnizor = (String) parent.getAdapter().getItem(position);
				} else
					selectedFurnizor = "";

				afiseazaArticoleCant();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		List<String> listFurnizor = new ArrayList<String>();
		listFurnizor.add(0, "Selectati furnizorul");

		ArrayAdapter<String> adapterFurnizor = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,
				listFurnizor.toArray(new String[listFurnizor.size()]));
		spinnerFurnizor.setAdapter(adapterFurnizor);

	}

	private void afiseazaArticoleCant() {

		List<ArticolCant> listArticoleCant = new ArrayList<ArticolCant>();

		if (!selectedTipCant.isEmpty() && !selectedFurnizor.isEmpty()) {

			for (ArticolCant artCant : listArticole) {

				if (artCant.getTipCant().equals(selectedTipCant) && artCant.getCaract().equals(selectedFurnizor))
					listArticoleCant.add(artCant);
			}

		}

		adapterPaleti = new AdapterArticoleCant(context, listArticoleCant);
		spinnerCant.setAdapter(adapterPaleti);

	}

	private void setUpLayout() {

		spinnerCant = (Spinner) findViewById(R.id.spinnerCant);

		spinnerTipCant = (Spinner) findViewById(R.id.spinnerTipCant);
		spinnerFurnizor = (Spinner) findViewById(R.id.spinnerFurnizor);
		setupSpinnerTipCantFurnizor();

		setSpinnerPaletiListener();

		btnAdaugaArticol = (Button) findViewById(R.id.btnOkArticol);
		addBtnAcceptaListener();

		btnRenuntaPaleti = (Button) findViewById(R.id.btnCancelPalet);

		addBtnRespingeListener();

	}

	private void setSpinnerPaletiListener() {
		spinnerCant.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				cantSelectat = (ArticolCant) arg0.getAdapter().getItem(arg2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void addBtnAcceptaListener() {
		btnAdaugaArticol.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null && cantSelectat != null) {
					listener.articolCantSelected(cantSelectat);
					dismiss();
				}
			}
		});
	}

	private void addBtnRespingeListener() {
		btnRenuntaPaleti.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.articolCantClosed();
				dismiss();
			}
		});
	}

	public void setArticoleCantListener(ArticolCantListener listener) {
		this.listener = listener;
	}

}
