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
import my.logon.screen.beans.VenitTCF;


public class AdapterVenitTCF extends BaseAdapter {

	private List<VenitTCF> listTCF;
	private Context context;
	private NumberFormat nf = new DecimalFormat("#,##0.00");

	public AdapterVenitTCF(Context context) {
		this.context = context;
	}

	static class ViewHolder {
		public TextView textVenitGrilaInc, textTargetPropus, textTargetReal, textCoefAfect, textVenitTcf;

	}

	public void setListTCF(List<VenitTCF> listTCF) {
		this.listTCF = listTCF;
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_tcf, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textVenitGrilaInc = (TextView) convertView.findViewById(R.id.textVenitGrilaInc);
			viewHolder.textTargetPropus = (TextView) convertView.findViewById(R.id.textTargetPropus);
			viewHolder.textTargetReal = (TextView) convertView.findViewById(R.id.textTargetReal);
			viewHolder.textCoefAfect = (TextView) convertView.findViewById(R.id.textCoefAfect);
			viewHolder.textVenitTcf = (TextView) convertView.findViewById(R.id.textVenitTcf);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final VenitTCF venitTcf = getItem(position);
		viewHolder.textVenitGrilaInc.setText(nf.format(Double.valueOf(venitTcf.getVenitGrInc())));
		viewHolder.textTargetPropus.setText(nf.format(Double.valueOf(venitTcf.getTargetPropus())));
		viewHolder.textTargetReal.setText(nf.format(Double.valueOf(venitTcf.getTargetRealizat())));
		viewHolder.textCoefAfect.setText(nf.format(Double.valueOf(venitTcf.getCoefAfectare())));
		viewHolder.textVenitTcf.setText(nf.format(Double.valueOf(venitTcf.getVenitTcf())));

		return convertView;
	}

	
	public int getCount() {
		return listTCF.size();
	}

	
	public VenitTCF getItem(int position) {
		return listTCF.get(position);
	}

	
	public long getItemId(int arg0) {
		return 0;
	}

}
