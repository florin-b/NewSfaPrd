package my.logon.screen.listeners;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.model.UserInfo;


public class SearchClientListener extends Observable {

	private String numeClient;
	private String depart;
	private String departAg;
	private String unitLog;
	private Context context;
	private String searchResult;

	public SearchClientListener() {

	}

	public void performSearchResults(Context context, String numeClient,
			String depart, String departAg, String unitLog) {
		this.context = context;
		this.numeClient = numeClient;
		this.depart = depart;
		this.departAg = departAg;
		this.unitLog = unitLog;

		searchClients client = new searchClients(context);
		client.execute();

	}

	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}

	private class searchClients extends AsyncTask<Void, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private searchClients(Context context) {
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
		protected String doInBackground(Void... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject(
						ConnectionStrings.getInstance().getNamespace(), "cautaClientAndroid");

				request.addProperty("numeClient", numeClient);
				request.addProperty("depart", depart);
				request.addProperty("departAg", departAg);
				request.addProperty("unitLog", unitLog);
				request.addProperty("tipUserSap", UserInfo.getInstance().getTipUserSap());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						ConnectionStrings.getInstance().getUrl(), 60000);
				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin"
								.getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace()
						+ "cautaClientAndroid", envelope, headerList);
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
					Toast toast = Toast.makeText(context, errMessage,
							Toast.LENGTH_SHORT);
					toast.show();
				} else {
					setSearchResult(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			} finally {
				triggerObservers();
			}
		}

	}

	private void triggerObservers() {
		setChanged();
		notifyObservers();
	}

}
