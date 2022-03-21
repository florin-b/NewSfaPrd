package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.DLExpirat;
import my.logon.screen.utils.UtilsFormatting;

public class DLExpirateAdapter extends BaseAdapter {

	Context context;
	List<DLExpirat> listDocumente;

	public DLExpirateAdapter(Context context, List<DLExpirat> listDocumente) {
		this.context = context;
		this.listDocumente = listDocumente;
	}

	static class ViewHolder {
		TextView textIdCmdClp, textClient, textData, textCmdSAP, textFurnizor;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.dl_expirate, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textIdCmdClp = (TextView) convertView.findViewById(R.id.textIdCmdClp);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textCmdSAP = (TextView) convertView.findViewById(R.id.textCmdSAP);
			viewHolder.textFurnizor = (TextView) convertView.findViewById(R.id.textFurnizor);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		DLExpirat documentDL = getItem(position);

		viewHolder.textIdCmdClp.setText(documentDL.getNrDocument());
		viewHolder.textClient.setText(documentDL.getNumeClient());

		viewHolder.textData.setText(UtilsFormatting.formatDate(documentDL.getDataDocument()));

		viewHolder.textCmdSAP.setText(documentDL.getNrDocumentSap());

		viewHolder.textFurnizor.setText(documentDL.getFurnizor());

		return convertView;
	}

	public int getCount() {
		return listDocumente.size();
	}

	public DLExpirat getItem(int position) {
		return listDocumente.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
