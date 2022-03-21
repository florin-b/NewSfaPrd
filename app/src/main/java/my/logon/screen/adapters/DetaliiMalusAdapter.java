package my.logon.screen.adapters;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import my.logon.screen.beans.SalarizareDetaliiMalusLite;

public class DetaliiMalusAdapter extends BaseAdapter {

	private Context context;
	private List<SalarizareDetaliiMalusLite> listDetaliiMalus;
	private NumberFormat numberFormat;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };

	public DetaliiMalusAdapter(List<SalarizareDetaliiMalusLite> listDetaliiMalus, Context context) {
		this.context = context;
		this.listDetaliiMalus = listDetaliiMalus;
		numberFormat = NumberFormat.getNumberInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);

	}

	static class ViewHolder {
		public TextView textNumeClient, textValoareFactura, textPenalizare;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_detalii_malus_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textValoareFactura = (TextView) convertView.findViewById(R.id.textValoareFactura);
			viewHolder.textPenalizare = (TextView) convertView.findViewById(R.id.textPenalizare);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SalarizareDetaliiMalusLite detaliu = getItem(position);

		viewHolder.textNumeClient.setText(detaliu.getNumeClient());
		viewHolder.textValoareFactura.setText(numberFormat.format(detaliu.getValoareFactura()));
		viewHolder.textPenalizare.setText(numberFormat.format(detaliu.getPenalizare()));

		convertView.setBackgroundColor(colors[colorPos % 2]);

		return convertView;
	}

	public int getCount() {
		return listDetaliiMalus.size();
	}

	public SalarizareDetaliiMalusLite getItem(int position) {
		return listDetaliiMalus.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
