package my.logon.screen.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.model.Constants;
import my.logon.screen.model.UserInfo;

public class UtilsUser {

	public static boolean isKA() {
		return UserInfo.getInstance().getTipUser().equals("KA") || UserInfo.getInstance().getTipUser().equals("DK");
	}

	public static boolean isUserKA() {
		return UserInfo.getInstance().getTipUser().equals("KA");
	}

	public static boolean isUserSK() {
		return UserInfo.getInstance().getTipUser().equals("SK");
	}

	public static boolean isCV() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")
				|| UserInfo.getInstance().getTipUser().equals("CVR") || UserInfo.getInstance().getTipUser().equals("SMR");
	}

	public static boolean isDVCV() {
		return UserInfo.getInstance().getTipUser().equals("DV") || UserInfo.getInstance().getCodDepart().equals("00");
	}

	public static boolean isAV() {
		return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("SD");
	}

	public static boolean isCVA() {
		return UserInfo.getInstance().getTipUserSap().equals("CVA") || UserInfo.getInstance().getTipUserSap().equals("SDCVA");
	}

	public static boolean hasObiective() {
		return UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("KA");
	}

	public static boolean isANYDV() {
		return UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("35");
	}

	public static boolean isDV() {
		return UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14");

	}

	public static boolean isSDBUCURESTI() {
		return UserInfo.getInstance().getTipAcces().equals("10") && UserInfo.getInstance().getUnitLog().substring(0, 2).equals("BU");
	}

	public static boolean isUserGed() {
		return UserInfo.getInstance().getTipUser().equals("CV") || UserInfo.getInstance().getTipUser().equals("SM")
				|| UserInfo.getInstance().getTipUser().equals("SMR");
	}

	public static boolean isAgentOrSD() {
		return UserInfo.getInstance().getTipUserSap().toUpperCase().contains("AV") || UserInfo.getInstance().getTipUserSap().toUpperCase().equals("SD")
				|| UserInfo.getInstance().getTipUserSap().toUpperCase().equals("ASDL");

	}

	public static boolean isAgentOrSDorKA() {
		return UserInfo.getInstance().getTipUserSap().toUpperCase().contains("AV") || UserInfo.getInstance().getTipUserSap().toUpperCase().equals("SD")
				|| UserInfo.getInstance().getTipUser().equals("KA");

	}

	public static boolean isUserSDKA() {
		return UserInfo.getInstance().getTipUserSap().equals("SDKA") || UserInfo.getInstance().getTipUser().equals("SK");
	}

	public static boolean isSD() {
		return UserInfo.getInstance().getTipUserSap().toUpperCase().equals("SD");
	}

	public static boolean isConsWood() {
		return UserInfo.getInstance().getTipUserSap().toUpperCase().contains("WOOD");
	}

	public static boolean isSMNou() {
		return UserInfo.getInstance().getTipUserSap().equals("SMR") || UserInfo.getInstance().getTipUserSap().equals("SMG")
				|| UserInfo.getInstance().getTipUserSap().equals("SMW")
				|| (UserInfo.getInstance().getTipAcces().equals("18") && UserInfo.getInstance().getTipUserSap().equals("WOOD"));
	}

	public static boolean isSuperAv() {
		return !UserInfo.getInstance().getCodSuperUser().isEmpty();
	}

	public static boolean isInfoUser() {
		return UserInfo.getInstance().getTipUserSap().equals(Constants.tipInfoAv);
	}

	public static boolean isSMR() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("SMR");
	}

	public static boolean isCVR() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("CVR");
	}

	public static boolean isSSCM() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("SSCM");
	}

	public static boolean isCGED() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("CGED");
	}

	public static boolean isOIVPD() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("OIVPD");
	}

	public static boolean isASDL() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("ASDL");
	}

	public static boolean isSDIP() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("SDIP");
	}

	public static boolean isUserIP() {
		return UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("SDIP") || UserInfo.getInstance().getTipUserSap().equalsIgnoreCase("CVIP");
	}

	public static String getTipSMNou() {
		String tipUser;

		if (UserInfo.getInstance().getTipAcces().equals("18") && UserInfo.getInstance().getTipUserSap().equals("WOOD"))
			tipUser = "SMW";
		else
			tipUser = UserInfo.getInstance().getTipUserSap();

		return tipUser;
	}
	
	public static boolean isAV_SD_01() {
		return (UserInfo.getInstance().getTipUser().equals("AV") || UserInfo.getInstance().getTipUser().equals("SD")) && UserInfo.getInstance().getCodDepart().equals("01");
	}
	
	public static boolean isCVO(){
		return UserInfo.getInstance().getTipUserSap().equals("CVO");
	}

	public static boolean isSVO(){
		return UserInfo.getInstance().getTipUserSap().equals("SVO");
	}	
	
	
	public static boolean isDV_WOOD() {
		if (isANYDV()) {

			String filDV = UserInfo.getInstance().getFilialeDV();

			if (filDV.contains(";")) {
				String[] filiale = UserInfo.getInstance().getFilialeDV().split(";");

				if (filiale.length > 0 && filiale[0].substring(2, 3).equals("4"))
					return true;
			}

		}

		return false;
	}

	public static boolean isDV_CONS() {
		String codDvCons = "00001027,00012326";

		if (codDvCons.contains(UserInfo.getInstance().getCod()))
			return true;

		return false;

	}

	public static boolean isMacaraVisible() {

		if (isAgentOrSD()) {
			if (UserInfo.getInstance().getCodDepart().contains("04") || UserInfo.getInstance().getCodDepart().contains("07")
					|| UserInfo.getInstance().getCodDepart().contains("09"))
				return true;
			else
				return false;
		} else if (isKA() || isCV()) {
			return true;
		}

		return false;
	}

	public static boolean isUserExceptieCONSGED() {

		String filialeExceptie = "AG, BH, CJ, CT, DJ, GL";

		if (UserInfo.getInstance().getTipUserSap().equals("CONS-GED") && filialeExceptie.contains(UserInfo.getInstance().getUnitLog().substring(0, 2)))
			return true;

		return false;
	}

	public static boolean isUserExceptieBV90Ged() {

		// pentru ag si sd de la 01, 02 si 05 se ofera accesul la BV90
		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")) {
			if (UserInfo.getInstance().getCodDepart().equals("02") || UserInfo.getInstance().getCodDepart().equals("05")
					|| UserInfo.getInstance().getCodDepart().equals("01"))
				return true;
		}

		return false;
	}

	public static boolean isUserSite() {
		return UserInfo.getInstance().getUserSite().equalsIgnoreCase("X");
	}

	public static boolean isUserCVO(){
		return UserInfo.getInstance().getTipUserSap().equals("CVO") || UserInfo.getInstance().getTipUserSap().equals("SVO");

	}

	public static boolean isUserCVOB(){
		return UserInfo.getInstance().getTipUserSap().equals("CVOB");

	}

	public static String getULUserSite(String unitLog, String depozit) {

		if (depozit.equals("MAV1") || depozit.equals("MAV2")) {
			if (unitLog.equals("BV90"))
				return "BV92";
			else
				return unitLog.substring(0, 2) + "2" + unitLog.substring(3, 4);
		} else {
			if (unitLog.equals("BV90"))
				return unitLog;
			else
				return unitLog.substring(0, 2) + "1" + unitLog.substring(3, 4);
		}

	}

	public static String serializeUserInfo() {

		JSONObject jsonUser = new JSONObject();
		JSONArray jsonArrayFiliale = new JSONArray();

		try {
			jsonUser.put("nume", UserInfo.getInstance().getNume());
			jsonUser.put("filiala", UserInfo.getInstance().getFiliala());
			jsonUser.put("cod", UserInfo.getInstance().getCod());
			jsonUser.put("numeDepart", UserInfo.getInstance().getNumeDepart());
			jsonUser.put("codDepart", UserInfo.getInstance().getCodDepart());
			jsonUser.put("unitLog", UserInfo.getInstance().getUnitLog());
			jsonUser.put("initUnitLog", UserInfo.getInstance().getInitUnitLog());
			jsonUser.put("tipAcces", UserInfo.getInstance().getTipAcces());
			jsonUser.put("parentScreen", UserInfo.getInstance().getParentScreen());
			jsonUser.put("filialeDV", UserInfo.getInstance().getFilialeDV());
			jsonUser.put("altaFiliala", UserInfo.getInstance().isAltaFiliala());
			jsonUser.put("tipUser", UserInfo.getInstance().getTipUser());
			jsonUser.put("departExtra", UserInfo.getInstance().getDepartExtra());
			jsonUser.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

			for (String filiala : UserInfo.getInstance().getExtraFiliale()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("filiala", filiala);
				jsonArrayFiliale.put(jsonObject);
			}

			jsonUser.put("extraFiliale", jsonArrayFiliale.toString());
			jsonUser.put("comisionCV", UserInfo.getInstance().getComisionCV());
			jsonUser.put("coefCorectie", UserInfo.getInstance().getCoefCorectie());
			jsonUser.put("userSite", UserInfo.getInstance().getUserSite());
			jsonUser.put("userWood", UserInfo.getInstance().isUserWood());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonUser.toString();

	}

	public static void deserializeUserInfo(String userInfoSer, Context context) {

		try {
			List<String> listExtraFiliale = new ArrayList<String>();
			JSONObject jsonObject = new JSONObject(userInfoSer);

			if (jsonObject instanceof JSONObject) {
				UserInfo.getInstance().setNume(jsonObject.getString("nume"));
				UserInfo.getInstance().setFiliala(jsonObject.getString("filiala"));
				UserInfo.getInstance().setCod(jsonObject.getString("cod"));
				UserInfo.getInstance().setNumeDepart(jsonObject.getString("numeDepart"));
				UserInfo.getInstance().setCodDepart(jsonObject.getString("codDepart"));
				UserInfo.getInstance().setUnitLog(jsonObject.getString("unitLog"));
				UserInfo.getInstance().setInitUnitLog(jsonObject.getString("initUnitLog"));
				UserInfo.getInstance().setTipAcces(jsonObject.getString("tipAcces"));
				UserInfo.getInstance().setParentScreen(jsonObject.getString("parentScreen"));
				UserInfo.getInstance().setFilialeDV(jsonObject.getString("filialeDV"));
				UserInfo.getInstance().setAltaFiliala(Boolean.valueOf(jsonObject.getString("altaFiliala")));
				UserInfo.getInstance().setTipUser(jsonObject.getString("tipUser"));
				UserInfo.getInstance().setDepartExtra(jsonObject.getString("departExtra"));
				UserInfo.getInstance().setTipUserSap(jsonObject.getString("tipUserSap"));
				UserInfo.getInstance().setComisionCV(Double.valueOf(jsonObject.getString("comisionCV")));
				UserInfo.getInstance().setCoefCorectie(jsonObject.getString("coefCorectie"));
				UserInfo.getInstance().setUserSite(jsonObject.getString("userSite"));
				UserInfo.getInstance().setUserWood(Boolean.valueOf(jsonObject.getString("userWood")));

				Object json = new JSONTokener(jsonObject.getString("extraFiliale")).nextValue();

				if (json instanceof JSONArray) {
					JSONArray jsonArray = new JSONArray(jsonObject.getString("extraFiliale"));

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject compObject = jsonArray.getJSONObject(i);
						listExtraFiliale.add(compObject.getString("filiala"));

					}
				}

				UserInfo.getInstance().setExtraFiliale(listExtraFiliale);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

}
