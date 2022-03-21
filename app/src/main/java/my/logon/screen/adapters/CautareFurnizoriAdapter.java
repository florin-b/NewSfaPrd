package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanFurnizor;


public class CautareFurnizoriAdapter extends BaseAdapter {

	private Context context;
	private List<BeanFurnizor> listFurnizori;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	static class ViewHolder {
		TextView textNumeFurnizor, textCodFurnizor, textTipClient;
	}

	public CautareFurnizoriAdapter(Context context) {
		this.context = context;
	}

	public void setListFurnizori(List<BeanFurnizor> listFurnizori) {
		this.listFurnizori = listFurnizori;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.client_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeFurnizor = (TextView) convertView.findViewById(R.id.textNumeClient);
			viewHolder.textCodFurnizor = (TextView) convertView.findViewById(R.id.textCodClient);
			viewHolder.textTipClient = (TextView) convertView.findViewById(R.id.textTipClient);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanFurnizor client = (BeanFurnizor) getItem(position);

		viewHolder.textNumeFurnizor.setText(client.getNumeFurnizor());
		viewHolder.textCodFurnizor.setText(client.getCodFurnizor());

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listFurnizori.size();

	}

	public Object getItem(int position) {
		return listFurnizori.get(position);

	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
