package my.logon.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.dialogs.ModifPretTranspDialog;
import my.logon.screen.listeners.ModifPretTransportListener;
import my.logon.screen.listeners.RezumatListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.UserInfo;

public class AdapterRezumatComanda extends BaseAdapter implements ModifPretTransportListener {

    private List<RezumatComanda> listComenzi;
    private Context context;

    private double valoareTotal;
    private NumberFormat nf = new DecimalFormat("#,##0.00");
    private RezumatListener listener;
    private List<CostTransportMathaus> costTransport;
    private String[] tipTransportArray = {"TRAP", "TCLI"};
    private String tipTransportCmd;
    private String filialeArondate;
    private ArticolComanda articolTransport;
    private ViewHolder myViewHolder;

    public AdapterRezumatComanda(Context context, List<RezumatComanda> listComenzi, List<CostTransportMathaus> costTransport, String tipTransportCmd, String filialeArondate) {
        this.context = context;
        this.listComenzi = listComenzi;
        this.costTransport = costTransport;
        this.tipTransportCmd = tipTransportCmd;
        this.filialeArondate = filialeArondate;
        clearTransportArticol(listComenzi);

    }


    public static class ViewHolder {
        TextView textNumeArticole, textCantArticole, textFurnizor, textTransport, textTotal, textNrComanda, tipTransport;
        ImageButton stergeComandaBtn, btnPretTransport;
        LinearLayout layoutTransport;
        Spinner spinnerTransport;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_rezumat_comanda, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textNumeArticole = (TextView) convertView.findViewById(R.id.textNumeArticole);
            viewHolder.textCantArticole = (TextView) convertView.findViewById(R.id.textCantArticole);
            viewHolder.textFurnizor = (TextView) convertView.findViewById(R.id.textFurnizor);

            viewHolder.textTransport = (TextView) convertView.findViewById(R.id.textTransport);
            viewHolder.textTotal = (TextView) convertView.findViewById(R.id.textTotal);
            viewHolder.textNrComanda = (TextView) convertView.findViewById(R.id.textNrComanda);
            viewHolder.tipTransport = (TextView) convertView.findViewById(R.id.tipTransport);
            viewHolder.stergeComandaBtn = (ImageButton) convertView.findViewById(R.id.stergeComandaBtn);

            viewHolder.btnPretTransport = (ImageButton) convertView.findViewById(R.id.btnPretTransport);

            viewHolder.layoutTransport = (LinearLayout) convertView.findViewById(R.id.layoutTransport);
            viewHolder.spinnerTransport = (Spinner) convertView.findViewById(R.id.spinnerTransport);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        myViewHolder = viewHolder;

        RezumatComanda rezumat = getItem(position);

        viewHolder.textNrComanda.setText("Comanda nr. " + (position + 1));
        viewHolder.textNumeArticole.setText(getNumeArticole(rezumat));
        viewHolder.textCantArticole.setText(getCantArticole(rezumat));
        viewHolder.textFurnizor.setText("Livrare: " + rezumat.getFilialaLivrare());
        viewHolder.tipTransport.setText(getTipTransport(rezumat.getFilialaLivrare()));


        if (articolTransport == null)
            viewHolder.textTransport.setText("Val. transp: " + getCostTransport(rezumat.getFilialaLivrare()));

        viewHolder.textTotal.setText("Total: " + nf.format(valoareTotal));

        ArrayAdapter<String> adapterSpinnerTransp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tipTransportArray);
        adapterSpinnerTransp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.spinnerTransport.setAdapter(adapterSpinnerTransp);

        String tipTranspArt = getTipTransport(rezumat.getFilialaLivrare());

        if (filialeArondate.contains(rezumat.getFilialaLivrare())) {
            viewHolder.tipTransport.setVisibility(View.GONE);
            viewHolder.spinnerTransport.setVisibility(View.VISIBLE);
            viewHolder.btnPretTransport.setVisibility(View.VISIBLE);

            String tipTransSelected = getTransportArticole(rezumat);
            if (tipTransSelected != null && !tipTransSelected.trim().isEmpty())
                tipTranspArt = tipTransSelected;

            if (tipTranspArt.equals("TRAP"))
                viewHolder.spinnerTransport.setSelection(0);
            else if (tipTranspArt.equals("TCLI")) {
                viewHolder.spinnerTransport.setSelection(1);
            }
        } else {
            viewHolder.spinnerTransport.setVisibility(View.GONE);
            viewHolder.btnPretTransport.setVisibility(View.INVISIBLE);
            viewHolder.tipTransport.setVisibility(View.VISIBLE);
            setTransportArticole(rezumat, tipTranspArt);
        }

        setListenerSpinnerTransport(viewHolder.spinnerTransport, rezumat, viewHolder);
        setListenerEliminaBtn(viewHolder.stergeComandaBtn, position);
        setListenerPretTransp(viewHolder, rezumat);

        return convertView;

    }


    private void schimbaPretTransport(RezumatComanda rezumat, String valTransport) {

        if (valTransport == null || valTransport.isEmpty() || valTransport.equals("null"))
            valTransport = "0";


        for (ArticolComanda articolComanda : rezumat.getListArticole()) {

            if (isArtTransp(articolComanda.getNumeArticol())) {
                articolComanda.setPretUnit(Double.valueOf(valTransport));
                articolComanda.setPret(articolComanda.getPretUnit());
                articolComanda.setPretUnitarClient(articolComanda.getPretUnit());
                articolComanda.setPretUnitarGed(articolComanda.getPretUnit());

                if (listener != null) {
                    listener.eliminaArticol(articolComanda);
                    listener.adaugaArticol(articolComanda);
                }

                break;
            }

        }


    }

    private boolean isExceptieTransportBV90(String filialaLivrare) {
        return UserInfo.getInstance().getUnitLog().equals("BV10") && filialaLivrare.equals("BV90") && isLivrareBV10();
    }

    private boolean isLivrareBV10() {

        if (!UserInfo.getInstance().getUnitLog().equals("BV10"))
            return false;

        for (int ii = 0; ii < listComenzi.size(); ii++) {
            for (int jj = 0; jj < listComenzi.get(ii).getListArticole().size(); jj++) {
                if (listComenzi.get(ii).getFilialaLivrare().equals("BV10"))
                    return true;
            }
        }
        return false;
    }

    private void setListenerSpinnerTransport(Spinner spinnerTransport, RezumatComanda rezumat, ViewHolder viewHolder) {

        spinnerTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipTransportSelected = (String) parent.getAdapter().getItem(position);
                setTransportArticole(rezumat, tipTransportSelected);

                if (tipTransportSelected.equals("TCLI")) {
                    eliminaArticolTransport(rezumat, viewHolder);
                    viewHolder.textTransport.setVisibility(View.INVISIBLE);
                    viewHolder.btnPretTransport.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.textTransport.setVisibility(View.VISIBLE);
                    viewHolder.btnPretTransport.setVisibility(View.VISIBLE);
                    adaugaArticolTransport(rezumat, viewHolder);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void eliminaArticolTransport(RezumatComanda rezumatComanda, ViewHolder viewHolder) {

        Iterator<ArticolComanda> artIterator = rezumatComanda.getListArticole().iterator();

        while (artIterator.hasNext()) {

            ArticolComanda artCom = artIterator.next();

            if (isArtTransp(artCom.getNumeArticol())) {
                articolTransport = artCom;
                artIterator.remove();
                notifyDataSetChanged();

                if (listener != null)
                    listener.eliminaArticol(articolTransport);

                break;
            }

        }


    }


    private boolean isArtTransp(String numeArticol) {
        return numeArticol != null && numeArticol.toUpperCase().contains("SERV") && numeArticol.toUpperCase().contains("TRANSP");
    }

    private void adaugaArticolTransport(RezumatComanda rezumatComanda, ViewHolder viewHolder) {

        boolean artTransp = false;

        for (ArticolComanda art : rezumatComanda.getListArticole()) {
            if (isArtTransp(art.getNumeArticol()))
                artTransp = true;
        }

        if (!artTransp && articolTransport != null) {
            rezumatComanda.getListArticole().add(articolTransport);
            viewHolder.textTransport.setText("Val. transp: " + articolTransport.getPret());
            notifyDataSetChanged();

            if (listener != null)
                listener.adaugaArticol(articolTransport);
        }


    }

    private void clearTransportArticol(List<RezumatComanda> listComenzi) {

        for (int ii = 0; ii < listComenzi.size(); ii++) {

            for (int jj = 0; jj < listComenzi.get(ii).getListArticole().size(); jj++) {
                listComenzi.get(ii).getListArticole().get(jj).setTipTransport(null);
            }

        }

    }

    private void setTransportArticole(RezumatComanda rezumatComanda, String tipTransp) {

        for (ArticolComanda art : rezumatComanda.getListArticole()) {
            art.setTipTransport(tipTransp);
        }

    }


    private String getTransportArticole(RezumatComanda rezumatComanda) {

        for (ArticolComanda art : rezumatComanda.getListArticole()) {
            if (art.getTipTransport() != null)
                return art.getTipTransport();
        }

        return null;

    }


    private String getTipTransport(String filiala) {

        String tipTransport = "";

        for (CostTransportMathaus cost : costTransport) {

            if (cost.getFiliala().equals(filiala)) {
                tipTransport = cost.getTipTransp();
                break;
            }

        }

        return tipTransport;
    }

    private double getCostTransport(String filiala) {

        double valTransport = 0;
        for (CostTransportMathaus cost : costTransport) {

            if (cost.getFiliala().equals(filiala) && !cost.getValTransp().equals("0")) {
                valTransport = Double.valueOf(cost.getValTransp());
                break;
            }

        }

        return valTransport;

    }


    private void setListenerEliminaBtn(ImageButton eliminaBtn, final int position) {

        eliminaBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                eliminaComanda(position);

            }
        });

    }


    private void setListenerPretTransp(ViewHolder viewHolder, RezumatComanda rezumat) {

        viewHolder.btnPretTransport.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                ModifPretTranspDialog dialog = new ModifPretTranspDialog(context, rezumat, String.valueOf(getCostTransport(rezumat.getFilialaLivrare())), viewHolder);
                dialog.setPretTransportListener(AdapterRezumatComanda.this);
                dialog.show();

            }
        });

    }

    @Override
    public void pretTransportModificat(RezumatComanda rezumat, String pretTransport, ViewHolder viewHolder) {

        schimbaPretTransport(rezumat, pretTransport);
        getNumeArticole(rezumat);
        viewHolder.textTotal.setText("Total: " + nf.format(valoareTotal));
        viewHolder.textTransport.setText("Val. transp: " + nf.format(Double.valueOf(pretTransport)));

    }

    private void eliminaComanda(int position) {

        Iterator<RezumatComanda> listIterator = listComenzi.iterator();
        List<String> listArticole = null;

        int crntPos = 0;
        String filialaLivrare = "";
        while (listIterator.hasNext()) {
            RezumatComanda rezumat = listIterator.next();
            if (crntPos == position) {
                listArticole = getArticoleComanda(rezumat);
                filialaLivrare = rezumat.getFilialaLivrare();
                listIterator.remove();
                break;
            }

            crntPos++;
        }

        notifyDataSetChanged();

        if (listener != null)
            listener.comandaEliminata(listArticole, filialaLivrare);

    }

    private List<String> getArticoleComanda(RezumatComanda rezumatComanda) {

        List<String> listArticole = new ArrayList<String>();

        for (ArticolComanda art : rezumatComanda.getListArticole()) {
            listArticole.add(art.getCodArticol());
        }

        return listArticole;

    }

    private String getNumeArticole(RezumatComanda rezumat) {

        StringBuilder str = new StringBuilder();

        valoareTotal = 0;

        for (ArticolComanda art : rezumat.getListArticole()) {
            str.append(art.getNumeArticol());
            str.append("\n");

            valoareTotal += art.getPret();
        }

        return str.toString();

    }

    private String getCantArticole(RezumatComanda rezumat) {

        StringBuilder str = new StringBuilder();

        for (ArticolComanda art : rezumat.getListArticole()) {
            str.append(art.getCantitate());
            str.append(" ");
            str.append(art.getUm());
            str.append(addSpaces(art.getUm()));
            str.append("\n");
        }

        return str.toString();

    }

    public String addSpaces(String um) {

        String spaces = "";

        for (int i = 0; i < 3 - um.length(); i++) {
            spaces += " ";
        }

        return spaces;
    }

    public void setRezumatListener(RezumatListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return listComenzi.size();
    }

    @Override
    public RezumatComanda getItem(int position) {
        return listComenzi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
