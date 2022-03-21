package my.logon.screen.dialogs;

import my.logon.screen.listeners.StadiuDialogListener;
import my.logon.screen.R;
import my.logon.screen.adapters.AdapterStadiuObiectiv;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import my.logon.screen.enums.EnumMotiveSuspendareObKA;
import my.logon.screen.enums.EnumStadiuObiectivKA;

public class SelectStadiuObiectiv extends Dialog {

	private ImageButton btnCancel, btnSave;
	private ListView listStadii;
	private Context context;
	private StadiuDialogListener listener;
	private Spinner spinnerMotiveSuspendare;
	private EnumStadiuObiectivKA stadiuObiectiv;

	public SelectStadiuObiectiv(String numeDialog, Context context) {
		super(context);

		this.context = context;

		setContentView(R.layout.select_tip_obiectiv_dialog);
		setTitle(numeDialog);
		setCancelable(true);

		setUpLayout();

	}

	private void setUpLayout() {

		spinnerMotiveSuspendare = (Spinner) findViewById(R.id.spinnerSuspendare);
		spinnerMotiveSuspendare.setVisibility(View.GONE);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
				EnumMotiveSuspendareObKA.getListMotive());

		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMotiveSuspendare.setAdapter(dataAdapter);		

		btnCancel = (ImageButton) findViewById(R.id.btnCancel);

		btnSave = (ImageButton) findViewById(R.id.btnSave);
		btnSave.setVisibility(View.GONE);
		setListenerBtnSave();

		listStadii = (ListView) findViewById(R.id.listStadii);
		AdapterStadiuObiectiv adapterStadiu = new AdapterStadiuObiectiv(context);

		listStadii.setAdapter(adapterStadiu);
		setListViewStadiiListener();

		setListenerCancelButton();
	}

	
	
	
	private void setListenerBtnSave() {
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (spinnerMotiveSuspendare.getSelectedItemPosition() == 0) {
					Toast.makeText(context, "Selectati motivul suspendarii", Toast.LENGTH_SHORT).show();
					return;
				}

				EnumMotiveSuspendareObKA motivSuspendare = EnumMotiveSuspendareObKA.getMotiv(spinnerMotiveSuspendare.getSelectedItemPosition() - 1);
				listener.stadiuSelected(stadiuObiectiv, motivSuspendare);
				dismiss();

			}
		});
	}

	private void setListViewStadiiListener() {

		listStadii.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (listener != null) {
					stadiuObiectiv = EnumStadiuObiectivKA.getStadiu(position);

					if (stadiuObiectiv == EnumStadiuObiectivKA.SUSPENDAT) {
						spinnerMotiveSuspendare.setVisibility(View.VISIBLE);
						btnSave.setVisibility(View.VISIBLE);
					} else {
						listener.stadiuSelected(stadiuObiectiv, EnumMotiveSuspendareObKA.MOTIV_SUSPENDARE);
						dismiss();
					}
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
