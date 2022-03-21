/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareArticoleAdapter;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.model.OperatiiArticol;
import my.logon.screen.model.OperatiiArticolFactory;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;

public class SelectArtReduceri extends ListActivity implements OperatiiArticolListener {

	Button articoleBtn, saveArtReduceriBtn, triStateBtn;
	String filiala = "", nume = "", cod = "";
	String articolResponse = "";
	String pretResponse = "";
	String codArticol = "";
	String numeArticol = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "", codSintetic = "", artUmVanz = "";

	public String globalDepozSel = "";

	ToggleButton tglButton, tglCantVal;

	private EditText txtNumeArticol, txtCantArt, txtProcRed;
	private TextView textCodArticol, textUm, textSintetic;
	private TextView textNumeArticol, labelProcRed, textProcRed;

	private Spinner spinnerUm;

	private int cautaArt = 0;

	private String[] umArray = { "BAG", "BUC", "CUT", "DNT", "G", "KG", "KM", "L", "M", "M2", "M3", "ML", "PAK", "PUN", "ROL", "SET", "TO" };

	boolean selArt = false, selSint = false, selNiv1 = false;

	private ArrayAdapter<String> adapterSpinnerUm = null;

	static final int DATE_DIALOG_ID = 1;
	LinearLayout artRedTbl1;

	OperatiiArticol opArticol;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectartreduceriheader);

		opArticol = OperatiiArticolFactory.createObject("OperatiiArticolImpl", this);
		opArticol.setListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Articole reduceri");
		actionBar.setDisplayHomeAsUpEnabled(true);

		ReduceriUlterioare.articoleReduceri = "";

		this.articoleBtn = (Button) findViewById(R.id.articoleBtn);
		addListenerBtnArticole();

		this.saveArtReduceriBtn = (Button) findViewById(R.id.saveArtReduceriBtn);
		addListenerBtnSaveArtReduceri();
		saveArtReduceriBtn.setVisibility(View.GONE);

		this.tglButton = (ToggleButton) findViewById(R.id.togglebutton);
		this.tglButton.setChecked(true);
		addListenerToggle();

		this.tglCantVal = (ToggleButton) findViewById(R.id.tglCantVal);
		addListenerToggleCantVal();

		labelProcRed = (TextView) findViewById(R.id.labelProcRed);
		textProcRed = (TextView) findViewById(R.id.txtProcRed);
		textSintetic = (TextView) findViewById(R.id.textSintetic);

		txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
		textNumeArticol = (TextView) findViewById(R.id.textNumeArticol);
		textCodArticol = (TextView) findViewById(R.id.textCodArticol);
		txtProcRed = (EditText) findViewById(R.id.txtProcRed);
		textUm = (TextView) findViewById(R.id.textUM);
		txtCantArt = (EditText) findViewById(R.id.txtCantArt);

		txtNumeArticol.setHint("Introduceti cod articol");

		addListenerTextCant();

		ReduceriUlterioare.articoleReduceri = "";

		textNumeArticol.setVisibility(View.INVISIBLE);
		textCodArticol.setVisibility(View.INVISIBLE);

		labelProcRed.setVisibility(View.INVISIBLE);
		textProcRed.setVisibility(View.INVISIBLE);

		spinnerUm = (Spinner) findViewById(R.id.spinnerUm);

		adapterSpinnerUm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, umArray);

		textUm.setVisibility(View.INVISIBLE);
		spinnerUm.setVisibility(View.INVISIBLE);

		artRedTbl1 = (LinearLayout) findViewById(R.id.ArtRedTable1);
		artRedTbl1.setVisibility(View.INVISIBLE);

		this.triStateBtn = (Button) findViewById(R.id.triStateBtn);
		addListenerTriStateBtn();

		selArt = true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}

	public void addListenerTextCant() {
		txtCantArt.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// tratare eventiment Enter
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					if (txtProcRed.getVisibility() == View.VISIBLE) {
						txtProcRed.requestFocus();
					} else {
						saveArtReduceriBtn.requestFocus();
					}

					return true;
				}
				return false;
			}
		});
	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglButton.isChecked()) {
					if (selArt) {
						txtNumeArticol.setHint("Introduceti cod articol");
					} else if (selSint) {
						txtNumeArticol.setHint("Introduceti cod sintetic");
					} else if (selNiv1) {
						txtNumeArticol.setHint("Introduceti cod nivel1");
					}

				} else {
					if (selArt) {
						txtNumeArticol.setHint("Introduceti nume articol");
					} else if (selSint) {
						txtNumeArticol.setHint("Introduceti nume sintetic");
					} else if (selNiv1) {
						txtNumeArticol.setHint("Introduceti nume nivel1");
					}
				}
			}
		});

	}

	public void addListenerToggleCantVal() {
		tglCantVal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglCantVal.isChecked()) {

					adapterSpinnerUm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					spinnerUm.setAdapter(adapterSpinnerUm);

					textUm.setVisibility(View.VISIBLE);
					spinnerUm.setVisibility(View.VISIBLE);

					// pentru articole se aduce um vanz
					if (selArt) {
						getUmVanz();
					}

				} else {
					textUm.setVisibility(View.INVISIBLE);
					spinnerUm.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	private void getUmVanz() {
		try {

			getArtUmVanz umVanz = new getArtUmVanz(this);
			umVanz.execute(("dummy"));

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void addListenerTriStateBtn() {
		triStateBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					if (triStateBtn.getText().toString().equals("Articol")) {
						triStateBtn.setText("Sintetic");
						selSint = true;
						selArt = false;
						selNiv1 = false;

						if (tglButton.isChecked()) {
							txtNumeArticol.setHint("Introduceti cod sintetic");
						} else {
							txtNumeArticol.setHint("Introduceti nume sintetic");
						}
						return;
					}
					if (triStateBtn.getText().toString().equals("Sintetic")) {
						triStateBtn.setText("Nivel1");
						selSint = false;
						selArt = false;
						selNiv1 = true;

						if (tglButton.isChecked()) {
							txtNumeArticol.setHint("Introduceti cod nivel1");
						} else {
							txtNumeArticol.setHint("Introduceti nume nivel1");
						}
						return;
					}
					if (triStateBtn.getText().toString().equals("Nivel1")) {
						triStateBtn.setText("Articol");
						selSint = false;
						selArt = true;
						selNiv1 = false;

						if (tglButton.isChecked()) {
							txtNumeArticol.setHint("Introduceti cod articol");
						} else {
							txtNumeArticol.setHint("Introduceti nume articol");
						}
						return;
					}

				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerBtnSaveArtReduceri() {
		saveArtReduceriBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					String cantitate = "-1", valoare = "-1", procent = "-1";
					String um = "-1", data1 = "-1", data2 = "-1", exceptii = "-1", codArticol = "-1";
					String numeArticol = "-1", sintetic = "-1";

					numeArticol = textNumeArticol.getText().toString().trim();
					codArticol = textCodArticol.getText().toString().trim();
					String cantArt = txtCantArt.getText().toString().trim();
					String procStr = txtProcRed.getText().toString().trim();

					// validari intrari
					if (ReduceriUlterioare.procRedB5.equals("") && cantArt.equals("") && procStr.equals("")) {
						Toast.makeText(getApplicationContext(), "Completati procentul de reducere!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (!cantArt.equals("") && procStr.equals("")) {
						Toast.makeText(getApplicationContext(), "Completati procentul de reducere!", Toast.LENGTH_SHORT).show();
						return;
					}

					// sf. validari

					sintetic = textSintetic.getText().toString();

					if (tglCantVal.isChecked()) // cantitate
					{
						if (!txtCantArt.getText().toString().trim().equals(""))
							cantitate = txtCantArt.getText().toString().trim();
						else
							cantitate = "0";

						um = spinnerUm.getSelectedItem().toString();
					} else {
						if (!txtCantArt.getText().toString().trim().equals(""))
							valoare = txtCantArt.getText().toString().trim();
						else
							valoare = "0";

						um = "RON";
					}

					// procent = ReduceriUlterioare.procRedB5;

					if (!textProcRed.getText().toString().trim().equals(""))
						procent = textProcRed.getText().toString().trim(); // individual
					else
						procent = "0";

					data1 = " ";
					data2 = " ";

					exceptii = " ";
					if (ReduceriUlterioare.articoleReduceri.indexOf(codArticol) == -1)
						ReduceriUlterioare.articoleReduceri += String.valueOf(cautaArt) + "#" + numeArticol + "#" + codArticol + "#" + cantitate + "#"
								+ valoare + "#" + um + "#" + procent + "#" + data1 + "#" + data2 + "#" + exceptii + "#" + sintetic + "@@";

					textNumeArticol.setText("");
					textCodArticol.setText("");

					txtCantArt.setText("");
					txtProcRed.setText("");

					numeArticol = "";
					codArticol = "";

				} catch (Exception e) {
					Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
					toast.show();
				}

			}
		});

	}

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeArticol.getText().toString().length() > 0) {
						performGetArticole();

					} else {
						Toast.makeText(getApplicationContext(), "Introduceti nume articol!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void performGetArticole() {

		try {

			String numeArticol = txtNumeArticol.getText().toString().trim().replace('*', '%');
			String tipCautare = "", tipArticol = "";

			if (tglButton.isChecked())
				tipCautare = "C";
			else
				tipCautare = "N";

			if (selArt) {
				tipArticol = "A";
				cautaArt = 1;
			} else if (selSint) {
				tipArticol = "S";
				cautaArt = 0;
			} else if (selNiv1) {
				tipArticol = "N";
				cautaArt = 2;
			}

			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("searchString", numeArticol);
			params.put("tipArticol", tipArticol);
			params.put("tipCautare", tipCautare);
			params.put("departament", UserInfo.getInstance().getCodDepart());

			if (selArt)
				opArticol.getArticoleDistributie(params);
			else if (selSint)
				opArticol.getSinteticeDistributie(params);
			else if (selNiv1)
				opArticol.getNivel1Distributie(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void populateListViewArticol(List<ArticolDB> resultsList) {

		CautareArticoleAdapter adapterArticole = new CautareArticoleAdapter(this, resultsList);
		setListAdapter(adapterArticole);

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {

		if (tglCantVal.isChecked())
			tglCantVal.performClick();

		ArticolDB articol = (ArticolDB) l.getAdapter().getItem(position);

		numeArticol = articol.getNume();
		codArticol = articol.getCod();

		codSintetic = articol.getSintetic();

		textNumeArticol.setVisibility(View.VISIBLE);
		textCodArticol.setVisibility(View.VISIBLE);
		txtCantArt.setText("");
		txtCantArt.requestFocus();
		txtProcRed.setText("");

		textNumeArticol.setText(numeArticol);
		textCodArticol.setText(codArticol);
		textSintetic.setText(codSintetic);

		artRedTbl1.setVisibility(View.VISIBLE);

		labelProcRed.setVisibility(View.VISIBLE);
		textProcRed.setVisibility(View.VISIBLE);

		saveArtReduceriBtn.setVisibility(View.VISIBLE);
	}

	private class getArtUmVanz extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog1;

		private getArtUmVanz(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog1 = new ProgressDialog(mContext);
			this.dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog1.setMessage("Aflare UM vanzare...");
			this.dialog1.setCancelable(false);
			this.dialog1.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), "getArtUmVanz");
				String codArt = codArticol;
				if (codArt.length() == 8)
					codArt = "0000000000" + codArt;

				request.addProperty("codArt", codArt);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(), 25000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + "getArtUmVanz", envelope, headerList);
				Object result = envelope.getResponse();
				response = result.toString();

			} catch (Exception e) {
				errMessage = e.getMessage();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				if (dialog1 != null) {
					dialog1.dismiss();
					dialog1 = null;
				}

				if (!errMessage.equals("")) {
					Toast toast1 = Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_LONG);
					toast1.show();

				} else {
					selectArtUmVanz(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void selectArtUmVanz(String umVanz) {

		if (!umVanz.trim().equals("")) {
			int i = adapterSpinnerUm.getPosition(umVanz);
			spinnerUm.setSelection(i);
		}

	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {
		switch (methodName) {

		case GET_SINTETICE_DISTRIBUTIE:
		case GET_NIVEL1_DISTRIBUTIE:
		case GET_ARTICOLE_DISTRIBUTIE:
			populateListViewArticol(opArticol.deserializeArticoleVanzare((String) result));
			break;

		default:
			break;

		}

	}

}