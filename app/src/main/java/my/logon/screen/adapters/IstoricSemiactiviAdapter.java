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
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanIstoricSemiactiv;

public class IstoricSemiactiviAdapter extends BaseAdapter {

	private int[] colors = new int[] { 0x30D1BC71, 0x30F6F2E4 };
	private List<BeanIstoricSemiactiv> listIstoric;
	Context context;

	static class ViewHolder {
		public TextView textAn, textLuna, textVanz03, textVanz06, textVanz07, textVanz09, textVanz040, textVanz041;

	}

	public IstoricSemiactiviAdapter(Context context, List<BeanIstoricSemiactiv> listIstoric) {
		this.context = context;
		this.listIstoric = listIstoric;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.row_istoric_semiactivi, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textAn = (TextView) convertView.findViewById(R.id.textAn);
			viewHolder.textLuna = (TextView) convertView.findViewById(R.id.textLuna);
			viewHolder.textVanz03 = (TextView) convertView.findViewById(R.id.textVanz03);
			viewHolder.textVanz06 = (TextView) convertView.findViewById(R.id.textVanz06);
			viewHolder.textVanz07 = (TextView) convertView.findViewById(R.id.textVanz07);
			viewHolder.textVanz09 = (TextView) convertView.findViewById(R.id.textVanz09);
			viewHolder.textVanz040 = (TextView) convertView.findViewById(R.id.textVanz040);
			viewHolder.textVanz041 = (TextView) convertView.findViewById(R.id.textVanz041);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanIstoricSemiactiv istoric = getItem(position);

		viewHolder.textAn.setText(istoric.getAn());
		viewHolder.textLuna.setText(istoric.getLuna());
		viewHolder.textVanz03.setText(istoric.getVanz03());
		viewHolder.textVanz06.setText(istoric.getVanz06());
		viewHolder.textVanz07.setText(istoric.getVanz07());
		viewHolder.textVanz09.setText(istoric.getVanz09());
		viewHolder.textVanz040.setText(istoric.getVanz040());
		viewHolder.textVanz041.setText(istoric.getVanz041());
		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;

	}

	public int getCount() {
		return listIstoric.size();
	}

	public BeanIstoricSemiactiv getItem(int position) {
		return listIstoric.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

}