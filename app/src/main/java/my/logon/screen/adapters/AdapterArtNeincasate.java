package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanArticolVanzari;


public class AdapterArtNeincasate extends BaseAdapter {

	private List<BeanArticolVanzari> listArticole;
	private Context context;
	private NumberFormat nf = new DecimalFormat("#,##0.00");

	public AdapterArtNeincasate(Context context, List<BeanArticolVanzari> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	static class ViewHolder {
		TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textValArt;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.art_neincasate, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textValArt = (TextView) convertView.findViewById(R.id.textValArt);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanArticolVanzari articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1) + ".");
		viewHolder.textNumeArt.setText(articol.getNumeArticol());
		viewHolder.textCodArt.setText(articol.getCodArticol());
		viewHolder.textCantArt.setText(nf.format(Double.valueOf(articol.getCantitateArticol())));
		viewHolder.textValArt.setText(nf.format(Double.valueOf(articol.getValoareArticol())));

		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public BeanArticolVanzari getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

}
