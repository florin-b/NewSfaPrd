package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanClient;


public class CautareClientiAdapter extends BaseAdapter {

	Context context;
	List<BeanClient> listClienti;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	static class ViewHolder {
		TextView textNumeClient, textCodClient, textTipClient;
	}

	public CautareClientiAdapter(Context context, List<BeanClient> listClienti) {
		this.context = context;
		this.listClienti = listClienti;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.client_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeClient = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textCodClient = (TextView) convertView.findViewById(R.id.textCodClient);
			viewHolder.textTipClient = (TextView) convertView.findViewById(R.id.textTipClient);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanClient client = getItem(position);

		viewHolder.textNumeClient.setText(client.getNumeClient());
		viewHolder.textCodClient.setText(client.getCodClient());
		viewHolder.textTipClient.setText(client.getTipClient());

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listClienti.size();
	}

	public BeanClient getItem(int position) {
		return listClienti.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
