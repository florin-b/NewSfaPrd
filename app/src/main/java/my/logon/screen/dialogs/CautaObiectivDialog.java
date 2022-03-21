package my.logon.screen.dialogs;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.CautaClientDialogListener;
import my.logon.screen.listeners.CautaObiectivListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.R;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.ScreenUtils;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.adapters.CautareObiectivConsAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import my.logon.screen.beans.ObiectivConsilier;
import my.logon.screen.enums.EnumOperatiiObiective;

public class CautaObiectivDialog extends Dialog implements ObiectiveListener {

	private ImageButton cancelButton;
	private Button btnCautaClient;
	private EditText textNumeClient;
	private OperatiiObiective opObiective;
	private ListView listViewObiective;
	private CautaObiectivListener listener;
	private Context context;
	private boolean isMeserias;
	private boolean isClientObiectivKA;
	private boolean isInstitPublica;

	public CautaObiectivDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.select_client_obiective_dialog);
		setTitle("Selectie obiectiv");
		setCancelable(true);

		opObiective = new OperatiiObiective(getContext());
		opObiective.setObiectiveListener(this);

		setUpLayout();

	}

	private void setUpLayout() {

		cancelButton = (ImageButton) findViewById(R.id.btnCancel);
		setListenerCancelButton();

		btnCautaClient = (Button) findViewById(R.id.btnCautaClient);
		setListenerCautaClient();

		textNumeClient = (EditText) findViewById(R.id.textNumeClient);
		textNumeClient.setHint("Nume obiectiv");
		listViewObiective = (ListView) findViewById(R.id.listClientiObiective);
		setListenerListClienti();

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
				cautaObiectiv();

			}
		});
	}

	private void setListenerListClienti() {
		listViewObiective.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ObiectivConsilier obiectiv = (ObiectivConsilier) listViewObiective.getItemAtPosition(arg2);
				if (listener != null) {
					listener.obiectivSelected(obiectiv);
					dismiss();
				}

			}
		});
	}

	public void setObiectivListener(CautaObiectivListener listener) {
		this.listener = listener;
	}

	private void cautaObiectiv() {

		String numeObiectiv = textNumeClient.getText().toString().trim().replace('*', '%');

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codConsilier", UserInfo.getInstance().getCod());
		params.put("numeObiectiv", numeObiectiv);

		opObiective.getObiectiveConsilieri(params);

	}

	private void afisListObiective(List<ObiectivConsilier> listObiective) {

		CautareObiectivConsAdapter adapterObiectiv = new CautareObiectivConsAdapter(getContext(), listObiective);
		listViewObiective.setAdapter(adapterObiectiv);

	}


	@Override
	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_OBIECTIVE_CONSILIERI:
			afisListObiective(opObiective.deserializeObiectiveConsilieri((String) result));
			break;
		default:
			break;
		}

	}

}
