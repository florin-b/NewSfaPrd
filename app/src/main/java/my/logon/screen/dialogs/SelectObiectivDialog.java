package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.listeners.SelectObiectivListener;
import my.logon.screen.R;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;
import my.logon.screen.adapters.AfisareObiectiveAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import my.logon.screen.beans.BeanObiectivAfisare;
import my.logon.screen.enums.EnumMeniuObiectiv;
import my.logon.screen.enums.EnumOperatiiObiective;

public class SelectObiectivDialog extends Dialog implements ObiectiveListener {

	private ImageButton cancelButton;
	private Spinner spinnerInterval;
	private static String[] optiuniInterval = { "Selectati un obiectiv creat ", "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile",
			"De peste 30 de zile" };
	private ListView listObiective;
	private OperatiiObiective obiective;
	private SelectObiectivListener listener;
	private EnumMeniuObiectiv tipOperatie;

	public SelectObiectivDialog(Context context) {
		super(context);

		setContentView(R.layout.select_obiective_dialog);
		setTitle("Selectie obiectiv");
		setCancelable(true);

		obiective = new OperatiiObiective(getContext());

		setupLayout();

	}

	private void setupLayout() {
		cancelButton = (ImageButton) findViewById(R.id.btnCancel);
		setListenerCancelButton();

		spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);
		populateSpinnerInterval();
		setListenerSpinnerInterval();

		listObiective = (ListView) findViewById(R.id.listObiective);
		setListObiectiveListener();

	}

	private void setListObiectiveListener() {
		listObiective.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BeanObiectivAfisare obiectiv = (BeanObiectivAfisare) parent.getAdapter().getItem(position);

				if (listener != null) {
					listener.obiectivSelected(obiectiv);
					dismiss();
				}

			}
		});
	}

	private void setListenerSpinnerInterval() {
		spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 > 0)
					getListObiective(arg2 - 1);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void getListObiective(int tipInterval) {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("tip", String.valueOf(tipOperatie.getCodMeniu()));
		params.put("interval", String.valueOf(tipInterval));

		obiective.setObiectiveListener(this);
		obiective.getListObiective(params);

	}

	private void populateSpinnerInterval() {

		ArrayList<HashMap<String, String>> listOptInterval = new ArrayList<HashMap<String, String>>();
		SimpleAdapter spinnerAdapter = new SimpleAdapter(getContext(), listOptInterval, R.layout.customrowselinterval,
				new String[] { "optInterval" }, new int[] { R.id.textTipInterval });

		HashMap<String, String> temp;

		for (int ii = 0; ii < optiuniInterval.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("optInterval", optiuniInterval[ii]);
			listOptInterval.add(temp);
		}

		spinnerInterval.setAdapter(spinnerAdapter);

	}

	private void setListenerCancelButton() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	private void populateListObiective(List<BeanObiectivAfisare> obiective) {
		AfisareObiectiveAdapter adapter = new AfisareObiectiveAdapter(getContext(), obiective);
		listObiective.setAdapter(adapter);

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_LIST_OBIECTIVE:
			populateListObiective(obiective.deserializeListaObiective((String) result));
			break;
		default:
			break;
		}

	}

	public void setObiectivSelectedListener(SelectObiectivListener listener) {
		this.listener = listener;
	}

	public EnumMeniuObiectiv getTipOperatie() {
		return tipOperatie;
	}

	public void setTipOperatie(EnumMeniuObiectiv tipOperatie) {
		this.tipOperatie = tipOperatie;
	}

}
