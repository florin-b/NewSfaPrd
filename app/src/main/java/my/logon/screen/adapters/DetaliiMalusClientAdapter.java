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
import my.logon.screen.beans.SalarizareDetaliiMalus;

public class DetaliiMalusClientAdapter extends BaseAdapter {

	private Context context;
	private List<SalarizareDetaliiMalus> listDetaliiMalus;
	private NumberFormat numberFormat;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };

	public DetaliiMalusClientAdapter(List<SalarizareDetaliiMalus> listDetaliiMalus, Context context) {
		this.context = context;
		this.listDetaliiMalus = listDetaliiMalus;
		numberFormat = NumberFormat.getNumberInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);

	}

	static class ViewHolder {
		public TextView textNrFact, textDataFact, textTPFact, textTPAgreat, textTPIstoric, textValInc, textDataInc, textZileInt, textCoefPen,
				textValPenal;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_detalii_malus_client_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNrFact = (TextView) convertView.findViewById(R.id.textNrFact);
			viewHolder.textDataFact = (TextView) convertView.findViewById(R.id.textDataFact);
			viewHolder.textTPFact = (TextView) convertView.findViewById(R.id.textTPFact);
			viewHolder.textTPAgreat = (TextView) convertView.findViewById(R.id.textTPAgreat);
			viewHolder.textTPIstoric = (TextView) convertView.findViewById(R.id.textTPIstoric);
			viewHolder.textValInc = (TextView) convertView.findViewById(R.id.textValInc);
			viewHolder.textDataInc = (TextView) convertView.findViewById(R.id.textDataInc);
			viewHolder.textZileInt = (TextView) convertView.findViewById(R.id.textZileInt);
			viewHolder.textCoefPen = (TextView) convertView.findViewById(R.id.textCoefPen);
			viewHolder.textValPenal = (TextView) convertView.findViewById(R.id.textValPenal);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SalarizareDetaliiMalus detaliu = getItem(position);

		viewHolder.textNrFact.setText(detaliu.getNrFactura());
		viewHolder.textDataFact.setText(detaliu.getDataFactura());
		viewHolder.textTPFact.setText(String.valueOf(detaliu.getTpFact()));
		viewHolder.textTPAgreat.setText(String.valueOf(detaliu.getTpAgreat()));
		viewHolder.textTPIstoric.setText(String.valueOf(detaliu.getTpIstoric()));
		viewHolder.textValInc.setText(numberFormat.format(detaliu.getValIncasare()));
		viewHolder.textDataInc.setText(detaliu.getDataIncasare());
		viewHolder.textZileInt.setText(String.valueOf(detaliu.getZileIntarziere()));
		viewHolder.textCoefPen.setText(numberFormat.format(detaliu.getCoefPenalizare()));
		viewHolder.textValPenal.setText(numberFormat.format(detaliu.getPenalizare()));

		convertView.setBackgroundColor(colors[colorPos % 2]);

		return convertView;
	}

	public int getCount() {
		return listDetaliiMalus.size();
	}

	public SalarizareDetaliiMalus getItem(int position) {
		return listDetaliiMalus.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
