/**
 * @author florinb
 *
 */
package my.logon.screen.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanArticolVanzari;
import my.logon.screen.beans.VanzariDocument;
import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.VanzariAgenti;
import my.logon.screen.screens.AfisRaportVanzariAg;

public class VanzariDocumenteAdapter extends BaseAdapter {

	private int[] colors = new int[] { 0x30D1BC71, 0x30F6F2E4 };
	private String selectedDocument = "", selectedClient = "", selectedAgent = "", selectedEmitere = "",
			selectedValoare = "";

	Context context;
	private List<VanzariDocument> listDocumente;

	NumberFormat nf;

	static class ViewHolder {
		public TextView textNrCrt, textNumeClient, textCodClient, textNumeAgent, textDocument, textDataDocument,
				textValoareDocument;
		public ImageButton btnListArtDoc;
	}

	public VanzariDocumenteAdapter(Context context, List<VanzariDocument> listDocumente) {
		this.context = context;
		this.listDocumente = listDocumente;

		nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		int colorPos = position % colors.length;

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.custom_art_row_vanzari_ag_1, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textCodClient = (TextView) convertView.findViewById(R.id.textCodClient);
			viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);
			viewHolder.textDocument = (TextView) convertView.findViewById(R.id.textDocument);
			viewHolder.textDataDocument = (TextView) convertView.findViewById(R.id.textDataDocument);
			viewHolder.textValoareDocument = (TextView) convertView.findViewById(R.id.textValoareDocument);
			viewHolder.btnListArtDoc = (ImageButton) convertView.findViewById(R.id.btnListArtDoc);

			convertView.setTag(viewHolder);
			convertView.setFocusableInTouchMode(false);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final VanzariDocument document = getItem(position);

		viewHolder.textNumeClient.setText(document.getNumeClient());
		viewHolder.textCodClient.setText(document.getCodClient());
		viewHolder.textNumeAgent.setText(document.getNumeAgent());
		viewHolder.textDocument.setText(document.getNrDocument());
		viewHolder.textDataDocument.setText(document.getDataDocument());

		if (document.getDataDocument().toLowerCase().contains("total")) {
			viewHolder.textNrCrt.setText("");
			viewHolder.btnListArtDoc.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.textNrCrt.setText(String.valueOf(position + 1));
			viewHolder.btnListArtDoc.setVisibility(View.VISIBLE);
		}

		viewHolder.textValoareDocument.setText(nf.format(Double.valueOf(document.getValoareDocument())));
		viewHolder.btnListArtDoc.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				selectedDocument = document.getNrDocument();
				selectedClient = document.getNumeClient();
				selectedAgent = document.getNumeAgent();
				selectedEmitere = document.getDataDocument();
				selectedValoare = document.getValoareDocument();

				performGetCmdArt();
			}
		});

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;

	}

	public void performGetCmdArt() {

		try {
			getCmdArt articole = new getCmdArt(context);
			articole.execute("dummy");
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private class getCmdArt extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getCmdArt(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Incarcare articole...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), "getArtCmdRapVanz");

				request.addProperty("nrDoc", selectedDocument);
				request.addProperty("tipCmd", VanzariAgenti.getInstance().tipComanda);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(),
						60000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + "getArtCmdRapVanz",
						envelope, headerList);
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
					Toast toast = Toast.makeText(context, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {

					populateArtCmdList(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}

		}

	}

	private void populateArtCmdList(String artCmd) {
		try {

			ArrayList<HashMap<String, String>> listDetArticole = new ArrayList<HashMap<String, String>>();
			ZebraAdapter_3 adapterArtDet = new ZebraAdapter_3(context, listDetArticole,
					R.layout.custom_row_cmd_art_vanz,
					new String[] { "nrCrt", "numeArt", "codArt", "cantArt", "valArt" }, new int[] { R.id.textNrCrt,
							R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt, R.id.textValArt });

			listDetArticole.clear();

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);

			HandleJSONData objArticolList = new HandleJSONData(context, artCmd);
			ArrayList<BeanArticolVanzari> articolArray = objArticolList.decodeJSONArticolVanzari();

			HashMap<String, String> temp;

			for (int i = 0; i < articolArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("numeArt", articolArray.get(i).getNumeArticol());
				temp.put("codArt", articolArray.get(i).getCodArticol());
				temp.put("cantArt", nf.format(Double.valueOf(articolArray.get(i).getCantitateArticol())));
				temp.put("valArt", nf.format(Double.valueOf(articolArray.get(i).getValoareArticol())));

				listDetArticole.add(temp);
			}

			AfisRaportVanzariAg.listViewCmdArt.setAdapter(adapterArtDet);

			AfisRaportVanzariAg.articoleDocSlidingDrawer.animateOpen();
			AfisRaportVanzariAg.textSelectedDocArt.setText(selectedDocument + "  ");
			AfisRaportVanzariAg.textSelectedClientArt.setText(selectedClient + "  ");
			AfisRaportVanzariAg.textSelectedAgentArt.setText(selectedAgent + "  ");
			AfisRaportVanzariAg.textSelectedDataArt.setText(selectedEmitere + "  ");
			AfisRaportVanzariAg.textSelectedValoareArt.setText(selectedValoare + " RON");

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public int getCount() {
		return listDocumente.size();
	}

	public VanzariDocument getItem(int arg0) {
		return listDocumente.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void setListDocumente(List<VanzariDocument> listDocumente) {
		this.listDocumente = listDocumente;
	}

}