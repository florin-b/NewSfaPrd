package my.logon.screen.model;

import java.util.HashMap;

import android.content.Context;

public interface IPreturi {
	void getPret(HashMap<String, String> params, String methodName, Context context);
}
