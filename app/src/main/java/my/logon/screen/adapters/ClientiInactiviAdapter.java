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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ClientiInactiviAdapter extends SimpleAdapter {

	private int[] colors = new int[] { 0x30D1BC71, 0x30F6F2E4 };
	Context context;

	static class ViewHolder {
		public TextView textNrCrt, textNumeClient, textCodClient, textStare, textTipClient;
		public ImageView image;
	}

	public ClientiInactiviAdapter(Context context,
			List<HashMap<String, String>> items, int resource, String[] from,
			int[] to) {
		super(context, items, resource, from, to);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int colorPos = position % colors.length;

		if (view != null) {
			LayoutInflater vi = ((Activity) context).getLayoutInflater();
			view = vi.inflate(R.layout.custom_art_row_clienti_inactivi, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) view.findViewById(R.id.textNrCrt);
			viewHolder.textNumeClient = (TextView) view
					.findViewById(R.id.textNumeClient);
			viewHolder.textCodClient = (TextView) view
					.findViewById(R.id.textCodClient);
			viewHolder.textStare = (TextView) view.findViewById(R.id.textStare);
			viewHolder.textTipClient = (TextView) view.findViewById(R.id.textTipClient);
			viewHolder.image = (ImageView) view
					.findViewById(R.id.imageAlertaClient);
			view.setTag(viewHolder);

			view.setFocusableInTouchMode(false);

		}

		ViewHolder holder = (ViewHolder) view.getTag();
		@SuppressWarnings("unchecked")
		HashMap<String, String> artMap = (HashMap<String, String>) this
				.getItem(position);

		String tokNewVal = artMap.get("nrCrt");
		holder.textNrCrt.setText(tokNewVal);

		tokNewVal = artMap.get("numeClient");
		holder.textNumeClient.setText(tokNewVal);

		tokNewVal = artMap.get("codClient");
		holder.textCodClient.setText(tokNewVal);
		
		tokNewVal = artMap.get("tipClient");
		holder.textTipClient.setText(tokNewVal);		

		tokNewVal = artMap.get("stare");
		holder.image.setImageResource(0);
		holder.textStare.setText("");
		if (tokNewVal.equals("X")) {
			holder.image.setImageResource(R.drawable.warning_icon);
		}

		view.setBackgroundColor(colors[colorPos]);

		return view;

	}

}