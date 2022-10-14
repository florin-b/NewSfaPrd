package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.ArticolMathaus;
import my.logon.screen.beans.CategorieMathaus;
import my.logon.screen.beans.RezultatArtMathaus;
import my.logon.screen.enums.EnumOperatiiMathaus;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiMathausListener;
import my.logon.screen.screens.AsyncTaskWSCall;

public class OperatiiMathaus implements AsyncTaskListener {

	Context context;
	OperatiiMathausListener listener;
	EnumOperatiiMathaus numeComanda;

	public OperatiiMathaus(Context context) {
		this.context = context;
	}

	public void getCategorii(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMathaus.GET_CATEGORII;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getArticole(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMathaus.GET_ARTICOLE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void cautaArticole(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMathaus.CAUTA_ARTICOLE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}
	
	public void cautaArticoleLocal(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMathaus.CAUTA_ARTICOLE_LOCAL;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public List<CategorieMathaus> deserializeCategorii(String categorii) {
		CategorieMathaus categorie = null;
		ArrayList<CategorieMathaus> objectsList = new ArrayList<CategorieMathaus>();

		JSONArray jsonObject = null;

		try {

			Object json = new JSONTokener(categorii).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(categorii);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject catObject = jsonObject.getJSONObject(i);

					categorie = new CategorieMathaus();
					categorie.setCod(catObject.getString("cod"));
					categorie.setNume(catObject.getString("nume"));
					categorie.setCodHybris(catObject.getString("codHybris"));
					categorie.setCodParinte(catObject.getString("codParinte"));

					objectsList.add(categorie);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public RezultatArtMathaus deserializeArticole(String articole) {

		RezultatArtMathaus rezultat = new RezultatArtMathaus();

		ArticolMathaus articol = null;
		ArrayList<ArticolMathaus> objectsList = new ArrayList<ArticolMathaus>();

		JSONArray jsonObject = null;

		try {

			JSONObject jsonMainObject = new JSONObject(articole);
			rezultat.setNrTotalArticole(jsonMainObject.getString("nrTotalArticole"));

			Object json = new JSONTokener(jsonMainObject.getString("listArticole")).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(jsonMainObject.getString("listArticole"));

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject catObject = jsonObject.getJSONObject(i);

					articol = new ArticolMathaus();
					articol.setCod(catObject.getString("cod"));
					articol.setNume(catObject.getString("nume"));
					articol.setDescriere(catObject.getString("descriere").equals("null") ? "" : catObject.getString("descriere"));
					articol.setAdresaImg(catObject.getString("adresaImg"));
					articol.setAdresaImgMare(catObject.getString("adresaImgMare"));
					articol.setSintetic(catObject.getString("sintetic"));
					articol.setNivel1(catObject.getString("nivel1"));
					articol.setUmVanz(catObject.getString("umVanz"));
					articol.setUmVanz10(catObject.getString("umVanz10"));
					articol.setTipAB(catObject.getString("tipAB"));
					articol.setDepart(catObject.getString("depart"));
					articol.setDepartAprob(catObject.getString("departAprob"));
					articol.setUmPalet(catObject.getString("umPalet").equals("1") ? true : false);
					articol.setStoc(catObject.getString("stoc"));
					articol.setCategorie(catObject.getString("categorie"));
					articol.setLungime(catObject.getString("lungime").equals("null") ? 0 : Double.valueOf(catObject.getString("lungime")));
					articol.setCatMathaus(catObject.getString("catMathaus"));
					articol.setPretUnitar(catObject.getString("pretUnitar"));
					articol.setLocal(Boolean.parseBoolean(catObject.getString("isLocal")));
					articol.setArticolSite(Boolean.parseBoolean(catObject.getString("isArticolSite")));
					
					articol.setTip1(catObject.getString("tip1"));
					articol.setTip2(catObject.getString("tip2"));
					articol.setPlanificator(catObject.getString("planificator"));

					objectsList.add(articol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		rezultat.setListArticole(objectsList);

		return rezultat;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}
	}

	public void setOperatiiMathausListener(OperatiiMathausListener listener) {
		this.listener = listener;
	}

}
