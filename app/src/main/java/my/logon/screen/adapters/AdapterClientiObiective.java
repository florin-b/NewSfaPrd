package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.utils.UtilsGeneral;

public class AdapterClientiObiective extends BaseAdapter {

	private List<BeanObiectiveConstructori> listConstructori;
	private Context context;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public AdapterClientiObiective(Context context, List<BeanObiectiveConstructori> listConstructori) {
		this.context = context;
		this.listConstructori = listConstructori;
	}

	public static class ViewHolder {
		TextView textNumeClient, textCodClient, textTipClient;

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

		if (position == 0) {
			viewHolder.textNumeClient.setText("Selectati un client");
			viewHolder.textCodClient.setText("");
			viewHolder.textTipClient.setText("");
		} else {
			BeanObiectiveConstructori constructor = getItem(position - 1);
			viewHolder.textNumeClient.setText(constructor.getNumeClient());
			viewHolder.textCodClient.setText(constructor.getCodClient());
			viewHolder.textTipClient.setText(UtilsGeneral.getNumeCategorieClient(constructor.getCodDepart()));
		}

		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

	public int getCount() {
		return listConstructori.size() + 1;
	}

	public BeanObiectiveConstructori getItem(int position) {
		return listConstructori.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
