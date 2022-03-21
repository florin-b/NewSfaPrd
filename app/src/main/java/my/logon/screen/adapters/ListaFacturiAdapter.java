//Creat de Robert
package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanListaFacturati;

public class ListaFacturiAdapter extends BaseAdapter {
	
	private List<BeanListaFacturati> listaFacturati;
	Context context;
	LayoutInflater inflater;
	DecimalFormat formatter = new DecimalFormat("#,###.00");
	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	
	public ListaFacturiAdapter(Context context, List<BeanListaFacturati> myList) {
		this.context = context;
		this.listaFacturati = myList;
		
		inflater = LayoutInflater.from(this.context);
		
	}
	
	
	
	private class ViewHolder {
		public TextView textNrCrt, textNrComanda, textDataEmitere, textValoare;
		
		public ViewHolder(View item) {
			
			textNrCrt = (TextView) item.findViewById(R.id.txtNr);
			textNrComanda = (TextView) item.findViewById(R.id.txtNrComanda);
			textDataEmitere = (TextView) item.findViewById(R.id.txtDataEmitere);
			textValoare = (TextView) item.findViewById(R.id.txtValoare);
			
		}
	}

	@Override
	public int getCount() {
		
		return listaFacturati.size();
	}

	@Override
	public BeanListaFacturati getItem(int position) {
		
		return (BeanListaFacturati) listaFacturati.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder mViewHolder;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.row_dialog_showfacturi, parent, false);
			
			
			mViewHolder = new ViewHolder(convertView);
			convertView.setTag(mViewHolder);
			convertView.setFocusableInTouchMode(false);
			convertView.setScrollbarFadingEnabled(false);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		BeanListaFacturati factura = getItem(position);
		
		mViewHolder.textNrCrt.setText(Integer.toString(position+1));
		mViewHolder.textNrComanda.setText(factura.getNrFactura());				
		mViewHolder.textDataEmitere.setText(factura.getDataEmitere());
		
		mViewHolder.textValoare.setText(formatter.format( Double.parseDouble(factura.getValoare())));
		
		
		
		return convertView;
	}
}
