//Creat de Robert
package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import my.logon.screen.R;
import my.logon.screen.listeners.TipMasinaLivrareListener;

public class SelectTipMasinaDialog extends Dialog {

    private Context context;
    private Button btnContinua;
    private String tipMasinaSelected;
    private TipMasinaLivrareListener listener;
    private CheckBox checkCscurt, checkCiveco;


    public SelectTipMasinaDialog(Context context, String[] listMasini) {
        super(context);
        setContentView(R.layout.select_tip_masina_dialog);
        this.context = context;
        setTitle("Tip camion");
        setCancelable(false);

        checkCscurt = findViewById(R.id.checkbox_cscurt);
        checkCiveco = findViewById(R.id.checkbox_civeco);

        if (listMasini[0].trim().isEmpty())
            checkCscurt.setEnabled(false);

        if (listMasini[1].trim().isEmpty())
            checkCiveco.setEnabled(false);

        setListenerCscurt();
        setListenerCIveco();

        btnContinua = findViewById(R.id.btnContinua);
        addBtnContinuaListener();

    }

    private void setListenerCscurt() {
        checkCscurt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tipMasinaSelected = "Camion scurt";
                    checkCiveco.setChecked(false);
                } else
                    tipMasinaSelected = "";
            }
        });
    }

    private void setListenerCIveco() {
        checkCiveco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tipMasinaSelected = "Camioneta IVECO";
                    checkCscurt.setChecked(false);
                } else
                    tipMasinaSelected = "";
            }
        });

    }

    private void addBtnContinuaListener() {
        btnContinua.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (listener != null) {
                    listener.tipMasinaLivrareSelected(tipMasinaSelected);
                    dismiss();
                }

            }
        });
    }


    public void setTipMasinaLivrareListener(TipMasinaLivrareListener listener) {
        this.listener = listener;
    }

}
