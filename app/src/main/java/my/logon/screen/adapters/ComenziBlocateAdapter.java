package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsFormatting;


public class ComenziBlocateAdapter extends BaseAdapter {

    Context context;
    List<BeanComandaCreata> listComenzi;
    NumberFormat nf2 = NumberFormat.getInstance();

    public ComenziBlocateAdapter(Context context,
            List<BeanComandaCreata> listComenzi) {
        this.context = context;
        this.listComenzi = listComenzi;
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
    }

    static class ViewHolder {
        TextView textIdCmd, textCodClient, textClient, textData, textSuma,
                textStare, textCmdSAP, textUL;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.modifcmdheaderlimcredit, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.textIdCmd = (TextView) convertView
                    .findViewById(R.id.textIdCmd);
            viewHolder.textCodClient = (TextView) convertView
                    .findViewById(R.id.textCodClient);
            viewHolder.textClient = (TextView) convertView
                    .findViewById(R.id.textClient);
            viewHolder.textData = (TextView) convertView
                    .findViewById(R.id.textData);
            viewHolder.textSuma = (TextView) convertView
                    .findViewById(R.id.textSuma);
            viewHolder.textStare = (TextView) convertView
                    .findViewById(R.id.textStare);
            viewHolder.textCmdSAP = (TextView) convertView
                    .findViewById(R.id.textCmdSAP);
            viewHolder.textUL = (TextView) convertView
                    .findViewById(R.id.textUL);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BeanComandaCreata comanda = getItem(position);

        viewHolder.textIdCmd.setText(comanda.getId());
        viewHolder.textCodClient.setText(comanda.getCodClient());
        viewHolder.textClient.setText(comanda.getNumeClient());
        viewHolder.textData.setText(UtilsFormatting.formatDate(comanda
                .getData()));
        viewHolder.textSuma.setText(nf2.format(Double.parseDouble(comanda
                .getSuma())));

        if (isDirectorOrSd())
            viewHolder.textUL.setText(comanda.getNumeAgent());
        else
            viewHolder.textUL.setText(" ");

        if (isDirector())
            viewHolder.textCmdSAP.setText(comanda.getFiliala());
        else
            viewHolder.textCmdSAP.setText(" ");

        viewHolder.textStare.setText(" ");

        return convertView;
    }

    public int getCount() {
        return listComenzi.size();
    }

    public BeanComandaCreata getItem(int position) {
        return listComenzi.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    boolean isDirectorOrSd() {
        return UserInfo.getInstance().getTipAcces().equals("10")
                || UserInfo.getInstance().getTipAcces().equals("12")
                || UserInfo.getInstance().getTipAcces().equals("14");
    }

    boolean isDirector() {
        return UserInfo.getInstance().getTipAcces().equals("12")
                || UserInfo.getInstance().getTipAcces().equals("14");
    }

}
