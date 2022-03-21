package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.TreeSet;

import my.logon.screen.listeners.SelectClientListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class SelectClientCmdSimDialog extends Dialog {

	private Context context;
	private ImageButton btnCancel;
	private ListView listView;
	private SelectClientListener clientListener;
	private TreeSet<String> setClienti;

	public SelectClientCmdSimDialog(Context context, TreeSet<String> setClienti) {
		super(context);

		this.context = context;
		this.setClienti = setClienti;

		setContentView(R.layout.select_client_cmd_sim_dialog);
		setTitle("Selectie client");
		setCancelable(true);
		setupLayout();
		populateListView();

	}

	private void setupLayout() {

		listView = (ListView) findViewById(R.id.listView);
		setListViewAgentiListener();

		btnCancel = (ImageButton) findViewById(R.id.btnCancel);
		setCancelButtonListener();

	}

	private void setListViewAgentiListener() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String client = (String) parent.getAdapter().getItem(position);

				if (clientListener != null)
					clientListener.clientSelected(client, position);

				dismiss();

			}
		});

	}

	private void populateListView() {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, new ArrayList<String>(setClienti));
		listView.setAdapter(adapter);

	}

	private void setCancelButtonListener() {
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setSelectClientListener(SelectClientListener listener) {
		this.clientListener = listener;
	}

}
