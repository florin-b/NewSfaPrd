package my.logon.screen.screens;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ClientSelection;
import my.logon.screen.listeners.ClientSelectionListener;
import my.logon.screen.listeners.GenericSpinnerSelection;
import my.logon.screen.listeners.SpinnerSelectionListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import my.logon.screen.R;
import my.logon.screen.utils.UtilsGeneral;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import my.logon.screen.beans.BeanAdresaGpsClient;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.enums.EnumTipAdresa;
import my.logon.screen.enums.EnumTipAlert;

public class AdaugaAdresaGpsClienti extends Fragment implements ClientSelectionListener, SpinnerSelectionListener,
		AsyncTaskListener, OnClickListener {

	private Button btnCautaClient;
	private ArrayList<HashMap<String, String>> listClienti = null;
	private SimpleAdapter adapterClienti;
	private ListView listViewClienti;
	private TextView textNumeClient;
	private Button btnSaveGpsData;
	private String globalCodClient, globalTipAdresa;
	private ListView listViewAdreseClient;
	private SimpleAdapter adapterAdrese;
	private View selectedItemView;
	private ArrayList<BeanAdresaGpsClient> adreseArray;
	private ImageButton btnDeleteAdr;
	private String globalSelectedAdrId = "";
	private boolean isAdrSediuSocial = false;
	private LinearLayout layoutDetClient;
	private Spinner spinnerLocatie;
	private boolean newAddress = false;
	private static int METERS_RANGE = 100;

	private ClientSelection clientSelected = new ClientSelection();
	private GenericSpinnerSelection itemSelected = new GenericSpinnerSelection();

	private View v;
	private EditText txtNumeClient;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		v = inflater.inflate(R.layout.locatie_client, container, false);

		// v.setBackgroundResource(R.drawable.articole64);

		addLayoutComponents();
		selectedItemView = new View(getActivity());
		clientSelected.setClientSelectionListener(this);
		itemSelected.setItemSelectionListener(this);

		return v;

	}

	private void addLayoutComponents() {

		txtNumeClient = (EditText) v.findViewById(R.id.txtNumeClient);
		txtNumeClient.setHint("Introduceti nume client");

		listClienti = new ArrayList<HashMap<String, String>>();
		adapterClienti = new SimpleAdapter(getActivity(), listClienti, R.layout.customrownumeclient, new String[] {
				"numeClient", "codClient", "tipClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient,
				R.id.textTipClient });

		btnCautaClient = (Button) v.findViewById(R.id.btnCautaClient);
		addListenerBtnCautaClient();

		listViewClienti = (ListView) v.findViewById(R.id.listViewClienti);
		listViewClienti.setOnItemClickListener(clientSelected);

		layoutDetClient = (LinearLayout) v.findViewById(R.id.layoutDetClient);
		layoutDetClient.setVisibility(View.INVISIBLE);

		textNumeClient = (TextView) v.findViewById(R.id.textNumeClient);

		addSpinnerLocatie();

		btnSaveGpsData = (Button) v.findViewById(R.id.btnSaveGpsData);
		btnSaveGpsData.setOnClickListener(this);

		listViewAdreseClient = (ListView) v.findViewById(R.id.listAdreseClient);
		listViewAdreseClient.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedItemView.setBackgroundResource(0);
				view.setBackgroundResource(R.color.pressed_item_color);
				selectedItemView = view;

				HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(position);
				setSelectedAdrId(artMap.get("id").replace(".", ""));

				//getGPSImage();

			}
		});

		btnDeleteAdr = (ImageButton) v.findViewById(R.id.deleteAdrBtn);
		btnDeleteAdr.setVisibility(View.INVISIBLE);
		btnDeleteAdr.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (getSelectedAdrId().equals("")) {
					UtilsGeneral.showCustomToast(getActivity(), "Selectati o adresa.", EnumTipAlert.Warning);
				} else {
					showAlertStergeAdresa();
				}

			}
		});

	}

	private void getGPSImage() {
		HashMap<String, String> params = new HashMap<String, String>();
		AsyncTaskListener contextListener = (AsyncTaskListener) AdaugaAdresaGpsClienti.this;

		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getGps", params);
		call.getCallResultsFromFragment();
	}

	private void removeAdrClient(String clientId) {

		if (adreseArray.size() > 0) {

			JSONObject adresaClient = null;

			for (int i = 0; i < adreseArray.size(); i++) {

				if (adreseArray.get(i).getId().equals(clientId)) {

					adresaClient = new JSONObject();

					try {
						adresaClient.put("codClient", adreseArray.get(i).getCodClient());
						adresaClient.put("codAgent", adreseArray.get(i).getCodAgent());
						adresaClient.put("data", adreseArray.get(i).getData());
						adresaClient.put("ora", adreseArray.get(i).getOra());
					} catch (JSONException e) {
						UtilsGeneral.showCustomToast(getActivity(), e.toString(), EnumTipAlert.Error);
					}

					break;
				}

			}

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("adresa", adresaClient.toString());
			AsyncTaskListener contextListener = (AsyncTaskListener) AdaugaAdresaGpsClienti.this;

			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "deleteAdresaGpsClient", params);
			call.getCallResultsFromFragment();

		}

	}

	private BeanAdresaGpsClient getAdresaClientObject() {

		BeanAdresaGpsClient adresa = new BeanAdresaGpsClient();

		String dateGps = getGpsLocation();

		if (dateGps.equals("-1")) {
			UtilsGeneral.showCustomToast(getActivity(), "Activati conexiunea GPS.", EnumTipAlert.Warning);
			return null;
		}

		if (dateGps.equals("0")) {
			UtilsGeneral.showCustomToast(getActivity(), "Coordonate GPS invalide. Salvati din nou.", EnumTipAlert.Warning);
			return null;
		}

		if (getCodClient().equals("")) {
			UtilsGeneral.showCustomToast(getActivity(), "Selectati clientul.", EnumTipAlert.Warning);
			return null;
		}

		if (getTipAdresa().equals("")) {
			UtilsGeneral.showCustomToast(getActivity(), "Selectati tipul de adresa.", EnumTipAlert.Warning);
			return null;
		}

		adresa.setCodAgent(UserInfo.getInstance().getCod());
		adresa.setCodClient(getCodClient());
		adresa.setDateGps(dateGps);
		adresa.setTipLocatie(getTipAdresa());

		return adresa;
	}

	private String getGpsLocation() {
		String gpsLocation = "";

		GPSLocator mGPS = new GPSLocator(getActivity());

		if (mGPS.canGetLocation) {
			double mLat = mGPS.getLatitude();
			double mLong = mGPS.getLongitude();

			if (mLat != 0.0)
				gpsLocation = String.valueOf(mLat) + "," + String.valueOf(mLong);
			else
				gpsLocation = "0";

		} else {
			gpsLocation = "-1";
		}

		return gpsLocation;
	}

	private void addSpinnerLocatie() {
		spinnerLocatie = (Spinner) v.findViewById(R.id.spinnerTipAdresa);

		ArrayList<HashMap<String, String>> listLocatie = new ArrayList<HashMap<String, String>>();

		SimpleAdapter adapterLocatie = new SimpleAdapter(getActivity(), listLocatie, R.layout.generic_rowlayout,
				new String[] { "stringName", "stringId" }, new int[] { R.id.textName, R.id.textId });

		HashMap<String, String> temp;

		for (EnumTipAdresa locatie : EnumTipAdresa.values()) {
			temp = new HashMap<String, String>();
			temp.put("stringName", locatie.getName());
			temp.put("stringId", locatie.getCode());
			listLocatie.add(temp);

		}

		spinnerLocatie.setAdapter(adapterLocatie);
		spinnerLocatie.setOnItemSelectedListener(itemSelected);

	}

	private void addListenerBtnCautaClient() {
		btnCautaClient.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (txtNumeClient.length() > 0) {
					performListClients();
				} else {
					UtilsGeneral.showCustomToast(getActivity(), "Introduceti nume client!", EnumTipAlert.Warning);
				}

			}
		});
	}

	private void performListClients() {
		try {

			String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codAgent", UserInfo.getInstance().getCod());
			params.put("numeClient", numeClient);

			AsyncTaskListener contextListener = (AsyncTaskListener) AdaugaAdresaGpsClienti.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getListaClienti", params);
			call.getCallResultsFromFragment();

		} catch (Exception e) {
			UtilsGeneral.showCustomToast(getActivity(), e.toString(), EnumTipAlert.Error);
		}
	}

	public void populateListViewClient(String clientResponse) {

		layoutDetClient.setVisibility(View.INVISIBLE);

		HandleJSONData objClientList = new HandleJSONData(getActivity(), clientResponse);
		ArrayList<BeanClient> clientArray = objClientList.decodeJSONClientList();

		listClienti.clear();
		adapterClienti.notifyDataSetChanged();

		if (clientArray.size() > 0) {
			HashMap<String, String> temp;

			for (int i = 0; i < clientArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("numeClient", clientArray.get(i).getNumeClient());
				temp.put("codClient", clientArray.get(i).getCodClient());
				temp.put("tipClient", "");
				listClienti.add(temp);
			}

			listViewClienti.setAdapter(adapterClienti);
		} else {
			UtilsGeneral.showCustomToast(getActivity(), "Nu exista inregistrari!", EnumTipAlert.Info);

		}

	}

	public void selectedClient(String codClient, String numeClient, View listView, View parentView) {

		layoutDetClient.setVisibility(View.VISIBLE);
		spinnerLocatie.setSelection(0);
		textNumeClient.setText(numeClient);
		getAdreseGpsClient(codClient);
		setCodClient(codClient);

	}

	private void getAdreseGpsClient(String codClient) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("codClient", codClient);

		AsyncTaskListener contextListener = (AsyncTaskListener) AdaugaAdresaGpsClienti.this;

		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getAdreseGpsClient", params);
		call.getCallResultsFromFragment();

	}

	public void selectedItem(View spinner, View parentView, String cod, String nume) {
		if (spinner.getId() == R.id.spinnerTipAdresa) {
			setTipAdresa(cod);

		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getAdreseGpsClient")) {

			if (result.equals("-1")) {
				UtilsGeneral.showCustomToast(getActivity(), "Eroare citire date.", EnumTipAlert.Error);
			} else {
				populateListViewAdrese((String) result);

				if (isNewAddress()) {
					addAdresaClient();
				}

			}
		}
		if (methodName.equals("setAdresaGpsClient")) {

			if (result.equals("-1")) {
				UtilsGeneral.showCustomToast(getActivity(), "Eroare salvare date.", EnumTipAlert.Error);
			} else {
				setNewAddress(false);
				UtilsGeneral.showCustomToast(getActivity(), "Date salvate.", EnumTipAlert.Info);
				getAdreseGpsClient(getCodClient());
			}
		}
		if (methodName.equals("deleteAdresaGpsClient")) {
			if (result.equals("-1")) {
				UtilsGeneral.showCustomToast(getActivity(), "Eroare stergere date.", EnumTipAlert.Error);
			} else {

				getAdreseGpsClient(getCodClient());
			}
		}

		if (methodName.equals("getListaClienti")) {
			populateListViewClient((String) result);
		}

		if (methodName.equals("getGps")) {
			setCustomBackground((String) result);
		}

	}

	private void setCustomBackground(String backImage) {

		byte[] bloc = Base64.decode(backImage, Base64.DEFAULT);
		Bitmap bmp = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
		BitmapDrawable bmd1 = new BitmapDrawable(this.getResources(), bmp);
		layoutDetClient.setBackgroundDrawable(bmd1);

	}

	public String getCodClient() {
		return globalCodClient;
	}

	public void setCodClient(String globalCodClient) {
		this.globalCodClient = globalCodClient;
	}

	public String getTipAdresa() {
		return globalTipAdresa;
	}

	public void setTipAdresa(String globalTipAdresa) {
		this.globalTipAdresa = globalTipAdresa;
	}

	public String getSelectedAdrId() {
		return globalSelectedAdrId;
	}

	public void setSelectedAdrId(String globalSelectedAdrId) {
		this.globalSelectedAdrId = globalSelectedAdrId;
	}

	public boolean isNewAddress() {
		return newAddress;
	}

	public void setNewAddress(boolean isNewAddress) {
		this.newAddress = isNewAddress;
	}

	private void populateListViewAdrese(String adreseList) {

		setSelectedAdrId("");

		HandleJSONData objAdreseList = new HandleJSONData(getActivity(), adreseList);
		adreseArray = objAdreseList.decodeJSONAdreseGpsClient();

		ArrayList<HashMap<String, String>> listAdrese = new ArrayList<HashMap<String, String>>();
		adapterAdrese = new SimpleAdapter(getActivity(), listAdrese, R.layout.customrow_adresaclient, new String[] {
				"id", "tipAdresa", "adresa" }, new int[] { R.id.textId, R.id.textTipAdresa, R.id.textAdresa });
		isAdrSediuSocial = false;

		if (adreseArray.size() > 0) {

			btnDeleteAdr.setVisibility(View.VISIBLE);

			listAdrese.clear();
			adapterAdrese.notifyDataSetChanged();

			HashMap<String, String> temp;

			for (int i = 0; i < adreseArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("id", adreseArray.get(i).getId() + ".");

				temp.put(
						"tipAdresa",
						EnumTipAdresa.valueOf(
								"L_" + String.valueOf(Integer.valueOf(adreseArray.get(i).getTipLocatie()))).getName());

				temp.put("adresa", adreseArray.get(i).getAdresa());

				if (adreseArray.get(i).getTipLocatie().equals("01"))
					isAdrSediuSocial = true;

				listAdrese.add(temp);
			}

			listViewAdreseClient.setAdapter(adapterAdrese);

		} else {
			listAdrese.clear();
			listViewAdreseClient.setAdapter(adapterAdrese);
			adapterAdrese.notifyDataSetChanged();
			btnDeleteAdr.setVisibility(View.INVISIBLE);
		}

	}

	private void performSaveGpsData() {

		BeanAdresaGpsClient selectedClient = getAdresaClientObject();

		if (selectedClient != null) {

			if (isAdrSediuSocial && selectedClient.getTipLocatie().equals("01")) {
				UtilsGeneral.showCustomToast(getActivity(), "Sediul social este salvat deja.", EnumTipAlert.Warning);
				return;
			}
			checkAdresaNoua(selectedClient);

		}

	}

	private void addAdresaClient() {
		BeanAdresaGpsClient selectedClient = getAdresaClientObject();
		JSONObject adresaClient = new JSONObject();

		try {
			adresaClient.put("codClient", selectedClient.getCodClient());
			adresaClient.put("codAgent", selectedClient.getCodAgent());
			adresaClient.put("tipLocatie", selectedClient.getTipLocatie());
			adresaClient.put("dateGps", selectedClient.getDateGps());
		} catch (JSONException e) {
			UtilsGeneral.showCustomToast(getActivity(), e.toString(), EnumTipAlert.Error);
		}

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("adresa", adresaClient.toString());

		AsyncTaskListener contextListener = (AsyncTaskListener) AdaugaAdresaGpsClienti.this;

		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "setAdresaGpsClient", params);
		call.getCallResultsFromFragment();

	}

	private void checkAdresaNoua(BeanAdresaGpsClient selectedClient) {

		double distance = 0;
		boolean canSaveThisAddr = true;

		if (adreseArray.size() > 0) {
			Location locStart = new Location("");
			Location locEnd = new Location("");
			String[] arrayCoord;

			for (int ii = 0; ii < adreseArray.size(); ii++) {
				arrayCoord = selectedClient.getDateGps().split(",");
				locStart.setLatitude(Double.valueOf(arrayCoord[0]));
				locStart.setLongitude(Double.valueOf(arrayCoord[0]));

				arrayCoord = adreseArray.get(ii).getDateGps().split(",");
				locEnd.setLatitude(Double.valueOf(arrayCoord[0]));
				locEnd.setLongitude(Double.valueOf(arrayCoord[0]));
				distance = locEnd.distanceTo(locStart);

				if (distance >= 0 && distance <= METERS_RANGE) {
					showAlertAdresa(ii, distance);
					canSaveThisAddr = false;
					break;
				}

			}

		}

		if (canSaveThisAddr) {
			addAdresaClient();
		}

	}

	private void showAlertStergeAdresa() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		String warningMessage = "Stergeti adresa ?";

		builder.setMessage(warningMessage).setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						removeAdrClient(getSelectedAdrId());

					}
				}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				}).setTitle("Atentie").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	private void showAlertAdresa(final int clientId, double distance) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		String stringDistance;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(1);
		nf.setMaximumFractionDigits(1);

		if (distance > 1000) {
			distance = distance / 1000;
			stringDistance = nf.format(distance) + " km";
		} else {
			stringDistance = nf.format(distance) + " m";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("O alta adresa aflata la " + stringDistance + " fata de pozitia actuala este salvata deja.\n");
		sb.append("Inlocuiti adresa?\n");

		builder.setMessage(sb.toString()).setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setNewAddress(true);
						removeAdrClient(adreseArray.get(clientId).getId());

					}
				}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setNewAddress(false);
						dialog.cancel();

					}
				}).setTitle("Atentie").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void onClick(View v) {

		if (v.getId() == R.id.btnSaveGpsData) {
			performSaveGpsData();

		}

	}

}
