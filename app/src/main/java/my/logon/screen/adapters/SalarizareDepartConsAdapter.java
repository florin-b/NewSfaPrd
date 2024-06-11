package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.beans.BeanSalarizareCVA;
import my.logon.screen.listeners.OperatiiSalarizareListener;

public class SalarizareDepartConsAdapter extends BaseAdapter {

    private Context context;
    private List<BeanSalarizareCVA> listAgenti;
    private NumberFormat numberFormat;
    private int[] colors = new int[]{0x30FFFFFF, 0x30D7DBDD};
    private OperatiiSalarizareListener listener;
    private String tipUserSap;

    public void setSalarizareDepartListener(OperatiiSalarizareListener listener) {
        this.listener = listener;
    }

    public SalarizareDepartConsAdapter(List<BeanSalarizareCVA> listAgenti, Context context) {
        this.context = context;
        this.listAgenti = listAgenti;

        numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(2);

    }

    static class ViewHolder {
        public TextView textNumeAgent, textVenitBaza, textVenitNruf, textVenitTcf, textVenitStocNociv, textCorectieInc, textVenitFinal;
        public Button detaliiBtn;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        int colorPos = position % 2;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_depart_cons_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);
            viewHolder.textVenitBaza = (TextView) convertView.findViewById(R.id.textVenitBaza);
            viewHolder.textVenitNruf = (TextView) convertView.findViewById(R.id.textVenitNruf);
            viewHolder.textVenitTcf = (TextView) convertView.findViewById(R.id.textVenitTcf);
            viewHolder.textVenitStocNociv = (TextView) convertView.findViewById(R.id.textVenitStocNociv);
            viewHolder.textCorectieInc = (TextView) convertView.findViewById(R.id.textCorectieInc);
            viewHolder.textVenitFinal = (TextView) convertView.findViewById(R.id.textVenitFinal);
            viewHolder.detaliiBtn = (Button) convertView.findViewById(R.id.btnDetalii);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BeanSalarizareCVA agent = getItem(position);

        viewHolder.textNumeAgent.setText(agent.getNumeAgent());
        viewHolder.textVenitBaza.setText(numberFormat.format(agent.getVenitBaza()));
        viewHolder.textVenitNruf.setText(numberFormat.format(agent.getVenitNruf()));
        viewHolder.textVenitTcf.setText(numberFormat.format(agent.getVenitTcf()));
        viewHolder.textVenitStocNociv.setText(numberFormat.format(agent.getVenitStocNociv()));
        viewHolder.textCorectieInc.setText(numberFormat.format(agent.getCorectIncas()));
        viewHolder.textVenitFinal.setText(numberFormat.format(agent.getVenitFinal()));

        viewHolder.detaliiBtn.setOnClickListener(v -> {

            if (listener != null)
                listener.detaliiAgentSelected(agent.getCodAgent(), agent.getNumeAgent());

        });

        if (colorPos % 2 == 0)
            convertView.setBackgroundColor(colors[0]);
        else
            convertView.setBackgroundColor(colors[1]);

        return convertView;
    }

    public int getCount() {
        return listAgenti.size();
    }

    public BeanSalarizareCVA getItem(int position) {
        return listAgenti.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}

