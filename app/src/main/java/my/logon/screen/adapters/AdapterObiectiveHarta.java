package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanObiectivHarta;
import my.logon.screen.enums.EnumJudete;
import my.logon.screen.utils.UtilsFormatting;


public class AdapterObiectiveHarta extends BaseAdapter {

	private List<BeanObiectivHarta> listObiective;
	private Context context;
	private boolean showNumeAgent;

	public AdapterObiectiveHarta(Context context, List<BeanObiectivHarta> listObiective, boolean showNumeAgent) {
		this.context = context;
		this.listObiective = listObiective;
		this.showNumeAgent = showNumeAgent;
	}

	public static class ViewHolder {
		TextView textNumeObiectiv, textBeneficiar, textStadiu, textDatacreare, textNrCrt, textNumeAgent;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.obiectiv_harta_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeObiectiv = (TextView) convertView.findViewById(R.id.textNumeObiectiv);
			viewHolder.textBeneficiar = (TextView) convertView.findViewById(R.id.textBeneficiar);
			viewHolder.textStadiu = (TextView) convertView.findViewById(R.id.textStadiu);
			viewHolder.textDatacreare = (TextView) convertView.findViewById(R.id.textDatacreare);
			viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanObiectivHarta obiectiv = getItem(position);

		viewHolder.textNrCrt.setText(String.valueOf(position + 1) + ".");
		viewHolder.textNumeObiectiv.setText(obiectiv.getNume());
		viewHolder.textBeneficiar.setText(obiectiv.getBeneficiar());
		viewHolder.textStadiu.setText(formatAddress(obiectiv.getAddress()));
		viewHolder.textDatacreare.setText(UtilsFormatting.formatDate(obiectiv.getData()));

		if (showNumeAgent)
			viewHolder.textNumeAgent.setText(obiectiv.getNumeAgent());

		if (position % 2 == 0)
			convertView.setBackgroundResource(R.drawable.shadow_dark);
		else
			convertView.setBackgroundResource(R.drawable.shadow_light);

		return convertView;
	}

	public int getCount() {
		return listObiective.size();
	}

	public BeanObiectivHarta getItem(int position) {
		return listObiective.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private String formatAddress(String address) {
		String formatted = address;

		if (address != null && address.contains("/")) {
			String[] adrTokens = address.split("/");

			for (int i = 0; i < adrTokens.length; i++)
				if (i == 0)
					formatted = EnumJudete.getNumeJudet(Integer.parseInt(adrTokens[i]));
				else
					formatted += "/" + adrTokens[i];

		}

		return formatted;
	}
}
