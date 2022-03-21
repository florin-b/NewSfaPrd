package my.logon.screen.model;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.beans.BeanConditiiHeader;
import my.logon.screen.model.UserInfo;

public class SerializeConditiiComanda {

	public String serializeArticoleConditii(List<BeanConditiiArticole> listArticole) {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;

		Iterator<BeanConditiiArticole> iterator = listArticole.iterator();

		try {

			BeanConditiiArticole articol = null;
			while (iterator.hasNext()) {
				articol = iterator.next();

				jsonObject = new JSONObject();
				jsonObject.put("cod", articol.getCod());
				jsonObject.put("nume", articol.getNume());
				jsonObject.put("cantitate", articol.getCantitate());
				jsonObject.put("um", articol.getUm());
				jsonObject.put("valoare", articol.getValoare());
				jsonObject.put("multiplu", articol.getMultiplu());
				jsonArray.put(jsonObject);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonArray.toString();

	}

	public String serializeHeaderConditii(BeanConditiiHeader header) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("id", header.getId());
			jsonObject.put("conditiiCalit", header.getConditiiCalit());
			jsonObject.put("nrFact", header.getNrFact());
			jsonObject.put("observatii", header.getObservatii());
			jsonObject.put("codAgent", UserInfo.getInstance().getCod());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	public String serializeConditiiObject(String header, String articole) {
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("header", header);
			jsonObject.put("articole", articole);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject.toString();

	}

}
