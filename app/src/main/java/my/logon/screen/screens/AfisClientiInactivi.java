/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.beans.BeanClientInactiv;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.model.UserInfo;
import my.logon.screen.adapters.ClientiInactiviAdapter;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.R;

import android.app.Fragment;
import android.app.LauncherActivity.ListItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AfisClientiInactivi extends Fragment implements AsyncTaskListener {

	private String reportParams = "0";
	static ListView listViewClInactivi;

	public static ArrayList<HashMap<String, String>> arrayListClInactivi = null;
	ClientiInactiviAdapter adapterClInactivi;

	LinearLayout layoutClInactivi, layoutArts, layoutArtDoc;
	Integer listViewSelPos = -1;
	String selectedDocument = "-1";

	public static String[] rapHeader = { "nrCrt", "numeClient", "codClient", "numeAgent", "stare" };

	public static String upload = " ";
	public static String GalleryImage;
	private ArrayList<ListItem> myItems = new ArrayList<ListItem>();

	public static final AfisClientiInactivi newInstance() {

		AfisClientiInactivi f = new AfisClientiInactivi();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.afis_clienti_inactivi, container, false);

		try {

			listViewClInactivi = (ListView) v.findViewById(R.id.listClientiInactivi);

			this.layoutArtDoc = (LinearLayout) v.findViewById(R.id.layout_artvmd_vanz);

			arrayListClInactivi = new ArrayList<HashMap<String, String>>();

			this.layoutClInactivi = (LinearLayout) v.findViewById(R.id.layoutHeaderClInactivi);
			this.layoutClInactivi.setVisibility(View.VISIBLE);

			this.adapterClInactivi = new ClientiInactiviAdapter(getActivity(), arrayListClInactivi,
					R.layout.custom_art_row_clienti_inactivi, new String[] { "nrCrt", "numeClient", "codClient",
							"numeAgent", "stare", "tipClient" }, new int[] { R.id.textNrCrt, R.id.textNumeClient,
							R.id.textCodClient, R.id.textNumeAgent, R.id.textStare, R.id.textTipClient });

			listViewClInactivi.setAdapter(this.adapterClInactivi);

			if (ClientiInactivi.selectedFiliala.equals("-1")) {
				Toast.makeText(getActivity(), "Selectati agentul sau filiala!", Toast.LENGTH_SHORT).show();
			} else {

				performGetRaportData();

			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

		return v;

	}

	private void performGetRaportData() {

		prepareReportData();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reportParams", reportParams);

		AsyncTaskListener contextListener = (AsyncTaskListener) AfisClientiInactivi.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getRaportClientiInactivi", params);
		call.getCallResultsFromFragment();

	}

	private void prepareReportData() {

		String selectedDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			selectedDepart = "10";
		}

		if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
				|| UserInfo.getInstance().getTipAcces().equals("27")) {
			selectedDepart = "11";
		}

		reportParams = ClientiInactivi.selectedAgent + "@" + ClientiInactivi.selectedFiliala + "@" + selectedDepart
				+ "@";

	}

	private void fillRaportData(String reportData) {

		try {

			HandleJSONData objClientList = new HandleJSONData(getActivity(), reportData);
			ArrayList<BeanClientInactiv> clientiArray = objClientList.decodeJSONClientiInactivi();

			if (clientiArray.size() > 0) {
				arrayListClInactivi.clear();

				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				nf.setMaximumFractionDigits(2);

				HashMap<String, String> temp;

				for (int i = 0; i < clientiArray.size(); i++) {
					temp = new HashMap<String, String>(100, 0.75f);

					temp.put("nrCrt", String.valueOf(i + 1) + ".");
					temp.put("numeClient", clientiArray.get(i).getNumeClient());
					temp.put("codClient", clientiArray.get(i).getCodClient());
					temp.put("numeAgent", clientiArray.get(i).getCodAgent());
					temp.put("stare", clientiArray.get(i).getStareClient());
					temp.put("tipClient", InfoStrings.getTipClient(clientiArray.get(i).getTipClient()));

					arrayListClInactivi.add(temp);
				}

				listViewClInactivi.setAdapter(adapterClInactivi);

			} else {
				Toast.makeText(getActivity(), "Nu exista date!", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public String toString() {
		return "AfisClientiInactivi [reportParams=" + reportParams + ", adapterClInactivi=" + adapterClInactivi
				+ ", layoutClInactivi=" + layoutClInactivi + ", layoutArts=" + layoutArts + ", layoutArtDoc="
				+ layoutArtDoc + ", listViewSelPos=" + listViewSelPos + ", selectedDocument=" + selectedDocument
				+ ", myItems=" + myItems + "]";
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getRaportClientiInactivi")) {
			fillRaportData((String) result);
		}

	}

}
