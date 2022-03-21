package my.logon.screen.dialogs;

import java.util.List;

import my.logon.screen.listeners.DatePersListener;
import my.logon.screen.R;
import my.logon.screen.adapters.AfisDatePersAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import my.logon.screen.beans.BeanDatePersonale;

public class DatePersClientDialog extends Dialog {

	private ImageButton cancelButton;

	private ListView listViewDate;
	private DatePersListener listener;
	private Context context;
	private List<BeanDatePersonale> listDatePersonale;

	public DatePersClientDialog(Context context, List<BeanDatePersonale> listDatePersonale) {
		super(context);
		this.context = context;
		this.listDatePersonale = listDatePersonale;

		setContentView(R.layout.date_pers_dialog);
		setTitle("Selectie client");
		setCancelable(true);

		setUpLayout();

	}

	private void setUpLayout() {

		cancelButton = (ImageButton) findViewById(R.id.btnCancel);
		setListenerCancelButton();

		listViewDate = (ListView) findViewById(R.id.listDatePersonale);
		setListenerListClienti();

		AfisDatePersAdapter adapterDate = new AfisDatePersAdapter(context, listDatePersonale);
		listViewDate.setAdapter(adapterDate);

	}

	private void setListenerCancelButton() {
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	private void setListenerListClienti() {
		listViewDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BeanDatePersonale datePersonale = (BeanDatePersonale) listViewDate.getItemAtPosition(arg2);
				if (listener != null) {
					listener.datePersSelected(datePersonale);
					dismiss();
				}

			}
		});
	}

	public void setDatePersListener(DatePersListener dateListener) {
		this.listener = dateListener;
	}

}
