/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.model.UserInfo;


public class SelectExceptiiReduceri extends Activity {

	Button articoleBtn, saveArtReduceriBtn, triStateBtnExc;
	String filiala = "", nume = "", cod = "";
	String articolResponse = "";
	String pretResponse = "";
	String codArticol = "";
	String numeArticol = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "", codSintetic = "";

	public String globalDepozSel = "";

	ToggleButton tglButton, tglCantVal;

	private EditText txtNumeArticol, txtCantArt, txtProcRed;
	private TextView textCodArticol, textExceptii, textSintetic;
	private TextView textNumeArticol;

	private ListView listExceptii;

	private int cautaArt = 0;

	private int listViewSelArt = -1;
	private String numeArtSel = "", codArtSel = "", codSint = "";

	private static ArrayList<HashMap<String, String>> listArticole = null;
	public SimpleAdapter adapterArticole;
	static final int DATE_DIALOG_ID = 1;
	boolean selArt = false, selSint = false, selNiv1 = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectexceptii);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Exceptii reduceri");
		actionBar.setDisplayHomeAsUpEnabled(true);

		listExceptii = (ListView) findViewById(R.id.listExc);

		listArticole = new ArrayList<HashMap<String, String>>();
		adapterArticole = new SimpleAdapter(this, listArticole,
				R.layout.customrowexceptii, new String[] { "numeArticol",
						"codArticol", "codSintetic" }, new int[] {
						R.id.textNumeArticol, R.id.textCodArticol,
						R.id.textCodSintetic });

		listExceptii.setAdapter(adapterArticole);
		listExceptii.setClickable(true);
		addListenerArtExceptii();
		registerForContextMenu(listExceptii);

		ReduceriUlterioare.articoleReduceri = "";

		this.articoleBtn = (Button) findViewById(R.id.articoleBtn);
		addListenerBtnArticole();

		this.tglButton = (ToggleButton) findViewById(R.id.togglebutton);
		this.tglButton.setChecked(true);
		addListenerToggle();

		txtNumeArticol = (EditText) findViewById(R.id.txtNumeArt);
		txtNumeArticol.setHint("Introduceti cod articol");

		ReduceriUlterioare.exceptii = "";

		this.triStateBtnExc = (Button) findViewById(R.id.triStateBtnExc);
		addListenerTriStateBtnExc();
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

	public void addListenerTriStateBtnExc() {
		triStateBtnExc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					if (triStateBtnExc.getText().toString().equals("Articol")) {
						triStateBtnExc.setText("Sintetic");
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
					if (triStateBtnExc.getText().toString().equals("Sintetic")) {
						triStateBtnExc.setText("Nivel1");
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
					if (triStateBtnExc.getText().toString().equals("Nivel1")) {
						triStateBtnExc.setText("Articol");
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
					Toast.makeText(getApplicationContext(), ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();
		String tipArt = " ";

		if (menuItemIndex == 0) // adaugare
		{
			if (listViewSelArt >= 0) {
				// actiune
				if (ReduceriUlterioare.articoleReduceri.indexOf(codArtSel) == -1) {
					if (selSint)
						tipArt = "sintetic";
					if (selArt)
						tipArt = "articol";
					if (selNiv1)
						tipArt = "nivel1";

					ReduceriUlterioare.exceptii += codArtSel + "#" + numeArtSel
							+ "#" + codSint + "#" + tipArt + "@@";
					codArtSel = "";
					numeArtSel = "";
					codSint = "";

				}

			}
		}

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.listExc) {

			Object obj = this.adapterArticole.getItem(listViewSelArt);
			String[] token = obj.toString().split(",");

			codArtSel = token[0].substring(token[0].indexOf('=') + 1,
					token[0].length());

			numeArtSel = token[1].substring(token[1].indexOf('=') + 1,
					token[1].length());

			codSint = token[2].substring(token[2].indexOf('=') + 1,
					token[2].length() - 1);

			if (!numeArtSel.trim().equals("")) {
				menu.setHeaderTitle(numeArtSel);
				menu.add(Menu.NONE, 0, 0, "Adauga exceptie");
			}

		}

	}

	public void addListenerToggle() {
		tglButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglButton.isChecked()) {

					txtNumeArticol.setHint("Introduceti cod articol");

				} else {

					txtNumeArticol.setHint("Introduceti nume articol");

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
					String excStr = textExceptii.getText().toString().trim();

					// validari intrari
					if (codArticol.equals("")) {
						Toast.makeText(getApplicationContext(),
								"Completati articol sau sintetic!",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (cantArt.equals("")) {
						Toast.makeText(getApplicationContext(),
								"Completati prag reducere!", Toast.LENGTH_SHORT)
								.show();
						return;
					}

					if (ReduceriUlterioare.tipReducere.equals("B1")) {
						if (procStr.equals("")) {
							Toast.makeText(getApplicationContext(),
									"Completati procent reducere!",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}

					if (textExceptii.isShown()) {

						if (excStr.contains(",")) {
							Toast.makeText(getApplicationContext(),
									"Folositi separatorul ;",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}

					// sf. validari

					sintetic = textSintetic.getText().toString();

					if (tglCantVal.isChecked()) // cantitate
					{
						cantitate = txtCantArt.getText().toString().trim();
						um = "123";
					} else {
						valoare = txtCantArt.getText().toString().trim();
						um = "RON";
					}

					if (!ReduceriUlterioare.procRedB5.equals("-1")) {
						procent = ReduceriUlterioare.procRedB5;
					} else {
						if (!procStr.equals(""))
							procent = txtProcRed.getText().toString().trim();
					}

					data1 = " ";
					data2 = " ";

					if (!excStr.equals(""))
						exceptii = textExceptii.getText().toString().trim();

					if (ReduceriUlterioare.articoleReduceri.indexOf(codArticol) == -1) {
						ReduceriUlterioare.articoleReduceri += String
								.valueOf(cautaArt)
								+ "#"
								+ numeArticol
								+ "#"
								+ codArticol
								+ "#"
								+ cantitate
								+ "#"
								+ valoare
								+ "#"
								+ um
								+ "#"
								+ procent
								+ "#"
								+ data1
								+ "#"
								+ data2
								+ "#"
								+ exceptii
								+ "#"
								+ sintetic
								+ "@@";
					}

					textNumeArticol.setText("");
					textCodArticol.setText("");

					txtCantArt.setText("");
					txtProcRed.setText("");
					textExceptii.setText("");

					numeArticol = "";
					codArticol = "";

				} catch (Exception e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							e.getMessage(), Toast.LENGTH_SHORT);
					toast.show();
				}

			}
		});

	}

	public void populateListViewArticol(String clientResponse) {

		if (clientResponse.length() > 0) {
			HashMap<String, String> temp;
			String[] tokenLinie = clientResponse.split("@@");
			String[] tokenClient;
			String client = "";
			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");
				temp.put("numeArticol", tokenClient[0]);
				temp.put("codArticol", tokenClient[1]);
				temp.put("codSintetic", tokenClient[2]);
				listArticole.add(temp);
			}

			if (tokenLinie.length > 1) {
				Toast.makeText(getApplicationContext(),
						String.valueOf(tokenLinie.length) + " inregistrari",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Nu exista articole!",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void addListenerBtnArticole() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeArticol.getText().toString().length() > 0) {
						performGetArticole();

					} else {
						Toast.makeText(getApplicationContext(),
								"Introduceti nume articol!", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void performGetArticole() {

		try {

			if (selArt) // cautare articole (local)
			{

				getArtFromDB articol = new getArtFromDB(this);
				articol.execute(("dummy"));
				cautaArt = 1;
			} else if (selSint) // cautare sintetice (in retea)
			{

				getSintetice sintetic = new getSintetice(this);
				sintetic.execute(("dummy"));
				cautaArt = 0;
			} else if (selNiv1) {
				getNiv1FromDB niv1 = new getNiv1FromDB(this);
				niv1.execute(("dummy"));
				cautaArt = 2;
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}

	}

	public void addListenerArtExceptii() {
		listExceptii
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {

						listViewSelArt = position;
						return false;

					}
				});
	}

	private class getSintetice extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog1;

		private getSintetice(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog1 = new ProgressDialog(mContext);
			this.dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog1.setMessage("Asteptati...");
			this.dialog1.setCancelable(false);
			this.dialog1.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {
				String numeArticol = txtNumeArticol.getText().toString().trim()
						.replace('*', '%');

				SoapObject request = new SoapObject(
						ConnectionStrings.getInstance().getNamespace(), "cautaSinteticeAndroid");

				request.addProperty("codSintetic", numeArticol);
				String tipArticol = "";
				if (tglButton.isChecked())
					tipArticol = "1";
				else
					tipArticol = "2";

				request.addProperty("tip", tipArticol);
				request.addProperty("depart", UserInfo.getInstance().getNumeDepart());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						ConnectionStrings.getInstance().getUrl(), 25000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin"
								.getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace()
						+ "cautaSinteticeAndroid", envelope, headerList);
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
					this.dialog1.dismiss();
					this.dialog1 = null;
				}

				if (!errMessage.equals("")) {
					Toast toast1 = Toast.makeText(getApplicationContext(),
							errMessage, Toast.LENGTH_LONG);
					toast1.show();
				} else {
					populateListViewArt(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private class getNiv1FromDB extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getNiv1FromDB(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Asteptati...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			SQLiteDatabase checkDB = null;
			try {

				String condArt = "";
				String numeArticol = txtNumeArticol.getText().toString().trim();
				if (tglButton.isChecked())
					condArt = " codnivel1 like '" + numeArticol + "%' ";
				else
					condArt = " upper(numenivel1) like  upper('" + numeArticol
							+ "%') ";

				checkDB = SQLiteDatabase.openDatabase(
						ConnectionStrings.getInstance().getNamespace(), null,
						SQLiteDatabase.OPEN_READWRITE);
				Cursor cur = checkDB.rawQuery(
						"SELECT distinct codnivel1,numenivel1 FROM nivel1def where "
								+ condArt + " order by numenivel1 ", null);

				if (cur != null) {
					if (cur.moveToFirst()) {
						do {

							response += cur.getString(1) + "#"
									+ cur.getString(0) + "#" + cur.getString(0)
									+ "@@";

						} while (cur.moveToNext());
					}
				}

				if (cur != null)
					cur.close();

			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {
				if (checkDB != null)
					checkDB.close();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				if (dialog != null) {
					this.dialog.dismiss();
					this.dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					populateListViewArt(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private class getArtFromDB extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getArtFromDB(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Asteptati...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			SQLiteDatabase checkDB = null;
			try {

				String condArt = "";
				String numeArticol = txtNumeArticol.getText().toString().trim();
				if (tglButton.isChecked())
					condArt = " id like '" + numeArticol + "%' ";
				else
					condArt = " upper(nume) like  upper('" + numeArticol
							+ "%') ";

				checkDB = SQLiteDatabase.openDatabase(
						ConnectionStrings.getInstance().getNamespace(), null,
						SQLiteDatabase.OPEN_READWRITE);
				Cursor cur = checkDB.rawQuery(
						"SELECT id,nume,sintetic FROM articoledef where "
								+ condArt + " order by nume ", null);

				if (cur != null) {
					if (cur.moveToFirst()) {
						do {

							response += cur.getString(1) + "#"
									+ cur.getString(0) + "#" + cur.getString(2)
									+ "@@";

						} while (cur.moveToNext());
					}
				}

				if (cur != null)
					cur.close();

			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {

				if (checkDB != null)
					checkDB.close();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				if (dialog != null) {
					this.dialog.dismiss();
					this.dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(),
							errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					populateListViewArt(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void populateListViewArt(String articolResponse) {
		try {
			if (articolResponse.length() > 0) {
				listArticole.clear();
				adapterArticole.notifyDataSetChanged();

				populateListViewArticol(articolResponse);

				txtNumeArticol.setText("");

				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txtNumeArticol.getWindowToken(), 0);

			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Nu exista inregistrari", Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (Exception ex) {
			Log.e("Error", ex.toString());
			Toast toast = Toast.makeText(getApplicationContext(),
					"Nu exista inregistrari", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	

}