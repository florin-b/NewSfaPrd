package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.ArticolDB;

public class AdapterServiciiAC extends BaseAdapter {

    private List<ArticolDB> listArticole;
    private Context context;

    public AdapterServiciiAC(Context context, List<ArticolDB> listArticole) {
        this.context = context;
        this.listArticole = listArticole;

    }

    static class ViewHolder {
        TextView textNumeArticol, textDimensiuni, textStoc, textTipCant;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_articol_cant, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textNumeArticol = (TextView) convertView.findViewById(R.id.textNumeArticol);
            viewHolder.textDimensiuni = (TextView) convertView.findViewById(R.id.textDimensiuni);
            viewHolder.textStoc = (TextView) convertView.findViewById(R.id.textStoc);
            viewHolder.textTipCant = (TextView) convertView.findViewById(R.id.textTipCant);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArticolDB articol = getItem(position);

        viewHolder.textNumeArticol.setText(articol.getNume());
        viewHolder.textDimensiuni.setText("");

        if (articol.getStoc().trim().isEmpty())
            viewHolder.textStoc.setText("");
        else
            viewHolder.textStoc.setText("Disponibil" + " : " + Math.round(Double.valueOf(articol.getStoc())));

        return convertView;

    }

    @Override
    public int getCount() {
        return listArticole.size();
    }

    @Override
    public ArticolDB getItem(int position) {
        return listArticole.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
