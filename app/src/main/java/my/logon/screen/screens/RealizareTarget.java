package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterVenitTCF;
import my.logon.screen.adapters.AdapterVenitTPR;
import my.logon.screen.beans.VenitAG;
import my.logon.screen.dialogs.SelectAgentDialog;
import my.logon.screen.enums.EnumOperatiiVenit;
import my.logon.screen.listeners.OperatiiVenitListener;
import my.logon.screen.listeners.SelectAgentDialogListener;
import my.logon.screen.model.Agent;
import my.logon.screen.model.CalculVenit;
import my.logon.screen.model.CalculVenitImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsUser;

public class RealizareTarget extends Activity implements OperatiiVenitListener, SelectAgentDialogListener {

	private ActionBar actionBar;
	private CalculVenit calculVenit;
	private ListView listTPR, listTCF;
	private TextView textAntetTcf, textAntetTpr;
	private LinearLayout layoutAntetTcf, layoutAntetTpr;
	private int dataRap;
	private String agentCode;
	private String agentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.realizare_target);

		actionBar = getActionBar();
		actionBar.setTitle("Realizare target");
		actionBar.setDisplayHomeAsUpEnabled(true);

		dataRap = UtilsDates.getYearMonthDate(0);

		setupLayout();

		calculVenit = new CalculVenitImpl(this);
		calculVenit.setOperatiiVenitListener(RealizareTarget.this);

		if (!UtilsUser.isSD()) {
			agentCode = UserInfo.getInstance().getCod();
			actionBar.setTitle("Realizare target" + " " + UtilsDates.getMonthName(String.valueOf(dataRap)));
			getCalculVenit(agentCode, dataRap);
		}

	}

	private void setupLayout() {
		listTPR = (ListView) findViewById(R.id.listTPR);
		listTCF = (ListView) findViewById(R.id.listTCF);

		textAntetTcf = (TextView) findViewById(R.id.textAntetTcf);
		textAntetTpr = (TextView) findViewById(R.id.textAntetTpr);

		textAntetTcf.setVisibility(View.INVISIBLE);
		textAntetTpr.setVisibility(View.INVISIBLE);

		layoutAntetTcf = (LinearLayout) findViewById(R.id.layoutAntetTcf);
		layoutAntetTpr = (LinearLayout) findViewById(R.id.layoutAntetTpr);

		layoutAntetTcf.setVisibility(View.INVISIBLE);
		layoutAntetTpr.setVisibility(View.INVISIBLE);

	}

	private void getCalculVenit(String codAgent, int dataRap) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		params.put("departament", UserInfo.getInstance().getCodDepart());
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("dataRap", String.valueOf(dataRap));

		calculVenit.getVenitTPR_TCF(params);

	}

	private void afiseazaVenit(Object result) {
		VenitAG venitAg = calculVenit.getVenit(result);

		AdapterVenitTPR adapterTPR = new AdapterVenitTPR(this);
		adapterTPR.setListTPR(venitAg.getVenitTpr());

		AdapterVenitTCF adapterTCF = new AdapterVenitTCF(this);
		adapterTCF.setListTCF(venitAg.getVenitTcf());

		listTPR.setAdapter(adapterTPR);
		listTCF.setAdapter(adapterTCF);

		if (venitAg.getVenitTcf().size() > 0 || venitAg.getVenitTpr().size() > 0)
			setUIVisibility(true);
		else {
			setUIVisibility(false);
			Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_LONG).show();
		}

	}

	private void CreateMenu(Menu menu) {

		SubMenu fileMenu = menu.addSubMenu(0, Menu.NONE, 1, "Luna");
		fileMenu.add(0, 1, UtilsDates.getYearMonthDate(-1), UtilsDates.getDateMonthString(-1).toUpperCase());
		fileMenu.add(0, 2, UtilsDates.getYearMonthDate(0), UtilsDates.getDateMonthString(0).toUpperCase());

		fileMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		if (UtilsUser.isSD()) {
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

		case 1:
		case 2:
			dataRap = item.getOrder();
			getCalculVenit(agentCode, dataRap);
			break;

		case 3:
			showSelectAgentDialog();
			break;

		case android.R.id.home:
			returnToMainMenu();
			return true;

		}
		return false;
	}

	private void setUIVisibility(boolean isVisible) {
		if (isVisible) {
			textAntetTcf.setVisibility(View.VISIBLE);
			textAntetTpr.setVisibility(View.VISIBLE);

			layoutAntetTcf.setVisibility(View.VISIBLE);
			layoutAntetTpr.setVisibility(View.VISIBLE);
		} else {
			textAntetTcf.setVisibility(View.INVISIBLE);
			textAntetTpr.setVisibility(View.INVISIBLE);

			layoutAntetTcf.setVisibility(View.INVISIBLE);
			layoutAntetTpr.setVisibility(View.INVISIBLE);
		}
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

	public void operatiiVenitComplete(EnumOperatiiVenit numeComanda, Object result) {
		afiseazaVenit(result);
		setActionBarTitle();

	}

	private void setActionBarTitle() {
		if (UtilsUser.isSD() && agentName != null)
			actionBar.setTitle("Realizare target" + " " + agentName + " - " + UtilsDates.getMonthName(String.valueOf(dataRap)));
		else
			actionBar.setTitle("Realizare target" + " - " + UtilsDates.getMonthName(String.valueOf(dataRap)));

	}

	public void agentSelected(Agent agent) {
		agentCode = agent.getCod();
		agentName = agent.getNume();
		getCalculVenit(agent.getCod(), dataRap);

	}

}
