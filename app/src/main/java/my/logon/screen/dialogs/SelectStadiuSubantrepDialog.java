package my.logon.screen.dialogs;

import my.logon.screen.listeners.StadiuDialogListener;
import my.logon.screen.R;
import my.logon.screen.adapters.AdapterStadiuSubantrepObiectiv;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import my.logon.screen.enums.EnumStadiuSubantrep;

public class SelectStadiuSubantrepDialog extends Dialog {

	private ImageButton btnCancel;
	private ListView listStadii;
	private Context context;
	private StadiuDialogListener listener;
	

	public SelectStadiuSubantrepDialog(String numeDialog, Context context) {
		super(context);

		this.context = context;
		
		setContentView(R.layout.select_stadiu_dialog);
		setTitle(numeDialog);
		setCancelable(true);

		setUpLayout();

	}

	private void setUpLayout() {
		btnCancel = (ImageButton) findViewById(R.id.btnCancel);
		listStadii = (ListView) findViewById(R.id.listStadii);

		AdapterStadiuSubantrepObiectiv adapterStadiu = new AdapterStadiuSubantrepObiectiv(context);

		listStadii.setAdapter(adapterStadiu);
		setListViewStadiiListener();

		setListenerCancelButton();
	}

	private void setListViewStadiiListener() {

		listStadii.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (listener != null) {
					EnumStadiuSubantrep stadiu = EnumStadiuSubantrep.getStadiu(position);
					listener.stadiuSelected(stadiu);
					dismiss();
				}

			}
		});

	}

	private void setListenerCancelButton() {
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void setStadiuDialogListener(StadiuDialogListener listener) {
		this.listener = listener;
	}

}
