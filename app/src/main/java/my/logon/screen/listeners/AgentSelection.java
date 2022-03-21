package my.logon.screen.listeners;

import java.util.HashMap;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AgentSelection implements OnItemSelectedListener {

	private AgentSelectionListener agentListener;

	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

		String selectedAgent = "0";
		HashMap<String, String> artMap = (HashMap<String, String>) parent.getAdapter().getItem(pos);

		selectedAgent = artMap.get("codAgent");

		if (selectedAgent.trim().equals(""))
			selectedAgent = "0";
		if (selectedAgent.equals("00000000"))
			selectedAgent = "0";

		if (agentListener != null) {
			agentListener.selectedAgent(selectedAgent);
		}

	}

	public void onNothingSelected(AdapterView<?> parent) {
		// TODO
	}

	public void setAgentListener(AgentSelectionListener listener) {
		this.agentListener = listener;
	}

}
