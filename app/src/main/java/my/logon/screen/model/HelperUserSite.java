package my.logon.screen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.HelperSiteListener;
import my.logon.screen.model.UserInfo;
import my.logon.screen.screens.AsyncTaskWSCall;
import android.content.Context;
import my.logon.screen.beans.DepoziteUl;
import my.logon.screen.enums.EnumOperatiiSite;

public class HelperUserSite implements AsyncTaskListener {

	private EnumOperatiiSite numeComanda;
	private HashMap<String, String> params;
	private Context context;
	private HelperSiteListener listener;

	public HelperUserSite(Context context) {
		this.context = context;
	}

	public void getDepoziteUl(HashMap<String, String> params) {
		numeComanda = EnumOperatiiSite.GET_DEPOZ_UL;
		this.params = params;

		performOperation();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeOp(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void setHelperSiteListener(HelperSiteListener listener) {
		this.listener = listener;
	}

	public List<String> deserListDepoziteUl(String depozite) {

		List<String> listDepozite = new ArrayList<String>();

		if (depozite != null && depozite.trim().length() > 0) {

			if (depozite.contains(","))
				listDepozite = Arrays.asList(depozite.split(","));
			else
				listDepozite.add(depozite);

		}

		return listDepozite;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.helperSiteComplete(methodName, result);
		}
	}

	public void setDepoziteUl(String ul) {
		DepoziteUl.getInstance().setListDepozite(deserListDepoziteUl(ul));

	}

	public static String getDepozitMavSite() {

		for (String depoz : DepoziteUl.getInstance().getListDepozite()) {
			if (!depoz.equals("BV90") && !depoz.equals(UserInfo.getInstance().getUnitLog()))
				return depoz;
		}

		return UserInfo.getInstance().getUnitLog();
	}

	public static boolean hasDepozitMagazin(List<String> listDepozite) {
		if (listDepozite.size() == 0)
			return false;

		if (listDepozite.size() == 1 && !listDepozite.contains("BV90"))
			return true;

		if (listDepozite.size() == 1 && listDepozite.contains("BV90"))
			return false;

		return true;

	}

}
