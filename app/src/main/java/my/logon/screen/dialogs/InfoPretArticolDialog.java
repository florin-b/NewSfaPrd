package my.logon.screen.dialogs;

import java.text.NumberFormat;

import my.logon.screen.R;
import my.logon.screen.utils.UtilsGeneral;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoPretArticolDialog extends Dialog {

    private String infoPretArticol;
    private String unitatePret;

    public InfoPretArticolDialog(Context context, String infoPretArticol,
            String unitatePret) {
        super(context);
        setContentView(R.layout.infoarticoldialogaprobcmd);
        setTitle("Detalii pret");
        this.infoPretArticol = infoPretArticol;
        this.unitatePret = unitatePret;

        setUpLayout();

    }

    public void showDialog() {
        this.show();
    }

    private void closeThisDialog() {
        this.dismiss();
    }

    public void setUpLayout() {

        String stringCondPret = "Nu exista informatii.";

        if (infoPretArticol.contains(";")) {
            NumberFormat nf2 = NumberFormat.getInstance();

            nf2.setMinimumFractionDigits(2);
            nf2.setMaximumFractionDigits(2);

            int ii = 0;
            String[] tokPret;

            Double valCondPret = 0.0;

            String[] condPret = infoPretArticol.split(";");
            stringCondPret = "";

            for (ii = 0; ii < condPret.length; ii++) {
                tokPret = condPret[ii].split(":");
                valCondPret = Double.valueOf(tokPret[1].replace(',', '.')
                        .trim());
                if (valCondPret != 0)
                    stringCondPret += tokPret[0]
                            + UtilsGeneral.addSpace(20 - tokPret[0].length())
                            + ":"
                            + UtilsGeneral.addSpace(10 - String.valueOf(
                                    nf2.format(valCondPret)).length())
                            + nf2.format(valCondPret) + " RON "
                            + System.getProperty("line.separator");

            }

            String lUnitPret = "Unitate pret";

            stringCondPret += System.getProperty("line.separator") + lUnitPret
                    + UtilsGeneral.addSpace(20 - lUnitPret.length()) + ":"
                    + UtilsGeneral.addSpace(10 - unitatePret.length())
                    + unitatePret;

        }

        TextView textInfoArticol = (TextView) findViewById(R.id.textInfoArticol);
        textInfoArticol.setText(stringCondPret);

        Button btnOkInfo = (Button) findViewById(R.id.btnOkInfo);
        btnOkInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                closeThisDialog();

            }
        });

    }

}
