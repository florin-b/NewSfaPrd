//Creat de Robert
package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.ListaFacturiAdapter;
import my.logon.screen.beans.BeanListaFacturati;
import my.logon.screen.enums.EnumClientiFacturati;
import my.logon.screen.listeners.ClientiFacturatiListener;
import my.logon.screen.model.OperatiiClientiFacturati;

	public class AfisareListaFacturiDialog extends Dialog implements ClientiFacturatiListener  {

	private Context context;
	private String codAgent;
	private String codClient;
	private String numeClient;
	private String data;
	ListView listFacturiClient;
	
	ImageButton btnClose;
	
	private OperatiiClientiFacturati operatiiClientFacturati;
	
	private ClientiFacturatiListener listener;
	private EnumClientiFacturati numeWebService;
	List<BeanListaFacturati> listFacturi = new ArrayList<BeanListaFacturati>();
	
	public AfisareListaFacturiDialog(Context context, String codAgent, CharSequence codClient, CharSequence numeClient, String data) {
		super(context);
		this.context = context;
		this.codAgent = codAgent;
		this.codClient = codClient.toString();
		this.numeClient = numeClient.toString();
		this.data = data;
		
		
		operatiiClientFacturati = new OperatiiClientiFacturati(context);
		
		setContentView(R.layout.dialog_showfacturi);
		setTitle("Lista Facturi - "+numeClient);
		
        listFacturiClient = (ListView) findViewById(R.id.lvFacturiClienti);
		
       
		btnClose = (ImageButton) findViewById(R.id.btn_dialogClose);
		
		closeDialog();
		setCancelable(true);
	}
	
	private void closeDialog() {
		
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
				dismiss();
				
			}
		});
		
	}

	public void showDialog() {
		
		this.show();
	}
	
	private void dismissCustomDialog() {
		this.dismiss();
	}
	
	
	private void fillListaFacturi(String result) {
		
		operatiiClientFacturati = new OperatiiClientiFacturati(context);
		listFacturi = operatiiClientFacturati.deserializeListaFacturi(result);
		
		if (listFacturi.size() > 0) {
						
			
			listFacturiClient.setAdapter(new ListaFacturiAdapter(context, listFacturi));			

		} else {
			Toast.makeText(context, "Nu exista date!", Toast.LENGTH_LONG).show();
		}
		
	}

	
	
	@Override
	public void operationClientiFacturatiComplete(EnumClientiFacturati numeWebService, Object result) {
		
		fillListaFacturi((String) result);
	}

}
