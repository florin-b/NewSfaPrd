package my.logon.screen.dialogs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ClientDialogListener;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.R;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectClientDialog extends Dialog implements AsyncTaskListener {

	private Context context;
	private RadioGroup radioGroupClnt;
	private Spinner spinnerClienti;
	private ArrayList<HashMap<String, String>> listClienti;
	private SimpleAdapter adapterClienti;
	private String selectedClient = "";
	private String agent, filiala;
	private ClientDialogListener listener;
	private Button btnOkClnt;

	public SelectClientDialog(Context context, String agent, String filiala) {
		super(context);
		this.context = context;
		this.agent = agent;
		this.filiala = filiala;

		setContentView(R.layout.selclientdialogafiscmd);
		setTitle("Selectie client");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void dismissCustomDialog() {
		this.dismiss();
	}

	private void setUpLayout() {
		spinnerClienti = (Spinner) findViewById(R.id.spinnerSelClient);
		spinnerClienti.setVisibility(View.INVISIBLE);

		listClienti = new ArrayList<HashMap<String, String>>();
		adapterClienti = new SimpleAdapter(context, listClienti, R.layout.rowlayoutagenti, new String[] { "numeClient",
				"codClient" }, new int[] { R.id.textNumeAgent, R.id.textCodAgent });

		radioGroupClnt = (RadioGroup) findViewById(R.id.radioGroup1);

		addRadioListener();

		btnOkClnt = (Button) findViewById(R.id.btnOkClnt);
		addBtnOkListener();

	}

	private void addRadioListener() {
		radioGroupClnt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {

				RadioButton checkedRadioButton = (RadioButton) radioGroupClnt.findViewById(checkedId);

				String selectedRadioBtn = checkedRadioButton.getText().toString();

				if (selectedRadioBtn.equals("Toti clientii")) {
					selectedClient = "";
					listClienti.clear();
					spinnerClienti.setVisibility(View.INVISIBLE);

				}
				if (selectedRadioBtn.equals("Alege un client")) {
					if (listClienti.size() == 0) {
						getListClienti();
					}

					spinnerClienti.setVisibility(View.VISIBLE);

				}

			}
		});

	}

	private void addBtnOkListener() {
		btnOkClnt.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			public void onClick(View v) {
				if (listClienti.size() > 0) {
					HashMap<String, String> artMap = (HashMap<String, String>) spinnerClienti.getSelectedItem();

					if (isUserGed()) {
						selectedClient = artMap.get("numeClient");
					} else {
						selectedClient = artMap.get("codClient");
					}

				}

				if (listener != null) {
					listener.operationClientComplete(selectedClient);
					dismissCustomDialog();
				}

			}
		});
	}

	private boolean isUserGed() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM");
	}

	public void setClientDialogListener(ClientDialogListener listener) {
		this.listener = listener;
	}

	public void getListClienti() {

		NumberFormat nf3 = new DecimalFormat("00000000");
		String fullCode = nf3.format(Integer.parseInt(agent)).toString();

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();

		params.put("codAgent", fullCode);
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("filiala", filiala);
		params.put("tipUser", UserInfo.getInstance().getTipUser());

		AsyncTaskListener contextListener = (AsyncTaskListener) SelectClientDialog.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, "getListClienti", params);
		call.getCallResultsFromFragment();

	}

	void populateClientiList(String clientiList) {

		if (!clientiList.equals("-1") && clientiList.length() > 0) {

			listClienti.clear();

			HashMap<String, String> temp;
			String[] tokenLinie = clientiList.split("@@");
			String[] tokenClient;
			String client = "";

			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");

				temp.put("numeClient", tokenClient[1]);
				temp.put("codClient", tokenClient[0]);

				listClienti.add(temp);
			}

			spinnerClienti.setAdapter(adapterClienti);

		} else {
			Toast.makeText(context, "Nu exista clienti!", Toast.LENGTH_SHORT).show();
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		populateClientiList((String) result);

	}

}
