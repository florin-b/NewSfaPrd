package my.logon.screen.screens;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.ClientiFacturatiAdapter;
import my.logon.screen.beans.BeanClientiFacturati;
import my.logon.screen.dialogs.AfisareListaFacturiDialog;
import my.logon.screen.enums.EnumClientiFacturati;
import my.logon.screen.listeners.ClientiFacturatiListener;
import my.logon.screen.model.OperatiiClientiFacturati;
import my.logon.screen.model.UserInfo;

public class ClientiFacturati extends FragmentActivity implements ClientiFacturatiAdapter.showDetaliiFacturiInter, ClientiFacturatiListener

{

	private ActionBar actionBar;
	String codAgent;
	String denumireClient;
	TextView titleLuna1, titleLuna2, titleLuna3, titleLuna4, titleLuna5, titleLuna6, titleLuna7;

	ListView listClientiFacturati;
	List<BeanClientiFacturati> listClienti = new ArrayList<BeanClientiFacturati>();
	Context context = ClientiFacturati.this;
	private OperatiiClientiFacturati operatiiClientFacturati;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.clientifacturati);

		codAgent = UserInfo.getInstance().getCod();

		actionBar = getActionBar();
		actionBar.setTitle("Clienti facturati");
		actionBar.setDisplayHomeAsUpEnabled(true);

		operatiiClientFacturati = new OperatiiClientiFacturati(this);
		operatiiClientFacturati.setClientiFacturatiListener(this);

		listClientiFacturati = (ListView) findViewById(R.id.lvClientiFacturati);
		titleLuna1 = (TextView) findViewById(R.id.txtTitleLuna1);
		titleLuna1.setText(afisareLunara(0));

		titleLuna2 = (TextView) findViewById(R.id.txtTitleLuna2);
		titleLuna2.setText(afisareLunara(-1));

		titleLuna3 = (TextView) findViewById(R.id.txtTitleLuna3);
		titleLuna3.setText(afisareLunara(-2));

		titleLuna4 = (TextView) findViewById(R.id.txtTitleLuna4);
		titleLuna4.setText(afisareLunara(-3));

		titleLuna5 = (TextView) findViewById(R.id.txtTitleLuna5);
		titleLuna5.setText(afisareLunara(-4));

		titleLuna6 = (TextView) findViewById(R.id.txtTitleLuna6);
		titleLuna6.setText(afisareLunara(-5));

		titleLuna7 = (TextView) findViewById(R.id.txtTitleLuna7);
		titleLuna7.setText(afisareLunara(-6));

		performGetRaportData();

	}

	public String afisareLunara(int lunaScadere) {
		SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, lunaScadere);
		String lunaAn = df.format(cal.getTime());
		return lunaAn;
	}

	@Override
	public void onBackPressed() {
		returnToMainMenu();
		return;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			returnToMainMenu();
		}
		return true;
	}

	private void returnToMainMenu() {
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
	}

	private void fillClientiFacturati(String reportData) {

		operatiiClientFacturati = new OperatiiClientiFacturati(context);
		listClienti = operatiiClientFacturati.deserializeClientiFacturatiKA(reportData);
		if (listClienti.size() > 0) {
			listClientiFacturati.setAdapter(new ClientiFacturatiAdapter(context, listClienti));

		} else {
			Toast.makeText(context, "Nu exista date!", Toast.LENGTH_LONG).show();
		}
	}

	private void performGetRaportData() {

		try {
			HashMap<String, String> params = new HashMap<String, String>();

			params.put("codAgent", UserInfo.getInstance().getCod());

			operatiiClientFacturati.getListClientiFacturatiKA(params);

		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void showDetaliiFacturi(CharSequence codClient, CharSequence numeClient, String lunaSelectata) {

		AfisareListaFacturiDialog dialogListaFacturi = new AfisareListaFacturiDialog(this, UserInfo.getInstance().getCod(), codClient, numeClient,
				lunaSelectata);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("codClient", codClient.toString());
		params.put("data", lunaSelectata);
		dialogListaFacturi.showDialog();
		operatiiClientFacturati.setClientiFacturatiListener(dialogListaFacturi);
		operatiiClientFacturati.getListFacturiClient(params);

	}

	@Override
	public void operationClientiFacturatiComplete(EnumClientiFacturati numeWebService, Object result) {
		fillClientiFacturati((String) result);

	}

}
