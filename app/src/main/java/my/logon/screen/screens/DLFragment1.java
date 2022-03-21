/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanFurnizor;
import my.logon.screen.beans.BeanFurnizorProduse;
import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.dialogs.MapAddressDialogF4;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.listeners.MapListener;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.OperatiiAdresa;
import my.logon.screen.model.OperatiiAdresaImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsGeneral;

public class DLFragment1 extends Fragment implements OperatiiAdresaListener, MapListener {

	private static EditText txtNumeClient, txtPersCont, txtTelefon, txtTipMarfa, txtMasaMarfa;

	private static AutoCompleteTextView txtOras, txtStrada;

	private Button btnPozitieAdresa;

	private static TextView txtDataLivrare, textLimitaCredit, textRestCredit;

	public static String tipCautare = "C"; // C - client, F - furnizor

	private Button cautaClientBtn, saveClntBtn;

	private static ArrayList<HashMap<String, String>> listClienti = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterClienti, adapterJudete, adapterFiliale, adapterPlata;

	String[] judete = { "ALBA", "ARAD", "ARGES", "BACAU", "BIHOR", "BISTRITA-NASAUD", "BOTOSANI", "BRAILA", "BRASOV", "BUCURESTI", "BUZAU", "CALARASI",
			"CARAS-SEVERIN", "CLUJ", "CONSTANTA", "COVASNA", "DAMBOVITA", "DOLJ", "GALATI", "GIURGIU", "GORJ", "HARGHITA", "HUNEDOARA", "IALOMITA", "IASI",
			"ILFOV", "MARAMURES", "MEHEDINTI", "MURES", "NEAMT", "OLT", "PRAHOVA", "SALAJ", "SATU-MARE", "SIBIU", "SUCEAVA", "TELEORMAN", "TIMIS", "TULCEA",
			"VALCEA", "VASLUI", "VRANCEA" };

	String[] codJudete = { "01", "02", "03", "04", "05", "06", "07", "09", "08", "40", "10", "51", "11", "12", "13", "14", "15", "16", "17", "52", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "31", "30", "32", "33", "34", "35", "36", "38", "37", "39" };

	String[] numeFiliala = { "Bacau", "Baia Mare", "Brasov", "Brasov-central", "Buc. Andronache", "Buc. Militari", "Buc. Otopeni", "Buc. Glina", "Constanta",
			"Cluj", "Craiova", "Focsani", "Galati", "Iasi", "Oradea", "Piatra Neamt", "Pitesti", "Ploiesti", "Timisoara", "Tg. Mures" };

	String[] codFiliala = { "BC10", "MM10", "BV10", "BV90", "BU13", "BU11", "BU12", "BU10", "CT10", "CJ10", "DJ10", "VN10", "GL10", "IS10", "BH10", "NT10",
			"AG10", "PH10", "TM10", "MS10" };

	String[] tipCamion = { "Prelata", "Perdea", "Platforma", "Descoperita", "Macara" };
	String[] tipIncarcare = { "Complet", "Partial" };

	String[] tipTransport = { "TRAP - Transport Arabesque", "TCLI - Transport client", "TFRN - Transport furnizor", "TERT - Transport curier" };

	String[] depozite = { "V1 - vanzare", "V2 - vanzare", "V3 - vanzare", "G1 - gratuite", "G2 - gratuite", "G3 - gratuite", "D1 - deteriorate",
			"D2 - deteriorate", "D3 - deteriorate", "DESC" };

	String[] tipPlata = { "B - Bilet la ordin", "C - Cec", "E - Plata in numerar", "O - Ordin de plata", "E1 - Numerar la sofer" };

	private ListView listViewClienti;
	private LinearLayout layoutClient, layoutAgenti;

	private static ArrayList<HashMap<String, String>> listJudete = null;

	String codClient = "", numeClient = "", codFurnizor = "", numeFurnizor = "", codFurnizorProd = "", numeFurnizorProd = "";

	private TextView codClientText, labelTipClient, labelLimitaCredit, labelRestCredit;
	private static TextView numeClientText, labelClient, textSelFurnizor, textSelFurnizorProd;

	public static EditText txtObservatiiClp;
	public static TextView textSelClient, txtValoareCLP;

	static final int DATE_DIALOG_ID = 1;

	private TextView labelCodClient, labelNumeClient, labelAgentiDl;

	public static LatLng coordAdresa;

	SlidingDrawer slidingDrawer;
	public static Spinner spinnerJudetDL, spinnerTipCamion, spinnerTipIncarcare, spinnerDepozDl_Dest, spinnerTipPlata, spinnerAgentiDl, spinnerTransp;

	private static ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterAgenti;

	LinearLayout layoutValoareClp;

	private OperatiiAdresa operatiiAdresa;

	private int mYear;
	private int mMonth;
	private int mDay;

	private HashMap<String, String> artMap = null;

	public static RadioButton radioClient, radioFiliala;

	public static Spinner spinnerTonaj;

	public static final DLFragment1 newInstance() {

		DLFragment1 f = new DLFragment1();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dl_fragment_1, container, false);

		try {

			this.layoutClient = (LinearLayout) v.findViewById(R.id.layoutClient);
			layoutClient.setVisibility(View.INVISIBLE);

			initLocale();
			
			txtNumeClient = (EditText) v.findViewById(R.id.txtNumeClient);
			txtNumeClient.setHint("Introduceti nume client");

			this.cautaClientBtn = (Button) v.findViewById(R.id.clientBtn);
			addListenerCautaClient();

			listClienti = new ArrayList<HashMap<String, String>>();
			adapterClienti = new SimpleAdapter(getActivity(), listClienti, R.layout.customrownumeclient, new String[] { "numeClient", "codClient" }, new int[] {
					R.id.textNumeClient, R.id.textCodClient });

			listViewClienti = (ListView) v.findViewById(R.id.listClienti);
			listViewClienti.setOnItemClickListener(new MyOnItemSelectedListener());
			listViewClienti.setVisibility(View.INVISIBLE);

			btnPozitieAdresa = (Button) v.findViewById(R.id.btnPozitieAdresa);
			setListnerBtnPozitieAdresa();

			coordAdresa = new LatLng(0, 0);

			this.slidingDrawer = (SlidingDrawer) v.findViewById(R.id.clientSlidingDrawer);
			addDrawerListener();

			slidingDrawer.setVisibility(View.GONE);

			operatiiAdresa = new OperatiiAdresaImpl(getActivity());
			operatiiAdresa.setOperatiiAdresaListener((OperatiiAdresaListener) this);

			codClientText = (TextView) v.findViewById(R.id.textCodClient);
			numeClientText = (TextView) v.findViewById(R.id.textNumeClient);

			labelCodClient = (TextView) v.findViewById(R.id.labelCodClient);
			labelNumeClient = (TextView) v.findViewById(R.id.labelNumeClient);

			labelClient = (TextView) v.findViewById(R.id.labelClient);

			this.saveClntBtn = (Button) v.findViewById(R.id.saveClntBtn);

			textSelClient = (TextView) v.findViewById(R.id.numeSelClient);
			addListenerTextSelClient();
			addListenerSaveClient();

			textSelFurnizor = (TextView) v.findViewById(R.id.numeSelFurnizor);
			addListenerTextSelFurnizor();

			textSelFurnizorProd = (TextView) v.findViewById(R.id.numeSelFurnizorProd);
			addListenerTextSelFurnizorProd();

			spinnerJudetDL = (Spinner) v.findViewById(R.id.spinnerJudetCLP);
			spinnerJudetDL.setOnItemSelectedListener(new regionSelectedListener());

			listJudete = new ArrayList<HashMap<String, String>>();
			adapterJudete = new SimpleAdapter(getActivity(), listJudete, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] {
					R.id.textNumeJudet, R.id.textCodJudet });
			fillJudete();

			txtOras = (AutoCompleteTextView) v.findViewById(R.id.txtOrasCLP);
			addTxtOrasListener();

			txtStrada = (AutoCompleteTextView) v.findViewById(R.id.txtStradaCLP);
			addTxtStradaListener();

			txtPersCont = (EditText) v.findViewById(R.id.txtPersContCLP);
			addTxtPersContListener();

			txtTelefon = (EditText) v.findViewById(R.id.txtTelefonCLP);
			addTxtTelefonListener();

			txtTipMarfa = (EditText) v.findViewById(R.id.txtTipMarfa);
			addTxtTipMarfaListener();

			txtMasaMarfa = (EditText) v.findViewById(R.id.txtMasaMarfa);
			addTxtMasaMarfaListener();

			txtDataLivrare = (TextView) v.findViewById(R.id.textDataLivrareCLP);
			addTxtDataLivrareListener();

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			String currentDate = sdf.format(new Date());
			txtDataLivrare.setText(currentDate);

			radioClient = (RadioButton) v.findViewById(R.id.radioClient);
			addListenerRadioClient();

			radioFiliala = (RadioButton) v.findViewById(R.id.radioFiliala);
			addListenerRadioFiliala();

			spinnerTipCamion = (Spinner) v.findViewById(R.id.spinnerTipCamion);
			ArrayAdapter<String> adapterSpinnerTransp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipCamion);
			adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTipCamion.setAdapter(adapterSpinnerTransp);

			spinnerTipIncarcare = (Spinner) v.findViewById(R.id.spinnerTipIncarcare);
			ArrayAdapter<String> adapterSpinnerIncarcare = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipIncarcare);
			adapterSpinnerIncarcare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTipIncarcare.setAdapter(adapterSpinnerIncarcare);

			spinnerDepozDl_Dest = (Spinner) v.findViewById(R.id.spinnerDepozDl_Dest);
			ArrayAdapter<String> adapterSpinnerDepoz = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, depozite);
			adapterSpinnerDepoz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerDepozDl_Dest.setAdapter(adapterSpinnerDepoz);
			spinnerDepozDl_Dest.setSelection(9);

			labelTipClient = (TextView) v.findViewById(R.id.labelTipClient);
			labelTipClient.setText("Selectie client");

			spinnerTipPlata = (Spinner) v.findViewById(R.id.spinnerTipPlata);

			ArrayAdapter<String> adapterSpinnerPlata = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipPlata);
			adapterSpinnerPlata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTipPlata.setAdapter(adapterSpinnerPlata);
			addListenerTipPlata();

			spinnerTransp = (Spinner) v.findViewById(R.id.spinnerTransp);
			ArrayAdapter<String> adapterSpinnerTransport = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipTransport);
			adapterSpinnerTransport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTransp.setAdapter(adapterSpinnerTransport);
			spinnerTransp.setOnItemSelectedListener(new OnSelectTipTransport());

			spinnerTonaj = (Spinner) v.findViewById(R.id.spinnerTonaj);
			setupSpinnerTonaj();

			labelLimitaCredit = (TextView) v.findViewById(R.id.labelLimitaCredit);
			textLimitaCredit = (TextView) v.findViewById(R.id.textLimitaCredit);
			labelRestCredit = (TextView) v.findViewById(R.id.labelRestCredit);
			textRestCredit = (TextView) v.findViewById(R.id.textRestCredit);

			layoutValoareClp = (LinearLayout) v.findViewById(R.id.layoutValoareCLP);
			layoutValoareClp.setVisibility(View.GONE);

			txtValoareCLP = (EditText) v.findViewById(R.id.txtValoareCLP);
			txtObservatiiClp = (EditText) v.findViewById(R.id.txtObservatiiClp);

			layoutAgenti = (LinearLayout) v.findViewById(R.id.layoutSelAgentiDl);

			spinnerAgentiDl = (Spinner) v.findViewById(R.id.spinnerAgentiDl);
			adapterAgenti = new SimpleAdapter(getActivity(), listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
					R.id.textNumeAgent, R.id.textCodAgent });

			spinnerAgentiDl.setAdapter(adapterAgenti);
			labelAgentiDl = (TextView) v.findViewById(R.id.labelAgentiDl);

			// ag sau ka
			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) {

				radioClient.setVisibility(View.GONE);
				radioFiliala.setVisibility(View.GONE);

				spinnerAgentiDl.setVisibility(View.GONE);
				labelAgentiDl.setVisibility(View.GONE);
			} else {

				// SD
				if (UserInfo.getInstance().getTipAcces().equals("10")) {
					radioClient.setVisibility(View.GONE);
					radioFiliala.setVisibility(View.GONE);
				}

				labelAgentiDl.setVisibility(View.VISIBLE);
				spinnerAgentiDl.setVisibility(View.VISIBLE);
				performGetAgenti();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

		return v;

	}

	private void setupSpinnerTonaj() {

		String[] tonajValues = { "Selectati tonajul", "3.5 T", "10 T", "Fara restrictie de tonaj" };

		ArrayAdapter<String> adapterTonaj = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tonajValues);
		adapterTonaj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTonaj.setAdapter(adapterTonaj);

	}

	public void addDrawerListener() {

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {

				if (tipCautare.equals("FP")) {
					txtNumeClient.setText(textSelFurnizor.getText().toString().trim());
					cautaClientBtn.performClick();
				}

				layoutClient.setVisibility(View.INVISIBLE);

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {

				slidingDrawer.setVisibility(View.INVISIBLE);
			}
		});

	}

	private void setListnerBtnPozitieAdresa() {
		btnPozitieAdresa.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Address address = new Address();

				address.setCity(txtOras.getText().toString().trim());
				address.setStreet(txtStrada.getText().toString().trim());
				address.setSector(UtilsGeneral.getNumeJudet(CreareDispozitiiLivrare.codJudet));

				if (!isAdresaComplet())
					return;

				FragmentManager fm = getFragmentManager();

				MapAddressDialogF4 mapDialog = new MapAddressDialogF4(address, getActivity(), fm);

				mapDialog.setMapListener(DLFragment1.this);
				mapDialog.show();
			}
		});
	}

	private boolean isAdresaComplet() {
		if (spinnerJudetDL.getSelectedItemPosition() == 0) {
			Toast.makeText(getActivity(), "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (txtOras.getText().toString().trim().equals("")) {
			Toast.makeText(getActivity(), "Completati localitatea", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void addListenerCautaClient() {
		cautaClientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeClient.length() > 0) {
						performListClients();
					} else {
						Toast.makeText(DLFragment1.this.getActivity(), "Introduceti nume client!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(DLFragment1.this.getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public class MyOnItemSelectedListener implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			try {

				HashMap<String, String> artMap = (HashMap<String, String>) adapterClienti.getItem(pos);

				if (tipCautare.equals("C")) {
					numeClient = artMap.get("numeClient");
					codClient = artMap.get("codClient");

					CreareDispozitiiLivrare.codClient = codClient;
					performClientDetails();
				}

				if (tipCautare.equals("F")) {

					numeFurnizor = artMap.get("numeClient");
					codFurnizor = artMap.get("codClient");

					CreareDispozitiiLivrare.codFurnizor = codFurnizor;

					layoutClient.setVisibility(View.VISIBLE);
					saveClntBtn.setVisibility(View.VISIBLE);
					labelCodClient.setVisibility(View.VISIBLE);
					labelNumeClient.setVisibility(View.VISIBLE);

					numeClientText.setText(numeFurnizor);
					codClientText.setText(codFurnizor);
				}

				if (tipCautare.equals("FP")) {

					numeFurnizorProd = artMap.get("numeClient");
					codFurnizorProd = artMap.get("codClient");

					CreareDispozitiiLivrare.codFurnizorProduse = codFurnizorProd;

					layoutClient.setVisibility(View.VISIBLE);
					saveClntBtn.setVisibility(View.VISIBLE);
					labelCodClient.setVisibility(View.VISIBLE);
					labelNumeClient.setVisibility(View.VISIBLE);

					numeClientText.setText(numeFurnizorProd);
					codClientText.setText(codFurnizorProd);
				}

			}

			catch (Exception ex) {
				Toast.makeText(DLFragment1.this.getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO
		}
	}

	private void performClientDetails() {

		try {
			getClientDetails details = new getClientDetails(getActivity());
			details.execute("dummy");
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private class getClientDetails extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getClientDetails(Context context) {
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
			try {

				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), "getClientDetAndroid");

				request.addProperty("codClient", codClient);
				request.addProperty("depart", UserInfo.getInstance().getCodDepart());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(), 60000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));

				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + "getClientDetAndroid", envelope, headerList);
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
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					listClientDetails(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void listClientDetails(String detailResponse) {
		if (detailResponse.equals("-1")) {
			Toast.makeText(getActivity(), "Client nedefinit pe acest departament!", Toast.LENGTH_SHORT).show();
			numeClientText.setText("");
			codClientText.setText("");

			codClient = "";
			numeClient = "";
			labelCodClient.setVisibility(View.GONE);
			labelNumeClient.setVisibility(View.GONE);

		} else {

			try {

				NumberFormat nf2 = NumberFormat.getInstance();
				nf2.setMinimumFractionDigits(2);
				nf2.setMaximumFractionDigits(2);

				layoutClient.setVisibility(View.VISIBLE);
				saveClntBtn.setVisibility(View.VISIBLE);
				labelCodClient.setVisibility(View.VISIBLE);
				labelNumeClient.setVisibility(View.VISIBLE);

				numeClientText.setText(numeClient);
				codClientText.setText(codClient);

				String[] token2 = detailResponse.split("#");

				labelLimitaCredit.setText("Limita credit");
				labelRestCredit.setText("Rest credit");

				textLimitaCredit.setText(nf2.format(Double.parseDouble(token2[1])));
				textRestCredit.setText(nf2.format(Double.parseDouble(token2[2])));

				if (token2[5].equals("X")) // client blocat
				{
					textRestCredit.setText("Client blocat - " + token2[8]);
					saveClntBtn.setVisibility(View.INVISIBLE);
				} else {
					saveClntBtn.setVisibility(View.VISIBLE);
				}

			} catch (Exception e) {
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
			}

		}

	}

	private void performListClients() {
		try {
			getClientName client = new getClientName(getActivity());
			client.execute("dummy");
		} catch (Exception e) {
			Toast.makeText(DLFragment1.this.getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private class getClientName extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getClientName(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Cautare...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {
				String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

				String methodName = "";

				if (tipCautare.equals("C"))
					methodName = "cautaClientAndroid";

				if (tipCautare.equals("F"))
					methodName = "cautaFurnizorAndroid";

				if (tipCautare.equals("FP"))
					methodName = "cautaFurnizorProduseAndroid";

				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), methodName);

				request.addProperty("numeClient", numeClient);
				request.addProperty("depart", UserInfo.getInstance().getCodDepart());
				request.addProperty("departAg", UserInfo.getInstance().getCodDepart());
				request.addProperty("unitLog", UserInfo.getInstance().getUnitLog());

				if (tipCautare.equals("FP"))
					request.addProperty("codFurnizor", CreareDispozitiiLivrare.codFurnizor);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(), 60000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + methodName, envelope, headerList);
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
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(DLFragment1.this.getActivity(), errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					listClients(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void listClients(String clientResponse) {

		if (clientResponse.length() > 0) {
			listClienti.clear();
			adapterClienti.notifyDataSetChanged();

			populateListViewClient(clientResponse);

			InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(txtNumeClient.getWindowToken(), 0);
			txtNumeClient.setText("");
		}
	}

	public void populateListViewClient(String clientResponse) {

		// cautare clienti
		if (tipCautare.equals("C")) {
			HandleJSONData objClientList = new HandleJSONData(getActivity(), clientResponse);
			ArrayList<BeanClient> clientArray = objClientList.decodeJSONClientList();

			if (clientArray.size() > 0) {
				HashMap<String, String> temp;

				for (int i = 0; i < clientArray.size(); i++) {
					temp = new HashMap<String, String>();
					temp.put("numeClient", clientArray.get(i).getNumeClient());
					temp.put("codClient", clientArray.get(i).getCodClient());
					listClienti.add(temp);
				}

				listViewClienti.setVisibility(View.VISIBLE);
				listViewClienti.setAdapter(adapterClienti);

			} else {
				Toast.makeText(getActivity(), "Nu exista inregistrari!", Toast.LENGTH_SHORT).show();

				if (tipCautare.equals("FP")) {
					textSelFurnizorProd.setText(numeFurnizor);
					CreareDispozitiiLivrare.codFurnizorProduse = codFurnizor;
				}

			}

		}

		// cautare furnizori
		if (tipCautare.equals("F")) {
			HandleJSONData objFurnizoriList = new HandleJSONData(getActivity(), clientResponse);
			ArrayList<BeanFurnizor> furnizorArray = objFurnizoriList.decodeJSONFurnizorList();

			if (furnizorArray.size() > 0) {
				HashMap<String, String> temp;

				for (int i = 0; i < furnizorArray.size(); i++) {
					temp = new HashMap<String, String>();
					temp.put("numeClient", furnizorArray.get(i).getNumeFurnizor());
					temp.put("codClient", furnizorArray.get(i).getCodFurnizor());
					listClienti.add(temp);
				}

				listViewClienti.setVisibility(View.VISIBLE);
				listViewClienti.setAdapter(adapterClienti);

			} else {
				Toast.makeText(getActivity(), "Nu exista inregistrari!", Toast.LENGTH_SHORT).show();

				if (tipCautare.equals("FP")) {
					textSelFurnizorProd.setText(numeFurnizor);
					CreareDispozitiiLivrare.codFurnizorProduse = codFurnizor;
				}

			}

		}

		// cautare furnizori produse
		if (tipCautare.equals("FP")) {
			HandleJSONData objFurnizoriProduseList = new HandleJSONData(getActivity(), clientResponse);
			ArrayList<BeanFurnizorProduse> furnizorProduseArray = objFurnizoriProduseList.decodeJSONFurnizorProduseList();

			if (furnizorProduseArray.size() > 0) {
				HashMap<String, String> temp;

				for (int i = 0; i < furnizorProduseArray.size(); i++) {
					temp = new HashMap<String, String>();
					temp.put("numeClient", furnizorProduseArray.get(i).getNumeFurnizorProduse());
					temp.put("codClient", furnizorProduseArray.get(i).getCodFurnizorProduse());
					listClienti.add(temp);
				}

				listViewClienti.setVisibility(View.VISIBLE);
				listViewClienti.setAdapter(adapterClienti);

			} else {
				Toast.makeText(getActivity(), "Nu exista inregistrari!", Toast.LENGTH_SHORT).show();

				if (tipCautare.equals("FP")) {
					textSelFurnizorProd.setText(numeFurnizor);
					CreareDispozitiiLivrare.codFurnizorProduse = codFurnizor;
				}

			}

		}

	}

	public void addListenerSaveClient() {
		saveClntBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClient.length() == 0 && codFurnizor.length() == 0) {
					Toast.makeText(getActivity(), "Faceti o selectie!", Toast.LENGTH_SHORT).show();
				} else {

					if (tipCautare.equals("C")) {
						textSelClient.setText(numeClient);
						CreareDispozitiiLivrare.codClient = codClient;

					}

					if (tipCautare.equals("F")) {
						textSelFurnizor.setText(numeFurnizor);
						CreareDispozitiiLivrare.codFurnizor = codFurnizor;

					}

					if (tipCautare.equals("FP")) {
						textSelFurnizorProd.setText(numeFurnizorProd);
						CreareDispozitiiLivrare.codFurnizorProduse = codFurnizorProd;

					}

					slidingDrawer.animateClose();

					numeClientText.setText("");
					codClientText.setText("");
					listClienti.clear();

					labelCodClient.setVisibility(View.GONE);
					labelNumeClient.setVisibility(View.GONE);

					saveClntBtn.setVisibility(View.GONE);

				}

			}
		});

	}

	public void fillJudete() {
		try {

			HashMap<String, String> temp;
			int i = 0;

			temp = new HashMap<String, String>();
			temp.put("numeJudet", "Selectati judetul");
			temp.put("codJudet", " ");
			listJudete.add(temp);

			for (i = 0; i < judete.length; i++) {
				temp = new HashMap<String, String>();
				temp.put("numeJudet", judete[i]);
				temp.put("codJudet", codJudete[i]);
				listJudete.add(temp);

			}

			spinnerJudetDL.setAdapter(adapterJudete);

		} catch (Exception ex) {
			Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public class regionSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			artMap = (HashMap<String, String>) adapterJudete.getItem(pos);
			CreareDispozitiiLivrare.codJudet = artMap.get("codJudet");

			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("codJudet", CreareDispozitiiLivrare.codJudet);

			operatiiAdresa.getAdreseJudet(params, null);

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public void addTxtOrasListener() {

		txtOras.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				try {

					CreareDispozitiiLivrare.oras = txtOras.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtStradaListener() {

		txtStrada.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {

					CreareDispozitiiLivrare.strada = txtStrada.getText().toString().isEmpty() ? " " : txtStrada.getText().toString();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtPersContListener() {

		txtPersCont.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {

					CreareDispozitiiLivrare.persCont = txtPersCont.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtTelefonListener() {

		txtTelefon.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				try {

					CreareDispozitiiLivrare.telefon = txtTelefon.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtTipMarfaListener() {

		txtTipMarfa.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {
					CreareDispozitiiLivrare.tipMarfa = txtTipMarfa.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtMasaMarfaListener() {

		txtMasaMarfa.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {

					CreareDispozitiiLivrare.masaMarfa = txtMasaMarfa.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});

	}

	public void addTxtDataLivrareListener() {

		txtDataLivrare.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// TODO

				try {

					CreareDispozitiiLivrare.dataLivrare = txtDataLivrare.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

		});

		txtDataLivrare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();

					DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener, today.year, today.month, today.monthDay);

					dialog.getDatePicker().setMinDate(new Date().getTime() - 1000);

					dialog.show();
				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

	}

	public class OnSelectTipTransport implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (pos == 0) {
				spinnerTonaj.setVisibility(View.VISIBLE);
			} else {
				spinnerTonaj.setVisibility(View.INVISIBLE);
				spinnerTonaj.setSelection(0);
			}

			String[] varTransport = spinnerTransp.getSelectedItem().toString().split("-");
			CreareDispozitiiLivrare.tipTransport = varTransport[0].trim();

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			Calendar clnd = new GregorianCalendar(mYear, mMonth, mDay);

			txtDataLivrare.setText(format.format(clnd.getTime()));
			CreareDispozitiiLivrare.dataLivrare = txtDataLivrare.getText().toString();

		}
	};

	public class filialaSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			artMap = (HashMap<String, String>) adapterFiliale.getItem(pos);
			CreareDispozitiiLivrare.codFilialaDest = artMap.get("codJudet");

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public void addListenerRadioClient() {
		radioClient.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					textSelClient.setText("");
					textSelClient.setVisibility(View.VISIBLE);
					labelClient.setText("Client");

					spinnerJudetDL.setEnabled(true);
					txtOras.setEnabled(true);
					txtStrada.setEnabled(true);
					txtPersCont.setEnabled(true);
					txtTelefon.setEnabled(true);

					spinnerDepozDl_Dest.setSelection(9);

					layoutAgenti.setVisibility(View.VISIBLE);
					spinnerAgentiDl.setSelection(0);

				}

			}
		});
	}

	public void addListenerRadioFiliala() {
		radioFiliala.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					CreareDispozitiiLivrare.codClient = UserInfo.getInstance().getUnitLog();
					textSelClient.setText(UserInfo.getInstance().getUnitLog());
					labelClient.setText("Filiala");

					spinnerJudetDL.setEnabled(false);
					spinnerJudetDL.setSelection(0);
					CreareDispozitiiLivrare.codJudet = " ";

					txtOras.setEnabled(false);
					txtOras.setText("");
					CreareDispozitiiLivrare.oras = " ";

					txtStrada.setEnabled(false);
					txtStrada.setText("");
					CreareDispozitiiLivrare.strada = " ";

					txtPersCont.setEnabled(false);
					txtPersCont.setText("");
					CreareDispozitiiLivrare.persCont = " ";

					txtTelefon.setEnabled(false);
					txtTelefon.setText("");
					CreareDispozitiiLivrare.telefon = " ";

					spinnerDepozDl_Dest.setSelection(0);

					layoutAgenti.setVisibility(View.GONE);

				}

			}
		});
	}

	public void addListenerTextSelClient() {
		textSelClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					if (radioClient.isChecked()) {
						txtNumeClient.setHint("Introduceti nume client");
						slidingDrawer.setVisibility(View.VISIBLE);
						slidingDrawer.animateOpen();
						tipCautare = "C";
						labelTipClient.setText("Selectie client");
					}

				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

	}

	public void addListenerTextSelFurnizor() {
		textSelFurnizor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					txtNumeClient.setHint("Introduceti nume furnizor");
					slidingDrawer.setVisibility(View.VISIBLE);
					slidingDrawer.animateOpen();
					tipCautare = "F";
					labelTipClient.setText("Selectie furnizor");
					layoutClient.setVisibility(View.INVISIBLE);

				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

	}

	public void addListenerTextSelFurnizorProd() {
		textSelFurnizorProd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					if (textSelFurnizor.getText().toString().trim().equals("")) {
						Toast toast = Toast.makeText(getActivity(), "Selectati furnizorul!", Toast.LENGTH_LONG);
						toast.show();
						return;
					}

					txtNumeClient.setHint("Introduceti nume furnizor produse");
					slidingDrawer.setVisibility(View.VISIBLE);
					slidingDrawer.animateOpen();
					tipCautare = "FP";
					labelTipClient.setText("Selectie furnizor produse");

				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

	}

	private void addListenerTipPlata() {
		spinnerTipPlata.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				if (pos == 4) {
					layoutValoareClp.setVisibility(View.VISIBLE);
				} else {
					layoutValoareClp.setVisibility(View.GONE);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void performGetAgenti() {
		try {
			getAgenti agenti = new getAgenti(getActivity());
			agenti.execute("dummy");
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private class getAgenti extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getAgenti(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Incarcare lista agenti...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject("http://SmartScan.org/", "getListAgenti");

				request.addProperty("filiala", UserInfo.getInstance().getUnitLog());
				request.addProperty("depart", UserInfo.getInstance().getCodDepart());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE("http://10.1.0.58/androidwebservices/service1.asmx", 60000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call("http://SmartScan.org/" + "getListAgenti", envelope, headerList);
				Object result = envelope.getResponse();
				response = result.toString();
			} catch (Exception e) {
				errMessage = e.getMessage();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;

				}

				if (errMessage.length() > 0) {
					Toast toast = Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					populateAgentiList(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void setAdresaLivrare(Address address) {

		if (address.getCity() != null && !address.getCity().isEmpty())
			txtOras.setText(address.getCity());

		String strStrada = address.getStreet().trim();

		if (address.getNumber() != null && address.getNumber().length() > 0)
			strStrada += " nr " + address.getNumber();

		txtStrada.setText(strStrada);

	}

	private void populateListLocalitati(BeanAdreseJudet listAdrese) {

		String[] arrayLocalitati = listAdrese.getListLocalitati().toArray(new String[listAdrese.getListLocalitati().size()]);
		ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		txtOras.setThreshold(0);
		txtOras.setAdapter(adapterLoc);

		String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
		ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayStrazi);

		txtStrada.setThreshold(0);
		txtStrada.setAdapter(adapterStrazi);

	}

	protected void populateAgentiList(String agentiList) {

		if (!agentiList.equals("-1") && agentiList.length() > 0) {

			listAgenti.clear();

			spinnerAgentiDl.setOnItemSelectedListener(new OnSelectAgent());

			HashMap<String, String> temp;
			String[] tokenLinie = agentiList.split("@@");
			String[] tokenClient;
			String client = "";

			temp = new HashMap<String, String>();
			temp.put("numeAgent", "Selectati un agent");
			temp.put("codAgent", " ");
			listAgenti.add(temp);

			int selectedAgentIndex = -1;
			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");

				temp.put("numeAgent", tokenClient[0]);
				temp.put("codAgent", tokenClient[1]);

				if (tokenClient[1].equals(CreareDispozitiiLivrare.selectedAgent)) {
					selectedAgentIndex = i;
				}

				listAgenti.add(temp);
			}

			spinnerAgentiDl.setAdapter(adapterAgenti);

			if (selectedAgentIndex != -1) {
				spinnerAgentiDl.setSelection(selectedAgentIndex + 2);
			}

		} else {
			Toast.makeText(getActivity(), "Nu exista agenti!", Toast.LENGTH_SHORT).show();
		}

	}

	// captare eveniment spinner agenti
	public class OnSelectAgent implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			String[] tokenAgent = spinnerAgentiDl.getSelectedItem().toString().split(",");
			String agentNr = tokenAgent[0].substring(tokenAgent[0].indexOf('=') + 1, tokenAgent[0].length());

			if (agentNr.trim().equals(""))
				agentNr = "0"; // fara selectie
			if (agentNr.equals("00000000"))
				agentNr = "0";

			CreareDispozitiiLivrare.selectedAgent = agentNr;

		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public static void clearClientData() {
		spinnerJudetDL.setSelection(0);
		spinnerDepozDl_Dest.setSelection(0);
		textSelClient.setText("");
		txtOras.setText("");
		txtStrada.setText("");
		txtPersCont.setText("");
		txtTelefon.setText("");
		textSelFurnizor.setText("");
		txtTipMarfa.setText("");
		txtMasaMarfa.setText("");
		txtValoareCLP.setText("");
		txtObservatiiClp.setText("");
		textSelFurnizorProd.setText("");

		spinnerTipPlata.setSelection(0);
		radioClient.performClick();

		spinnerAgentiDl.setSelection(0);

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
		txtDataLivrare.setText(sdf.format(new Date()));
	}

	
	private void initLocale()
	{
		Locale locale = new Locale("en", "US");
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
	}
	
	public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {
		switch (numeComanda) {
		case GET_ADRESE_JUDET:
			populateListLocalitati(operatiiAdresa.deserializeListAdrese(result));
			break;
		default:
			break;
		}

	}

	
	public void addressSelected(LatLng coord, android.location.Address address) {
		coordAdresa = coord;
		setAdresaLivrare(MapUtils.getAddress(address));
	}

}
