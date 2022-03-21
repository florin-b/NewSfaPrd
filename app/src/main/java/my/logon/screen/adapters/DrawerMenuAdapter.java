/**
 * @author florinb
 * 
 */
package my.logon.screen.adapters;

import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DrawerMenuAdapter extends SimpleAdapter {

	Context context;

	static class ViewHolder {
		public TextView menuName, menuId;
	}

	public DrawerMenuAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {

		super(context, items, resource, from, to);
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {

			LayoutInflater vi = ((Activity) context).getLayoutInflater();
			v = vi.inflate(R.layout.rowlayout_menu_item, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.menuName = (TextView) v.findViewById(R.id.textMenuName);
			viewHolder.menuId = (TextView) v.findViewById(R.id.textMenuId);

			v.setTag(viewHolder);

		}

		ViewHolder holder = (ViewHolder) v.getTag();

		@SuppressWarnings("unchecked")
		HashMap<String, String> artMap = (HashMap<String, String>) this.getItem(position);

		String tokNewVal = artMap.get("menuName");
		holder.menuName.setText(tokNewVal);

		if (position == 5) {
			// holder.menuName.setTypeface(null, Typeface.BOLD);
			holder.menuName.setTextSize(20);
			holder.menuName.setTextColor(Color.parseColor("#8DB600"));
		} else {
			if (position == 6) {
				holder.menuName.setTextColor(Color.parseColor("#C19A6B"));
			} else
				holder.menuName.setTypeface(null, Typeface.NORMAL);
		}

		tokNewVal = artMap.get("menuId");
		holder.menuId.setText(tokNewVal);

		return v;

	}

}