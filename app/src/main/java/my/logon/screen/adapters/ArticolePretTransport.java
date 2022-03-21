package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.ArticolComanda;

public class ArticolePretTransport extends BaseAdapter {

	private Context context;
	private List<ArticolComanda> listArticole;
	int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	private NumberFormat nf2;

	public static class ViewHolder {
		TextView textCrt, textNumeArticol, textValTransport;
	}

	public ArticolePretTransport(Context context, List<ArticolComanda> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
		nf2 = NumberFormat.getInstance();
		nf2.setMaximumFractionDigits(2);
		nf2.setMinimumFractionDigits(2);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.row_transp_articol, parent, false);
			viewHolder.textCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArticol = (TextView) convertView.findViewById(R.id.textNumeArticol);
			viewHolder.textValTransport = (TextView) convertView.findViewById(R.id.textValoareTransport);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolComanda articol = getItem(position);

		viewHolder.textCrt.setText(String.valueOf(++position) + ".");
		viewHolder.textNumeArticol.setText(articol.getNumeArticol());
		viewHolder.textValTransport.setText(nf2.format(articol.getValTransport()));

		convertView.setBackgroundColor(colors[colorPos]);

		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public ArticolComanda getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

}
