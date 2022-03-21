package my.logon.screen.screens;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.listeners.DialogObiectiveKAListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.adapters.AdapterObiectiveAfisare;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import my.logon.screen.beans.BeanObiectivAfisare;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.beans.BeanStadiuObiectiv;
import my.logon.screen.beans.BeanUrmarireEveniment;
import my.logon.screen.dialogs.OptiuniObiectKaDialog;
import my.logon.screen.enums.EnumDepartExtra;
import my.logon.screen.enums.EnumDepartFinisaje;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.enums.EnumUrmarireObiective;

public class AfiseazaObiectiveKA extends Activity implements DialogObiectiveKAListener, ObiectiveListener {

	private OptiuniObiectKaDialog optiuniDialog;
	private Spinner spinnerObiective;
	private OperatiiObiective operatiiObiective;
	private TextView detaliiText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_afis_ob_ka);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Afisare obiective KA");
		actionBar.setDisplayHomeAsUpEnabled(true);

		optiuniDialog = new OptiuniObiectKaDialog(this);
		optiuniDialog.setDialogListener(this);

		operatiiObiective = new OperatiiObiective(this);
		operatiiObiective.setObiectiveListener(this);

		setupLayout();

	}

	private void setupLayout() {
		spinnerObiective = (Spinner) findViewById(R.id.spinnerObiective);
		spinnerObiective.setVisibility(View.INVISIBLE);
		setSpinnerObiectiveListener();

		detaliiText = (TextView) findViewById(R.id.detaliiObiectiv);
		detaliiText.setText("");
	}

	private void setSpinnerObiectiveListener() {
		spinnerObiective.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					BeanObiectivAfisare idObiectiv = (BeanObiectivAfisare) parent.getAdapter().getItem(position);
					getDetaliiObiectiv(idObiectiv);

				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void getDetaliiObiectiv(BeanObiectivAfisare idObiectiv) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", idObiectiv.getId());
		operatiiObiective.getDetaliiObiectiv(params);
	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Lista");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			optiuniDialog.show();
			break;

		case 1:
			addMapFragment();
			break;

		case android.R.id.home:
			returnHome();
			break;

		}

		return super.onOptionsItemSelected(item);

	}

	private void addMapFragment() {

	}

	private void displayListObiective(List<BeanObiectivAfisare> listObiective) {

		BeanObiectivAfisare dummyOb = new BeanObiectivAfisare();

		dummyOb.setNume("Selectati un obiectiv");

		dummyOb.setBeneficiar(" ");
		dummyOb.setCodStatus("-1");
		dummyOb.setData("");
		dummyOb.setId(" ");

		listObiective.add(0, dummyOb);

		AdapterObiectiveAfisare adapterOb = new AdapterObiectiveAfisare(getApplicationContext(), listObiective);
		spinnerObiective.setAdapter(adapterOb);

		spinnerObiective.setVisibility(View.VISIBLE);
		detaliiText.setText("");

	}

	private void displayDetaliiOb(BeanObiectiveGenerale detaliiObiectiv) {

		StringBuilder strDetalii = new StringBuilder();
		String departFinisaje = "", client = "";

		String adresa = detaliiObiectiv.getAdresaObiectiv().getNumeJudet() + "/" + detaliiObiectiv.getAdresaObiectiv().getOras();
		String strada = detaliiObiectiv.getAdresaObiectiv().getStrada() != null ? "/" + detaliiObiectiv.getAdresaObiectiv().getStrada() : "";

		adresa += strada;

		strDetalii.append("Adresa :" + UtilsFormatting.addSpace("Adresa :", 30) + adresa + "\n");
		strDetalii.append("Beneficiar :" + UtilsFormatting.addSpace("Beneficiar :", 30) + detaliiObiectiv.getNumeBeneficiar() + "\n");
		strDetalii.append("Antreprenor general :" + UtilsFormatting.addSpace("Antreprenor general :", 30) + detaliiObiectiv.getNumeAntreprenorGeneral() + "\n");
		strDetalii.append("Categorie :" + UtilsFormatting.addSpace("Categorie :", 30) + detaliiObiectiv.getCategorieObiectiv().getNumeCategorie() + "\n");
		strDetalii.append("Valoare :" + UtilsFormatting.addSpace("Valoare :", 30)
				+ UtilsFormatting.format2Decimals(Double.parseDouble(detaliiObiectiv.getValoareObiectiv()), true) + " RON \n");

		strDetalii.append("\n\n");
		strDetalii.append("Stadii:" + "\n");

		List<BeanStadiuObiectiv> listStadii = detaliiObiectiv.getStadiiDepart();

		List<BeanObiectiveConstructori> listConstructori = detaliiObiectiv.getListConstructori();
		List<BeanUrmarireEveniment> listEvenimente = detaliiObiectiv.getListEvenimente();

		Iterator<BeanUrmarireEveniment> iteratorEv = null;

		for (BeanStadiuObiectiv stadiu : listStadii) {

			client = "";

			if (stadiu.getCodDepart().equals("04"))
				departFinisaje = "Fundatie si suprastructura";
			else
				departFinisaje = EnumDepartFinisaje.getNumeDepart(stadiu.getCodDepart());

			strDetalii.append("\n");
			strDetalii.append(departFinisaje + " :" + UtilsFormatting.addSpace(departFinisaje, 35) + stadiu.getNumeStadiu() + "\n");

			for (BeanObiectiveConstructori constructor : listConstructori) {

				if (stadiu.getCodDepart().equals("00")) {

					if (constructor.getCodDepart().equals("03") || constructor.getCodDepart().equals("06") || constructor.getCodDepart().equals("07")) {

						client = "Client :" + UtilsFormatting.addSpace("Client :", 37) + constructor.getNumeClient() + " ("
								+ EnumDepartExtra.getNumeDepart(constructor.getCodDepart()) + ")  \n";

					}

				} else if (stadiu.getCodDepart().equals("04")) {

					if (constructor.getCodDepart().equals("04")) {

						client = "Client :" + UtilsFormatting.addSpace("Client :", 37) + constructor.getNumeClient() + " \n";
					}

				} else {

					if (constructor.getCodDepart().equals(stadiu.getCodDepart())) {

						client = "Client :" + UtilsFormatting.addSpace("Client :", 37) + constructor.getNumeClient() + " \n";
					}

				}

				if (!client.equals("")) {
					strDetalii.append(client);
					client = "";
				}

				iteratorEv = listEvenimente.iterator();
				while (iteratorEv.hasNext()) {
					BeanUrmarireEveniment eveniment = iteratorEv.next();
					if (eveniment.getCodDepart().equals(stadiu.getCodDepart()) && eveniment.getCodClient().equals(constructor.getCodClient())) {
						strDetalii.append(UtilsFormatting.addSpace("", 43) + EnumUrmarireObiective.getNumeEveniment(eveniment.getIdEveniment()) + " :"
								+ UtilsFormatting.addSpace(EnumUrmarireObiective.getNumeEveniment(eveniment.getIdEveniment()), 10) + eveniment.getData()
								+ " Obs: " + eveniment.getObservatii() + " \n");

						iteratorEv.remove();

					}

				}

			}

		}

		detaliiText.setText(strDetalii.toString());

	}

	private void returnHome() {
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(this, MainMenu.class);
		startActivity(nextScreen);

		finish();
	}

	public void selectionComplete(List<BeanObiectivAfisare> listObiective) {
		displayListObiective(listObiective);

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case GET_DETALII_OBIECTIV:
			displayDetaliiOb(operatiiObiective.deserializeDetaliiObiectiv((String) result));

			break;
		default:
			break;
		}

	}

}
