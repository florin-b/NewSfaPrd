package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.listeners.ZileLivrareListener;

public class DateLivrareFilialaDialog extends Dialog {


    private Context context;
    private ListView listViewDateLivrare;
    private Button btnSalveaza;
    private Button btnRenunta;
    private String dataLivrare;
    private ZileLivrareListener zileLivrareListener;
    private String zileLivrare[];

    public DateLivrareFilialaDialog(Context context, String[] zileLivrare) {
        super(context);
        this.context = context;
        this.zileLivrare = zileLivrare;

        setContentView(R.layout.date_livrare_filiala_dialog);
        setTitle("Selectati data de livrare:");
        setCancelable(false);
        setupLayout();

    }

    private void setupLayout() {


        listViewDateLivrare = (ListView) findViewById(R.id.listDateLivrare);
        btnSalveaza = (Button) findViewById(R.id.btnSalveaza);
        btnRenunta = (Button) findViewById(R.id.btnRenunta);

        setListenerBtnSalveaza();
        setListenerBtnRenunta();
        setListenerZileLivrare();
        afisZileLivrare(zileLivrare);
    }

    private void setListenerBtnSalveaza() {
        btnSalveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataLivrare == null) {
                    Toast.makeText(context, "Selectati o data de livrare.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (zileLivrareListener != null)
                    zileLivrareListener.dataLivrareSelected(dataLivrare);

                dismiss();
            }
        });
    }

    private void setListenerBtnRenunta() {
        btnRenunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setListenerZileLivrare() {
        listViewDateLivrare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> hashDataLivrare = (HashMap<String, String>) listViewDateLivrare.getItemAtPosition(position);
                dataLivrare = hashDataLivrare.get("startInterval");

                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.LTGRAY);
            }
        });


    }


    private void afisZileLivrare(String[] arrayDate) {

        ArrayList<HashMap<String, String>> listZileLivrare = new ArrayList<HashMap<String, String>>();
        SimpleAdapter adapterZileLivrare = new SimpleAdapter(context, listZileLivrare, R.layout.row_zile_livrare, new String[]{"startInterval"},
                new int[]{R.id.textDataLivrare});

        HashMap<String, String> temp;
        for (int ii = 0; ii < arrayDate.length; ii++) {
            temp = new HashMap<String, String>();
            temp.put("startInterval", arrayDate[ii]);
            listZileLivrare.add(temp);
        }

        listViewDateLivrare.setAdapter(adapterZileLivrare);

    }

    public void setZileLivrareListener(ZileLivrareListener listener) {
        this.zileLivrareListener = listener;
    }


}
