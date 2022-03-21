package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import my.logon.screen.R;
import my.logon.screen.enums.EnumStadiuObiectivKA;


public class AdapterStadiuObiectiv extends BaseAdapter {

	private Context context;

	public static class ViewHolder {
		TextView textStatus;
		ImageView imageStatus;
	}

	public AdapterStadiuObiectiv(Context context) {
		this.context = context;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_stadiu, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textStatus = (TextView) convertView.findViewById(R.id.textStatus);
			viewHolder.imageStatus = (ImageView) convertView.findViewById(R.id.imageStatus);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textStatus.setText(EnumStadiuObiectivKA.getNumeStadiu(position));

		switch (position) {
		case 0:
			viewHolder.imageStatus.setBackgroundResource(R.drawable.green_button_32);
			break;
		case 1:
			viewHolder.imageStatus.setBackgroundResource(R.drawable.blue_button_32);
			break;
		case 2:
			viewHolder.imageStatus.setBackgroundResource(R.drawable.yellow_button_32);
			break;
		}

		return convertView;
	}

	public int getCount() {
		return EnumStadiuObiectivKA.values().length;

	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

}
