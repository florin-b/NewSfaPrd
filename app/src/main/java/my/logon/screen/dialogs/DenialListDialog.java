package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.DenialListDialogListener;
import my.logon.screen.R;
import my.logon.screen.model.ComenziDAO;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import my.logon.screen.enums.EnumComenziDAO;

public class DenialListDialog extends Dialog implements ComenziDAOListener {

	private String codRespingere;
	private int tipOpCmd;
	private boolean cmdAngajament;
	private Context context;
	private ArrayList<HashMap<String, String>> listMotive;
	private DenialListDialogListener listener;

	public DenialListDialog(Context context) {
		super(context);
		this.context = context;
		setContentView(R.layout.denialaprobdialog);
		setTitle("Motiv respingere");
		setCancelable(true);

	}

	public void showDialog() {
		getListMotiveRespingere();

	}

	private void dismissThisDialog() {
		this.dismiss();
	}

	private void setUpLayout() {
		final Spinner denialOptions = (Spinner) findViewById(R.id.spinnerSelDenialOptions);

		SimpleAdapter adapterMotive = new SimpleAdapter(context, listMotive, R.layout.rowlayoutrespingere,
				new String[] { "numeLinie", "codLinie" }, new int[] { R.id.textNumeLinie, R.id.textCodLinie });

		denialOptions.setAdapter(adapterMotive);

		Button btnOkDenial = (Button) findViewById(R.id.btnOkDenial);
		btnOkDenial.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			public void onClick(View v) {

				if (!cmdAngajament) {
					tipOpCmd = 1;
				} else
					tipOpCmd = 8; // respingere comanda angajament

				if (denialOptions.getAdapter().getCount() > 0) {
					HashMap<String, String> artMap = (HashMap<String, String>) denialOptions.getSelectedItem();
					codRespingere = artMap.get("codLinie");

					if (listener != null) {
						listener.denialOperationOccured(tipOpCmd, codRespingere);
					}
					dismissThisDialog();
				}

			}
		});

		Button btnCancelDenial = (Button) findViewById(R.id.btnCancelDenial);
		btnCancelDenial.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dismissThisDialog();

			}
		});

	}

	public void getListMotiveRespingere() {

		try {
			HashMap<String, String> params = new HashMap<String, String>();
			ComenziDAO comenzi = ComenziDAO.getInstance(context);
			comenzi.setComenziDAOListener(this);
			comenzi.getMotiveRespingere(params);

		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateRespingereList(String result) {

		listMotive = new ArrayList<HashMap<String, String>>();
		if (result.contains("@@")) {

			HashMap<String, String> temp;
			String[] tokenLinie = result.split("@@");
			String[] tokenClient;
			String client = "";

			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");

				temp.put("numeLinie", tokenClient[1]);
				temp.put("codLinie", tokenClient[0]);

				listMotive.add(temp);
			}

		}
	}

	public void setDenialListDialogListener(DenialListDialogListener listener) {
		this.listener = listener;
	}

	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		if (methodName == EnumComenziDAO.GET_LISTA_RESPINGERE) {
			populateRespingereList((String) result);
			setUpLayout();
			this.show();
		}

	}

}
