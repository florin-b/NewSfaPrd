package my.logon.screen.screens;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.GetArticoleFromDB;
import my.logon.screen.model.WebServiceCall;
import my.logon.screen.model.WebServiceCallFromFragment;
import android.content.Context;

public class AsyncTaskWSCall {

	private String methodName;
	private HashMap<String, String> params;
	private Context context;
	private AsyncTaskListener contextListener;

	public AsyncTaskWSCall(Context context) {
		this.context = context;
	}

	public AsyncTaskWSCall(String methodName, Context context) {
		this.context = context;
		this.methodName = methodName;
	}

	public AsyncTaskWSCall(AsyncTaskListener contextListener, Context context) {
		this.context = context;
		this.contextListener = contextListener;
	}

	public AsyncTaskWSCall(String methodName, HashMap<String, String> params, AsyncTaskListener myListener, Context context) {
		this.contextListener = myListener;
		this.methodName = methodName;
		this.params = params;
		this.context = context;

	}

	public AsyncTaskWSCall(Context context, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
	}

	public AsyncTaskWSCall(Context context, AsyncTaskListener contextListener, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
		this.contextListener = contextListener;
	}

	public void getCallResults() {
		new WebServiceCall(context, methodName, params).execute();
	}

	
	public Object getCallResultsSyncActivity() {

		Object retVal = null;
		try {
			retVal = new WebServiceCall(context, methodName, params).execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	public Object getCallResultsSync() {

		Object retVal = null;
		try {
			retVal = new WebServiceCallFromFragment(context, contextListener, methodName, params).execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public void getCallResultsFromFragment() {
		new WebServiceCallFromFragment(context, contextListener, methodName, params).execute();
	}

	public void getArtFromDB(String codArt, boolean isArt, boolean isName) {
		new GetArticoleFromDB(codArt, isArt, isName, contextListener, context).execute();
	}

}
