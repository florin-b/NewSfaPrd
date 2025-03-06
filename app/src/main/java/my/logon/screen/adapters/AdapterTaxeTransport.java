package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.Details;

public class AdapterTaxeTransport extends BaseAdapter {

	private List<Details> listTaxe;
	private Context context;

	public AdapterTaxeTransport(Context context, List<Details> listTaxe) {
		this.context = context;
		this.listTaxe = listTaxe;
	}

	static class ViewHolder {
		TextView textNumeTaxa, textValoareTaxa;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_taxa_transport, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeTaxa = convertView.findViewById(R.id.textNumeTaxa);
			viewHolder.textValoareTaxa = convertView.findViewById(R.id.textValoareTaxa);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Details taxaTransport = getItem(position);
		viewHolder.textNumeTaxa.setText(taxaTransport.getText1());
		viewHolder.textValoareTaxa.setText(taxaTransport.getText2());

		return convertView;

	}

	
	public int getCount() {
		return listTaxe.size();
	}

	
	public Details getItem(int position) {
		return listTaxe.get(position);
	}

	
	public long getItemId(int position) {
		return 0;
	}

}
