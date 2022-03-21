/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.GenericDialogListener;
import my.logon.screen.listeners.OperatiiReturListener;
import my.logon.screen.model.OperatiiReturMarfa;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.adapters.ArticoleReturAdapter;
import my.logon.screen.adapters.ComandaReturAfisAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.BeanComandaRetur;
import my.logon.screen.beans.BeanComandaReturAfis;
import my.logon.screen.dialogs.GenericAlertDialog;
import my.logon.screen.enums.EnumDaNuOpt;
import my.logon.screen.enums.EnumDialogConstraints;
import my.logon.screen.enums.EnumRetur;

public class AfisareReturMarfa extends Activity implements OperatiiReturListener, GenericDialogListener {

	private Spinner spinnerComenziRetur;
	public static String selectedCmd = "", selectedCmdSap = "", clpDeSters = "", strCodStatusCmd = "";
	private ListView listArticoleComandaRetur;
	private TextView textStrada, textPersContact, textTelefon, textOras, textJudet, textDataLivrare, textTipTransport;
	private LinearLayout layoutDateComanda;

	private static int intervalAfisare = 0, stare = 0;
	private LinearLayout aprobareLayout;
	private Button aprobaBtn, respingeBtn;
	private BeanComandaReturAfis comandaRetur;

	OperatiiReturMarfa operatiiRetur;

	private enum EnumOperatieComanda {
		APROBA("2"), RESPINGE("3");

		String tipOperatie;

		EnumOperatieComanda(String tipOperatie) {
			this.tipOperatie = tipOperatie;
		}

		public String getTipOperatie() {
			return tipOperatie;
		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afisare_retur_marfa);

		operatiiRetur = new OperatiiReturMarfa(this);
		operatiiRetur.setOperatiiReturListener(this);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Comenzi retur");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerComenziRetur = (Spinner) findViewById(R.id.spinnerComenziRetur);
		setSpinnerComenziReturListener();

		spinnerComenziRetur.setVisibility(View.INVISIBLE);
		listArticoleComandaRetur = (ListView) findViewById(R.id.listArticoleComandaRetur);
		listArticoleComandaRetur.setVisibility(View.INVISIBLE);

		textStrada = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textOras = (TextView) findViewById(R.id.textOras);
		textJudet = (TextView) findViewById(R.id.textJudet);
		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);
		textTipTransport = (TextView) findViewById(R.id.textTransport);

		aprobareLayout = (LinearLayout) findViewById(R.id.aprobareLayout);
		aprobareLayout.setVisibility(View.INVISIBLE);

		if (UserInfo.getInstance().getTipUserSap().equals("SD"))
			setUpAprobareLayout();

		layoutDateComanda = (LinearLayout) findViewById(R.id.layoutDateComanda);
		layoutDateComanda.setVisibility(View.INVISIBLE);

		getListComenziRetur(intervalAfisare, stare);

	}

	private void setUpAprobareLayout() {
		aprobaBtn = (Button) findViewById(R.id.aprobaBtn);
		respingeBtn = (Button) findViewById(R.id.respingeBtn);

		setAprobaCmdListener();
		setRespingeCmdListener();

	}

	private void setAprobaCmdListener() {
		aprobaBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showConfirmationDialog(EnumDialogConstraints.APROBARE_COMANDA);
			}
		});

	}

	private void setRespingeCmdListener() {
		respingeBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showConfirmationDialog(EnumDialogConstraints.RESPINGERE_COMANDA);
			}
		});
	}

	private void showConfirmationDialog(EnumDialogConstraints dialogConstraints) {

		String title = "Confirmare";
		String alertMessage = "";

		switch (dialogConstraints) {
		case APROBARE_COMANDA:
			alertMessage = "Aprobati comanda?";
			break;
		case RESPINGERE_COMANDA:
			alertMessage = "Respingeti comanda?";
			break;
		default:
			break;
		}

		GenericAlertDialog alertDialog = new GenericAlertDialog(AfisareReturMarfa.this, title, alertMessage, dialogConstraints);
		alertDialog.setGenericDialogListener(AfisareReturMarfa.this);
		alertDialog.showAlertDialog();

	}

	private void setSpinnerComenziReturListener() {
		spinnerComenziRetur.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				comandaRetur = (BeanComandaReturAfis) parent.getAdapter().getItem(position);
				getArticoleComanda(comandaRetur.getId());

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Interval");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		if (UserInfo.getInstance().getTipUserSap().equals("SD")) {
			MenuItem mnu2 = menu.add(0, 1, 1, "Tip");
			mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			final CharSequence[] constrCmd = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };
			final AlertDialog.Builder builderConstr = new AlertDialog.Builder(this);
			final AlertDialog alertConstr;
			builderConstr.setTitle("Afiseaza comenzile create");
			builderConstr.setCancelable(true);
			builderConstr.setSingleChoiceItems(constrCmd, intervalAfisare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					intervalAfisare = which;

					getListComenziRetur(intervalAfisare, stare);
					dialog.cancel();

				}
			});

			alertConstr = builderConstr.create();
			alertConstr.setCancelable(true);
			alertConstr.show();

			return true;

		case 1:

			final CharSequence[] constrStare = { "In curs de aprobare", "Aprobate", "Respinse" };
			final AlertDialog.Builder builderStare = new AlertDialog.Builder(this);
			final AlertDialog alertStare;
			builderStare.setTitle("Afiseaza comenzile");
			builderStare.setCancelable(true);
			builderStare.setSingleChoiceItems(constrStare, stare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					stare = which;

					getListComenziRetur(intervalAfisare, stare);
					dialog.cancel();

				}
			});

			alertStare = builderStare.create();
			alertStare.setCancelable(true);
			alertStare.show();

			return true;

		case android.R.id.home:
			clearAllData();
			UserInfo.getInstance().setParentScreen("");
			final Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void getListComenziRetur(int intervalAfisare, int stare) {

		String localDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipUserSap().startsWith("C"))
			localDepart = "11";

		if (UserInfo.getInstance().getTipUserSap().startsWith("K"))
			localDepart = "10";

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("tipUser", UserInfo.getInstance().getTipUserSap());
		params.put("depart", localDepart);
		params.put("interval", String.valueOf(intervalAfisare));
		params.put("stare", String.valueOf(stare + 1));

		operatiiRetur.getListaComenziSalvate(params);

	}

	private void populateListComenzi(List<BeanComandaReturAfis> listComenzi) {

		if (listComenzi.size() > 0) {
			ComandaReturAfisAdapter adapter = new ComandaReturAfisAdapter(listComenzi, getApplicationContext());
			spinnerComenziRetur.setAdapter(adapter);
			spinnerComenziRetur.setVisibility(View.VISIBLE);

			if (UserInfo.getInstance().getTipUserSap().equals("SD") && stare == 0)
				aprobareLayout.setVisibility(View.VISIBLE);
			else
				aprobareLayout.setVisibility(View.INVISIBLE);

		} else {
			spinnerComenziRetur.setVisibility(View.INVISIBLE);
			listArticoleComandaRetur.setVisibility(View.INVISIBLE);
			layoutDateComanda.setVisibility(View.INVISIBLE);
			aprobareLayout.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi", Toast.LENGTH_SHORT).show();

		}

	}

	private void getArticoleComanda(String idComanda) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", idComanda);

		String tipUser = UserInfo.getInstance().getTipUserSap();

		if ((tipUser.equals("SD") && (stare == 0) || stare == 2) || tipUser.contains("AV"))
			operatiiRetur.getArticoleComandaSalvata(params);
		else
			operatiiRetur.getArticoleComandaSalvataSap(params);

	}

	private void populateArticoleComanda(BeanComandaRetur comandaRetur) {
		ArticoleReturAdapter adapter = new ArticoleReturAdapter(this, comandaRetur.getArrayListArticole());
		listArticoleComandaRetur.setAdapter(adapter);
		listArticoleComandaRetur.setVisibility(View.VISIBLE);

		layoutDateComanda.setVisibility(View.VISIBLE);

		textStrada.setText(comandaRetur.getAdresaStrada());
		textPersContact.setText(comandaRetur.getNumePersContact());
		textTelefon.setText(comandaRetur.getTelPersContact());
		textOras.setText(comandaRetur.getAdresaOras());
		textJudet.setText(UtilsGeneral.getNumeJudet(comandaRetur.getAdresaCodJudet()));
		textDataLivrare.setText(comandaRetur.getDataLivrare());
		textTipTransport.setText(comandaRetur.getTipTransport());

	}

	private void opereazaComandaRetur(EnumOperatieComanda tipOperatie) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", comandaRetur.getId());
		params.put("tipOperatie", tipOperatie.getTipOperatie());
		operatiiRetur.opereazaComanda(params);

	}

	private void clearAllData() {
		selectedCmd = "";
		selectedCmdSap = "";
		clpDeSters = "";
		strCodStatusCmd = "";
		intervalAfisare = 0;
		stare = 0;
	}

	public void onResume() {
		super.onResume();

	}

	public void onBackPressed() {

		clearAllData();
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);

		finish();
		return;
	}

	public void operationReturComplete(EnumRetur methodName, Object result) {

		switch (methodName) {
		case GET_COMENZI_SALVATE:
			populateListComenzi(operatiiRetur.deserializeComenziSalvate(result));
			break;

		case GET_ARTICOLE_COMANDA_SAP:
		case GET_ARTICOLE_COMANDA_SALVATA:
			populateArticoleComanda(operatiiRetur.deserializeComandaSalvata(result));
			break;

		case OPEREAZA_COMANDA:
			getListComenziRetur(intervalAfisare, stare);
			break;
		default:
			break;
		}

	}

	public void dialogResponse(EnumDialogConstraints constraint, EnumDaNuOpt response) {

		switch (constraint) {
		case APROBARE_COMANDA:
			if (response == EnumDaNuOpt.DA) {
				opereazaComandaRetur(EnumOperatieComanda.APROBA);
			}
			break;

		case RESPINGERE_COMANDA:
			if (response == EnumDaNuOpt.DA) {
				opereazaComandaRetur(EnumOperatieComanda.RESPINGE);
			}
			break;
		default:
			break;
		}

	}

}
