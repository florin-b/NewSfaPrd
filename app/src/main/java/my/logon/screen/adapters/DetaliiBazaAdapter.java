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
import my.logon.screen.beans.SalarizareDetaliiBaza;

public class DetaliiBazaAdapter extends BaseAdapter {

	private Context context;
	private List<SalarizareDetaliiBaza> listDetalii;
	private NumberFormat numberFormat;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };

	public DetaliiBazaAdapter(List<SalarizareDetaliiBaza> listDetalii, Context context) {
		this.context = context;
		this.listDetalii = listDetalii;
		numberFormat = NumberFormat.getNumberInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);

	}

	static class ViewHolder {
		public TextView textNumeClient, textCodSintetic, textNumeSintetic, textValNet, textT0, textT1A, textT1D, textT1, textVenitBaza;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_detalii_baza_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textCodSintetic = (TextView) convertView.findViewById(R.id.textCodSintetic);
			viewHolder.textNumeSintetic = (TextView) convertView.findViewById(R.id.textNumeSintetic);
			viewHolder.textValNet = (TextView) convertView.findViewById(R.id.textValNet);
			viewHolder.textT0 = (TextView) convertView.findViewById(R.id.textT0);
			viewHolder.textT1A = (TextView) convertView.findViewById(R.id.textT1A);
			viewHolder.textT1D = (TextView) convertView.findViewById(R.id.textT1D);
			viewHolder.textT1 = (TextView) convertView.findViewById(R.id.textT1);
			viewHolder.textVenitBaza = (TextView) convertView.findViewById(R.id.textVenitBaza);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SalarizareDetaliiBaza detaliu = getItem(position);

		viewHolder.textNumeClient.setText(detaliu.getNumeClient());
		viewHolder.textCodSintetic.setText(detaliu.getCodSintetic());
		viewHolder.textNumeSintetic.setText(detaliu.getNumeSintetic());
		viewHolder.textValNet.setText(numberFormat.format(detaliu.getValoareNeta()));
		viewHolder.textT0.setText(numberFormat.format(detaliu.getT0()));
		viewHolder.textT1A.setText(numberFormat.format(detaliu.getT1A()));
		viewHolder.textT1D.setText(numberFormat.format(detaliu.getT1D()));
		viewHolder.textT1.setText(numberFormat.format(detaliu.getT1()));
		viewHolder.textVenitBaza.setText(numberFormat.format(detaliu.getVenitBaza()));

		if (colorPos % 2 == 0)
			convertView.setBackgroundColor(colors[colorPos]);
		else
			convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

	public int getCount() {
		return listDetalii.size();
	}

	public SalarizareDetaliiBaza getItem(int position) {
		return listDetalii.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
