package my.logon.screen.dialogs;

import java.util.List;

import my.logon.screen.listeners.AutocompleteDialogListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

public class AutoCompleteDialog extends Dialog {

	private Context context;
	private String initString;
	private List<String> optionsList;
	private AutoCompleteTextView autocompleteText;
	private ImageButton btnCancel;
	private int actionId;
	private AutocompleteDialogListener listener;

	public AutoCompleteDialog(Context context, String initString, List<String> optionsList, int actionId) {
		super(context);
		this.context = context;
		this.initString = initString;
		this.optionsList = optionsList;
		this.actionId = actionId;

		setContentView(R.layout.autocomplete_dialog);

		setCancelable(true);

		setupLayout();
	}

	private void setupLayout() {
		autocompleteText = (AutoCompleteTextView) findViewById(R.id.textAutocomplete);

		String[] arrayLocalitati = optionsList.toArray(new String[optionsList.size()]);
		ArrayAdapter<String> adapterAuto = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		if (initString.trim() != "") {
			autocompleteText.setText(initString);
			autocompleteText.setSelection(autocompleteText.getText().length());
		}

		autocompleteText.setThreshold(0);
		autocompleteText.setAdapter(adapterAuto);

		setAutocompleteListener();

		btnCancel = (ImageButton) findViewById(R.id.btnCancel);
		setBtnCancelListener();

	}

	private void setAutocompleteListener() {
		autocompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (listener != null)
					listener.selectionComplete(autocompleteText.getText().toString(), actionId);

				dismiss();

			}

		});
	}

	private void setBtnCancelListener() {
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void setAutocompleteListener(AutocompleteDialogListener listener) {
		this.listener = listener;
	}

}
