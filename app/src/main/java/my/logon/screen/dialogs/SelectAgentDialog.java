package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.OperatiiAgentListener;
import my.logon.screen.listeners.SelectAgentDialogListener;
import my.logon.screen.R;
import my.logon.screen.model.Agent;
import my.logon.screen.model.OperatiiAgent;
import my.logon.screen.model.OperatiiFiliala;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsUser;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;


public class SelectAgentDialog extends Dialog implements OperatiiAgentListener {

	private ImageButton btnCancel;
	private Context context;
	private OperatiiAgent opAgent;
	private ListView listViewAgenti;
	private SelectAgentDialogListener listener;
	private Spinner spinnerFiliale;

	public SelectAgentDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.select_agent_dialog);
		setTitle("Selectie agent");
		setCancelable(true);

		setUpLayout();

		opAgent = OperatiiAgent.getInstance();
		opAgent.setOperatiiAgentListener(this);

		if (!UtilsUser.isDV())
			opAgent.getListaAgenti(UserInfo.getInstance().getUnitLog(), getUserDepart(), context, false, getTipAgent());

	}

	public void showDialog() {
		this.show();
	}

	private String getUserDepart() {
		if (UserInfo.getInstance().getTipUserSap().equals("SM") || UtilsUser.isSMNou() || UserInfo.getInstance().getTipUserSap().equals("SDCVA")
				|| UserInfo.getInstance().getTipUserSap().equals("CVO") || UtilsUser.isSDIP())
			return "11";
		else if (UserInfo.getInstance().getTipAcces().equals("32"))
			return "10";
		else if (UtilsUser.isDV() && UserInfo.getInstance().getInitDivizie().equals("11"))
			return "11";
		else
			return UserInfo.getInstance().getCodDepart();
	}

	private String getTipAgent() {
		String tipAgent = null;

		if (UtilsUser.isSMNou())
			tipAgent = UtilsUser.getTipSMNou();
		else if (UserInfo.getInstance().getTipUserSap().equals("SDCVA"))
			tipAgent = UserInfo.getInstance().getTipUserSap();
		else if (UserInfo.getInstance().getTipUserSap().equals("CVO"))
			tipAgent = "CVO";
		else if (UtilsUser.isSDIP())
			tipAgent = "SDIP";
		else if (UtilsUser.isDV())
			tipAgent = "DV";

		return tipAgent;
	}

	private void setUpLayout() {

		if (UtilsUser.isDV()) {

			spinnerFiliale = (Spinner) findViewById(R.id.spinnerFiliale);

			ArrayList<HashMap<String, String>> listFiliale = OperatiiFiliala.getInstance().getListToateFiliale();

			SimpleAdapter adapterFiliale = new SimpleAdapter(context, listFiliale, R.layout.rowlayoutagenti, new String[] { "numeFiliala", "codFiliala" },
					new int[] { R.id.textNumeAgent, R.id.textCodAgent });

			spinnerFiliale.setAdapter(adapterFiliale);
			spinnerFiliale.setVisibility(View.VISIBLE);

			setSpinnerFilialaListener();
		}

		listViewAgenti = (ListView) findViewById(R.id.listViewAgenti);
		setListViewAgentiListener();

		btnCancel = (ImageButton) findViewById(R.id.btnCancel);
		setCancelButtonListener();

	}

	private void setSpinnerFilialaListener() {

		spinnerFiliale.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> artFiliala = (HashMap<String, String>) arg0.getSelectedItem();
				if (!artFiliala.get("codFiliala").trim().isEmpty())
					opAgent.getListaAgenti(artFiliala.get("codFiliala"), getUserDepart(), context, false, getTipAgent());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				 

			}
		});
	}

	private void setListViewAgentiListener() {

		listViewAgenti.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Agent agent = (Agent) parent.getAdapter().getItem(position);

				if (listener != null)
					listener.agentSelected(agent);

				dismiss();

			}
		});

	}

	private void setCancelButtonListener() {
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dismiss();
			}
		});
	}

	@Override
	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		populateListViewAgenti(opAgent.getListObjAgenti());

	}

	public void setAgentDialogListener(SelectAgentDialogListener listener) {
		this.listener = listener;
	}

	private void populateListViewAgenti(List<Agent> listObjAgenti) {
		listObjAgenti.remove(0);
		ArrayAdapter<Agent> adapter = new ArrayAdapter<Agent>(context, android.R.layout.simple_list_item_1, listObjAgenti);
		listViewAgenti.setAdapter(adapter);

	}
}
