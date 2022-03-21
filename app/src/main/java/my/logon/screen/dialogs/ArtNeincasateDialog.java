package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.adapters.AdapterArtNeincasate;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import my.logon.screen.beans.BeanArticolVanzari;

public class ArtNeincasateDialog extends Dialog {

	private Context context;

	private List<BeanArticolVanzari> listArticole;
	private String referinta;

	public ArtNeincasateDialog(Context context, List<BeanArticolVanzari> listArticole, String referinta) {
		super(context);
		this.context = context;

		this.listArticole = listArticole;
		this.referinta = referinta;

		setContentView(R.layout.art_neinc_dialog);
		setCancelable(true);

		setupLayout();

	}

	private void setupLayout() {

		this.setTitle(referinta);

		Button btnClose = (Button) findViewById(R.id.btnClose);
		setListenerBtnClose(btnClose);

		ListView listViewArticole = (ListView) findViewById(R.id.listArtNeinc);

		AdapterArtNeincasate adapter = new AdapterArtNeincasate(context, listArticole);
		listViewArticole.setAdapter(adapter);
	}

	private void setListenerBtnClose(Button button) {
		button.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				dismiss();

			}

		});
	}

	public void showDialog(ArrayList<HashMap<String, String>> arrayListArticole, List<ArticolComanda> objArticole) {

	}

	void dismissThisDialog() {
		this.dismiss();
	}

}
