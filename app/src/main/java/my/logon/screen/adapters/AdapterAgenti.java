package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import my.logon.screen.R;
import my.logon.screen.model.Agent;

public class AdapterAgenti extends BaseAdapter {

    private List<Agent> listAgenti;
    private String codDepart;
    private Context context;
    private int[] colors = new int[]{0x30DEDEDE, 0x30d4ac0d};

    public AdapterAgenti(Context context, List<Agent> listAgenti, String codDepart) {
        this.context = context;
        this.listAgenti = listAgenti;
        this.codDepart = codDepart;

    }

    static class ViewHolder {
        TextView textNumeAgent;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_agent_comanda, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textNumeAgent = (TextView) convertView.findViewById(R.id.textNumeAgent);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Agent agent = getItem(position);
        viewHolder.textNumeAgent.setText(agent.getNume());



        if (agent.getDepart().trim().length() == 0 || codDepart.substring(0, 2).equals(agent.getDepart().substring(0, 2)))
            convertView.setBackgroundColor(colors[0]);
        else
            convertView.setBackgroundColor(colors[1]);


        return convertView;

    }

    @Override
    public int getCount() {
        return listAgenti.size();
    }

    @Override
    public Agent getItem(int position) {
        return listAgenti.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
