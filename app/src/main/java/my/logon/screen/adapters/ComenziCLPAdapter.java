package my.logon.screen.adapters;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.utils.UtilsFormatting;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import my.logon.screen.beans.BeanDocumentCLP;

public class ComenziCLPAdapter extends BaseAdapter {

	Context context;
	List<BeanDocumentCLP> listDocumente;
	int restrictiiAfisare;
	String statusClp;

	static class ViewHolder {
		TextView textIdCmdClp, textClient, textData, textCmdSAP, textUL, textAgent, textStatusClp, textDepoz;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.list_comenzi_clp, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.textIdCmdClp = (TextView) convertView.findViewById(R.id.textIdCmdClp);
			viewHolder.textClient = (TextView) convertView.findViewById(R.id.textClient);
			viewHolder.textData = (TextView) convertView.findViewById(R.id.textData);
			viewHolder.textCmdSAP = (TextView) convertView.findViewById(R.id.textCmdSAP);
			viewHolder.textUL = (TextView) convertView.findViewById(R.id.textUL);
			viewHolder.textAgent = (TextView) convertView.findViewById(R.id.textAgent);
			viewHolder.textStatusClp = (TextView) convertView.findViewById(R.id.textStatusClp);
			viewHolder.textDepoz = (TextView) convertView.findViewById(R.id.textDepoz);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanDocumentCLP documentClp = getItem(position);

		viewHolder.textIdCmdClp.setText(documentClp.getNrDocument());
		viewHolder.textClient.setText(documentClp.getNumeClient());
		viewHolder.textData.setText(documentClp.getDataDocument());
		viewHolder.textData.setText(UtilsFormatting.formatDate(documentClp.getDataDocument()));

		if (!documentClp.getNrDocumentSap().equals("-1")) {
			viewHolder.textCmdSAP.setText(documentClp.getNrDocumentSap());
		} else {
			viewHolder.textCmdSAP.setText(" ");
		}

		viewHolder.textUL.setText(documentClp.getUnitLog());

		statusClp = "";
		if (0 == restrictiiAfisare) {
			statusClp = InfoStrings.statusAprobCmd(Integer.parseInt(documentClp.getStatusDocument()));
		}
		viewHolder.textStatusClp.setText("Furnizor: " + documentClp.getFurnizor() + System.getProperty("line.separator") + statusClp);
		viewHolder.textAgent.setText(documentClp.getNumeAgent());
		viewHolder.textDepoz.setText(documentClp.getDepozit());

		return convertView;
	}

	public ComenziCLPAdapter(Context context, List<BeanDocumentCLP> listDocumente, int restrictiiAfisare) {
		this.context = context;
		this.listDocumente = listDocumente;
		this.restrictiiAfisare = restrictiiAfisare;
	}

	public int getCount() {
		return listDocumente.size();
	}

	public BeanDocumentCLP getItem(int position) {
		return listDocumente.get(position);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

}
