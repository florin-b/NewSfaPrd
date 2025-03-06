package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.OptiuneLivrare;

public class AdapterOptiuniLivrare extends BaseAdapter {

    private List<OptiuneLivrare> optiuniLivrare;
    private Context context;


    public AdapterOptiuniLivrare(Context context, List<OptiuneLivrare> optiuniLivrare) {
        this.context = context;
        this.optiuniLivrare = optiuniLivrare;
    }

    static class ViewHolder {
        TextView textTipLivrare, textValoareivrare;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_tip_livrare, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textTipLivrare = convertView.findViewById(R.id.textTipLivrare);
            viewHolder.textValoareivrare = convertView.findViewById(R.id.textValoareLivrare);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OptiuneLivrare optiuneLivrare = getItem(position);

        viewHolder.textTipLivrare.setText(optiuneLivrare.getNumeOptiune());
        viewHolder.textValoareivrare.setText(optiuneLivrare.getValoareOptiune());

        return convertView;

    }


    public int getCount() {
        return optiuniLivrare.size();
    }


    public OptiuneLivrare getItem(int position) {
        return optiuniLivrare.get(position);
    }


    public long getItemId(int position) {
        return 0;
    }

}
