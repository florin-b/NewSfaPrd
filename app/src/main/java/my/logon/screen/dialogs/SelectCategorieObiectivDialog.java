package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.listeners.SelectCategorieListener;
import my.logon.screen.R;
import my.logon.screen.adapters.AdapterCategorieObiectiv;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import my.logon.screen.beans.BeanCategorieObiectiv;
import my.logon.screen.enums.EnumCategorieObiectiv;

public class SelectCategorieObiectivDialog extends Dialog {

	private ImageButton cancelButton;
	private ListView listCategorii;
	private SelectCategorieListener categorieListener;

	public SelectCategorieObiectivDialog(Context context) {
		super(context);

		setContentView(R.layout.select_list_dialog);
		setTitle("Selectie categorie");
		setCancelable(true);

		setUpLayout();

	}

	private void setUpLayout() {

		cancelButton = (ImageButton) findViewById(R.id.btnCancel);
		setListenerCancelButton();

		listCategorii = (ListView) findViewById(R.id.listOptions);

		AdapterCategorieObiectiv adapter = new AdapterCategorieObiectiv(getContext(), listCategorii());
		listCategorii.setAdapter(adapter);
		setListCategoriiListener();

	}

	private List<BeanCategorieObiectiv> listCategorii() {
		List<BeanCategorieObiectiv> listCategorii = new ArrayList<BeanCategorieObiectiv>();

		BeanCategorieObiectiv categorie = null;
		for (EnumCategorieObiectiv enumc : EnumCategorieObiectiv.values()) {
			categorie = new BeanCategorieObiectiv();
			categorie.setCodCategorie(enumc.getCodCategorie());
			categorie.setNumeCategorie(enumc.getNumeCategorie());
			listCategorii.add(categorie);

		}

		return listCategorii;
	}

	private void setListCategoriiListener() {
		listCategorii.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (categorieListener != null) {
					BeanCategorieObiectiv categorie = (BeanCategorieObiectiv) arg0.getAdapter().getItem(arg2);
					categorieListener.categorieSelected(categorie);
					dismiss();
				}

			}
		});
	}

	public void setListCategoriiListener(SelectCategorieListener categorieListener) {
		this.categorieListener = categorieListener;
	}

	private void setListenerCancelButton() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

}
