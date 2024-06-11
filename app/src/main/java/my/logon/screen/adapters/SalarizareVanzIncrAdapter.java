package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.beans.BeanVanzariIncr;

public class SalarizareVanzIncrAdapter extends BaseAdapter {

    private Context context;
    private List<BeanVanzariIncr> listDetalii;
    private NumberFormat numberFormat;
    private int[] colors = new int[]{0x30FFFFFF, 0x30D7DBDD};

    public SalarizareVanzIncrAdapter(List<BeanVanzariIncr> listDetalii, Context context) {
        this.context = context;
        this.listDetalii = listDetalii;
        numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(2);

    }

    static class ViewHolder {
        public TextView data_an, data_luna, nume_client, valoare, depart_ag, depart_cl, nume_agent;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        int colorPos = position % 2;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_vanz_incr_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.data_an = (TextView) convertView.findViewById(R.id.data_an);
            viewHolder.data_luna = (TextView) convertView.findViewById(R.id.data_luna);
            viewHolder.nume_client = (TextView) convertView.findViewById(R.id.nume_client);
            viewHolder.valoare = (TextView) convertView.findViewById(R.id.valoare);
            viewHolder.depart_ag = (TextView) convertView.findViewById(R.id.depart_ag);
            viewHolder.depart_cl = (TextView) convertView.findViewById(R.id.depart_cl);
            viewHolder.nume_agent = (TextView) convertView.findViewById(R.id.nume_agent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BeanVanzariIncr detaliuCVS = getItem(position);

        viewHolder.data_an.setText(detaliuCVS.getAnv());
        viewHolder.data_luna.setText(detaliuCVS.getLuna());
        viewHolder.nume_client.setText(detaliuCVS.getKunnr());
        viewHolder.valoare.setText(detaliuCVS.getFacturat());
        viewHolder.depart_ag.setText(detaliuCVS.getSpartAv());
        viewHolder.depart_cl.setText(detaliuCVS.getSpartCl());
        viewHolder.nume_agent.setText(detaliuCVS.getPernr());

        if (colorPos % 2 == 0)
            convertView.setBackgroundColor(colors[0]);
        else
            convertView.setBackgroundColor(colors[1]);

        return convertView;
    }

    public int getCount() {
        return listDetalii.size();
    }

    public BeanVanzariIncr getItem(int position) {
        return listDetalii.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}

