package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.ArticolDB;


public class CautareArticoleAdapter extends BaseAdapter {

	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };
	Context context;
	List<ArticolDB> listArticole;

	static class ViewHolder {
		public TextView textNumeArticol, textCodArticol, textTipArt, textStoc;
	}

	public CautareArticoleAdapter(Context context, List<ArticolDB> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.articol_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeArticol = (TextView) convertView.findViewById(R.id.textNumeArticol);
			viewHolder.textCodArticol = (TextView) convertView.findViewById(R.id.textCodArticol);
			viewHolder.textTipArt = (TextView) convertView.findViewById(R.id.textTipArt);
			viewHolder.textStoc = (TextView) convertView.findViewById(R.id.textStoc);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolDB articol = getItem(position);
		viewHolder.textNumeArticol.setText(articol.getNume());
		viewHolder.textCodArticol.setText(articol.getCod());
		viewHolder.textTipArt.setText(articol.getTipAB());

		if (Double.valueOf(articol.getStoc()) > 0)
			viewHolder.textStoc.setText(articol.getStoc() + " " + articol.getUmVanz());

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public ArticolDB getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
