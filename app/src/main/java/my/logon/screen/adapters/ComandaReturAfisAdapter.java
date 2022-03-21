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
import my.logon.screen.beans.BeanComandaReturAfis;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.utils.UtilsFormatting;


public class ComandaReturAfisAdapter extends BaseAdapter {

	private Context context;
	private List<BeanComandaReturAfis> listComenzi;
	NumberFormat numberFormat = new DecimalFormat("#0.00");

	static class ViewHolder {
		public TextView textIdCmd, textClient, textData, textStare, textNrDocument, textNumeAgent;
	}

	public ComandaReturAfisAdapter(List<BeanComandaReturAfis> listComenzi, Context context) {
		this.context = context;
		this.listComenzi = listComenzi;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.cmd_retur_afis_row, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textIdCmd = (TextView) convertView.findViewById(R.id.textIdCmd);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textStare = (TextView) convertView.findViewById(R.id.textStare);
			viewHolder.textNrDocument = (TextView) convertView.findViewById(R.id.textNrDocument);
			viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanComandaReturAfis comanda = getItem(position);

		viewHolder.textIdCmd.setText(comanda.getId());
		viewHolder.textClient.setText(comanda.getNumeClient());
		viewHolder.textData.setText(UtilsFormatting.formatDate(comanda.getDataCreare()));
		viewHolder.textStare.setText(InfoStrings.statusAprobCmd(Integer.parseInt(comanda.getStatus())));
		viewHolder.textNrDocument.setText(comanda.getNrDocument());
		viewHolder.textNumeAgent.setText(comanda.getNumeAgent());

		return convertView;
	}

	public int getCount() {
		return listComenzi.size();
	}

	public BeanComandaReturAfis getItem(int position) {
		return listComenzi.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
