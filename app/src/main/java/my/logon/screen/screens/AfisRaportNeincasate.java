/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.Fragment;
import android.app.LauncherActivity.ListItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.adapters.NeincasateAdapter;
import my.logon.screen.beans.BeanFacturaNeincasata;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;

public class AfisRaportNeincasate extends Fragment implements AsyncTaskListener {

	private String reportParams = "0";
	static ListView listViewNeincasate;
	public static ListView listViewCmdArt;

	public static ArrayList<HashMap<String, String>> arrayListNeincasate = null;
	NeincasateAdapter adapterNeincasate;

	LinearLayout layout_Docs, layout_Arts, layoutArtDoc;
	Integer listViewSelPos = -1;
	String selectedDocument = "-1";

	ArrayList<HashMap<String, String>> listDetArticole = null;
	static SlidingDrawer articoleDocSlidingDrawer;

	public static String[] rapHeader1 = { "nrCrt", "numeClient", "codClient", "numeAgent", "nrDocument",
			"dataDocument", "valoareDocument" };
	public static String[] rapHeader2 = { "nrCrt", "numeAgent", "numeArt", "codArt", "cantArt", "valArt" };

	public static String upload = " ";
	public static String GalleryImage;
	public ArrayList<ListItem> myItems = new ArrayList<ListItem>();
	public static TextView textSelectedDocArt, textSelectedClientArt, textSelectedAgentArt, textSelectedDataArt,
			textSelectedValoareArt;
	static View selectedItemView;

	public static final AfisRaportNeincasate newInstance() {

		AfisRaportNeincasate f = new AfisRaportNeincasate();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.afis_raport_neincasate, container, false);

		try {

			listViewNeincasate = (ListView) v.findViewById(R.id.listNeincasate);

			arrayListNeincasate = new ArrayList<HashMap<String, String>>();
			adapterNeincasate = new NeincasateAdapter(getActivity(), arrayListNeincasate,
					R.layout.custom_art_row_neincasate, new String[] { "dummy", "nrCrt", "referinta", "emitere",
							"scadenta", "acoperit", "tipPlata", "scadentaBO", "valoare", "incasat", "rest" },
					new int[] { R.id.emptyView, R.id.textNrCrt, R.id.textReferinta, R.id.textEmitere,
							R.id.textScadenta, R.id.textAcoperit, R.id.textTipPlata, R.id.textScadentaBO,
							R.id.textValoare, R.id.textIncasat, R.id.textRest });

			
			listViewNeincasate.setAdapter(adapterNeincasate);

			if (Neincasate.selectedFiliala.equals("-1")) {
				Toast.makeText(getActivity(), "Selectati agentul sau filiala!", Toast.LENGTH_SHORT).show();
			} else {

				performGetRaportData();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return v;

	}

	private void performGetRaportData() {
		try {

			prepareReportData();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("reportParams", reportParams);
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

			AsyncTaskListener contextListener = (AsyncTaskListener) AfisRaportNeincasate.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getRaportNeincasateData",
					params);
			call.getCallResultsFromFragment();

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void prepareReportData() {

		String clienti = "";

		// clienti
		if (0 == Neincasate.clientListCode.size())
			clienti = "0";

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < Neincasate.clientListCode.size(); i++) {
			sb.append('#').append(Neincasate.clientListCode.get(i));
		}

		clienti = sb.length() > 0 ? sb.toString() : "0";

		reportParams = clienti + "@" + Neincasate.selectedAgent + "@";

	}

	private void fillRaportData(String reportData) {

		try {

			HandleJSONData objFacturiList = new HandleJSONData(getActivity(), reportData);
			ArrayList<BeanFacturaNeincasata> neincasateArray = objFacturiList.decodeJSONFacturiNeincasate();

			if (neincasateArray.size() > 0) {
				arrayListNeincasate.clear();

				double totalFact = 0, totalIncasat = 0, totalRest = 0, totalAcoperit = 0;
				double totalGenFact = 0, totalGenIncasat = 0, totalGenRest = 0, totalGenAcoperit = 0;

				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				nf.setMaximumFractionDigits(2);

				String codClient = "", numeAgent = "";

				HashMap<String, String> temp;

				for (int i = 0; i < neincasateArray.size(); i++) {

					// alt client
					if (!codClient.equals(neincasateArray.get(i).getCodClient())) {

						if (0 != totalRest) {

							temp = new HashMap<String, String>(50, 0.75f);
							temp.put("nrCrt", " ");
							temp.put("referinta", " ");
							temp.put("emitere", " ");
							temp.put("scadenta", " ");
							temp.put("acoperit", nf.format(totalAcoperit));
							temp.put("tipPlata", " ");
							temp.put("scadentaBO", " ");
							temp.put("valoare", nf.format(totalFact));
							temp.put("incasat", nf.format(totalIncasat));
							temp.put("rest", nf.format(totalRest));

							arrayListNeincasate.add(temp);

						}

						temp = new HashMap<String, String>(1, 0.75f);
						temp.put("nrCrt", " ");
						temp.put("referinta", neincasateArray.get(i).getNumeClient());
						temp.put("emitere", " ");
						temp.put("scadenta", " ");
						temp.put("acoperit", " ");
						temp.put("tipPlata", " ");
						temp.put("scadentaBO", " ");
						temp.put("valoare", " ");
						temp.put("incasat", " ");
						temp.put("rest", " ");

						arrayListNeincasate.add(temp);

						totalFact = 0;
						totalIncasat = 0;
						totalRest = 0;
						totalAcoperit = 0;

					}

					// alt agent
					if (Neincasate.clientListCode.size() > 0) {
						if (!numeAgent.equals(neincasateArray.get(i).getNumeAgent())) {

							if (0 != totalRest) {

								temp = new HashMap<String, String>(1, 0.75f);
								temp.put("nrCrt", " ");
								temp.put("referinta", " ");
								temp.put("emitere", " ");
								temp.put("scadenta", " ");
								temp.put("acoperit", nf.format(totalAcoperit));
								temp.put("tipPlata", " ");
								temp.put("scadentaBO", " ");
								temp.put("valoare", nf.format(totalFact));
								temp.put("incasat", nf.format(totalIncasat));
								temp.put("rest", nf.format(totalRest));

								arrayListNeincasate.add(temp);

							}

							temp = new HashMap<String, String>(30, 0.75f);
							temp.put("nrCrt", " ");
							temp.put("referinta", neincasateArray.get(i).getNumeAgent());
							temp.put("emitere", " ");
							temp.put("scadenta", " ");
							temp.put("acoperit", " ");
							temp.put("tipPlata", " ");
							temp.put("scadentaBO", " ");
							temp.put("valoare", " ");
							temp.put("incasat", " ");
							temp.put("rest", " ");

							arrayListNeincasate.add(temp);

							totalFact = 0;
							totalIncasat = 0;
							totalRest = 0;
							totalAcoperit = 0;

						}

					}

					temp = new HashMap<String, String>(50, 0.75f);
					temp.put("nrCrt", String.valueOf(i + 1) + ".");
					temp.put("referinta", neincasateArray.get(i).getReferinta());
					temp.put("emitere", neincasateArray.get(i).getEmitere());
					temp.put("scadenta", neincasateArray.get(i).getScadenta());
					temp.put("acoperit", nf.format(Double.valueOf(neincasateArray.get(i).getAcoperit())));
					temp.put("tipPlata", neincasateArray.get(i).getTipPlata());
					temp.put("scadentaBO", neincasateArray.get(i).getScadentaBO());
					temp.put("valoare", nf.format(Double.valueOf(neincasateArray.get(i).getValoare())));
					temp.put("incasat", nf.format(Double.valueOf(neincasateArray.get(i).getIncasat())));
					temp.put("rest", nf.format(Double.valueOf(neincasateArray.get(i).getRest())));

					codClient = neincasateArray.get(i).getCodClient();
					numeAgent = neincasateArray.get(i).getNumeAgent();

					totalFact += Double.parseDouble(neincasateArray.get(i).getValoare());
					totalIncasat += Double.parseDouble(neincasateArray.get(i).getIncasat());
					totalRest += Double.parseDouble(neincasateArray.get(i).getRest());
					totalAcoperit += Double.valueOf(neincasateArray.get(i).getAcoperit());

					totalGenFact += Double.parseDouble(neincasateArray.get(i).getValoare());
					totalGenIncasat += Double.parseDouble(neincasateArray.get(i).getIncasat());
					totalGenRest += Double.parseDouble(neincasateArray.get(i).getRest());
					totalGenAcoperit += Double.valueOf(neincasateArray.get(i).getAcoperit());

					arrayListNeincasate.add(temp);
				}

				if (0 != totalRest) {

					temp = new HashMap<String, String>(1, 0.75f);
					temp.put("nrCrt", " ");
					temp.put("referinta", " ");
					temp.put("emitere", " ");
					temp.put("scadenta", " ");
					temp.put("acoperit", nf.format(totalAcoperit));
					temp.put("tipPlata", " ");
					temp.put("scadentaBO", " ");
					temp.put("valoare", nf.format(totalFact));
					temp.put("incasat", nf.format(totalIncasat));
					temp.put("rest", nf.format(totalRest));

					arrayListNeincasate.add(temp);

				}

				// total general
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("referinta", " ");
				temp.put("emitere", " ");
				temp.put("scadenta", " ");
				temp.put("acoperit", " ");
				temp.put("tipPlata", " ");
				temp.put("scadentaBO", " ");
				temp.put("valoare", " ");
				temp.put("incasat", " ");
				temp.put("rest", " ");
				arrayListNeincasate.add(temp);

				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("referinta", " ");
				temp.put("emitere", " ");
				temp.put("scadenta", " ");
				temp.put("acoperit", nf.format(totalGenAcoperit));
				temp.put("tipPlata", " ");
				temp.put("scadentaBO", " ");
				temp.put("valoare", nf.format(totalGenFact));
				temp.put("incasat", nf.format(totalGenIncasat));
				temp.put("rest", nf.format(totalGenRest));
				arrayListNeincasate.add(temp);

				listViewNeincasate.setAdapter(adapterNeincasate);

			} else {
				Toast.makeText(getActivity(), "Nu exista date!", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getRaportNeincasateData")) {
			fillRaportData((String)result);
		}

	}

}
