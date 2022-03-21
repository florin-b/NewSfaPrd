package my.logon.screen.screens;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.listeners.SelectAgentDialogListener;
import my.logon.screen.model.Agent;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsUser;
import my.logon.screen.adapters.ClientiAlocatiAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import my.logon.screen.beans.ClientAlocat;
import my.logon.screen.dialogs.SelectAgentDialog;
import my.logon.screen.enums.EnumClienti;

public class ClientiAlocati extends Activity implements SelectAgentDialogListener, OperatiiClientListener {

	private ActionBar actionBar;
	private String agentCode;
	private String agentName;

	private ListView listViewClienti;
	private LinearLayout mainLayout;

	private OperatiiClient opClienti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.clienti_alocati);

		actionBar = getActionBar();
		actionBar.setTitle("Clienti alocati");
		actionBar.setDisplayHomeAsUpEnabled(true);

		opClienti = new OperatiiClient(this);
		opClienti.setOperatiiClientListener(ClientiAlocati.this);

		if (!UtilsUser.isSD() && !UtilsUser.isUserSDKA() && !UtilsUser.isUserSK()) {
			agentCode = UserInfo.getInstance().getCod();
			getClientiAlocati(agentCode, UserInfo.getInstance().getUnitLog());

		}

		mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
		listViewClienti = (ListView) findViewById(R.id.listClienti);


	}

	public void getClientiAlocati(String codAgent, String filiala) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		params.put("filiala", filiala);
		opClienti.getClientiAlocati(params);
	}

	private void CreateMenu(Menu menu) {

		if (UtilsUser.isSD() || UtilsUser.isUserSDKA() || UtilsUser.isUserSK()) {
			MenuItem mnu2 = menu.add(0, 3, 1, "Agenti");
			mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		CreateMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 3:
			showSelectAgentDialog();
			break;

		case android.R.id.home:
			returnToMainMenu();
			return true;

		}
		return false;
	}

	private void showSelectAgentDialog() {
		SelectAgentDialog selectAgent = new SelectAgentDialog(this);
		selectAgent.setAgentDialogListener(this);
		selectAgent.show();
	}

	@Override
	public void onBackPressed() {
		returnToMainMenu();
		return;
	}

	private void returnToMainMenu() {
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	private void showListClienti(String result) {

		List<ClientAlocat> listClienti = opClienti.deserializeClientiAlocati(result);

		if (!listClienti.isEmpty()) {
			mainLayout.setVisibility(View.VISIBLE);
			ClientiAlocatiAdapter clientiAdapter = new ClientiAlocatiAdapter(listClienti, getApplicationContext());
			listViewClienti.setAdapter(clientiAdapter);
		} else {
			mainLayout.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
		}

	}

	public void agentSelected(Agent agent) {
		agentCode = agent.getCod();
		agentName = agent.getNume();
		actionBar.setTitle("Clienti alocati " + " " + agentName);
		getClientiAlocati(agentCode, UserInfo.getInstance().getUnitLog());

	}

	@Override
	public void operationComplete(EnumClienti methodName, Object result) {
		showListClienti((String) result);

	}

}
