package my.logon.screen.listeners;

import java.util.HashMap;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ClientSelection implements OnItemClickListener {

	private ClientSelectionListener clientListener;

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {

		HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);
		ListView listView = (ListView) parent;

		String numeClient = artMap.get("numeClient");
		String codClient = artMap.get("codClient");

		if (clientListener != null) {
			clientListener.selectedClient(codClient, numeClient, listView, view);
		}

	}

	public void setClientSelectionListener(ClientSelectionListener clientListener) {
		this.clientListener = clientListener;
	}

}
