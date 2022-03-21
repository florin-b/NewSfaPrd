/**
 * @author florinb
 *
 */
package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.ArticolComanda;

public class ArticoleGedAdapter extends BaseAdapter {

    private List<ArticolComanda> listArticole;
    private Context context;
    NumberFormat numberFormat = new DecimalFormat("#0.00");

    static class ViewHolder {

        public TextView textNrCrt, textNumeArt, textCodArt, textCantArt,
                textPretUnit, textUmArt, textPretUnitClient, textUmBaza;

        public ImageView image;
    }

    public ArticoleGedAdapter(List<ArticolComanda> listArticole, Context context) {
        this.listArticole = listArticole;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.customrowview_ged, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textNrCrt = (TextView) convertView
                    .findViewById(R.id.textNrCrt);
            viewHolder.textNumeArt = (TextView) convertView
                    .findViewById(R.id.textNumeArt);
            viewHolder.textCodArt = (TextView) convertView
                    .findViewById(R.id.textCodArt);
            viewHolder.textCantArt = (TextView) convertView
                    .findViewById(R.id.textCantArt);
            viewHolder.textUmArt = (TextView) convertView
                    .findViewById(R.id.textUmArt);
            viewHolder.textPretUnit = (TextView) convertView
                    .findViewById(R.id.textPretUnit);
            viewHolder.textPretUnitClient = (TextView) convertView
                    .findViewById(R.id.textPretUnitClient);
            viewHolder.textUmBaza = (TextView) convertView
                    .findViewById(R.id.textUmBaza);
            viewHolder.image = (ImageView) convertView
                    .findViewById(R.id.imageAlertaPret);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArticolComanda articol = getItem(position);

        viewHolder.textNrCrt.setText(String.valueOf(position + 1));
        viewHolder.textCodArt.setText(articol.getCodArticol());
        viewHolder.textNumeArt.setText(articol.getNumeArticol());
        viewHolder.textCantArt.setText(numberFormat.format(articol
                .getCantitate()));
        viewHolder.textUmArt.setText(articol.getUm());
        viewHolder.textPretUnit.setText(numberFormat.format(articol
                .getPretUnitarGed()));
        viewHolder.textUmBaza.setText(articol.getUmb());
        viewHolder.textPretUnitClient.setText(numberFormat.format(articol
                .getPretUnitarClient()));
        viewHolder.image.setImageResource(0);

        if (articol.getTipAlertPret() != null) {
            if (articol.getTipAlertPret().equals("FM")) { // pret foarte mic
                viewHolder.image.setImageResource(R.drawable.alert_pret_1);
            }

            if (articol.getTipAlertPret().equals("M")) { // pret mic
                viewHolder.image.setImageResource(R.drawable.alert_pret_2);
            }
        }

        return convertView;

    }

    public int getCount() {
        return listArticole.size();
    }

    public ArticolComanda getItem(int position) {
        return listArticole.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setListArticole(List<ArticolComanda> listArticole) {
        this.listArticole = listArticole;
        notifyDataSetChanged();
    }

    public List<ArticolComanda> getArticoleComanda() {
        return listArticole;
    }

}
