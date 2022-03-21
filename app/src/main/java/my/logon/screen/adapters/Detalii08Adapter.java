package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.beans.SalarizareDetaliiInc08;


public class Detalii08Adapter extends BaseAdapter {

	private Context context;
	private List<SalarizareDetaliiInc08> listDetalii08;
	private NumberFormat numberFormat ;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };

	public Detalii08Adapter(List<SalarizareDetaliiInc08> listDetalii08, Context context) {
		this.context = context;
		this.listDetalii08 = listDetalii08;
		numberFormat = NumberFormat.getNumberInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);
		
	}

	static class ViewHolder {
		public TextView textNumeClient, textValoareIncasare, textVenitCorectat;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_detalii_0_8_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textValoareIncasare = (TextView) convertView.findViewById(R.id.textValoareIncasare);
			viewHolder.textVenitCorectat = (TextView) convertView.findViewById(R.id.textVenitCorectat);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SalarizareDetaliiInc08 detaliu = getItem(position);

		viewHolder.textNumeClient.setText(detaliu.getNumeClient());
		viewHolder.textValoareIncasare.setText(numberFormat.format(detaliu.getValoareIncasare()));
		viewHolder.textVenitCorectat.setText(numberFormat.format(detaliu.getVenitCorectat()));

		if (colorPos % 2 == 0)
			convertView.setBackgroundColor(colors[colorPos]);
		else
			convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

	public int getCount() {
		return listDetalii08.size();
	}

	public SalarizareDetaliiInc08 getItem(int position) {
		return listDetalii08.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
