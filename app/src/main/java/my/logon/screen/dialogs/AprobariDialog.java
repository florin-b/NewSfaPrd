package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.OperatiiAprobariListener;
import my.logon.screen.model.OperatiiAprobari;
import my.logon.screen.R;
import my.logon.screen.adapters.AprobareComandaAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import my.logon.screen.beans.Aprobare;
import my.logon.screen.enums.EnumAprobari;

//Author Petru 

public class AprobariDialog extends Dialog implements OperatiiAprobariListener {

	private Context context;
	private ListView listView;
	private ImageButton btnClose;
	private RelativeLayout rel;
	private TextView comanda;

	private OperatiiAprobari opAprobari;

	public AprobariDialog(Context context) {
		super(context);

		this.context = context;
		setContentView(R.layout.aprobare_dialog);
		setTitle("Aprobare comanda");
		setCancelable(true);

		opAprobari = new OperatiiAprobari(context);
		opAprobari.setOperatiiAprobariListener(this);

		setupLayout();

	}

	private void setupLayout() {
		btnClose = (ImageButton) findViewById(R.id.btnCancel);
		rel = (RelativeLayout) findViewById(R.id.relList);
		listView = (ListView) findViewById(R.id.lista);
		comanda = (TextView) findViewById(R.id.aprobata);
		setCancelButtonListener();

	}

	private void setCancelButtonListener() {
		btnClose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void getAproveData(String nrComanda) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrComanda", nrComanda);

		opAprobari.getAprobariComenziKA(params);

	}

	@Override
	public void operationComplete(EnumAprobari methodName, Object result) {
		switch (methodName) {
		case GET_APROBARI_COMENZI_KA:
			afisListaAprobariKa(opAprobari.deserializeAprobariKA((String) result));
			break;

		default:
			break;
		}

	}

	private void afisListaAprobariKa(ArrayList<Aprobare> deserializeListAprobariKa) {
		if (deserializeListAprobariKa.isEmpty()) {
			rel.setVisibility(View.GONE);
			comanda.setVisibility(View.VISIBLE);
			comanda.setText("Comanda a primit toate aprobarile necesare.");
		}
		AprobareComandaAdapter adapter = new AprobareComandaAdapter(context, deserializeListAprobariKa);
		listView.setAdapter(adapter);

	}

}
//End Petru 
