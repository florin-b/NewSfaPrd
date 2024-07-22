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
import my.logon.screen.beans.SalarizareDetaliiCVS;
import my.logon.screen.listeners.OperatiiSalarizareListener;

public class SalarizareCvsAdapter extends BaseAdapter {

	private Context context;
	private List<SalarizareDetaliiCVS> listDetalii;
	private NumberFormat numberFormat;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };
	private OperatiiSalarizareListener listener;

	public void setSalarizareDepartListener(OperatiiSalarizareListener listener) {
		this.listener = listener;
	}

	public SalarizareCvsAdapter(List<SalarizareDetaliiCVS> listDetalii, Context context) {
		this.context = context;
		this.listDetalii = listDetalii;
		numberFormat = NumberFormat.getNumberInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);

	}

	static class ViewHolder {
		public TextView agent, valNociv, valTotal, prag, procent, venitBaza, venitCVS;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_cvs_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.agent = (TextView) convertView.findViewById(R.id.agent);
			viewHolder.valNociv = (TextView) convertView.findViewById(R.id.valNociv);
			viewHolder.valTotal = (TextView) convertView.findViewById(R.id.valTotal);
			viewHolder.prag = (TextView) convertView.findViewById(R.id.prag);
			viewHolder.procent = (TextView) convertView.findViewById(R.id.procent);
			viewHolder.venitBaza = (TextView) convertView.findViewById(R.id.venitBaza);
			viewHolder.venitCVS = (TextView) convertView.findViewById(R.id.venitCVS);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final SalarizareDetaliiCVS detaliuCVS = getItem(position);

		viewHolder.agent.setText(detaliuCVS.getAgent());
		viewHolder.valNociv.setText(numberFormat.format(detaliuCVS.getValNociv()));
		viewHolder.valTotal.setText(numberFormat.format(detaliuCVS.getValTotal()));
		viewHolder.prag.setText(numberFormat.format(detaliuCVS.getPrag()));
		viewHolder.procent.setText(numberFormat.format(detaliuCVS.getProcent()));
		viewHolder.venitCVS.setText(numberFormat.format(detaliuCVS.getVenitCvs()));
		viewHolder.venitBaza.setText(String.valueOf(detaliuCVS.getVenitBaza()));

		if (colorPos % 2 == 0)
			convertView.setBackgroundColor(colors[0]);
		else
			convertView.setBackgroundColor(colors[1]);

		return convertView;
	}

	public int getCount() {
		return listDetalii.size();
	}

	public SalarizareDetaliiCVS getItem(int position) {
		return listDetalii.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
