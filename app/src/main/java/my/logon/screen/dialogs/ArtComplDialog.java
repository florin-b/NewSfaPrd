package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.listeners.ArtComplDialogListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.R;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ArticolComandaGed;
import my.logon.screen.model.OperatiiArticolImpl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import my.logon.screen.beans.BeanArticolCautare;
import my.logon.screen.enums.EnumArticoleDAO;

public class ArtComplDialog extends Dialog implements OperatiiArticolListener {

	String articoleComplementare;
	Context context;
	ArtComplDialogListener listener;
	ArrayList<HashMap<String, String>> arrayListArticole;
	List<ArticolComanda> objArticole;
	List<ArticolComandaGed> objArticoleGed;
	List<BeanArticolCautare> listArticoleCompl = new ArrayList<BeanArticolCautare>();

	public ArtComplDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.artcompdlgbox);
		setTitle("Articole complementare");
		setCancelable(false);

	}

	public void showDialog(ArrayList<HashMap<String, String>> arrayListArticole, List<ArticolComanda> objArticole) {

		this.arrayListArticole = arrayListArticole;
		this.objArticole = objArticole;

		OperatiiArticolImpl opArticol = new OperatiiArticolImpl(context);
		opArticol.setListener(this);
		opArticol.getArticoleComplementare(objArticole);

	}

	public void showDialogGed(List<ArticolComanda> objArticole) {

		List<ArticolComanda> articol = new ArrayList<ArticolComanda>();

		for (int i = 0; i < objArticole.size(); i++)
			articol.add(objArticole.get(i));

		OperatiiArticolImpl opArticol = new OperatiiArticolImpl(context);
		opArticol.setListener(this);
		opArticol.getArticoleComplementare(articol);

	}

	void dismissThisDialog() {
		this.dismiss();
	}

	void setUpLayout() {
		ListView listViewArtCompl = (ListView) findViewById(R.id.listArtCmp);
		ArrayList<HashMap<String, String>> arrayListArtCompl = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterArtCompl = new SimpleAdapter(context, arrayListArtCompl, R.layout.rowlayoutclientred,
				new String[] { "numeClient", "codClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient }

		);

		HashMap<String, String> temp;

		Iterator<BeanArticolCautare> iterator = listArticoleCompl.iterator();
		BeanArticolCautare artCompl;

		while (iterator.hasNext()) {
			artCompl = iterator.next();
			temp = new HashMap<String, String>();
			temp.put("numeClient", artCompl.getNume());
			temp.put("codClient", artCompl.getCod());
			arrayListArtCompl.add(temp);

		}

		listViewArtCompl.setAdapter(adapterArtCompl);

		Button btnSaveCmd = (Button) findViewById(R.id.btnSaveCmd);
		btnSaveCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (listener != null) {
					listener.operationArtComplComplete(true);
				}
				dismissThisDialog();

			}
		});

		Button btnBackToCmd = (Button) findViewById(R.id.btnBackToCmd);
		btnBackToCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (listener != null) {
					listener.operationArtComplComplete(false);
				}
				dismissThisDialog();

			}
		});

		this.show();

	}

	public void setArtComplListener(ArtComplDialogListener listener) {
		this.listener = listener;
	}

	void validateArtComplResult(Object result) {

		BeanArticolCautare articolCompl = null;

		try {
			Object json = new JSONTokener((String) result).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String) result);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject articolObject = jsonObject.getJSONObject(i);

					articolCompl = new BeanArticolCautare();
					articolCompl.setCod(articolObject.getString("cod"));
					articolCompl.setNume(articolObject.getString("nume"));
					listArticoleCompl.add(articolCompl);

				}

			}

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

		if (listArticoleCompl.size() > 0) {
			setUpLayout();
		} else {
			if (listener != null) {
				listener.operationArtComplComplete(true);
			}
		}

	}

	public void operationComplete(EnumArticoleDAO methodName, Object result) {
		switch (methodName) {
		case GET_ARTICOLE_COMPLEMENTARE:
			validateArtComplResult(result);
			break;
		default:
			break;
		}

	}

}
