package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareClientiAdapter;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.listeners.CautaClientDialogListener;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.ScreenUtils;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class CautaClientDialog extends Dialog implements OperatiiClientListener {

	private ImageButton cancelButton;
	private Button btnCautaClient;
	private EditText textNumeClient;
	private OperatiiClient opClient;
	private ListView listClientiObiective;
	private CautaClientDialogListener listener;
	private Context context;
	private boolean isMeserias;
	private boolean isClientObiectivKA;
	private boolean isInstitPublica;
	private String numeClient;
	private RadioGroup radioClientIP;
	private EnumTipClientIP tipClientIP = EnumTipClientIP.CONSTR;

	public CautaClientDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.select_client_obiective_dialog);
		setTitle("Selectie client");
		setCancelable(true);

		opClient = new OperatiiClient(getContext());
		opClient.setOperatiiClientListener(this);

		setUpLayout();

	}

	private void setUpLayout() {

		cancelButton = (ImageButton) findViewById(R.id.btnCancel);
		setListenerCancelButton();

		btnCautaClient = (Button) findViewById(R.id.btnCautaClient);
		setListenerCautaClient();

		radioClientIP = (RadioGroup) findViewById(R.id.radioClientIP);
		setListenerRadioClientIP();

		textNumeClient = (EditText) findViewById(R.id.textNumeClient);

		listClientiObiective = (ListView) findViewById(R.id.listClientiObiective);
		setListenerListClienti();

	}

	private void setListenerRadioClientIP() {
		radioClientIP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				listClientiObiective.setAdapter(new CautareClientiAdapter(getContext(), new ArrayList<BeanClient>()));
				textNumeClient.setText("");

				switch (checkedId) {

				case R.id.radioConstruct:
					tipClientIP = EnumTipClientIP.CONSTR;
					break;
				case R.id.radioNonConstruct:
					tipClientIP = EnumTipClientIP.NONCONSTR;
					break;

				}
			}

		});
	}

	private void setListenerCancelButton() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	private void setListenerCautaClient() {
		btnCautaClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ScreenUtils.hideSoftKeyboard(context, textNumeClient);
				if (isMeserias()) {
					cautaMeserias();
				} else if (isInstitPublica()) {
					cautaInstitPub();
				} else {
					cautaClient();
				}

			}
		});
	}

	private void setListenerListClienti() {
		listClientiObiective.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BeanClient client = (BeanClient) listClientiObiective.getItemAtPosition(arg2);

				if (UtilsUser.isUserIP())
					client.setTipClientIP(tipClientIP);

				if (listener != null) {
					listener.clientSelected(client);
					dismiss();
				}

			}
		});
	}

	public void setClientSelectedListener(CautaClientDialogListener listener) {
		this.listener = listener;
	}

	private void cautaClient() {
		String numeClient = textNumeClient.getText().toString().trim().replace('*', '%');

		String localUnitLog = UserInfo.getInstance().getUnitLog();

		if (isClientObiectivKA)
			localUnitLog = "NN10";

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("depart", "00");
		params.put("departAg", UserInfo.getInstance().getCodDepart());
		params.put("unitLog", localUnitLog);
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

		opClient.getListClienti(params);
	}

	private void cautaMeserias() {
		String numeClient = textNumeClient.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		opClient.getListMeseriasi(params);
	}

	private void cautaInstitPub() {
		String numeClient = textNumeClient.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("unitLog", UserInfo.getInstance().getUnitLog());
		params.put("tipUser", UserInfo.getInstance().getTipUserSap());
		params.put("tipClient", tipClientIP.toString());

		opClient.getClientiInstitPub(params);
	}

	private void afisListClienti(List<BeanClient> listClienti) {
		CautareClientiAdapter adapterClienti = new CautareClientiAdapter(getContext(), listClienti);
		listClientiObiective.setAdapter(adapterClienti);
	}

	public void operationComplete(EnumClienti methodName, Object result) {
		switch (methodName) {
		case GET_LISTA_CLIENTI:
		case GET_LISTA_MESERIASI:
		case GET_CLIENTI_INST_PUB:
			afisListClienti(opClient.deserializeListClienti((String) result));
			break;
		default:
			break;
		}

	}

	public boolean isMeserias() {
		return isMeserias;
	}

	public void setMeserias(boolean isMeserias) {
		this.isMeserias = isMeserias;
	}

	public boolean isClientObiectivKA() {
		return isClientObiectivKA;
	}

	public void setClientObiectivKA(boolean isClientObiectivKA) {
		this.isClientObiectivKA = isClientObiectivKA;
	}

	public boolean isInstitPublica() {
		return isInstitPublica;
	}

	public void setInstitPublica(boolean isInstitPublica) {
		this.isInstitPublica = isInstitPublica;

		if (isInstitPublica) {
			radioClientIP.setVisibility(View.VISIBLE);
			textNumeClient.setHint("Nume client sau CIF");
		} else
			textNumeClient.setHint("Nume client");

	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	@Override
	public void show() {

		super.show();

		if (numeClient != null && !numeClient.isEmpty()) {
			textNumeClient.setText(numeClient);
			cautaClient();
		}
	}

}
