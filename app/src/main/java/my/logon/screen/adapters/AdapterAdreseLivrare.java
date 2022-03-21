package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.utils.UtilsGeneral;

public class AdapterAdreseLivrare extends BaseAdapter {

	private List<BeanAdresaLivrare> listAdrese;
	private Context context;

	public AdapterAdreseLivrare(Context context, List<BeanAdresaLivrare> listAdrese) {
		this.context = context;
		this.listAdrese = listAdrese;
	}

	static class ViewHolder {
		TextView textJudet, textLocalitate, textStrada, textNrStrada;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_adresa_livrare, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textJudet = (TextView) convertView.findViewById(R.id.textJudet);
			viewHolder.textLocalitate = (TextView) convertView.findViewById(R.id.textLocalitate);
			viewHolder.textStrada = (TextView) convertView.findViewById(R.id.textStrada);
			viewHolder.textNrStrada = (TextView) convertView.findViewById(R.id.textNrStrada);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanAdresaLivrare adresa = getItem(position);

		viewHolder.textJudet.setText(UtilsGeneral.getNumeJudet(adresa.getCodJudet()));
		viewHolder.textLocalitate.setText(adresa.getOras());
		viewHolder.textStrada.setText(adresa.getStrada());
		viewHolder.textNrStrada.setText(adresa.getNrStrada());

		return convertView;

	}

	
	public int getCount() {
		return listAdrese.size();
	}

	
	public BeanAdresaLivrare getItem(int position) {
		return listAdrese.get(position);
	}

	
	public long getItemId(int position) {
		return 0;
	}

}
