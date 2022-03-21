package my.logon.screen.screens;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class MyCustomDialog {

	Context context;
	
	public MyCustomDialog(Context context)
	{
		this.context = context;
	}
	
	
	public void showPrompt()
	{
		Builder builder = new Builder(context);
		
		builder.setTitle("Title");
		
		builder.setMessage("message");
		
		
		AlertDialog dialog = builder.create();
		
		
		dialog.show();
		
	}
	
	
}
