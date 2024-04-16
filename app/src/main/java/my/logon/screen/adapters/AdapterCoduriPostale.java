package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanCodPostal;

public class AdapterCoduriPostale extends BaseAdapter {

	private List<BeanCodPostal> listCoduri;
	private Context context;

	public AdapterCoduriPostale(Context context, List<BeanCodPostal> listCoduri) {
		this.context = context;
		this.listCoduri = listCoduri;
	}

	static class ViewHolder {
		TextView textLocalitate, textStrada, textNrStrada, textCodPostal;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_cod_postal, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textLocalitate = convertView.findViewById(R.id.textLocalitate);
			viewHolder.textStrada = convertView.findViewById(R.id.textStrada);
			viewHolder.textNrStrada = convertView.findViewById(R.id.textNrStrada);
			viewHolder.textCodPostal = convertView.findViewById(R.id.textCodPostal);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanCodPostal codPostal = getItem(position);
		viewHolder.textLocalitate.setText(codPostal.getLocalitate());
		viewHolder.textStrada.setText(codPostal.getStrada());
		viewHolder.textNrStrada.setText(codPostal.getNrStrada());
		viewHolder.textCodPostal.setText(codPostal.getCodPostal());

		return convertView;

	}

	
	public int getCount() {
		return listCoduri.size();
	}

	
	public BeanCodPostal getItem(int position) {
		return listCoduri.get(position);
	}

	
	public long getItemId(int position) {
		return 0;
	}

}
