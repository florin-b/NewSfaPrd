/**
 * @author florinb
 *
 */
package my.logon.screen.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

//zebra
public class ListMenuSpecialAdapter extends SimpleAdapter {

	private int[] colors = new int[] { 0x30F6F2E4, 0x30F6F2E4, 0x30F6F2E4 };

	public ListMenuSpecialAdapter(Context context,
			List<HashMap<String, String>> items, int resource, String[] from,
			int[] to) {

		super(context, items, resource, from, to);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		// int colorPos = position % colors.length;
		// view.setBackgroundColor(colors[colorPos]);

		if (position <= 3)
			view.setBackgroundColor(colors[0]);
		if (position == 4)
			view.setBackgroundColor(colors[1]);
		if (position > 4)
			view.setBackgroundColor(colors[2]);

		return view;

	}

}