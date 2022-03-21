package my.logon.screen.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class VanzariAgenti {

	private static VanzariAgenti instance = new VanzariAgenti();

	public List<String> clientListName = new ArrayList<String>();
	public List<String> clientListCode = new ArrayList<String>();
	public List<String> articolListName = new ArrayList<String>();
	public List<String> articolListCode = new ArrayList<String>();

	public String tipArticol = "A";

	public String startIntervalRap = "", stopIntervalRap = "";
	public String selectedFiliala = "-1", selectedAgent = "-1", tipComanda = "E";

	private VanzariAgenti() {

	}

	public static VanzariAgenti getInstance() {
		return instance;
	}

	public String getReportParamObject() {

		String serializedResult;
		VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

		String selectedDepart = UserInfo.getInstance().getCodDepart();

		if (UserInfo.getInstance().getTipAcces().equals("35") || UserInfo.getInstance().getTipAcces().equals("32")) {
			selectedDepart = "10";
		}

		if (UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("18")
				|| UserInfo.getInstance().getTipAcces().equals("27") || UserInfo.getInstance().getTipAcces().equals("41")
				|| UserInfo.getInstance().getTipAcces().equals("39")) {
			selectedDepart = "11";
		}
		
		if (UserInfo.getInstance().getCod().equals("00010281") || UserInfo.getInstance().getCod().equals("00018768"))
			selectedDepart = "11";

		JSONObject obj = null;

		try {

			obj = new JSONObject();
			obj.put("tipArticol", vanzariInstance.tipArticol);
			obj.put("agent", vanzariInstance.selectedAgent);
			obj.put("filiala", vanzariInstance.selectedFiliala);
			obj.put("departament", selectedDepart);
			obj.put("startInterval", vanzariInstance.startIntervalRap);
			obj.put("stopInterval", vanzariInstance.stopIntervalRap);
			obj.put("tipComanda", vanzariInstance.tipComanda);
			obj.put("articole", articolListCode);
			obj.put("clienti", clientListCode);
			obj.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

		} catch (Exception ex) {

		}

		serializedResult = obj.toString();

		return serializedResult;

	}

}
