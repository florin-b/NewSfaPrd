/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.adapters.ClientiSemiactiviAdapter;
import my.logon.screen.beans.BeanClientSemiactiv;
import my.logon.screen.dialogs.IstoricSemiactiviDialog;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;

public class AfisClientiSemiactivi extends Fragment implements AsyncTaskListener, OnClickListener {

	
	static ListView listViewClSemiactivi;

	public static ArrayList<HashMap<String, String>> arrayListClInactivi = null;
	ClientiSemiactiviAdapter adapterClSemiactivi;

	LinearLayout layoutClSemiactivi, layoutArts, layoutArtDoc;
	Integer listViewSelPos = -1;
	String selectedDocument = "-1";

	private enum ENUM_SORT_BY {
		NUME_CLIENT, JUDET, LOCALITATE, VANZARI_MEDII
	};

	public static String upload = " ";
	public static String GalleryImage;

	private TextView textNumeClient, textJudet, textLocalitate, textVanzMed;

	private boolean sortAscNumeClient = true, sortAscJudet = true, sortAscLocalitate = true, sortAscVanzMed = true;
	private ArrayList<BeanClientSemiactiv> clientiList;
	private HandleJSONData objClientList;
	private BeanClientSemiactiv clientSelected;

	public static final AfisClientiSemiactivi newInstance() {

		AfisClientiSemiactivi f = new AfisClientiSemiactivi();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.afis_clienti_semiactivi, container, false);

		try {

			listViewClSemiactivi = (ListView) v.findViewById(R.id.listClientiInactivi);
			setListenerClSemiactivi();

			this.layoutArtDoc = (LinearLayout) v.findViewById(R.id.layout_artvmd_vanz);

			arrayListClInactivi = new ArrayList<HashMap<String, String>>();

			this.layoutClSemiactivi = (LinearLayout) v.findViewById(R.id.layoutHeaderClInactivi);
			this.layoutClSemiactivi.setVisibility(View.VISIBLE);

			if (ClientiSemiactivi.selectedFiliala.equals("-1")) {
				Toast.makeText(getActivity(), "Selectati agentul sau filiala!", Toast.LENGTH_SHORT).show();
			} else {
				performGetRaportData();
			}

			addLayoutComponents(v);

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

		return v;

	}

	private void setListenerClSemiactivi() {
		listViewClSemiactivi.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				clientSelected = (BeanClientSemiactiv) parent.getAdapter().getItem(position);
				getIstoricClient(clientSelected.getCodClient());

				return false;
			}
		});

	}

	private void getIstoricClient(String codClient) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codClient", codClient);

		AsyncTaskListener contextListener = (AsyncTaskListener) AfisClientiSemiactivi.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getIstoricClientSemiactiv", params);
		call.getCallResultsFromFragment();
	}

	private void addLayoutComponents(View v) {
		textNumeClient = (TextView) v.findViewById(R.id.textNumeClient);
		textNumeClient.setOnClickListener(this);

		textJudet = (TextView) v.findViewById(R.id.textJudet);
		textJudet.setOnClickListener(this);

		textLocalitate = (TextView) v.findViewById(R.id.textLocalitate);
		textLocalitate.setOnClickListener(this);

		textVanzMed = (TextView) v.findViewById(R.id.textVanzMed);
		textVanzMed.setOnClickListener(this);

	}

	private void performGetRaportData() {

		String selectedDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			selectedDepart = "10";
		}

		if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
				|| UserInfo.getInstance().getTipAcces().equals("27")) {
			selectedDepart = "11";
		}

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codAgent", ClientiSemiactivi.selectedAgent);
		params.put("codDepart", selectedDepart);
		params.put("filiala", ClientiSemiactivi.selectedFiliala);

		AsyncTaskListener contextListener = (AsyncTaskListener) AfisClientiSemiactivi.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getClientiSemiactivi", params);
		call.getCallResultsFromFragment();

	}

	private void afisIstoric(String reportData) {
		objClientList = new HandleJSONData(getActivity(), reportData);
		objClientList.decodeIstoricSemiactivi();
		IstoricSemiactiviDialog dialog = new IstoricSemiactiviDialog(getActivity(), objClientList.decodeIstoricSemiactivi(), clientSelected.getNumeClient());
		dialog.show();

	}

	private void fillRaportData(String reportData) {

		objClientList = new HandleJSONData(getActivity(), reportData);
		clientiList = objClientList.decodeJSONClientiSemiactivi();

		if (clientiList.size() > 0) {
			adapterClSemiactivi = new ClientiSemiactiviAdapter(getActivity(), clientiList);
			listViewClSemiactivi.setAdapter(adapterClSemiactivi);

		} else {
			Toast.makeText(getActivity(), "Nu exista date!", Toast.LENGTH_LONG).show();
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getClientiSemiactivi")) {
			fillRaportData((String) result);
		}

		if (methodName.equals("getIstoricClientSemiactiv")) {
			afisIstoric((String) result);
		}

	}

	private void populateListClienti(ArrayList<BeanClientSemiactiv> listaClienti) {

		if (listaClienti != null) {
			ClientiSemiactiviAdapter adapter = new ClientiSemiactiviAdapter(getActivity(), listaClienti);
			listViewClSemiactivi.setAdapter(adapter);
		}

	}

	private void sortNumeClient() {
		setHeaderFontStyle();
		setTextIcon(textNumeClient, sortAscNumeClient);

		populateListClienti(sortList(clientiList, !sortAscNumeClient, ENUM_SORT_BY.NUME_CLIENT));

		sortAscNumeClient = !sortAscNumeClient;
		textNumeClient.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortJudet() {
		setHeaderFontStyle();
		setTextIcon(textJudet, sortAscJudet);

		populateListClienti(sortList(clientiList, !sortAscJudet, ENUM_SORT_BY.JUDET));

		sortAscJudet = !sortAscJudet;
		textJudet.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortLocalitate() {
		setHeaderFontStyle();
		setTextIcon(textLocalitate, sortAscLocalitate);

		populateListClienti(sortList(clientiList, !sortAscLocalitate, ENUM_SORT_BY.LOCALITATE));

		sortAscLocalitate = !sortAscLocalitate;
		textLocalitate.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void sortVanzariMedii() {
		setHeaderFontStyle();
		setTextIcon(textVanzMed, sortAscVanzMed);

		populateListClienti(sortList(clientiList, !sortAscVanzMed, ENUM_SORT_BY.VANZARI_MEDII));

		sortAscVanzMed = !sortAscVanzMed;
		textVanzMed.setTypeface(Typeface.DEFAULT_BOLD);
	}

	private void setTextIcon(TextView textView, boolean tipSort) {
		if (tipSort) {
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_down_green, 0, 0, 0);
		} else {
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort_up_green, 0, 0, 0);
		}
	}

	private ArrayList<BeanClientSemiactiv> sortList(ArrayList<BeanClientSemiactiv> listaClienti, boolean ascending, ENUM_SORT_BY tipSort) {

		switch (tipSort) {
		case NUME_CLIENT:
			Collections.sort(listaClienti, new BeanClientSemiactiv.CompareNumeClient(ascending));
			break;

		case JUDET:
			Collections.sort(listaClienti, new BeanClientSemiactiv.CompareJudet(ascending));
			break;

		case LOCALITATE:
			Collections.sort(listaClienti, new BeanClientSemiactiv.CompareLocalitate(ascending));
			break;

		case VANZARI_MEDII:
			Collections.sort(listaClienti, new BeanClientSemiactiv.CompareVanzMed(ascending));
			break;

		default:
			break;

		}

		return listaClienti;
	}

	private void setHeaderFontStyle() {
		textNumeClient.setTypeface(Typeface.DEFAULT);
		textJudet.setTypeface(Typeface.DEFAULT);
		textLocalitate.setTypeface(Typeface.DEFAULT);
		textVanzMed.setTypeface(Typeface.DEFAULT);

	}

	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.textNumeClient) {
			sortNumeClient();
		} else if (id == R.id.textJudet) {
			sortJudet();
		} else if (id == R.id.textLocalitate) {
			sortLocalitate();
		} else if (id == R.id.textVanzMed) {
			sortVanzariMedii();
		} else {
		}

	}

}
