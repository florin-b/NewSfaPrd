package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanDatePersonale;
import my.logon.screen.enums.EnumJudete;

public class AfisDatePersAdapter extends BaseAdapter {

	private Context context;
	private List<BeanDatePersonale> listDatePers;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	static class ViewHolder {
		TextView textNume, textCnp, textAdresa, textDiviziiClient;
	}

	public AfisDatePersAdapter(Context context, List<BeanDatePersonale> listDatePers) {
		this.context = context;
		this.listDatePers = listDatePers;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.date_pers_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNume = (TextView) convertView.findViewById(R.id.textNume);
			viewHolder.textCnp = (TextView) convertView.findViewById(R.id.textCnp);
			viewHolder.textAdresa = (TextView) convertView.findViewById(R.id.textAdresa);
			viewHolder.textDiviziiClient = (TextView) convertView.findViewById(R.id.textDiviziiClient);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanDatePersonale datePers = getItem(position);

		viewHolder.textNume.setText(datePers.getNume());
		viewHolder.textCnp.setText(datePers.getCnp());

		String adresa = EnumJudete.getNumeJudet(Integer.parseInt(datePers.getCodjudet())) + ", " + datePers.getLocalitate();

		if (!datePers.getStrada().isEmpty())
			adresa += ", " + datePers.getStrada();

		viewHolder.textAdresa.setText(adresa);
		viewHolder.textDiviziiClient.setText(!datePers.getDivizii().isEmpty() ? "Divizii:" + datePers.getDivizii() : "");

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listDatePers.size();
	}

	public BeanDatePersonale getItem(int position) {
		return listDatePers.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
