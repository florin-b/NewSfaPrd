package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.ObiectivConsilier;


public class CautareObiectivConsAdapter extends BaseAdapter {

	Context context;
	List<ObiectivConsilier> listObiective;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	static class ViewHolder {
		TextView textNumeObiectiv, textDataCreare, textAdresa;
	}

	public CautareObiectivConsAdapter(Context context, List<ObiectivConsilier> listObiective) {
		this.context = context;
		this.listObiective = listObiective;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		int colorPos = position % colors.length;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.obiectiv_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNumeObiectiv = (TextView) convertView.findViewById(R.id.textNumeObiectiv);
			viewHolder.textDataCreare = (TextView) convertView.findViewById(R.id.textDataCreare);
			viewHolder.textAdresa = (TextView) convertView.findViewById(R.id.textAdresa);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ObiectivConsilier obiectiv = getItem(position);

		viewHolder.textNumeObiectiv.setText(obiectiv.getBeneficiar());
		viewHolder.textDataCreare.setText(obiectiv.getDataCreare());
		viewHolder.textAdresa.setText(obiectiv.getAdresa());

		convertView.setBackgroundColor(colors[colorPos]);
		return convertView;
	}

	public int getCount() {
		return listObiective.size();
	}

	public ObiectivConsilier getItem(int position) {
		return listObiective.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
