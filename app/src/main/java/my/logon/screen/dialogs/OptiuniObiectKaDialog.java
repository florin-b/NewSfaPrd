package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.DialogObiectiveKAListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.listeners.OperatiiAgentListener;
import my.logon.screen.model.Agent;
import my.logon.screen.model.OperatiiAgent;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import my.logon.screen.enums.EnumFilialeKA;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.enums.EnumTipInterval;
import my.logon.screen.enums.EnumTipUser;

public class OptiuniObiectKaDialog extends Dialog implements OperatiiAgentListener, ObiectiveListener {

	private Spinner spinnerFiliale, spinnerAgenti, spinnerInterval;
	private OperatiiAgent operatiiAgent;
	private String selectedFiliala, selectedAgent = "00";
	private int selectedInterval;
	private ImageButton buttonOk, buttonCancel;
	private OperatiiObiective operatiiObiective;
	private DialogObiectiveKAListener listener;
	private LinearLayout layoutInterval, layoutAgenti;

	public OptiuniObiectKaDialog(Context context) {
		super(context);

		setContentView(R.layout.optiuni_ka_dialog);
		setTitle("Selectii");
		setCancelable(true);

		operatiiAgent = OperatiiAgent.getInstance();
		operatiiAgent.setOperatiiAgentListener(this);

		operatiiObiective = new OperatiiObiective(getContext());

		setupLayout();
	}

	private void setupLayout() {
		spinnerFiliale = (Spinner) findViewById(R.id.spinnerFiliale);
		setupSpinnerFiliale();

		spinnerInterval = (Spinner) findViewById(R.id.spinnerInterval);

		setupSpinnerInterval();

		spinnerAgenti = (Spinner) findViewById(R.id.spinnerAgenti);

		layoutInterval = (LinearLayout) findViewById(R.id.layoutInterval);
		layoutAgenti = (LinearLayout) findViewById(R.id.layoutAgenti);

		layoutAgenti.setVisibility(View.INVISIBLE);
		layoutInterval.setVisibility(View.INVISIBLE);

		if (isUserDV())
			layoutAgenti.setVisibility(View.GONE);

		buttonOk = (ImageButton) findViewById(R.id.btnOk);
		setListenerBtnOk();
		buttonCancel = (ImageButton) findViewById(R.id.btnCancel);
		setListenerBtnCancel();

	}

	private void setListenerBtnCancel() {
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});

	}

	private void setListenerBtnOk() {
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isInputValid()) {
					getListObiective();
					dismiss();
				}

			}
		});

	}

	private void getListObiective() {

		String depart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipUser().equals("DK"))
			depart = "00";

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", selectedAgent);
		params.put("filiala", selectedFiliala);
		params.put("tip", "-1");
		params.put("interval", String.valueOf(selectedInterval));
		params.put("depart", depart);

		operatiiObiective.setObiectiveListener(this);
		operatiiObiective.getListObiective(params);
	}

	private boolean isInputValid() {
		if (spinnerFiliale.getAdapter().getCount() > 1 && spinnerFiliale.getSelectedItemPosition() == 0) {
			Toast.makeText(getContext(), "Selectati filiala ", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void setupSpinnerInterval() {
		spinnerInterval.setAdapter(new ArrayAdapter<EnumTipInterval>(getContext(), android.R.layout.simple_list_item_1, EnumTipInterval.values()));
		spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				selectedInterval = ((EnumTipInterval) spinnerInterval.getSelectedItem()).getCod();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void setupSpinnerFiliale() {

		spinnerFiliale.setAdapter(new ArrayAdapter<EnumFilialeKA>(getContext(), android.R.layout.simple_list_item_1, getFiliale()));
		spinnerFiliale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					selectedFiliala = ((EnumFilialeKA) spinnerFiliale.getSelectedItem()).getCod();
					layoutInterval.setVisibility(View.VISIBLE);

					if (isUserDK())
						getListaAgenti(selectedFiliala);
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private EnumFilialeKA[] getFiliale() {
		if (isUserSK())
			return EnumFilialeKA.getFilialeSK();
		else
			return EnumFilialeKA.values();
	}

	private boolean isUserDK() {
		return UserInfo.getInstance().getTipUser().equals(EnumTipUser.DK.getTipAcces());
	}

	private boolean isUserDV() {
		return UserInfo.getInstance().getTipUser().equals(EnumTipUser.DV.getTipAcces());
	}

	private boolean isUserSK() {
		return UserInfo.getInstance().getTipUser().equals(EnumTipUser.SK.getTipAcces());
	}

	private void getListaAgenti(String filiala) {
		operatiiAgent.getListaAgenti(filiala, getDepartUser(), getContext(), true, null);
	}

	private String getDepartUser() {
		String codDepart = "00";

		if (UserInfo.getInstance().getTipUser().equals(EnumTipUser.DV.getTipAcces()))
			codDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipUser().equals(EnumTipUser.DK.getTipAcces()))
			codDepart = "10";

		return codDepart;

	}

	private void populateSpinnerAgenti(List<Agent> listAgenti) {

		layoutAgenti.setVisibility(View.VISIBLE);
		spinnerAgenti.setAdapter(new ArrayAdapter<Agent>(getContext(), android.R.layout.simple_list_item_1, listAgenti));
		spinnerAgenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedAgent = ((Agent) spinnerAgenti.getSelectedItem()).getCod();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		populateSpinnerAgenti(operatiiAgent.getListObjAgenti());

	}

	public void setDialogListener(DialogObiectiveKAListener listener) {
		this.listener = listener;
	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_LIST_OBIECTIVE:
			if (listener != null)
				listener.selectionComplete(operatiiObiective.deserializeListaObiective((String) result));

			break;
		default:
			break;
		}

	}

}
