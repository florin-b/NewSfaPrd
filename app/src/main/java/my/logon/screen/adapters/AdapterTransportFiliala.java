package my.logon.screen.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanTaxaCamion;
import my.logon.screen.beans.Details;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.OptiuneLivrare;
import my.logon.screen.beans.TaxaTransport;
import my.logon.screen.beans.TaxeLivrare;
import my.logon.screen.dialogs.TaxeTransportDialog;
import my.logon.screen.enums.EnumTipCamion;
import my.logon.screen.helpers.HelperMathaus;

public class AdapterTransportFiliala extends BaseAdapter {

    private List<TaxaTransport> listTaxeTransport;
    private LivrareMathaus dateLivrare;
    private Context context;
    private DecimalFormat df;

    public AdapterTransportFiliala(Context context, List<TaxaTransport> listTaxeTransport, LivrareMathaus dateLivrare) {
        this.context = context;
        this.listTaxeTransport = listTaxeTransport;
        this.dateLivrare = dateLivrare;

        df = new DecimalFormat("#####0.00");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
    }

    static class ViewHolder {
        TextView textNumeFiliala, labelMacara;
        EditText costMacara;
        ImageButton detaliiCost;
        Spinner spinnerTipTransport;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_transp_filiala, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textNumeFiliala = convertView.findViewById(R.id.textNumeFiliala);
            viewHolder.spinnerTipTransport = convertView.findViewById(R.id.spinnerTipTransport);
            viewHolder.costMacara = convertView.findViewById(R.id.cost_macara);
            viewHolder.detaliiCost = convertView.findViewById(R.id.detalii_cost);
            viewHolder.labelMacara = convertView.findViewById(R.id.label_macara);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TaxaTransport taxaTransport = getItem(position);

        viewHolder.detaliiCost.setVisibility(View.INVISIBLE);
        viewHolder.labelMacara.setVisibility(View.INVISIBLE);
        viewHolder.costMacara.setVisibility(View.INVISIBLE);

        viewHolder.detaliiCost.setOnClickListener(v -> {
            showTaxeMasinaFiliala(position);
        });

        viewHolder.textNumeFiliala.setText(taxaTransport.getFiliala());
        AdapterOptiuniLivrare adapterTransp = new AdapterOptiuniLivrare(context, getOptiuniLivrare(taxaTransport));
        viewHolder.spinnerTipTransport.setAdapter(adapterTransp);
        viewHolder.spinnerTipTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OptiuneLivrare optiune = (OptiuneLivrare) viewHolder.spinnerTipTransport.getSelectedItem();
                viewHolder.detaliiCost.setVisibility(View.INVISIBLE);
                viewHolder.costMacara.setVisibility(View.INVISIBLE);
                viewHolder.labelMacara.setVisibility(View.INVISIBLE);


                if (position > 0) {
                    taxaTransport.setSelectedCamion(optiune.getTipCamion());
                    taxaTransport.setAcceptaMacara(false);
                    taxaTransport.setTaxaMacaraAgent(0);

                    if (optiune.isMacara()) {

                        taxaTransport.setAcceptaMacara(true);
                        String taxaMacara = getValoareTransportMacara(taxaTransport, dateLivrare);

                        if (Double.valueOf(taxaMacara) > 0) {
                            viewHolder.labelMacara.setVisibility(View.VISIBLE);

                            if (optiune.getNumeOptiune().contains("macara"))
                                viewHolder.labelMacara.setText("Taxa macara");
                            else
                                viewHolder.labelMacara.setText("Taxa lift");

                            viewHolder.costMacara.setVisibility(View.VISIBLE);
                            viewHolder.costMacara.setText(taxaMacara);
                        }
                    }

                    if (Double.valueOf(optiune.getValoareOptiune()) > 0)
                        viewHolder.detaliiCost.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        viewHolder.costMacara.setTag(position);
        viewHolder.costMacara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position = (Integer) viewHolder.costMacara.getTag();
                if (s.length() > 0)
                    listTaxeTransport.get(position).setTaxaMacaraAgent(Double.parseDouble(s.toString()));
                else
                    listTaxeTransport.get(position).setTaxaMacaraAgent(0);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;

    }


    private void showTaxeMasinaFiliala(int position) {

        TaxaTransport taxaTransport = getItem(position);
        List<Details> listTaxe = null;

        for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {
            if (taxaCamion.getTipCamion().equals(taxaTransport.getSelectedCamion()) && taxaTransport.isAcceptaMacara() == isCamionLiftMacara(taxaCamion.getTaxeLivrare())) {
                listTaxe = new ArrayList<>();
                Details details = new Details();

                if (taxaCamion.getTaxeLivrare().getValoareTaxaTransport() > 0) {
                    details.setText1(taxaCamion.getTaxeLivrare().getNumeTaxaTransport());
                    details.setText2(df.format(taxaCamion.getTaxeLivrare().getValoareTaxaTransport()));
                    listTaxe.add(details);
                }

                if (taxaCamion.getTaxeLivrare().getValoareTaxaZona() > 0) {
                    details = new Details();
                    details.setText1(taxaCamion.getTaxeLivrare().getNumeTaxaZona());
                    details.setText2(df.format(taxaCamion.getTaxeLivrare().getValoareTaxaZona()));
                    listTaxe.add(details);
                }

                if (taxaCamion.getTaxeLivrare().getValoareTaxaAcces() > 0) {
                    details = new Details();
                    details.setText1(taxaCamion.getTaxeLivrare().getNumeTaxaAcces());
                    details.setText2(df.format(taxaCamion.getTaxeLivrare().getValoareTaxaAcces()));
                    listTaxe.add(details);
                }

            }
        }

        if (listTaxe != null && !listTaxe.isEmpty()) {
            TaxeTransportDialog taxeDialog = new TaxeTransportDialog(context, listTaxe);
            taxeDialog.show();
        }

    }

    private List<OptiuneLivrare> getOptiuniLivrare(TaxaTransport taxaTransport) {

        List<OptiuneLivrare> listOptiuniLivare = new ArrayList<>();

        String numeOptiune;
        for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {

            OptiuneLivrare optiuneLivrare = new OptiuneLivrare();

            if (taxaCamion.getTipCamion().equals(EnumTipCamion.CAMION_SCURT)) {
                optiuneLivrare.setNumeOptiune("Camion scurt");
            } else if (taxaCamion.getTipCamion().equals(EnumTipCamion.CAMION_IVECO)) {
                optiuneLivrare.setNumeOptiune("Camion Iveco");
            } else if (taxaCamion.getTipCamion().equals(EnumTipCamion.CAMION_ORICARE)) {
                optiuneLivrare.setNumeOptiune("Orice tip de camion");
            }

            numeOptiune = optiuneLivrare.getNumeOptiune();

            if (taxaCamion.getTaxeLivrare().isMacara())
                optiuneLivrare.setNumeOptiune(numeOptiune + " cu macara");
            else if (taxaCamion.getTaxeLivrare().isLift())
                optiuneLivrare.setNumeOptiune(numeOptiune + " cu lift");

            optiuneLivrare.setValoareOptiune(getValoareTransport(taxaTransport, taxaCamion));
            optiuneLivrare.setTaxaTransport(taxaTransport);
            optiuneLivrare.setMacara(taxaCamion.getTaxeLivrare().isMacara() || taxaCamion.getTaxeLivrare().isLift());
            optiuneLivrare.setTipCamion(taxaCamion.getTipCamion());
            listOptiuniLivare.add(optiuneLivrare);


        }

        OptiuneLivrare optiuneLivrare = new OptiuneLivrare();
        optiuneLivrare.setNumeOptiune("Apasati pentru selectie");
        optiuneLivrare.setValoareOptiune("");
        optiuneLivrare.setTaxaTransport(null);

        listOptiuniLivare.add(0, optiuneLivrare);

        return listOptiuniLivare;

    }


    private String getValoareTransport(TaxaTransport taxaTransport, BeanTaxaCamion taxaCamion) {
        double valoareTransport = 0;

        for (BeanTaxaCamion taxa : taxaTransport.getListTaxe()) {

            if (taxa.getTipCamion().equals(taxaCamion.getTipCamion()) && (isCamionLiftMacara(taxa.getTaxeLivrare()) == isCamionLiftMacara(taxaCamion.getTaxeLivrare()))) {
                valoareTransport = taxa.getTaxeLivrare().getValoareTaxaTransport() + taxa.getTaxeLivrare().getValoareTaxaAcces() +
                        taxa.getTaxeLivrare().getValoareTaxaZona();
            }
        }

        return df.format(valoareTransport);
    }

    private boolean isCamionLiftMacara(TaxeLivrare taxeLivrare) {
        return taxeLivrare.isLift() || taxeLivrare.isMacara();
    }


    private String getValoareTransportMacara(TaxaTransport taxaTransport, LivrareMathaus dateLivrare) {
        double valoareTransport = 0;

        for (BeanTaxaCamion taxa : taxaTransport.getListTaxe()) {

            if (taxa.getTipCamion().equals(taxaTransport.getSelectedCamion())) {
                int nrPaleti = HelperMathaus.getNrPaletiFiliala(dateLivrare, taxaTransport.getFiliala());
                valoareTransport = taxa.getTaxeLivrare().getTaxaMacara() * nrPaleti;
            }
        }

        return df.format(valoareTransport);
    }


    public int getCount() {
        return listTaxeTransport.size();
    }


    public TaxaTransport getItem(int position) {
        return listTaxeTransport.get(position);
    }


    public long getItemId(int position) {
        return 0;
    }

}
