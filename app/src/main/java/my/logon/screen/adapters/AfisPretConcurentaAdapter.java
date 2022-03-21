package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanPretConcurenta;
import my.logon.screen.utils.UtilsFormatting;


public class AfisPretConcurentaAdapter extends BaseAdapter {

	private Context context;
	List<BeanPretConcurenta> listArticole;

	public AfisPretConcurentaAdapter(Context context, List<BeanPretConcurenta> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	static class ViewHolder {
		public TextView textCrt, textData, textValoare;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.customrowpret_conc, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textCrt = (TextView) convertView.findViewById(R.id.textCrt);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textValoare = (TextView) convertView.findViewById(R.id.textValoare);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanPretConcurenta pretArticol = getItem(position);

		viewHolder.textCrt.setText(String.valueOf(position + 1));
		viewHolder.textData.setText(UtilsFormatting.formatDate(pretArticol.getData()));
		viewHolder.textValoare.setText(pretArticol.getValoare());

		return convertView;
	}

	public int getCount() {
		return listArticole.size();
	}

	public BeanPretConcurenta getItem(int position) {
		return listArticole.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

}
