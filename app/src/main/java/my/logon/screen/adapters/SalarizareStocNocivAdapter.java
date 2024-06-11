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
import my.logon.screen.beans.BeanVanzariVS;

public class SalarizareStocNocivAdapter extends BaseAdapter {

    private Context context;
    private List<BeanVanzariVS> listDetalii;
    private NumberFormat numberFormat;
    private int[] colors = new int[]{0x30FFFFFF, 0x30D7DBDD};

    public SalarizareStocNocivAdapter(List<BeanVanzariVS> listDetalii, Context context) {
        this.context = context;
        this.listDetalii = listDetalii;
        numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(2);

    }

    static class ViewHolder {
        public TextView cod, nume, tipAng, articol, valoare, venitBaza, coefSal;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        int colorPos = position % 2;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.salarizare_stoc_nociv_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.cod = (TextView) convertView.findViewById(R.id.cod);
            viewHolder.nume = (TextView) convertView.findViewById(R.id.nume);
            viewHolder.tipAng = (TextView) convertView.findViewById(R.id.tipAng);
            viewHolder.articol = (TextView) convertView.findViewById(R.id.articol);
            viewHolder.valoare = (TextView) convertView.findViewById(R.id.valoare);
            viewHolder.venitBaza = (TextView) convertView.findViewById(R.id.venit_baza);
            viewHolder.coefSal = (TextView) convertView.findViewById(R.id.coef_sal);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BeanVanzariVS detaliuCVS = getItem(position);

        viewHolder.cod.setText(detaliuCVS.getPernr());
        viewHolder.nume.setText(detaliuCVS.getEname());
        viewHolder.tipAng.setText(detaliuCVS.getShortStr());
        viewHolder.articol.setText(detaliuCVS.getMatnr());
        viewHolder.valoare.setText(numberFormat.format(detaliuCVS.getNetwrCalc()));
        viewHolder.venitBaza.setText(numberFormat.format(detaliuCVS.getVenitBaza()));
        viewHolder.coefSal.setText(numberFormat.format(detaliuCVS.getCoefSal()));

        if (colorPos % 2 == 0)
            convertView.setBackgroundColor(colors[0]);
        else
            convertView.setBackgroundColor(colors[1]);

        return convertView;
    }

    public int getCount() {
        return listDetalii.size();
    }

    public BeanVanzariVS getItem(int position) {
        return listDetalii.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}
