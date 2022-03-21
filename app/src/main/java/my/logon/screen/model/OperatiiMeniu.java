package my.logon.screen.model;

import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiMeniuListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import my.logon.screen.beans.BeanDeviceInfo;
import my.logon.screen.enums.EnumOperatiiMeniu;

public class OperatiiMeniu implements AsyncTaskListener {

	private EnumOperatiiMeniu numeComanda;
	private Context context;
	private OperatiiMeniuListener meniuListener;

	public OperatiiMeniu(Context context) {
		this.context = context;
	}

	public void savePinMeniu(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMeniu.SAVE_PIN;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeOp(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void blocheazaMeniu(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMeniu.BLOCHEAZA_MENIU;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeOp(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void deblocheazaMeniu(HashMap<String, String> params) {
		numeComanda = EnumOperatiiMeniu.DEBLOCHEAZA_MENIU;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeOp(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public String serializeDeviceInfo(BeanDeviceInfo deviceInfo) {

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("sdkVer", deviceInfo.getSdkVer());
			jsonObject.put("man", deviceInfo.getMan());
			jsonObject.put("model", deviceInfo.getModel());
			jsonObject.put("appName", deviceInfo.getAppName());
			jsonObject.put("appVer", deviceInfo.getAppVer());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject.toString();

	}	
	
	public void setOperatiiMeniuListener(OperatiiMeniuListener meniuListener) {
		this.meniuListener = meniuListener;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (meniuListener != null)
			meniuListener.pinCompleted(numeComanda, Boolean.parseBoolean(result.toString().toLowerCase()));
	}

}
