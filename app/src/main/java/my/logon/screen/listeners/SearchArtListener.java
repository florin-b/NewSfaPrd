package my.logon.screen.listeners;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Observable;

import my.logon.screen.connectors.ConnectionStrings;


public class SearchArtListener extends Observable {

	private String artName = "";
	private String searchType = "";
	private String artType = "";
	private Context context;
	private String searchResult;

	public SearchArtListener() {

	}

	public void performSearchResults(Context context, String artName,
			String searchType, String artType) {

		this.context = context;
		this.artName = artName;
		this.searchType = searchType;
		this.artType = artType;

		getArtFromDB articol = new getArtFromDB(context);
		articol.execute();

	}

	public String getSearchResults() {
		return searchResult;
	}

	public void setSearchResults(String searchResult) {
		this.searchResult = searchResult;
	}

	private class getArtFromDB extends AsyncTask<Void, Void, String> {
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
		protected String doInBackground(Void... url) {
			String response = "";
			SQLiteDatabase checkDB = null;
			Cursor cur = null;
			try {

				String condArt = "";

				if (searchType.equals("C")) {
					if (artType.equals("S"))
						condArt = " st.id = '" + artName + "' ";
					else
						condArt = " ar.id like '" + artName + "%' ";
				} else {
					if (artType.equals("S"))
						condArt = " upper(st.nume) like  upper('" + artName
								+ "%') ";
					else
						condArt = " upper(ar.nume) like  upper('" + artName
								+ "%') ";
				}

				checkDB = SQLiteDatabase.openDatabase(
						ConnectionStrings.getInstance().getNamespace(), null,
						SQLiteDatabase.OPEN_READWRITE);
				cur = checkDB
						.rawQuery(
								"SELECT ar.id, ar.nume, st.cod_depart, ar.tip_art FROM articoledef ar, sinteticedef st where "
										+ " st.id = ar.sintetic and "
										+ condArt
										+ " order by ar.nume ", null);

				if (cur != null) {
					if (cur.moveToFirst()) {
						do {

							response += cur.getString(1) + "#"
									+ cur.getString(0) + "#" + cur.getString(2)
									+ "#" + cur.getString(3) + "@@";

						} while (cur.moveToNext());
					}
				}

			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {

				if (cur != null)
					cur.close();

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
					dialog.dismiss();
					dialog = null;
				}

				if (!errMessage.equals("")) {

					Toast toast = Toast.makeText(context, errMessage,
							Toast.LENGTH_SHORT);
					toast.show();

				} else {
					setSearchResults(result);

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
