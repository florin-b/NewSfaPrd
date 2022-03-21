package my.logon.screen.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.beans.ArticolDB;
import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.screens.CreareComanda;


public class GetArticoleFromDB extends AsyncTask<Void, Void, List<ArticolDB>> {

	String errMessage = "";
	Context mContext;
	private ProgressDialog dialog;
	private AsyncTaskListener listener;
	private boolean isArt;
	private boolean isName;
	private String codArt;

	public GetArticoleFromDB(String codArt, boolean isArt, boolean isName, AsyncTaskListener listener, Context context) {
		super();
		this.mContext = context;
		this.listener = listener;
		this.isArt = isArt;
		this.isName = isName;
		this.codArt = codArt;
	}

	protected void onPreExecute() {
		dialog = new ProgressDialog(mContext);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Asteptati...");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected List<ArticolDB> doInBackground(Void... url) {

		SQLiteDatabase checkDB = null;
		List<ArticolDB> listArticole = null;

		try {

			String condArt = "";

			if (isName) // cod
			{
				if (isArt)
					condArt = " st.id = '" + codArt + "' ";
				else
					condArt = " ar.id like '" + codArt + "%' ";
			} else // nume
			{
				if (isArt)
					condArt = " upper(st.nume) like  upper('" + codArt + "%') ";
				else
					condArt = " upper(ar.nume) like  upper('" + codArt + "%') ";
			}

			checkDB = SQLiteDatabase.openDatabase(ConnectionStrings.getInstance().getNamespace(), null,
					SQLiteDatabase.OPEN_READWRITE);
			Cursor cur = checkDB.rawQuery(
					"SELECT ar.id,ar.nume, ar.umvanz_d, ar.umvanz_g, st.cod_depart, ar.tip_art FROM articoledef ar, sinteticedef st where "
							+ " st.id = ar.sintetic and " + condArt + " order by ar.nume ", null);

			String umVanzArt = "";
			listArticole = new ArrayList<ArticolDB>();
			ArticolDB articolDB;

			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						if (CreareComanda.canalDistrib.equals("10"))
							umVanzArt = cur.getString(2);
						else
							umVanzArt = cur.getString(3);

						articolDB = new ArticolDB();
						articolDB.setNume(cur.getString(1));
						articolDB.setCod(cur.getString(0));
						articolDB.setUmVanz(umVanzArt);
						articolDB.setDepart(cur.getString(4));
						articolDB.setTipAB(cur.getString(5));
						listArticole.add(articolDB);

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
		return listArticole;
	}

	@Override
	protected void onPostExecute(List<ArticolDB> resultList) {
		try {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (!errMessage.equals("")) {
				Toast toast = Toast.makeText(mContext, errMessage, Toast.LENGTH_SHORT);
				toast.show();
			} else {
				listener.onTaskComplete("getArtFromDB", resultList);
			}
		} catch (Exception e) {
			Toast toast = Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

}
