package my.logon.screen.model;

import android.content.Context;

public class OperatiiArticolFactory {

	public static OperatiiArticolImpl createObject(String objectType, Context context) {
		OperatiiArticolImpl object = null;

		if (objectType.equals("OperatiiArticolImpl")) {
			object = new OperatiiArticolImpl(context);
		}

		return object;
	}

}
