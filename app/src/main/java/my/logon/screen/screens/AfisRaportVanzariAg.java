/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.VanzariAgenti;
import my.logon.screen.adapters.VanzariArticoleAdapter;
import my.logon.screen.adapters.VanzariDocumenteAdapter;
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
import my.logon.screen.beans.VanzariArticol;
import my.logon.screen.beans.VanzariDocument;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.R;

public class AfisRaportVanzariAg extends Fragment implements AsyncTaskListener {

	static ListView listViewRapVanz;
	public static ListView listViewCmdArt;

	public static ArrayList<HashMap<String, String>> arrayListRapVanz = null;
	VanzariDocumenteAdapter adapterVanzDocumente;
	VanzariArticoleAdapter adapterVanzArticole;

	LinearLayout layout_Docs, layout_Arts, layoutArtDoc;
	Integer listViewSelPos = -1;
	String selectedDocument = "-1";

	ArrayList<HashMap<String, String>> listDetArticole = null;
	public static SlidingDrawer articoleDocSlidingDrawer;

	public static String[] rapHeader1 = { "nrCrt", "numeClient", "codClient", "numeAgent", "nrDocument", "dataDocument", "valoareDocument" };
	public static String[] rapHeader2 = { "nrCrt", "numeAgent", "numeArt", "codArt", "cantArt", "valArt" };

	public static String upload = " ";
	public static String GalleryImage;
	public ArrayList<ListItem> myItems = new ArrayList<ListItem>();
	public static TextView textSelectedDocArt, textSelectedClientArt, textSelectedAgentArt, textSelectedDataArt, textSelectedValoareArt;
	static View selectedItemView;

	public static final AfisRaportVanzariAg newInstance() {

		AfisRaportVanzariAg f = new AfisRaportVanzariAg();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.afis_raport_vanz_ag, container, false);

		try {

			listViewRapVanz = (ListView) v.findViewById(R.id.listArtRapVanzAg);

			layoutArtDoc = (LinearLayout) v.findViewById(R.id.layout_artvmd_vanz);
			listViewCmdArt = (ListView) layoutArtDoc.findViewById(R.id.listViewCmdArt);

			articoleDocSlidingDrawer = (SlidingDrawer) v.findViewById(R.id.articoleDocSlidingDrawer);

			selectedItemView = new View(getActivity());

			textSelectedDocArt = (TextView) v.findViewById(R.id.textSelectedDocArt);
			textSelectedDocArt.setText("");

			textSelectedClientArt = (TextView) v.findViewById(R.id.textSelectedClientArt);
			textSelectedClientArt.setText("");

			textSelectedAgentArt = (TextView) v.findViewById(R.id.textSelectedAgentArt);
			textSelectedAgentArt.setText("");

			textSelectedDataArt = (TextView) v.findViewById(R.id.textSelectedDataArt);
			textSelectedDataArt.setText("");

			textSelectedValoareArt = (TextView) v.findViewById(R.id.textSelectedValoareArt);
			textSelectedValoareArt.setText("");

			arrayListRapVanz = new ArrayList<HashMap<String, String>>();

			// afisare pe documente
			if (0 == VanzariAgenti.getInstance().articolListCode.size()) {
				layout_Arts = (LinearLayout) v.findViewById(R.id.layoutHeaderVanz_Arts);
				layout_Arts.setVisibility(View.GONE);

				layout_Docs = (LinearLayout) v.findViewById(R.id.layoutHeaderVanz_Docs);
				layout_Docs.setVisibility(View.VISIBLE);

				adapterVanzDocumente = new VanzariDocumenteAdapter(getActivity(), new ArrayList<VanzariDocument>());
				listViewRapVanz.setAdapter(adapterVanzDocumente);

			} else // afisare pe articole/sintetice
			{
				layout_Arts = (LinearLayout) v.findViewById(R.id.layoutHeaderVanz_Arts);
				layout_Arts.setVisibility(View.VISIBLE);

				layout_Docs = (LinearLayout) v.findViewById(R.id.layoutHeaderVanz_Docs);
				layout_Docs.setVisibility(View.GONE);

				adapterVanzArticole = new VanzariArticoleAdapter(getActivity(), new ArrayList<VanzariArticol>());
				listViewRapVanz.setAdapter(adapterVanzArticole);

				articoleDocSlidingDrawer.setVisibility(View.GONE);

			}

			if (VanzariAgenti.getInstance().selectedFiliala.equals("-1")) {
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

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("reportParams", VanzariAgenti.getInstance().getReportParamObject());

			AsyncTaskListener contextListener = (AsyncTaskListener) AfisRaportVanzariAg.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getRaportVanzariAgData", params);
			call.getCallResultsFromFragment();

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void fillRaportData(String reportData) {
		if (reportData.contains("@@")) {

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);

			String[] mainToken = reportData.split("@@");
			String[] tokenLinie;
			double totalVanzari = 0;

			// afisare pe document
			if (VanzariAgenti.getInstance().articolListCode.size() == 0 || VanzariAgenti.getInstance().tipComanda.equals("A")) {

				List<VanzariDocument> listDocumente = new ArrayList<VanzariDocument>();
				VanzariDocument document;

				for (int i = 0; i < mainToken.length; i++) {

					tokenLinie = mainToken[i].split("#");

					document = new VanzariDocument();
					document.setNumeClient(tokenLinie[1]);
					document.setCodClient(tokenLinie[4]);
					document.setNumeAgent(tokenLinie[3]);
					document.setNrDocument(tokenLinie[0]);
					document.setDataDocument(tokenLinie[2]);
					document.setValoareDocument(tokenLinie[5]);

					totalVanzari += Double.valueOf(tokenLinie[5]);

					listDocumente.add(document);

				}

				document = new VanzariDocument();
				document.setNumeClient("");
				document.setCodClient("");
				document.setNumeAgent("");
				document.setNrDocument("");
				document.setDataDocument("Total:");
				document.setValoareDocument(String.valueOf(totalVanzari));
				listDocumente.add(document);

				adapterVanzDocumente.setListDocumente(listDocumente);
				listViewRapVanz.setAdapter(adapterVanzDocumente);

			}
			// afisare pe articol
			else {

				List<VanzariArticol> listVanzari = new ArrayList<VanzariArticol>();
				VanzariArticol articol;

				for (int i = 0; i < mainToken.length; i++) {

					tokenLinie = mainToken[i].split("#");

					articol = new VanzariArticol();
					articol.setNumeAgent(tokenLinie[2]);
					articol.setNumeArt(tokenLinie[1]);
					articol.setCodArt(tokenLinie[0]);
					articol.setCantArt(tokenLinie[3]);
					articol.setValArt(tokenLinie[4]);
					listVanzari.add(articol);

					totalVanzari += Double.valueOf(tokenLinie[4]);

				}

				articol = new VanzariArticol();
				articol.setNumeAgent("");
				articol.setNumeArt("");
				articol.setCodArt("");
				articol.setCantArt("Total: ");
				articol.setValArt(String.valueOf(totalVanzari));
				listVanzari.add(articol);

				adapterVanzArticole.setListArticole(listVanzari);
				listViewRapVanz.setAdapter(adapterVanzArticole);

			}

		}
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getRaportVanzariAgData")) {
			fillRaportData((String) result);
		}

	}

}
