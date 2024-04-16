package my.logon.screen.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.enums.EnumTipAlert;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.UserInfo;
import my.logon.screen.screens.CreareComanda;
import my.logon.screen.screens.CreareComandaGed;

public class UtilsGeneral {

    public static String[] numeFiliale = {"Andronache", "Bacau", "Baia-Mare", "Brasov", "Buzau", "Constanta", "Cluj", "Craiova", "Focsani", "Galati", "Glina",
            "Hunedoara", "Iasi", "Militari", "Oradea", "Otopeni", "Piatra-Neamt", "Pitesti", "Ploiesti", "Sibiu", "Suceava", "Timisoara", "Tg. Mures"};

    public static String[] codFiliale = {"BU13", "BC10", "MM10", "BV10", "BZ10", "CT10", "CJ10", "DJ10", "VN10", "GL10", "BU10", "HD10", "IS10", "BU11",
            "BH10", "BU12", "NT10", "AG10", "PH10", "SB10", "SV10", "TM10", "MS10"};

    public static String[] numeDivizii = {"Lemnoase", "Feronerie", "Parchet", "Materiale grele", "Electrice", "Gips", "Chimice", "Instalatii", "Hidroizolatii"};

    public static String[] codDivizii = {"01", "02", "03", "04", "05", "06", "07", "08", "09"};

    public static String[] numeJudete = {"Selectati un judet", "ALBA", "ARAD", "ARGES", "BACAU", "BIHOR", "BISTRITA-NASAUD", "BOTOSANI", "BRAILA", "BRASOV",
            "BUCURESTI", "BUZAU", "CALARASI", "CARAS-SEVERIN", "CLUJ", "CONSTANTA", "COVASNA", "DAMBOVITA", "DOLJ", "GALATI", "GIURGIU", "GORJ", "HARGHITA",
            "HUNEDOARA", "IALOMITA", "IASI", "ILFOV", "MARAMURES", "MEHEDINTI", "MURES", "NEAMT", "OLT", "PRAHOVA", "SALAJ", "SATU-MARE", "SIBIU", "SUCEAVA",
            "TELEORMAN", "TIMIS", "TULCEA", "VALCEA", "VASLUI", "VRANCEA"};

    public static String[] codJudete = {" ", "01", "02", "03", "04", "05", "06", "07", "09", "08", "40", "10", "51", "11", "12", "13", "14", "15", "16", "17",
            "52", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "31", "30", "32", "33", "34", "35", "36", "38", "37", "39"};

    private static String[] depoziteDISTRIB = {"V1 - vanzare", "V2 - vanzare", "V3 - vanzare", "G1 - gratuite", "G2 - gratuite", "G3 - gratuite",
             "DESC", "GAR1"};

    public static String[] tipReducere = {"1 factura (red. in pret)", "2 facturi", "1 factura (red. separat)"};

    public static String[] getDepoziteDistributie() {
        List<String> listDepozite = new ArrayList<String>(Arrays.asList(depoziteDISTRIB));

        if (UserInfo.getInstance().getCodDepart().equals("02") || UserInfo.getInstance().getDepartExtra().contains("02"))
            listDepozite.add("92V1");

        if (UserInfo.getInstance().getCodDepart().equals("05") || UtilsUser.isKA() || UtilsUser.isUserSDKA() || UtilsUser.isUserKA())
            listDepozite.add("95V1");

        listDepozite.add("DSCM");

        if (DateLivrare.getInstance().getTipComandaDistrib() != TipCmdDistrib.COMANDA_LIVRARE)
            listDepozite.add("MAV1");

        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_DETERIORATE) {
            listDepozite.add("D1 - deteriorate");
            listDepozite.add("MAD1");
        }

        if (DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.DISPOZITIE_LIVRARE)
            listDepozite.remove("DESC");

        return listDepozite.toArray(new String[listDepozite.size()]);
    }

    public static String[] getDepoziteGed() {

        List<String> listDepozite = new ArrayList<String>(Arrays.asList(depoziteDISTRIB));

        listDepozite.add("MAV1");
        listDepozite.add("MAV2");
        listDepozite.add("92V1");
        listDepozite.add("95V1");

        if (DateLivrare.getInstance().getTipComandaGed() == TipCmdGed.ARTICOLE_DETERIORATE || DateLivrare.getInstance().getTipComandaDistrib() == TipCmdDistrib.ARTICOLE_DETERIORATE) {
            listDepozite.add("D1 - deteriorate");
            listDepozite.add("MAD1");
        }

        if (UtilsUser.isConsWood())
            listDepozite.add("WOOD");

        listDepozite.add("DSCM");

        return listDepozite.toArray(new String[listDepozite.size()]);

    }

    public static String[] getDepoziteSite() {
        List<String> listDepozite = new ArrayList<String>(Arrays.asList(getDepoziteGed()));

        return listDepozite.toArray(new String[listDepozite.size()]);

    }

    public static void trateazaExceptieMAV_BU(ArrayAdapter<String> adapterSpinnerDepozite) {

        // pentru filialele BU se elimina MAV2, conform solicitarii din
        // 17.09.2020
        if (UserInfo.getInstance().getUnitLog().startsWith("BU"))
            adapterSpinnerDepozite.remove("MAV2");
    }

    public static String[] getDepoziteMav() {
        return new String[]{"MAV1", "MAV2"};
    }

    public static String[] getDepoziteWood() {

        List<String> listDepozite = new ArrayList<String>(Arrays.asList(depoziteDISTRIB));

        listDepozite.add("WOOD");
        listDepozite.add("92V1");

        return listDepozite.toArray(new String[listDepozite.size()]);

    }

    public static void showCustomToast(Context context, String strMessage, EnumTipAlert tipAlert) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_layout, null);

        ImageView image = (ImageView) view.findViewById(R.id.imageView1);

        if (tipAlert == EnumTipAlert.Info)
            image.setBackgroundResource(R.drawable.green_icon);
        else if (tipAlert == EnumTipAlert.Warning)
            image.setBackgroundResource(R.drawable.yellow_icon);
        else if (tipAlert == EnumTipAlert.Error)
            image.setBackgroundResource(R.drawable.red_icon);

        TextView message = (TextView) view.findViewById(R.id.textMessage);
        message.setText(strMessage);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();

    }

    public static HashMap<String, String> newHashMapInstance() {
        return new HashMap<String, String>();
    }

    public static String getNumeCategorieClient(String codCategorie) {
        String numeCategorie = "";

        if (codCategorie.equals("04"))
            numeCategorie = "Fundatie si suprastructura";

        if (codCategorie.equals("09"))
            numeCategorie = "Acoperis";

        if (codCategorie.equals("08"))
            numeCategorie = "Instalatii";

        if (codCategorie.equals("05"))
            numeCategorie = "Electrice";

        if (codCategorie.equals("03"))
            numeCategorie = "Parchet";

        if (codCategorie.equals("06"))
            numeCategorie = "Gips";

        if (codCategorie.equals("07"))
            numeCategorie = "Chimice";

        return numeCategorie;
    }

    public static String addSpace(int nrCars) {
        String retVal = "";
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < nrCars; i++)
            sb.append(' ');

        retVal = sb.toString();
        return retVal;
    }

    public static String getDescTipPlata(String codPlata, String termenPlata) {

        String tipPlata = "nedefinit";

        if (codPlata.equals("B")) {
            tipPlata = "Bilet la ordin";
        } else if (codPlata.equals("C")) {
            tipPlata = "Cec";
        } else if (codPlata.equals("E")) {
            tipPlata = "Plata in numerar";
        } else if (codPlata.equals("L")) {
            tipPlata = "Plata interna buget-trezorerie";
        } else if (codPlata.equals("O")) {
            if (termenPlata != null && termenPlata.equals("C000"))
                tipPlata = "Ordin de plata avans";
            else
                tipPlata = "Ordin de plata";
        } else if (codPlata.equals("U")) {
            tipPlata = "Plata interna-alte institutii";
        } else if (codPlata.equals("W")) {
            tipPlata = "Plata in strainatate-banci";
        } else if (codPlata.equals("BRD")) {
            tipPlata = "Card BRD";
        } else if (codPlata.equals("ING")) {
            tipPlata = "Card ING";
        } else if (codPlata.equals("UNI")) {
            tipPlata = "Card Unicredit";
        } else if (codPlata.equals("E1")) {
            tipPlata = "Ramburs";
        } else if (codPlata.equals("CB")) {
            tipPlata = "Card bancar";
        } else if (codPlata.equals("LC")) {
            tipPlata = "Limita credit";
        } else if (codPlata.equals("OPA")) {
            tipPlata = "OP avans";
        } else if (codPlata.equals("R")) {
            tipPlata = "Ramburs";
        } else if (codPlata.equals("N")) {
            tipPlata = "Plata in numerar";
        }

        return tipPlata;

    }

    public static String getDescTipTransport(String codTransport) {

        String tipTransport = "";

        if (codTransport.equals("TCLI")) {
            tipTransport = "Transport client";
        } else if (codTransport.equals("TRAP")) {
            tipTransport = "Transport Arabesque";
        } else if (codTransport.equals("TERT")) {
            tipTransport = "Transport terti";
        } else if (codTransport.equals("TERR")) {
            tipTransport = "Curier rapid";
        } else if (codTransport.equals("TFRN")) {
            tipTransport = "Transport furnizor";
        }
        return tipTransport;

    }

    public static String getTipCantarire(String codCantarire) {
        String tipCantarire = "";

        if (codCantarire.equals("0"))
            tipCantarire = "Nu";

        if (codCantarire.equals("1"))
            tipCantarire = "Da";

        return tipCantarire;
    }

    public static String getFormattedDate(String rawDate) {
        String formattedDate = rawDate;

        if (!rawDate.contains(".")) {
            try {
                Integer.parseInt(rawDate);
                formattedDate = rawDate.substring(6, 8) + "." + rawDate.substring(4, 6) + "." + rawDate.substring(0, 4);
            } catch (Exception ex) {
            }
        }

        return formattedDate;
    }

    public static String getTipReducere(String codReducere) {
        String tipReducere = "";

        if (codReducere == null)
            return tipReducere;

        if (codReducere.equals("X")) {
            tipReducere = "2 facturi";
        }
        if (codReducere.equals(" ")) {
            tipReducere = "1 factura (red. in pret)";
        }
        if (codReducere.equals("R")) {
            tipReducere = "1 factura (red. separat)";
        }

        return tipReducere;
    }

    public static String getDescStatusArt(String codStatus) {

        String statusArt = "";

        if (codStatus.equals("9")) {
            statusArt = "Stoc insuficient";
        }
        if (codStatus.equals("19")) {
            statusArt = "Articol fara pret";
        }
        if (codStatus.equals("16")) {
            statusArt = "Articol modificat";
        }
        if (codStatus.equals("17")) {
            statusArt = "Articol adaugat";
        }
        if (codStatus.equals("18")) {
            statusArt = "Articol sters";
        }

        return statusArt;

    }

    public static String getDate(int nrDaysToAdd) {
        String newDate = "";

        Date today = new Date();
        SimpleDateFormat FormattedDATE = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, nrDaysToAdd);
        newDate = (String) (FormattedDATE.format(c.getTime()));

        return newDate;
    }

    public static String[] getStareMeniu(String stareMeniu) {
        return stareMeniu.replace("[", "").replace("]", "").split(",");
    }

    public static String getTipTransport(String tipTransport) {
        String transportRet = "";

        if (tipTransport.toLowerCase().contains("trap"))
            transportRet = "TRAP";

        if (tipTransport.toLowerCase().contains("tcli"))
            transportRet = "TCLI";

        if (tipTransport.toLowerCase().contains("tert"))
            transportRet = "TERT";

        if (tipTransport.toLowerCase().contains("tfrn"))
            transportRet = "TFRN";

        return transportRet;
    }

    public static String getNumeJudet(String codJudet) {
        String retVal = "Nedefinit";

        if (codJudet.equals("01"))
            retVal = "ALBA";

        if (codJudet.equals("02"))
            retVal = "ARAD";

        if (codJudet.equals("03"))
            retVal = "ARGES";

        if (codJudet.equals("04"))
            retVal = "BACAU";

        if (codJudet.equals("05"))
            retVal = "BIHOR";

        if (codJudet.equals("06"))
            retVal = "BISTRITA-NASAUD";

        if (codJudet.equals("07"))
            retVal = "BOTOSANI";

        if (codJudet.equals("09"))
            retVal = "BRAILA";

        if (codJudet.equals("08"))
            retVal = "BRASOV";

        if (codJudet.equals("40"))
            retVal = "BUCURESTI";

        if (codJudet.equals("10"))
            retVal = "BUZAU";

        if (codJudet.equals("51"))
            retVal = "CALARASI";

        if (codJudet.equals("11"))
            retVal = "CARAS-SEVERIN";

        if (codJudet.equals("12"))
            retVal = "CLUJ";

        if (codJudet.equals("13"))
            retVal = "CONSTANTA";

        if (codJudet.equals("14"))
            retVal = "COVASNA";

        if (codJudet.equals("15"))
            retVal = "DAMBOVITA";

        if (codJudet.equals("16"))
            retVal = "DOLJ";

        if (codJudet.equals("17"))
            retVal = "GALATI";

        if (codJudet.equals("52"))
            retVal = "GIURGIU";

        if (codJudet.equals("18"))
            retVal = "GORJ";

        if (codJudet.equals("19"))
            retVal = "HARGHITA";

        if (codJudet.equals("20"))
            retVal = "HUNEDOARA";

        if (codJudet.equals("21"))
            retVal = "IALOMITA";

        if (codJudet.equals("22"))
            retVal = "IASI";

        if (codJudet.equals("23"))
            retVal = "ILFOV";

        if (codJudet.equals("24"))
            retVal = "MARAMURES";

        if (codJudet.equals("25"))
            retVal = "MEHEDINTI";

        if (codJudet.equals("26"))
            retVal = "MURES";

        if (codJudet.equals("27"))
            retVal = "NEAMT";

        if (codJudet.equals("28"))
            retVal = "OLT";

        if (codJudet.equals("29"))
            retVal = "PRAHOVA";

        if (codJudet.equals("31"))
            retVal = "SALAJ";

        if (codJudet.equals("30"))
            retVal = "SATU MARE";

        if (codJudet.equals("32"))
            retVal = "SIBIU";

        if (codJudet.equals("33"))
            retVal = "SUCEAVA";

        if (codJudet.equals("34"))
            retVal = "TELEORMAN";

        if (codJudet.equals("35"))
            retVal = "TIMIS";

        if (codJudet.equals("36"))
            retVal = "TULCEA";

        if (codJudet.equals("38"))
            retVal = "VALCEA";

        if (codJudet.equals("37"))
            retVal = "VASLUI";

        if (codJudet.equals("39"))
            retVal = "VRANCEA";

        return retVal;
    }

    public static String getFiliala(String numeFiliala) {
        String fl = "NN10";

        if (numeFiliala.equals("BACAU"))
            fl = "BC10";

        if (numeFiliala.equals("BUZAU"))
            fl = "BZ10";

        if (numeFiliala.equals("GALATI"))
            fl = "GL10";

        if (numeFiliala.equals("PITESTI"))
            fl = "AG10";

        if (numeFiliala.equals("TIMISOARA"))
            fl = "TM10";

        if (numeFiliala.equals("ORADEA"))
            fl = "BH10";

        if (numeFiliala.equals("FOCSANI"))
            fl = "VN10";

        if (numeFiliala.equals("GLINA"))
            fl = "BU10";

        if (numeFiliala.equals("ANDRONACHE"))
            fl = "BU13";

        if (numeFiliala.equals("OTOPENI"))
            fl = "BU12";

        if (numeFiliala.equals("CLUJ"))
            fl = "CJ10";

        if (numeFiliala.equals("BAIA"))
            fl = "MM10";

        if (numeFiliala.equals("MILITARI"))
            fl = "BU11";

        if (numeFiliala.equals("CONSTANTA"))
            fl = "CT10";

        if (numeFiliala.equals("BRASOV"))
            fl = "BV10";

        if (numeFiliala.equals("PLOIESTI"))
            fl = "PH10";

        if (numeFiliala.equals("PIATRA"))
            fl = "NT10";

        if (numeFiliala.equals("MURES"))
            fl = "MS10";

        if (numeFiliala.equals("IASI"))
            fl = "IS10";

        if (numeFiliala.equals("CRAIOVA"))
            fl = "DJ10";

        if (numeFiliala.equals("SIBIU"))
            fl = "SB10";

        if (numeFiliala.equals("SUCEAVA"))
            fl = "SV10";

        if (numeFiliala.equals("DEVA"))
            fl = "HD10";

        return fl;

    }

    public static String getDepart(String numeDepart) {
        String dpt = "00";

        if (numeDepart.equals("CHIM"))
            dpt = "07";

        if (numeDepart.equals("DIVE"))
            dpt = "10";

        if (numeDepart.equals("ELEC"))
            dpt = "05";

        if (numeDepart.equals("FERO"))
            dpt = "02";

        if (numeDepart.equals("GIPS"))
            dpt = "06";

        if (numeDepart.equals("INST"))
            dpt = "08";

        if (numeDepart.equals("LEMN"))
            dpt = "01";

        if (numeDepart.equals("MATE"))
            dpt = UserInfo.getInstance().getInitDivizie();

        if (numeDepart.equals("PARC"))
            dpt = "03";

        if (numeDepart.equals("CHIM"))
            dpt = "07";

        if (numeDepart.equals("HIDR"))
            dpt = "09";

        if (numeDepart.equals("LEFA"))
            dpt = "02";

        return dpt;

    }

    public static boolean isFilialaMathaus(String filiala) {

        String filialeMathaus = "CT10, BU10, AG10, DJ10, BH10, IS10, GL10";

        return filialeMathaus.contains(filiala);

    }

    public static List<String> getFilialaMathaus() {
        List<String> filialeMathaus = new ArrayList<String>();

        filialeMathaus.add("CT10");
        filialeMathaus.add("BU10");
        filialeMathaus.add("AG10");
        filialeMathaus.add("DJ10");
        filialeMathaus.add("BH10");
        filialeMathaus.add("IS10");
        filialeMathaus.add("GL10");

        return filialeMathaus;
    }

    public static boolean isMathausMare(String filiala) {

        String MathausMare = "AG10, BU10, IS10";

        return MathausMare.contains(filiala);
    }

    public static boolean isMathausMic(String filiala) {

        String MathausMic = "GL10, CT10, DJ10, BH10";

        return MathausMic.contains(filiala);
    }

    public static String getUnitLogGed(String unitLog) {
        return unitLog.substring(0, 2) + "2" + unitLog.substring(3, 4);
    }

    public static String getUnitLogDistrib(String unitLog) {
        return unitLog.substring(0, 2) + "1" + unitLog.substring(3, 4);
    }

    public static boolean isUlGed(String unitLog) {
        return unitLog.substring(2, 3).equals("2");
    }

    public static boolean isUlDistrib(String unitLog) {
        return unitLog.substring(2, 3).equals("1");
    }

    public static void setSpinnerSelectedItem(Spinner spinner, String position) {

        for (int ii = 0; ii < spinner.getAdapter().getCount(); ii++) {
            if (spinner.getItemAtPosition(ii).equals(position)) {
                spinner.setSelection(ii);
                break;
            }
        }

    }

    public static boolean isFilMareLivrTCLIDistrib() {

        String filialaLivrare = "";
        if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_VANZARE))
            filialaLivrare = CreareComanda.filialaAlternativa;
        else if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.COMANDA_LIVRARE))
            filialaLivrare = DateLivrare.getInstance().getCodFilialaCLP();

        return filialaLivrare.equals("IS10") || filialaLivrare.equals("AG10") || filialaLivrare.equals("BU10");
    }

    public static boolean isFilMareLivrTCLIGed() {

        String filialaLivrare = "";
        if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_VANZARE))
            filialaLivrare = CreareComandaGed.filialaAlternativa;
        else if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.COMANDA_LIVRARE))
            filialaLivrare = DateLivrare.getInstance().getCodFilialaCLP();

        return filialaLivrare.equals("IS10") || filialaLivrare.equals("IS20") || filialaLivrare.equals("AG10") || filialaLivrare.equals("AG20")
                || filialaLivrare.equals("BU10") || filialaLivrare.equals("BU20");
    }

    public static boolean isAceeasiFiliala(String filiala1, String filiala2){

        String fil1Brut = filiala1.substring(0,2) + filiala1.substring(3,4);
        String fil2Brut = filiala2.substring(0,2) + filiala2.substring(3,4);

        return fil1Brut.equals(fil2Brut);
    }


    public static boolean isInFilialaArondate(String filialeArondate, String filialaAgent){

        String[] filialeAr = filialeArondate.split(",");

        for (String fil : filialeAr){
            if (isAceeasiFiliala(fil, filialaAgent))
                return true;
        }

        return false;
    }

}
