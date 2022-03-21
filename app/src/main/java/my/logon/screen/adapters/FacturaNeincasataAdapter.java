/**
 * @author florinb
 *
 */
package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.FacturaNeincasataLite;
import my.logon.screen.patterns.FactNeincDataComparator;
import my.logon.screen.patterns.FactNeincDocComparator;
import my.logon.screen.patterns.FactNeincOrdComparator;
import my.logon.screen.patterns.NeincasateChainedComparator;


public class FacturaNeincasataAdapter extends BaseAdapter {

	private List<FacturaNeincasataLite> listFacturi;
	private Context context;
	private NumberFormat numberFormat = new DecimalFormat("#,##0.00");
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	private double sumaPlata;
	private int ordSelectie = 0;

	static class ViewHolder {

		public TextView textDocument, textDataEmitere, textRestPlata, textPlatit;
		public CheckBox checkBox;

	}

	public FacturaNeincasataAdapter(List<FacturaNeincasataLite> listFacturi, Context context) {
		this.listFacturi = listFacturi;
		this.context = context;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.row_neincasata, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textDocument = (TextView) convertView.findViewById(R.id.textDocument);
			viewHolder.textDataEmitere = (TextView) convertView.findViewById(R.id.textDataEmitere);
			viewHolder.textRestPlata = (TextView) convertView.findViewById(R.id.textRestPlata);
			viewHolder.textPlatit = (TextView) convertView.findViewById(R.id.textPlatit);

			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.selectDocument);

			viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					int getPosition = (Integer) buttonView.getTag();
					listFacturi.get(getPosition).setSelectata(isChecked);

					if (isChecked && listFacturi.get(getPosition).getOrdineSelectie() == -1) {
						listFacturi.get(getPosition).setOrdineSelectie(ordSelectie);
						ordSelectie++;
					}

					if (isSumaDisponibila() && isChecked) {
						viewHolder.checkBox.setChecked(isChecked);
					} else {
						listFacturi.get(getPosition).setOrdineSelectie(-1);
						viewHolder.checkBox.setChecked(false);
						listFacturi.get(getPosition).setSelectata(false);
						listFacturi.get(getPosition).setPlatit(0);
						notifyDataSetChanged();

					}

				}
			});

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final FacturaNeincasataLite factura = getItem(position);

		viewHolder.textDocument.setText(factura.getNrDocument());
		viewHolder.textDataEmitere.setText(factura.getDataEmitere());
		viewHolder.textRestPlata.setText(numberFormat.format(factura.getRestPlata()));
		viewHolder.textPlatit.setText(numberFormat.format(factura.getPlatit()));
		viewHolder.checkBox.setTag(position);
		viewHolder.checkBox.setChecked(listFacturi.get(position).isSelectata());

		convertView.setBackgroundColor(colors[position % colors.length]);

		return convertView;

	}

	private boolean isSumaDisponibila() {

		List<FacturaNeincasataLite> tempFacturi = new ArrayList<FacturaNeincasataLite>(listFacturi);

		Collections.sort(tempFacturi, new FactNeincOrdComparator());

		boolean disponibil = true;

		double restDisponibil = sumaPlata;

		for (FacturaNeincasataLite factura : tempFacturi) {
			if (factura.isSelectata()) {
				if (restDisponibil >= factura.getRestPlata()) {
					restDisponibil -= factura.getRestPlata();
					factura.setPlatit(Math.round(factura.getRestPlata() * 100.00) / 100.00);
				} else {
					if (restDisponibil > 0) {
						factura.setPlatit(Math.round(restDisponibil * 100.00) / 100.00);
						restDisponibil -= factura.getRestPlata();

					} else
						disponibil = false;

				}
			}

		}

		refreshListFacturi(tempFacturi);

		return disponibil;
	}

	@SuppressWarnings("unchecked")
	private void refreshListFacturi(List<FacturaNeincasataLite> tempFacturi) {

		this.listFacturi = new ArrayList<FacturaNeincasataLite>(tempFacturi);
		Collections.sort(listFacturi, new NeincasateChainedComparator<FacturaNeincasataLite>(new FactNeincDataComparator(), new FactNeincDocComparator()));
		notifyDataSetChanged();

	}

	@SuppressWarnings("unchecked")
	public void sortByData() {

		Collections.sort(listFacturi, new NeincasateChainedComparator<FacturaNeincasataLite>(new FactNeincDataComparator(), new FactNeincDocComparator()));
		notifyDataSetChanged();
	}

	public int getCount() {
		return listFacturi.size();
	}

	public FacturaNeincasataLite getItem(int position) {
		return listFacturi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public double getSumaPlata() {
		return sumaPlata;
	}

	public void setSumaPlata(double sumaPlata) {
		this.sumaPlata = sumaPlata;
	}

}
