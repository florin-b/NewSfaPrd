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
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.utils.UtilsFormatting;


public class ComandaModificareAdapter extends BaseAdapter {

	private List<BeanComandaCreata> listComenzi;
	private Context context;
	

	public ComandaModificareAdapter(List<BeanComandaCreata> listComenzi, Context context) {
		this.listComenzi = listComenzi;
		this.context = context;
		

	}

	static class ViewHolder {
		public TextView textIdCmd, textClient, textData, textSuma, textStare, textCmdSAP;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		NumberFormat numberFormat = new DecimalFormat("#0.00");

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.modificare_cmd_header, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.textIdCmd = (TextView) convertView.findViewById(R.id.textIdCmd);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textSuma = (TextView) convertView.findViewById(R.id.textSuma);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textCmdSAP = (TextView) convertView.findViewById(R.id.textCmdSAP);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanComandaCreata comanda = getItem(position);

		viewHolder.textIdCmd.setText(comanda.getId());
		viewHolder.textClient.setText(comanda.getNumeClient());
		viewHolder.textData.setText(UtilsFormatting.formatDate(comanda.getData()));

		viewHolder.textSuma.setText(numberFormat.format(Double.parseDouble(comanda.getSuma())));
		viewHolder.textStare.setText(comanda.getStare());
		if (!comanda.getCmdSap().equals("-1"))
			viewHolder.textCmdSAP.setText(comanda.getCmdSap());
		else
			viewHolder.textCmdSAP.setText(" ");

		return convertView;
	}

	public int getCount() {
		return listComenzi.size();
	}

	public BeanComandaCreata getItem(int position) {
		return listComenzi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
