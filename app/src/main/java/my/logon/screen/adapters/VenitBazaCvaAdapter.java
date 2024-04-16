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
import my.logon.screen.beans.SalarizareCVABazaCL;

public class VenitBazaCvaAdapter extends BaseAdapter {

	private Context context;
	private List<SalarizareCVABazaCL> listDetalii;
	private NumberFormat numberFormat;
	private int[] colors = new int[] { 0x30FFFFFF, 0x30D7DBDD };

	public VenitBazaCvaAdapter(List<SalarizareCVABazaCL> listDetalii, Context context) {
		this.context = context;
		this.listDetalii = listDetalii;
		numberFormat = NumberFormat.getNumberInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);
		
	}

	static class ViewHolder {
		public TextView textKDGRP, textKUNNR, textName1, textMATKL, textWGBEZ, textValNet, textT0, textT1A, textT1D, textT1, textVenitBaza, textCoefX,
				textT1AProc, textT1DProc;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		int colorPos = position % 2;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.venit_baza_cva_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textKDGRP = (TextView) convertView.findViewById(R.id.textKDGRP);
			viewHolder.textKUNNR = (TextView) convertView.findViewById(R.id.textKUNNR);
			viewHolder.textName1 = (TextView) convertView.findViewById(R.id.textName1);
			viewHolder.textMATKL = (TextView) convertView.findViewById(R.id.textMATKL);
			viewHolder.textWGBEZ = (TextView) convertView.findViewById(R.id.textWGBEZ);
			viewHolder.textValNet = (TextView) convertView.findViewById(R.id.textValNet);
			viewHolder.textT0 = (TextView) convertView.findViewById(R.id.textT0);
			viewHolder.textT1A = (TextView) convertView.findViewById(R.id.textT1A);
			viewHolder.textT1D = (TextView) convertView.findViewById(R.id.textT1D);
			viewHolder.textT1 = (TextView) convertView.findViewById(R.id.textT1);
			viewHolder.textVenitBaza = (TextView) convertView.findViewById(R.id.textVenitBaza);
			viewHolder.textCoefX = (TextView) convertView.findViewById(R.id.textCoefX);
			viewHolder.textT1AProc = (TextView) convertView.findViewById(R.id.textT1AProc);
			viewHolder.textT1DProc = (TextView) convertView.findViewById(R.id.textT1DProc);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SalarizareCVABazaCL detaliu = getItem(position);

		viewHolder.textKDGRP.setText(detaliu.getKDGRP());
		viewHolder.textKUNNR.setText(detaliu.getKUNNR());
		viewHolder.textName1.setText(detaliu.getNAME1());
		viewHolder.textMATKL.setText(detaliu.getMATKL());
		viewHolder.textWGBEZ.setText(detaliu.getWGBEZ());

		viewHolder.textValNet.setText(numberFormat.format(detaliu.getVAL_NET()));
		viewHolder.textT0.setText(numberFormat.format(detaliu.getT0()));
		viewHolder.textT1A.setText(numberFormat.format(detaliu.getT1A()));
		viewHolder.textT1D.setText(numberFormat.format(detaliu.getT1D()));
		viewHolder.textT1.setText(numberFormat.format(detaliu.getT1()));
		viewHolder.textVenitBaza.setText(numberFormat.format(detaliu.getVENIT_BAZA()));
		viewHolder.textCoefX.setText(detaliu.getCOEF_X().trim().replace(",","."));
		viewHolder.textT1AProc.setText(detaliu.getT1A_PROC().trim().replace(",","."));
		viewHolder.textT1DProc.setText(detaliu.getT1D_PROC().trim().replace(",","."));

		if (colorPos % 2 == 0)
			convertView.setBackgroundColor(colors[colorPos]);
		else
			convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

	public int getCount() {
		return listDetalii.size();
	}

	public SalarizareCVABazaCL getItem(int position) {
		return listDetalii.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}


