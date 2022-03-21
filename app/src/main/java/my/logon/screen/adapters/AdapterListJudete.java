package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanJudet;


public class AdapterListJudete extends BaseAdapter {

	private List<BeanJudet> listJudete;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };
	private Context context;

	public AdapterListJudete(Context context, List<BeanJudet> listJudete) {
		this.context = context;
		this.listJudete = listJudete;
	}

	static class ViewHolder {
		TextView textNumeJudet, textCodJudet;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.generic_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeJudet = (TextView) convertView.findViewById(R.id.textNume);
			viewHolder.textCodJudet = (TextView) convertView.findViewById(R.id.textCod);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanJudet judet = getItem(position);

		viewHolder.textNumeJudet.setText(judet.getNumeJudet());
		viewHolder.textCodJudet.setText(judet.getCodJudet());

		convertView.setBackgroundColor(colors[position % colors.length]);

		return convertView;
	}

	public int getCount() {
		return listJudete.size();
	}

	public BeanJudet getItem(int position) {
		return listJudete.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

}
