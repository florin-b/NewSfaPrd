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
import my.logon.screen.beans.ArticolCLP;


public class ArticoleCLPAdapter extends BaseAdapter {

	Context context;
	List<ArticolCLP> listArticole;
	int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public ArticoleCLPAdapter(Context context, List<ArticolCLP> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	static class ViewHolder {
		TextView textNrCrt, textNumeArt, textCodArt, textCantArt, textUmArt, textDepozit, textStatusArt;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		NumberFormat nf2 = new DecimalFormat("#0.00");
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.art_comenzi_clp, parent, false);

			viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArt = (TextView) convertView.findViewById(R.id.textNumeArt);
			viewHolder.textCodArt = (TextView) convertView.findViewById(R.id.textCodArt);
			viewHolder.textCantArt = (TextView) convertView.findViewById(R.id.textCantArt);
			viewHolder.textUmArt = (TextView) convertView.findViewById(R.id.textUmArt);
			viewHolder.textDepozit = (TextView) convertView.findViewById(R.id.textDepozit);
			viewHolder.textStatusArt = (TextView) convertView.findViewById(R.id.textStatusArt);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ArticolCLP articol = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1));
		viewHolder.textNumeArt.setText(articol.getNume());
		viewHolder.textCodArt.setText(articol.getCod());
		viewHolder.textCantArt.setText(nf2.format(Float.parseFloat(articol.getCantitate())));
		viewHolder.textUmArt.setText(articol.getUmBaza());
		viewHolder.textDepozit.setText(articol.getDepozit());
		viewHolder.textStatusArt.setText(articol.getStatus());

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public ArticolCLP getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
