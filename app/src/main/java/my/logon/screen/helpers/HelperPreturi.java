package my.logon.screen.helpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.utils.UtilsFormatting;

public class HelperPreturi {

    public static String getInfoPret(PretArticolGed pretArticol, NumberFormat nf){


        String[] condPret = pretArticol.getConditiiPret().split(";");

        int ii = 0;
        String[] tokPret;
        String stringCondPret = "";
        Double valCondPret = 0.0;




        stringCondPret += "Pret minim" + addSpace(20 - "Pret minim".length()) + ":"
                + addSpace(10 - nf.format(pretArticol.getPretMinim()).length()) + nf.format(pretArticol.getPretMinim())
                + System.getProperty("line.separator");




        for (ii = 0; ii < condPret.length; ii++) {
            tokPret = condPret[ii].split(":");

            if (!isCondPret(tokPret[0]))
                continue;

            if (stringCondPret.toLowerCase().contains("tva"))
                continue;

            valCondPret = Double.valueOf(tokPret[1].replace(',', '.').trim());
            if (valCondPret != 0) {
                stringCondPret += tokPret[0] + addSpace(20 - tokPret[0].length()) + ":"
                        + addSpace(10 - String.valueOf(nf.format(valCondPret)).length()) + nf.format(valCondPret)
                        + System.getProperty("line.separator");

            }

        }

        return stringCondPret;
    }

    private static boolean isCondPret(String condPret){

        if (condPret == null)
            return false;

        if (condPret.toLowerCase().contains("tva"))
            return true;

        if (condPret.toLowerCase().contains("verde"))
            return true;

        return false;
    }


    public static String getIstoricPret(String infoIstoric){

        String istoricPret = "";
        DecimalFormat df = new DecimalFormat("#0.00");

        if (infoIstoric.contains(":")) {
            String[] arrayIstoric = infoIstoric.split(":");

            if (arrayIstoric.length > 0 && arrayIstoric[0].contains("@")) {


                String[] arrayPret = arrayIstoric[0].split("@");

                istoricPret = "Istoric pret" + addSpace(20 - "Istoric pret".length()) + ":"
                        + addSpace(9 - df.format(Double.valueOf(arrayPret[0])).length()) + df.format(Double.valueOf(arrayPret[0])) + " /" +
                        addSpace(3 - String.valueOf(arrayPret[1]).length())+
                         arrayPret[1] +
                        addSpace(3 - String.valueOf(arrayPret[2]).length())
                        + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2) + System.getProperty("line.separator");

            }

            if (arrayIstoric.length > 1 && arrayIstoric[1].contains("@")) {

                String[] arrayPret = arrayIstoric[1].split("@");

                istoricPret +=  addSpace(20 ) + ":"
                        + addSpace(9 - df.format(Double.valueOf(arrayPret[0])).length()) + df.format(Double.valueOf(arrayPret[0])) + " /" +
                        addSpace(3 - String.valueOf(arrayPret[1]).length())+
                        arrayPret[1] +
                        addSpace(3 - String.valueOf(arrayPret[2]).length())
                        + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2) + System.getProperty("line.separator");


            }

            if (arrayIstoric.length > 2 && arrayIstoric[2].contains("@")) {

                String[] arrayPret = arrayIstoric[2].split("@");

                istoricPret +=  addSpace(20 ) + ":"
                        + addSpace(9 - df.format(Double.valueOf(arrayPret[0])).length()) + df.format(Double.valueOf(arrayPret[0])) + " /" +
                        addSpace(3 - String.valueOf(arrayPret[1]).length())+
                        arrayPret[1] +
                        addSpace(3 - String.valueOf(arrayPret[2]).length())
                        + arrayPret[2] + " - " + UtilsFormatting.getMonthNameFromDate(arrayPret[3], 2) + System.getProperty("line.separator");

            }



        }

        return istoricPret;

    }

    private static String addSpace(int nrCars) {
        String retVal = "";

        for (int i = 0; i < nrCars; i++)
            retVal += " ";

        return retVal;
    }
}
