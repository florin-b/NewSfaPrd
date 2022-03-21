/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.io.PrintWriter;
import java.io.StringWriter;

import my.logon.screen.model.UserInfo;

public class ExceptionHandler implements
		Thread.UncaughtExceptionHandler {
	private final Context myContext;

	public ExceptionHandler(Context context) {
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

		System.err.println(stackTrace);

		Intent intent = new Intent(myContext, CrashReport.class);
		intent.putExtra(CrashReport.STACKTRACE, stackTrace.toString()
				+ " User:" + UserInfo.getInstance().getCod());
		myContext.startActivity(intent);

		Process.killProcess(Process.myPid());
		System.exit(10);
	}
}