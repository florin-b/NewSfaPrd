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
public class SpecialAdapter extends SimpleAdapter {

	private int[] colors = new int[] { 0x30FFFFFF, 0x30F6F9ED };

	public SpecialAdapter(Context context, List<HashMap<String, String>> items,
			int resource, String[] from, int[] to) {

		super(context, items, resource, from, to);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int colorPos = position % colors.length;
		view.setBackgroundColor(colors[colorPos]);

		return view;

	}

}
