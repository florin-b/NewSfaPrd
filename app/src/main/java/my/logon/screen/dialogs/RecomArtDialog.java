package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.beans.BeanArticolCautare;

public class RecomArtDialog extends Dialog {

    private ListView listViewArt;
    private Button btnOk;
    private List<BeanArticolCautare> listArticole;
    private Context context;
    private ArrayList<HashMap<String, String>> hashArtRec;
    private String numeArticol;


    public RecomArtDialog(Context context, List<BeanArticolCautare> listArticole, String numeArticol) {
        super(context);
        this.context = context;
        this.listArticole = listArticole;
        this.numeArticol = numeArticol;
        setContentView(R.layout.art_recom_dialog);

        setTitle("Cumparate impreuna cu " + numeArticol);
        populateHashRec();
        setupLayout();

    }



    private void setupLayout() {
        listViewArt = (ListView) findViewById(R.id.listArtRecom);

        SimpleAdapter adapterMotive = new SimpleAdapter(context, hashArtRec, R.layout.row_art_recom,
                new String[]{"numeLinie", "codLinie"}, new int[]{R.id.textNumeLinie, R.id.textCodLinie});

        listViewArt.setAdapter(adapterMotive);

        btnOk = (Button) findViewById(R.id.btnOk);
        setListenerBtnOk();

    }

    private void populateHashRec() {

        hashArtRec = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> temp;

        for (BeanArticolCautare articol : listArticole) {
            {
                temp = new HashMap<String, String>();
                temp.put("numeLinie", articol.getNume());
                temp.put("codLinie", articol.getCod());
                hashArtRec.add(temp);
            }

        }
    }

    @Override
    public void show() {
        super.show();
    }

    private void setListenerBtnOk() {
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dismiss();

            }
        });
    }

}
