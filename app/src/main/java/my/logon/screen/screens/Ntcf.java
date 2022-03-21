package my.logon.screen.screens;

import java.util.HashMap;
import java.util.Map.Entry;

import my.logon.screen.listeners.OperatiiVenitListener;
import my.logon.screen.listeners.SelectAgentDialogListener;
import my.logon.screen.model.Agent;
import my.logon.screen.model.CalculVenit;
import my.logon.screen.model.CalculVenitImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsUser;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import my.logon.screen.beans.VenituriNTCF;
import my.logon.screen.dialogs.SelectAgentDialog;
import my.logon.screen.enums.EnumOperatiiVenit;

public class Ntcf extends Activity implements OperatiiVenitListener, SelectAgentDialogListener {

	private ActionBar actionBar;
	private CalculVenit calculVenit;
	private int dataRap;
	private String agentCode;
	private String agentName;

	private TextView cl_fact_aa_ian, cl_fact_aa_feb, cl_fact_aa_mar, cl_fact_aa_apr, cl_fact_aa_mai, cl_fact_aa_iun, cl_fact_aa_iul, cl_fact_aa_aug,
			cl_fact_aa_sep, cl_fact_aa_oct, cl_fact_aa_nov, cl_fact_aa_dec;

	private TextView target_curent_ian, target_curent_feb, target_curent_mar, target_curent_apr, target_curent_mai, target_curent_iun,
			target_curent_iul, target_curent_aug, target_curent_sep, target_curent_oct, target_curent_nov, target_curent_dec;

	private TextView client_fact_curent_ian, client_fact_curent_feb, client_fact_curent_mar, client_fact_curent_apr, client_fact_curent_mai,
			client_fact_curent_iun, client_fact_curent_iul, client_fact_curent_aug, client_fact_curent_sep, client_fact_curent_oct,
			client_fact_curent_nov, client_fact_curent_dec;

	private TextView coef_afect_ian, coef_afect_feb, coef_afect_mar, coef_afect_apr, coef_afect_mai, coef_afect_iun, coef_afect_iul, coef_afect_aug,
			coef_afect_sep, coef_afect_oct, coef_afect_nov, coef_afect_dec;

	private TableLayout table_ntcf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.realizare_ntcf);

		actionBar = getActionBar();
		actionBar.setTitle("Realizare target");
		actionBar.setDisplayHomeAsUpEnabled(true);

		dataRap = UtilsDates.getYearMonthDate(0);

		setupLayout();

		calculVenit = new CalculVenitImpl(this);
		calculVenit.setOperatiiVenitListener(Ntcf.this);

		if (!UtilsUser.isSD()) {
			agentCode = UserInfo.getInstance().getCod();
			getDateNTCF(agentCode);

		}

	}

	private void setupLayout() {

		table_ntcf = (TableLayout) findViewById(R.id.table_ntcf);

		cl_fact_aa_ian = (TextView) findViewById(R.id.cl_fact_aa_ian);
		cl_fact_aa_feb = (TextView) findViewById(R.id.cl_fact_aa_feb);
		cl_fact_aa_mar = (TextView) findViewById(R.id.cl_fact_aa_mar);
		cl_fact_aa_apr = (TextView) findViewById(R.id.cl_fact_aa_apr);
		cl_fact_aa_mai = (TextView) findViewById(R.id.cl_fact_aa_mai);
		cl_fact_aa_iun = (TextView) findViewById(R.id.cl_fact_aa_iun);
		cl_fact_aa_iul = (TextView) findViewById(R.id.cl_fact_aa_iul);
		cl_fact_aa_aug = (TextView) findViewById(R.id.cl_fact_aa_aug);
		cl_fact_aa_sep = (TextView) findViewById(R.id.cl_fact_aa_sep);
		cl_fact_aa_oct = (TextView) findViewById(R.id.cl_fact_aa_oct);
		cl_fact_aa_nov = (TextView) findViewById(R.id.cl_fact_aa_nov);
		cl_fact_aa_dec = (TextView) findViewById(R.id.cl_fact_aa_dec);

		target_curent_ian = (TextView) findViewById(R.id.target_curent_ian);
		target_curent_feb = (TextView) findViewById(R.id.target_curent_feb);
		target_curent_mar = (TextView) findViewById(R.id.target_curent_mar);
		target_curent_apr = (TextView) findViewById(R.id.target_curent_apr);
		target_curent_mai = (TextView) findViewById(R.id.target_curent_mai);
		target_curent_iun = (TextView) findViewById(R.id.target_curent_iun);
		target_curent_iul = (TextView) findViewById(R.id.target_curent_iul);
		target_curent_aug = (TextView) findViewById(R.id.target_curent_aug);
		target_curent_sep = (TextView) findViewById(R.id.target_curent_sep);
		target_curent_oct = (TextView) findViewById(R.id.target_curent_oct);
		target_curent_nov = (TextView) findViewById(R.id.target_curent_nov);
		target_curent_dec = (TextView) findViewById(R.id.target_curent_dec);

		client_fact_curent_ian = (TextView) findViewById(R.id.client_fact_curent_ian);
		client_fact_curent_feb = (TextView) findViewById(R.id.client_fact_curent_feb);
		client_fact_curent_mar = (TextView) findViewById(R.id.client_fact_curent_mar);
		client_fact_curent_apr = (TextView) findViewById(R.id.client_fact_curent_apr);
		client_fact_curent_mai = (TextView) findViewById(R.id.client_fact_curent_mai);
		client_fact_curent_iun = (TextView) findViewById(R.id.client_fact_curent_iun);
		client_fact_curent_iul = (TextView) findViewById(R.id.client_fact_curent_iul);
		client_fact_curent_aug = (TextView) findViewById(R.id.client_fact_curent_aug);
		client_fact_curent_sep = (TextView) findViewById(R.id.client_fact_curent_sep);
		client_fact_curent_oct = (TextView) findViewById(R.id.client_fact_curent_oct);
		client_fact_curent_nov = (TextView) findViewById(R.id.client_fact_curent_nov);
		client_fact_curent_dec = (TextView) findViewById(R.id.client_fact_curent_dec);

		coef_afect_ian = (TextView) findViewById(R.id.coef_afect_ian);
		coef_afect_feb = (TextView) findViewById(R.id.coef_afect_feb);
		coef_afect_mar = (TextView) findViewById(R.id.coef_afect_mar);
		coef_afect_apr = (TextView) findViewById(R.id.coef_afect_apr);
		coef_afect_mai = (TextView) findViewById(R.id.coef_afect_mai);
		coef_afect_iun = (TextView) findViewById(R.id.coef_afect_iun);
		coef_afect_iul = (TextView) findViewById(R.id.coef_afect_iul);
		coef_afect_aug = (TextView) findViewById(R.id.coef_afect_aug);
		coef_afect_sep = (TextView) findViewById(R.id.coef_afect_sep);
		coef_afect_oct = (TextView) findViewById(R.id.coef_afect_oct);
		coef_afect_nov = (TextView) findViewById(R.id.coef_afect_nov);
		coef_afect_dec = (TextView) findViewById(R.id.coef_afect_dec);

	}

	private void getCalculVenit(String codAgent, int dataRap) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		params.put("departament", UserInfo.getInstance().getCodDepart());
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("dataRap", String.valueOf(dataRap));

		calculVenit.getVenitTPR_TCF(params);

	}

	public void getDateNTCF(String codAgent) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", codAgent);
		calculVenit.getVenitNTCF(params);

	}

	private void CreateMenu(Menu menu) {

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

	private void afiseazaDateNTCF(VenituriNTCF venituriNTCF) {

		table_ntcf.setVisibility(View.VISIBLE);

		for (Entry<String, Object> clAnterior : venituriNTCF.getClientFactAnAnterior().entrySet()) {

			if (clAnterior.getKey().equals("1")) {
				cl_fact_aa_ian.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("2")) {
				cl_fact_aa_feb.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("3")) {
				cl_fact_aa_mar.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("4")) {
				cl_fact_aa_apr.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("5")) {
				cl_fact_aa_mai.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("6")) {
				cl_fact_aa_iun.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("7")) {
				cl_fact_aa_iul.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("8")) {
				cl_fact_aa_aug.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("9")) {
				cl_fact_aa_sep.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("10")) {
				cl_fact_aa_oct.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("11")) {
				cl_fact_aa_nov.setText(clAnterior.getValue().toString());
			} else if (clAnterior.getKey().equals("12")) {
				cl_fact_aa_dec.setText(clAnterior.getValue().toString());
			}

		}

		for (Entry<String, Object> targetCurent : venituriNTCF.getTargetAnCurent().entrySet()) {

			if (targetCurent.getKey().equals("1")) {
				target_curent_ian.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("2")) {
				target_curent_feb.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("3")) {
				target_curent_mar.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("4")) {
				target_curent_apr.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("5")) {
				target_curent_mai.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("6")) {
				target_curent_iun.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("7")) {
				target_curent_iul.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("8")) {
				target_curent_aug.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("9")) {
				target_curent_sep.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("10")) {
				target_curent_oct.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("11")) {
				target_curent_nov.setText(targetCurent.getValue().toString());
			} else if (targetCurent.getKey().equals("12")) {
				target_curent_dec.setText(targetCurent.getValue().toString());
			}

		}

		for (Entry<String, Object> clAnterior : venituriNTCF.getClientFactAnCurent().entrySet()) {

			if (clAnterior.getKey().equals("1"))
				client_fact_curent_ian.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("2"))
				client_fact_curent_feb.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("3"))
				client_fact_curent_mar.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("4"))
				client_fact_curent_apr.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("5"))
				client_fact_curent_mai.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("6"))
				client_fact_curent_iun.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("7"))
				client_fact_curent_iul.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("8"))
				client_fact_curent_aug.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("9"))
				client_fact_curent_sep.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("10"))
				client_fact_curent_oct.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("11"))
				client_fact_curent_nov.setText(clAnterior.getValue().toString());
			else if (clAnterior.getKey().equals("12"))
				client_fact_curent_dec.setText(clAnterior.getValue().toString());
		}

		for (Entry<String, Object> cAfectare : venituriNTCF.getCoefAfectare().entrySet()) {

			if (cAfectare.getKey().equals("1"))
				coef_afect_ian.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("2"))
				coef_afect_feb.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("3"))
				coef_afect_mar.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("4"))
				coef_afect_apr.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("5"))
				coef_afect_mai.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("6"))
				coef_afect_iun.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("7"))
				coef_afect_iul.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("8"))
				coef_afect_aug.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("9"))
				coef_afect_sep.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("10"))
				coef_afect_oct.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("11"))
				coef_afect_nov.setText(cAfectare.getValue().toString());
			else if (cAfectare.getKey().equals("12"))
				coef_afect_dec.setText(cAfectare.getValue().toString());
		}

	}

	public void operatiiVenitComplete(EnumOperatiiVenit numeComanda, Object result) {

		switch (numeComanda) {
		case GET_VENIT_NTCF:
			afiseazaDateNTCF(calculVenit.deserializeDateNTCF(result));
			break;
		default:
			break;

		}

	}

	public void agentSelected(Agent agent) {
		agentCode = agent.getCod();
		agentName = agent.getNume();
		actionBar.setTitle("Ntcf" + " " + agentName);
		getDateNTCF(agentCode);

	}

}
