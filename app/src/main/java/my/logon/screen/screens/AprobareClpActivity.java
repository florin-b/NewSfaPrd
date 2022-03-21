/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.ClpDAOListener;
import my.logon.screen.model.ClpDAO;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.adapters.ArticoleCLPAdapter;
import my.logon.screen.adapters.ComenziCLPAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.BeanDocumentCLP;
import my.logon.screen.beans.ComandaCLP;
import my.logon.screen.beans.DateLivrareCLP;
import my.logon.screen.enums.EnumClpDAO;

public class AprobareClpActivity extends Activity implements ClpDAOListener {

	private Spinner spinnerCmdClp;
	public SimpleAdapter adapterComenziClp, adapterArtCmdClp;

	private static ArrayList<HashMap<String, String>> listComenziClp = new ArrayList<HashMap<String, String>>(),
			listArtCmdClp = new ArrayList<HashMap<String, String>>();
	public static String selectedCmd = "", selectedCmdSap = "";
	ListView listViewArtCmdClp;

	private TextView textAdrLivr, textPersContact, textTelefon, textOras, textJudet, textDataLivrare, textTipPlata, textTipTransport, textAprobatOC,
			textObservatii;

	private TextView textValTransp, textProcTransp;

	LinearLayout layoutCmdCondHead;
	Button btnAprobaClp, btnRespingeClp;
	Integer tipOpCmdClp = -1;

	Button delClpBtn;
	RelativeLayout layoutDelBtn;

	ClpDAO operatiiClp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.aprobare_clp_header);

		operatiiClp = new ClpDAO(this);
		operatiiClp.setClpDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Aprobare CLP");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerCmdClp = (Spinner) findViewById(R.id.spinnerCmdClp);

		adapterComenziClp = new SimpleAdapter(this, listComenziClp, R.layout.list_comenzi_clp, new String[] { "idCmdClp", "numeClient", "data", "cmdSAP", "ul",
				"agent", "depoz", "status" }, new int[] { R.id.textIdCmdClp, R.id.textClient, R.id.textData, R.id.textCmdSAP, R.id.textUL, R.id.textAgent,
				R.id.textDepoz, R.id.textStatusClp });

		spinnerCmdClp.setAdapter(adapterComenziClp);
		addSpinnerCmdListener();

		listViewArtCmdClp = (ListView) findViewById(R.id.listArtCmdClp);

		layoutDelBtn = (RelativeLayout) findViewById(R.id.layoutDelBtn);
		layoutDelBtn.setVisibility(View.GONE);

		adapterArtCmdClp = new SimpleAdapter(this, listArtCmdClp, R.layout.art_comenzi_clp, new String[] { "nrCrt", "numeArt", "codArt", "cantArt", "umArt",
				"depozit", "status" }, new int[] { R.id.textNrCrt, R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt, R.id.textUmArt, R.id.textDepozit,
				R.id.textStatusArt });

		listViewArtCmdClp.setAdapter(adapterArtCmdClp);
		listViewArtCmdClp.setVisibility(View.INVISIBLE);

		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textOras = (TextView) findViewById(R.id.textOras);
		textJudet = (TextView) findViewById(R.id.textJudet);
		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);
		textObservatii = (TextView) findViewById(R.id.textObservatii);

		layoutCmdCondHead = (LinearLayout) findViewById(R.id.layoutCmdCondHead);
		layoutCmdCondHead.setVisibility(View.INVISIBLE);

		btnAprobaClp = (Button) findViewById(R.id.aprobaClp);
		btnRespingeClp = (Button) findViewById(R.id.respingeClp);

		btnAprobaClp.setVisibility(View.INVISIBLE);
		addListenerAprobaClp();

		btnRespingeClp.setVisibility(View.INVISIBLE);
		addListenerRespingeClp();

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textTipTransport = (TextView) findViewById(R.id.textTransport);
		textAprobatOC = (TextView) findViewById(R.id.textAprobatOC);

		delClpBtn = (Button) findViewById(R.id.delClpBtn);
		delClpBtn.setVisibility(View.GONE);

		textValTransp = (TextView) findViewById(R.id.textValTransp);
		textProcTransp = (TextView) findViewById(R.id.textProcTransp);

		try {

			refreshClpList();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	protected void populateCmdList(String cmdList) {
		HandleJSONData objDocList = new HandleJSONData(this, cmdList);
		ArrayList<BeanDocumentCLP> docCLPArray = objDocList.decodeJSONDocumentCLP();

		ComenziCLPAdapter adapterComenziClp = new ComenziCLPAdapter(this, docCLPArray, 1);
		spinnerCmdClp.setAdapter(adapterComenziClp);

		if (docCLPArray.size() > 0) {

			spinnerCmdClp.setVisibility(View.VISIBLE);
			layoutCmdCondHead.setVisibility(View.VISIBLE);
			btnAprobaClp.setVisibility(View.VISIBLE);
			btnRespingeClp.setVisibility(View.VISIBLE);

		} else {
			listComenziClp.clear();
			spinnerCmdClp.setAdapter(adapterComenziClp);
			spinnerCmdClp.setVisibility(View.INVISIBLE);

			listArtCmdClp.clear();
			adapterArtCmdClp.notifyDataSetChanged();
			listViewArtCmdClp.setVisibility(View.INVISIBLE);

			layoutCmdCondHead.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();

			btnAprobaClp.setVisibility(View.INVISIBLE);
			btnRespingeClp.setVisibility(View.INVISIBLE);
		}

	}

	private void performArtCmd() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nrCmd", selectedCmd);

			operatiiClp.getArticoleComandaJSON(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateArtCmdList(ComandaCLP dateComanda) {
		if (dateComanda.getArticole().size() > 0) {
			listViewArtCmdClp.setVisibility(View.VISIBLE);
			DateLivrareCLP dateLivrare = dateComanda.getDateLivrare();

			textAdrLivr.setText(dateLivrare.getAdrLivrare());
			textPersContact.setText(dateLivrare.getPersContact());
			textTelefon.setText(dateLivrare.getTelefon());
			textOras.setText(dateLivrare.getOras());
			textJudet.setText(dateLivrare.getJudet());
			textDataLivrare.setText(dateLivrare.getData());
			textTipPlata.setText(dateLivrare.getTipPlata());
			textTipTransport.setText(dateLivrare.getMijlocTransport());
			textAprobatOC.setText(dateLivrare.getAprobatOC());
			textObservatii.setText(dateLivrare.getObsComanda());
			textValTransp.setText(UtilsFormatting.format2Decimals(Double.valueOf(dateLivrare.getValTransp()), true) + " RON");
			textProcTransp.setText(UtilsFormatting.format2Decimals(Double.valueOf(dateLivrare.getProcTransp()), true) + " %");

			List<ArticolCLP> listArticole = dateComanda.getArticole();

			ArticoleCLPAdapter adapterArticole = new ArticoleCLPAdapter(this, listArticole);
			listViewArtCmdClp.setAdapter(adapterArticole);

		}
	}

	public void addListenerAprobaClp() {
		btnAprobaClp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showConfirmationApprovalAlert();

			}
		});

	}

	public void showConfirmationApprovalAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Aprobati cererea?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				tipOpCmdClp = 0; // aprobare
				opereazaComandaClp();

			}
		}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void addListenerRespingeClp() {
		btnRespingeClp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showConfirmationDenialAlert();

			}
		});

	}

	public void showConfirmationDenialAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Respingeti cererea?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				tipOpCmdClp = 1; // respingere
				opereazaComandaClp();

			}
		}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void opereazaComandaClp() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			NumberFormat nf3 = new DecimalFormat("00000000");
			String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();
			String localTipUser = "NN";

			if (UserInfo.getInstance().getTipAcces().equals("9"))
				localTipUser = "AV";

			else if (UserInfo.getInstance().getTipAcces().equals("10"))
				localTipUser = "SD";

			else if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14"))
				localTipUser = "DV";
			
			else if (UserInfo.getInstance().getTipAcces().equals("18"))
				localTipUser = "SM";

			params.put("nrCmd", selectedCmd);
			params.put("nrCmdSAP", selectedCmdSap);
			params.put("tipOp", String.valueOf(tipOpCmdClp));
			params.put("codUser", fullCode);
			params.put("tipUser", localTipUser);
			params.put("filiala", UserInfo.getInstance().getUnitLog());

			operatiiClp.operatiiComanda(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void refreshClpList() {

		try {

			listComenziClp.clear();
			spinnerCmdClp.setAdapter(adapterComenziClp);

			listArtCmdClp.clear();
			listViewArtCmdClp.setAdapter(adapterArtCmdClp);

			HashMap<String, String> params = new HashMap<String, String>();

			String localTipUser = "NN";

			if (UserInfo.getInstance().getTipAcces().equals("9"))
				localTipUser = "AV";
			else if (UserInfo.getInstance().getTipAcces().equals("10"))
				localTipUser = "SD";
			else if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14"))
				localTipUser = "DV";
			else if (UserInfo.getInstance().getTipAcces().equals("18"))
				localTipUser = "SM";

			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("depart", UserInfo.getInstance().getCodDepart());
			params.put("tipClp", "-1"); // comenzi spre aprobare
			params.put("interval", "");
			params.put("tipUser", localTipUser);
			params.put("codUser", UserInfo.getInstance().getCod());

			operatiiClp.getListComenzi(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();
		return;
	}

	private void addSpinnerCmdListener() {
		spinnerCmdClp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BeanDocumentCLP documentClp = ((BeanDocumentCLP) parent.getAdapter().getItem(position));
				selectedCmd = documentClp.getNrDocument();
				selectedCmdSap = documentClp.getNrDocumentSap();

				performArtCmd();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	public void operationClpComplete(EnumClpDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			populateCmdList((String) result);
			break;
		case OPERATIE_COMANDA:
			selectedCmd = "-1";
			selectedCmdSap = "-1";
			Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_SHORT).show();
			refreshClpList();
			break;
		case GET_ARTICOLE_COMANDA_JSON:
			populateArtCmdList(operatiiClp.decodeArticoleComanda((String) result));
			break;

		default:
			break;
		}

	}

}
