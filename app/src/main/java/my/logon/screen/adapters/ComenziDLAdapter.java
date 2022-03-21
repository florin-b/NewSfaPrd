package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanDocumentCLP;
import my.logon.screen.model.InfoStrings;
import my.logon.screen.utils.UtilsFormatting;

public class ComenziDLAdapter extends BaseAdapter {

    Context context;
    List<BeanDocumentCLP> listDocumente;

    public ComenziDLAdapter(Context context, List<BeanDocumentCLP> listDocumente) {
        this.context = context;
        this.listDocumente = listDocumente;
    }

    static class ViewHolder {
        TextView textIdCmdClp, textClient, textData, textCmdSAP, textUL,
                textAgent, textStatusClp, textDepoz;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_comenzi_clp, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textIdCmdClp = (TextView) convertView
                    .findViewById(R.id.textIdCmdClp);
            viewHolder.textClient = (TextView) convertView
                    .findViewById(R.id.textClient);
            viewHolder.textData = (TextView) convertView
                    .findViewById(R.id.textData);
            viewHolder.textCmdSAP = (TextView) convertView
                    .findViewById(R.id.textCmdSAP);
            viewHolder.textUL = (TextView) convertView
                    .findViewById(R.id.textUL);
            viewHolder.textAgent = (TextView) convertView
                    .findViewById(R.id.textAgent);
            viewHolder.textStatusClp = (TextView) convertView
                    .findViewById(R.id.textStatusClp);
            viewHolder.textDepoz = (TextView) convertView
                    .findViewById(R.id.textDepoz);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanDocumentCLP documentDL = getItem(position);

        viewHolder.textIdCmdClp.setText(documentDL.getNrDocument());
        viewHolder.textClient.setText(documentDL.getNumeClient());

        viewHolder.textData.setText(UtilsFormatting.formatDate(documentDL
                .getDataDocument()));

        viewHolder.textCmdSAP.setText(documentDL.getNrDocumentSap());
        viewHolder.textUL.setText(documentDL.getUnitLog());
        viewHolder.textAgent.setText(documentDL.getNumeAgent());
        viewHolder.textStatusClp.setText(documentDL.getStatusDocument()
                + System.getProperty("line.separator")
                + InfoStrings.statusAprobCmd(Integer.valueOf(documentDL
                        .getFurnizor())));
        viewHolder.textDepoz.setText(documentDL.getDepozit());

        return convertView;
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
