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
import my.logon.screen.beans.VenitTPR;


public class AdapterVenitTPR extends BaseAdapter {

	private List<VenitTPR> listTPR;
	private Context context;
	private NumberFormat nf = new DecimalFormat("#,##0.00");
	
	public AdapterVenitTPR(Context context) {
		this.context = context;
	}

	static class ViewHolder {
		public TextView textArticol, textVenitGrInc, textPondere, textTargetPropCant, textTargetRealCant, textUm, textTargetPropVal, textRealizatVal,
				textRealizareTarget, textTargetPonderat;

	}

	public void setListTPR(List<VenitTPR> listTPR) {
		this.listTPR = listTPR;
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_tpr, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textArticol = (TextView) convertView.findViewById(R.id.textArticol);
			viewHolder.textVenitGrInc = (TextView) convertView.findViewById(R.id.textVenitGrInc);
			viewHolder.textPondere = (TextView) convertView.findViewById(R.id.textPondere);
			viewHolder.textTargetPropCant = (TextView) convertView.findViewById(R.id.textTargetPropCant);
			viewHolder.textTargetRealCant = (TextView) convertView.findViewById(R.id.textTargetRealCant);
			viewHolder.textUm = (TextView) convertView.findViewById(R.id.textUm);
			viewHolder.textTargetPropVal = (TextView) convertView.findViewById(R.id.textTargetPropVal);
			viewHolder.textRealizatVal = (TextView) convertView.findViewById(R.id.textRealizatVal);
			viewHolder.textRealizareTarget = (TextView) convertView.findViewById(R.id.textRealizareTarget);
			viewHolder.textTargetPonderat = (TextView) convertView.findViewById(R.id.textTargetPonderat);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final VenitTPR venitTpr = getItem(position);
		viewHolder.textArticol.setText(venitTpr.getCodN2() + " " + venitTpr.getNumeN2());
		viewHolder.textVenitGrInc.setText(nf.format(Double.valueOf(venitTpr.getVenitGrInc())));
		viewHolder.textPondere.setText(nf.format(Double.valueOf(venitTpr.getPondere())));
		viewHolder.textTargetPropCant.setText(nf.format(Double.valueOf(venitTpr.getTargetPropCant())));

		viewHolder.textTargetRealCant.setText(nf.format(Double.valueOf(venitTpr.getTargetRealCant())));
		viewHolder.textUm.setText(venitTpr.getUm());
		viewHolder.textTargetPropVal.setText(nf.format(Double.valueOf(venitTpr.getTargetPropVal())));
		viewHolder.textRealizatVal.setText(nf.format(Double.valueOf(venitTpr.getTargetRealVal())));
		viewHolder.textRealizareTarget.setText(nf.format(Double.valueOf(venitTpr.getRealizareTarget())));
		viewHolder.textTargetPonderat.setText(nf.format(Double.valueOf(venitTpr.getTargetPonderat())));

		return convertView;
	}

	
	public int getCount() {
		return listTPR.size();
	}

	
	public VenitTPR getItem(int position) {
		return listTPR.get(position);
	}

	
	public long getItemId(int arg0) {
		return 0;
	}

}
