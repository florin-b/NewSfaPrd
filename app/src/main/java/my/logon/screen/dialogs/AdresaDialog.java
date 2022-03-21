package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterListJudete;
import my.logon.screen.beans.BeanAdresaGenerica;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.BeanJudet;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.listeners.AdresaDialogListener;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.model.OperatiiAdresa;
import my.logon.screen.model.OperatiiAdresaImpl;
import my.logon.screen.utils.UtilsGeneral;

public class AdresaDialog extends Dialog implements OperatiiAdresaListener {

	private Spinner spinnerJudete;
	private AutoCompleteTextView textLocalitate;
	private AutoCompleteTextView textStrada;
	private ImageButton saveButton, cancelButton;
	private AdresaDialogListener adresaListener;
	private boolean showStradaField;
	private OperatiiAdresa operatiiAdresa;

	public AdresaDialog(Context context, boolean showStradaField) {
		super(context);
		this.showStradaField = showStradaField;

		setContentView(R.layout.adresa_dialog);
		setTitle("Adresa");
		setCancelable(true);

		operatiiAdresa = new OperatiiAdresaImpl(context);
		operatiiAdresa.setOperatiiAdresaListener(this);

		setupLayout();
	}

	private void setupLayout() {

		spinnerJudete = (Spinner) findViewById(R.id.spinnerJudete);

		AdapterListJudete adapter = new AdapterListJudete(getContext(), listJudete());
		spinnerJudete.setAdapter(adapter);
		setSpinnerJudeteListener();

		textLocalitate = (AutoCompleteTextView) findViewById(R.id.textOras);
		textLocalitate.setHint("Completati orasul");
		textStrada = (AutoCompleteTextView) findViewById(R.id.textStrada);
		textStrada.setHint("Completati strada, nr");

		if (!showStradaField)
			textStrada.setVisibility(View.INVISIBLE);

		saveButton = (ImageButton) findViewById(R.id.btnSave);
		cancelButton = (ImageButton) findViewById(R.id.btnCancel);

		setSaveButtonListener();
		setCancelButtonListener();

	}

	private void setSpinnerJudeteListener() {
		spinnerJudete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {

					BeanJudet judet = (BeanJudet) spinnerJudete.getSelectedItem();

					HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
					params.put("codJudet", judet.getCodJudet());
					operatiiAdresa.getAdreseJudet(params, null);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private List<BeanJudet> listJudete() {
		List<BeanJudet> listJudete = new ArrayList<BeanJudet>();

		BeanJudet judet = null;
		for (int i = 0; i < UtilsGeneral.numeJudete.length; i++) {
			judet = new BeanJudet();
			judet.setCodJudet(UtilsGeneral.codJudete[i]);
			judet.setNumeJudet(UtilsGeneral.numeJudete[i]);
			listJudete.add(judet);
		}

		return listJudete;
	}

	private void setSaveButtonListener() {
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (isAdresaValid()) {
					if (adresaListener != null) {
						adresaListener.adresaSelected(getAdresa());
						dismiss();
					}
				}

			}
		});
	}

	private BeanAdresaGenerica getAdresa() {
		BeanAdresaGenerica adresa = new BeanAdresaGenerica();

		BeanJudet judet = (BeanJudet) spinnerJudete.getSelectedItem();
		adresa.setCodJudet(judet.getCodJudet());
		adresa.setNumeJudet(judet.getNumeJudet());
		adresa.setOras(textLocalitate.getText().toString().replace("/", " ").trim());
		adresa.setStrada(textStrada.getText().toString().replace("/", " ").trim());

		return adresa;

	}

	private void setCancelButtonListener() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void setAdresaDialogListener(AdresaDialogListener adresaListener) {
		this.adresaListener = adresaListener;
	}

	private boolean isAdresaValid() {
		if (spinnerJudete.getSelectedItemPosition() == 0) {
			Toast.makeText(getContext(), "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (textLocalitate.getText().toString().trim().length() == 0) {
			Toast.makeText(getContext(), "Completati orasul", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void populateListLocalitati(BeanAdreseJudet listAdrese) {

		String[] arrayLocalitati = listAdrese.getListLocalitati().toArray(new String[listAdrese.getListLocalitati().size()]);
		ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		textLocalitate.setThreshold(0);
		textLocalitate.setAdapter(adapterLoc);

		String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
		ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arrayStrazi);

		textStrada.setThreshold(0);
		textStrada.setAdapter(adapterStrazi);

	}

	public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {
		switch (numeComanda) {
		case GET_ADRESE_JUDET:
			populateListLocalitati(operatiiAdresa.deserializeListAdrese(result));
			break;
		default:
			break;
		}
	}
}
