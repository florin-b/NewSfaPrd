package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterCabluri;
import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.listeners.Cablu05SelectedListener;
import my.logon.screen.listeners.CantCablu05Listener;

public class Cabluri05Dialog extends Dialog implements CantCablu05Listener {

	private Context context;
	private ListView listViewCabluri;
	private List<BeanCablu05> listCabluri;

	private Button btnSalveaza;
	private TextView textCantArticol;
	private String strCantArticol;
	private Cablu05SelectedListener listener;

	public Cabluri05Dialog(Context context, List<BeanCablu05> listCabluri, String strCantArticol) {
		super(context);
		this.context = context;
		this.listCabluri = listCabluri;
		this.strCantArticol = strCantArticol;

		setContentView(R.layout.cabluri_05_dialog);
		setTitle("Selectare sursa");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void setUpLayout() {

		listViewCabluri = (ListView) findViewById(R.id.listViewCabluri);
		AdapterCabluri adapterCabluri = new AdapterCabluri(context, listCabluri, Double.parseDouble(strCantArticol));
		adapterCabluri.setCantCabluListener(this);
		listViewCabluri.setAdapter(adapterCabluri);

		textCantArticol = (TextView) findViewById(R.id.textCantArticol);
		textCantArticol.setText(strCantArticol);

		btnSalveaza = (Button) findViewById(R.id.btnSalveaza);
		btnSalveaza.setEnabled(false);
		setBtnSalveazaListener();

	}

	private void setBtnSalveazaListener() {
		btnSalveaza.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (listener != null) {
					listener.cabluriSelected(getBoxeCabluri());
					dismiss();

				}

			}
		});
	}

	private List<BeanCablu05> getBoxeCabluri() {

		List<BeanCablu05> selCabluri = new ArrayList<BeanCablu05>();

		for (BeanCablu05 cablu : listCabluri) {

			if (cablu.isSelected())
				selCabluri.add(cablu);
		}

		return selCabluri;

	}

	public void setCabluSelectedListener(Cablu05SelectedListener listener) {
		this.listener = listener;
	}

	@Override
	public void cantitateCabluChanged(String cantitate) {
		textCantArticol.setText(cantitate);

		if (Double.valueOf(cantitate) == 0)
			btnSalveaza.setEnabled(true);
		else
			btnSalveaza.setEnabled(false);

	}

}
