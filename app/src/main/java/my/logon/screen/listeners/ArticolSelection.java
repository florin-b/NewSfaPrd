package my.logon.screen.listeners;

import java.util.HashMap;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArticolSelection implements OnItemClickListener {

	private ArticolSelectionListener articolListener;

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
		HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);

		ListView listView = (ListView)parent;
		
		String numeArticol = artMap.get("numeArticol");	
		String codArticol = artMap.get("codArticol");
		String umVanz = artMap.get("umVanz");
		
		if (articolListener != null) {
			articolListener.selectedArticol(listView, view, codArticol, numeArticol, umVanz);
		}

	}

	public void setArticolSelectionListener(ArticolSelectionListener articolListener) {
		this.articolListener = articolListener;
	}

}
