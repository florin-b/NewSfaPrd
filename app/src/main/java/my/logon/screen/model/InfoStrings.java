/**
 * @author florinb
 *
 */
package my.logon.screen.model;

import java.util.Locale;

public class InfoStrings {

	public static String statusSAPMsg(Integer msgId) {
		String retVal = "";

		switch (msgId) {
		case -1:
			retVal = "Comanda NU a fost salvata.";
			break;
		case 0:
			retVal = "Comanda salvata.";
			break;
		case 1:
			retVal = "Comanda in curs de validare.";
			break;
		case 2:
			retVal = "Comanda validata.";
			break;
		case 3:
			retVal = "Comanda in curs de modificare.";
			break;
		case 4:
			retVal = "Comanda modificata.";
			break;
		case 5:
			retVal = "Comanda in curs de stergere.";
			break;
		case 6:
			retVal = "Comanda stearsa.";
			break;
		case 8:
			retVal = "Comanda facturata si emisa.";
			break;
		case 9:
			retVal = "Comanda invalida. Stoc insuficient.";
			break;
		case 10:
			retVal = "Comanda invalida datorita limitei de credit.";
			break;
		case 11:
			retVal = "Comanda salvata.";
			break;
		case 12:
			retVal = "Cmd. angaj. in curs de aprobare.";
			break;
		case 13:
			retVal = "Cmd. angaj. aprobata.";
			break;
		case 14:
			retVal = "Cmd. angaj. respinsa.";
			break;
		case 16:
			retVal = "Comanda simulata.";
			break;

		case 30:
			retVal = "Comanda nealocata pe borderou";
			break;

		case 31:
			retVal = "Comanda alocata pe borderou";
			break;
		case 96:
			retVal = "Comanda salvata. Tipul de transport a fost redeterminat.";

			break;	
		case 98:
			retVal = "Comanda nu a fost salvata. Tipul de transport nu s-a putut determina.";
			break;			
		case 99:
			retVal = "Atentie! Clientul nu apartine diviziei din document.";
			break;
		default:
			retVal = "Stare comanda necunoscuta. Cod stare: " + msgId;
			break;
		}

		return retVal;
	}

	public static String statusAprobCmd(Integer msgId) {
		String retVal = "";

		switch (msgId) {
		case -1:
			retVal = "Comanda NU a fost salvata.";
			break;

		case 0:
			retVal = "Comanda emisa.";
			break;

		case 1:
			retVal = "Comanda in curs de aprobare.";
			break;

		case 2:
			retVal = "Comanda aprobata.";
			break;

		case 3:
			retVal = "Comanda respinsa.";
			break;

		case 4:
			retVal = "Comanda supusa unor conditii.";
			break;

		case 5:
			retVal = "Comanda stearsa.";
			break;

		case 6:
			retVal = "Cmd. angaj. in curs de aprobare.";
			break;

		case 7:
			retVal = "Cmd. angaj. aprobata.";
			break;

		case 8:
			retVal = "Cmd. angaj. respinsa.";
			break;

		case 9:
			retVal = "Comanda invalida. Stoc insuficient.";
			break;

		case 10:
			retVal = "Comanda invalida datorita limitei de credit.";
			break;

		case 15:
			retVal = "Comanda facturata.";
			break;

		case 20:
			retVal = "Comanda simulata cu rezervare stoc."; // pt. comenzi GED
			break;

		case 21:
			retVal = "Comanda simulata fara rezervare stoc."; // pt. comenzi GED
			break;

		case 41:
			retVal = "Comanda fara rez. stoc cu conditii";
			break;
			
		case 30:
			retVal = "Comanda nealocata pe borderou";
			break;

		case 31:
			retVal = "Comanda alocata pe borderou";
			break;

		}
		return retVal;
	}

	public static String getTipClient(String codTip) {
		String retVal = " ";

		if (codTip.equals("01"))
			retVal = "Client final";

		if (codTip.equals("02"))
			retVal = "Constructor general";

		if (codTip.equals("03"))
			retVal = "Constructor special";

		if (codTip.equals("04"))
			retVal = "Revanzator";

		if (codTip.equals("05"))
			retVal = "Producator mobila";

		if (codTip.equals("06"))
			retVal = "Debitor mat. lemnoase";

		if (codTip.equals("07"))
			retVal = "Tepar";

		if (codTip.equals("08"))
			retVal = "Nespecificat";

		if (codTip.equals("09"))
			retVal = "Client extern UE";

		if (codTip.equals("10"))
			retVal = "Client extern non-UE";

		return retVal;
	}

	public static String numeJudet(String codJudet) {
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
			retVal = "SATU-MARE";

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

	public static String getULFiliala(String numeUL) {
		String fl = "NN10";

		if (numeUL.equals("BACAU"))
			fl = "BC10";

		if (numeUL.equals("GALATI"))
			fl = "GL10";

		if (numeUL.equals("PITESTI"))
			fl = "AG10";

		if (numeUL.equals("TIMISOARA"))
			fl = "TM10";

		if (numeUL.equals("ORADEA"))
			fl = "BH10";

		if (numeUL.equals("FOCSANI"))
			fl = "VN10";

		if (numeUL.equals("GLINA"))
			fl = "BU10";

		if (numeUL.equals("CLUJ"))
			fl = "CJ10";

		if (numeUL.equals("BAIA"))
			fl = "MM10";

		if (numeUL.equals("MILITARI"))
			fl = "BU11";

		if (numeUL.equals("CONSTANTA"))
			fl = "CT10";

		if (numeUL.equals("BRASOV"))
			fl = "BV10";

		if (numeUL.equals("PLOIESTI"))
			fl = "PH10";

		if (numeUL.equals("PIATRA"))
			fl = "NT10";

		if (numeUL.equals("MUREA"))
			fl = "MS10";

		if (numeUL.equals("IASI"))
			fl = "IS10";

		if (numeUL.equals("OTOPENI"))
			fl = "BU12";

		return fl;

	}

	public static String getNumeUL(String numeUL) {
		String numeFil = "NN10";

		if (numeUL.equals("BC10"))
			numeFil = "BACAU";

		if (numeUL.equals("BZ10"))
			numeFil = "BUZAU";

		if (numeUL.equals("HD10"))
			numeFil = "HUNEDOARA";

		if (numeUL.equals("GL10"))
			numeFil = "GALATI";

		if (numeUL.equals("AG10"))
			numeFil = "ARGES";

		if (numeUL.equals("TM10"))
			numeFil = "TIMISOARA";

		if (numeUL.equals("BH10"))
			numeFil = "BIHOR";

		if (numeUL.equals("VN10"))
			numeFil = "VRANCEA";

		if (numeUL.equals("BU10"))
			numeFil = "BUC. GLINA";

		if (numeUL.equals("CJ10"))
			numeFil = "CLUJ";

		if (numeUL.equals("MM10"))
			numeFil = "MARAMURES";

		if (numeUL.equals("BU11"))
			numeFil = "BUC. MILITARI";

		if (numeUL.equals("CT10"))
			numeFil = "CONSTANTA";

		if (numeUL.equals("BV10"))
			numeFil = "BRASOV";

		if (numeUL.equals("BV90"))
			numeFil = "BRASOV CENTRAL";

		if (numeUL.equals("PH10"))
			numeFil = "PRAHOVA";

		if (numeUL.equals("NT10"))
			numeFil = "NEAMT";

		if (numeUL.equals("MS10"))
			numeFil = "MURES";

		if (numeUL.equals("IS10"))
			numeFil = "IASI";

		if (numeUL.equals("BU12"))
			numeFil = "BUC. OTOPENI";

		if (numeUL.equals("BU13"))
			numeFil = "BUC. ANDRONACHE";

		if (numeUL.equals("DJ10"))
			numeFil = "DOLJ";

		if (numeUL.equals("SB10"))
			numeFil = "SIBIU";

		if (numeUL.equals("SV10"))
			numeFil = "SUCEAVA";

		return numeFil;

	}

	public static String getDepartName(String departCode) {
		String retVal = "nedef";

		if (departCode.equals("01"))
			retVal = "Lemnoase";

		if (departCode.equals("02"))
			retVal = "Feronerie";

		if (departCode.equals("03"))
			retVal = "Parchet";

		if (departCode.equals("04"))
			retVal = "Mat.grele";

		if (departCode.equals("05"))
			retVal = "Electrice";

		if (departCode.equals("06"))
			retVal = "Gips";

		if (departCode.equals("07"))
			retVal = "Chimice";

		if (departCode.equals("08"))
			retVal = "Instalatii";

		if (departCode.equals("09"))
			retVal = "Hidroizolatii";

		return retVal;
	}

	public static String getNumeDepart(String codDepart) {
		String dpt = "NEDE";

		if (codDepart.equals("07"))
			dpt = "CHIM";

		if (codDepart.equals("10"))
			dpt = "DIVE";

		if (codDepart.equals("05"))
			dpt = "ELEC";

		if (codDepart.equals("02"))
			dpt = "FERO";

		if (codDepart.equals("06"))
			dpt = "GIPS";

		if (codDepart.equals("08"))
			dpt = "INST";

		if (codDepart.equals("01"))
			dpt = "LEMN";

		if (codDepart.equals("04"))
			dpt = "MATE";

		if (codDepart.equals("03"))
			dpt = "PARC";

		if (codDepart.equals("09"))
			dpt = "HIDR";

		return dpt;

	}

	public static String getClientGenericGedWood_faraFact(String filiala, String tipClient) {
		String codClient = "00";

		if (tipClient.equals("PF")) {
			if (filiala.equals("AG10"))
				codClient = "4119000320";

			else if (filiala.equals("BH10"))
				codClient = "4119000321";

			else if (filiala.equals("BV10"))
				codClient = "4119000322";

			else if (filiala.equals("CJ10"))
				codClient = "4119000323";

			else if (filiala.equals("CT10"))
				codClient = "4119000324";

			else if (filiala.equals("DJ10"))
				codClient = "4119000325";

			else if (filiala.equals("GL10"))
				codClient = "4119000326";

			else if (filiala.equals("IS10"))
				codClient = "4119000327";

			else if (filiala.equals("MM10"))
				codClient = "4119000412";

			else if (filiala.equals("BU10"))
				codClient = "4119000430";


		}

		return codClient;

	}

	public static String getClientGenericGed_CONSGED_faraFactura(String filiala, String tipClient) {
		String codClient = "00";

		if (tipClient.equals("PF")) {
			if (filiala.equals("AG10"))
				codClient = "4119000286";

			else if (filiala.equals("BH10"))
				codClient = "4119000290";

			else if (filiala.equals("CJ10"))
				codClient = "4119000295";

			else if (filiala.equals("CT10"))
				codClient = "4119000296";

			else if (filiala.equals("DJ10"))
				codClient = "4119000297";

			else if (filiala.equals("GL10"))
				codClient = "4119000298";

			else if (filiala.equals("BU10") && UserInfo.getInstance().getInitDivizie().equals("15"))
				codClient = "4119000435";

		}

		return codClient;
	}

	public static String getClientCVO_cuFact_faraCnp(String filiala, String tipClient) {

		String codClient = "00";

		if (filiala.equals("AG10"))
			codClient = "4119000386";

		else if (filiala.equals("BC10"))
			codClient = "4119000388";

		else if (filiala.equals("BH10"))
			codClient = "4119000390";

		else if (filiala.equals("BV10"))
			codClient = "4119000393";

		else if (filiala.equals("BZ10"))
			codClient = "4119000394";

		else if (filiala.equals("CJ10"))
			codClient = "4119000395";

		else if (filiala.equals("CT10"))
			codClient = "4119000396";

		else if (filiala.equals("DJ10"))
			codClient = "4119000397";

		else if (filiala.equals("GL10"))
			codClient = "4119000398";

		else if (filiala.equals("HD10"))
			codClient = "4119000399";

		else if (filiala.equals("IS10"))
			codClient = "4119000400";

		else if (filiala.equals("MM10"))
			codClient = "4119000401";

		else if (filiala.equals("MS10"))
			codClient = "4119000402";

		else if (filiala.equals("NT10"))
			codClient = "4119000403";

		else if (filiala.equals("PH10"))
			codClient = "4119000404";

		else if (filiala.equals("SB10"))
			codClient = "4119000405";

		else if (filiala.equals("TM10"))
			codClient = "4119000406";

		else if (filiala.equals("VN10"))
			codClient = "4119000407";

		else if (filiala.equals("BU13"))
			codClient = "4119000387";

		else if (filiala.equals("BU10"))
			codClient = "4119000389";

		else if (filiala.equals("BU11"))
			codClient = "4119000391";

		else if (filiala.equals("BU12"))
			codClient = "4119000392";

		else if (filiala.equals("SV10"))
			codClient = "4119001376";


		return codClient;

	}

	public static String getClientGed_FaraFactura(String filiala) {

		String codClient = "00";

		if (filiala.equals("AG10"))
			codClient = "4119000365";

		else if (filiala.equals("BC10"))
			codClient = "4119000363";

		else if (filiala.equals("BH10"))
			codClient = "4119000366";

		else if (filiala.equals("BV10"))
			codClient = "4119000372";

		else if (filiala.equals("BZ10"))
			codClient = "4119000374";

		else if (filiala.equals("CJ10"))
			codClient = "4119000375";

		else if (filiala.equals("CT10"))
			codClient = "4119000376";

		else if (filiala.equals("DJ10"))
			codClient = "4119000367";

		else if (filiala.equals("GL10"))
			codClient = "4119000373";

		else if (filiala.equals("HD10"))
			codClient = "4119000377";

		else if (filiala.equals("IS10"))
			codClient = "4119000380";

		else if (filiala.equals("MM10"))
			codClient = "4119000382";

		else if (filiala.equals("MS10"))
			codClient = "4119000383";

		else if (filiala.equals("NT10"))
			codClient = "4119000384";

		else if (filiala.equals("PH10"))
			codClient = "4119000385";

		else if (filiala.equals("SB10"))
			codClient = "4119000378";

		else if (filiala.equals("TM10"))
			codClient = "4119000381";

		else if (filiala.equals("VN10"))
			codClient = "4119000379";

		else if (filiala.equals("BU13"))
			codClient = "4119000362";

		else if (filiala.equals("BU10"))
			codClient = "4119000364";

		else if (filiala.equals("BU11"))
			codClient = "4119000370";

		else if (filiala.equals("BU12"))
			codClient = "4119000371";

		return codClient;

	}

	public static String getClientGenericGedWood(String filiala, String tipClient) {
		String codClient = "00";

		if (tipClient.equals("PF")) {
			if (filiala.equals("AG10"))
				codClient = "4119000225";

			else if (filiala.equals("BH10"))
				codClient = "4119000210";

			else if (filiala.equals("BV10"))
				codClient = "4119000201";

			else if (filiala.equals("CJ10"))
				codClient = "4119000179";

			else if (filiala.equals("CT10"))
				codClient = "4119000216";

			else if (filiala.equals("DJ10"))
				codClient = "4119000247";

			else if (filiala.equals("GL10"))
				codClient = "4119000175";

			else if (filiala.equals("IS10"))
				codClient = "4119000195";

			else if (filiala.equals("TM10"))
				codClient = "4119000251";

			else if (filiala.equals("MM10"))
				codClient = "4119000357";

			else if (filiala.equals("BU10"))
				codClient = "4119000431";

			else if (filiala.equals("MS10"))
				codClient = "4119001146";

		}

		if (tipClient.equals("PJ")) {
			if (filiala.equals("AG10"))
				codClient = "4119000226";

			else if (filiala.equals("BH10"))
				codClient = "4119000211";

			else if (filiala.equals("BV10"))
				codClient = "4119000202";

			else if (filiala.equals("CJ10"))
				codClient = "4119000180";

			else if (filiala.equals("CT10"))
				codClient = "4119000217";

			else if (filiala.equals("DJ10"))
				codClient = "4119000248";

			else if (filiala.equals("GL10"))
				codClient = "4119000176";

			else if (filiala.equals("IS10"))
				codClient = "4119000317";

			else if (filiala.equals("TM10"))
				codClient = "4119000252";

			else if (filiala.equals("MM10"))
				codClient = "4119000358";

			else if (filiala.equals("BU10"))
				codClient = "4119000432";

			else if (filiala.equals("MS10"))
				codClient = "4119001147";

		}

		return codClient;
	}

	public static String getClientGenericGed(String filiala, String tipClient) {
		String codClient = "00";

		if (tipClient.equals("PF")) {
			if (filiala.equals("AG10"))
				codClient = "4119000023";

			else if (filiala.equals("BC10"))
				codClient = "4119000024";

			else if (filiala.equals("BH10"))
				codClient = "4119000025";

			else if (filiala.equals("BV10"))
				codClient = "4119000030";

			else if (filiala.equals("BZ10"))
				codClient = "4119000255";

			else if (filiala.equals("CJ10"))
				codClient = "4119000031";

			else if (filiala.equals("CT10"))
				codClient = "4119000080";

			else if (filiala.equals("DJ10"))
				codClient = "4119000032";

			else if (filiala.equals("GL10"))
				codClient = "4119000033";

			else if (filiala.equals("HD10"))
				codClient = "4119000260";

			else if (filiala.equals("IS10"))
				codClient = "4119000034";

			else if (filiala.equals("MM10"))
				codClient = "4119000035";

			else if (filiala.equals("MS10"))
				codClient = "4119000036";

			else if (filiala.equals("NT10"))
				codClient = "4119000037";

			else if (filiala.equals("PH10"))
				codClient = "4119000038";

			else if (filiala.equals("TM10"))
				codClient = "4119000039";

			else if (filiala.equals("VN10"))
				codClient = "4119000040";

			else if (filiala.equals("BU13"))
				codClient = "4119000029";

			else if (filiala.equals("BU10")) {
				if (!UserInfo.getInstance().getInitDivizie().equals("15"))
					codClient = "4119000028";
				else if (UserInfo.getInstance().getInitDivizie().equals("15"))
					codClient = "4119000436";
			}

			else if (filiala.equals("BU11"))
				codClient = "4119000027";

			else if (filiala.equals("BU12"))
				codClient = "4119000026";

			else if (filiala.equals("SB10"))
				codClient = "4119000360";
			else if (filiala.equals("SV10"))
				codClient = "4119001377";

		}

		if (tipClient.equals("PJ")) {
			if (filiala.equals("AG10"))
				codClient = "4119000059";

			else if (filiala.equals("BC10"))
				codClient = "4119000060";

			else if (filiala.equals("BH10"))
				codClient = "4119000061";

			else if (filiala.equals("BV10"))
				codClient = "4119000066";

			else if (filiala.equals("BZ10"))
				codClient = "4119000256";

			else if (filiala.equals("CJ10"))
				codClient = "4119000067";

			else if (filiala.equals("CT10"))
				codClient = "4119000082";

			else if (filiala.equals("DJ10"))
				codClient = "4119000068";

			else if (filiala.equals("GL10"))
				codClient = "4119000069";

			else if (filiala.equals("HD10"))
				codClient = "4119000261";

			else if (filiala.equals("IS10"))
				codClient = "4119000070";

			else if (filiala.equals("MM10"))
				codClient = "4119000071";

			else if (filiala.equals("MS10"))
				codClient = "4119000072";

			else if (filiala.equals("NT10"))
				codClient = "4119000073";

			else if (filiala.equals("PH10"))
				codClient = "4119000074";

			else if (filiala.equals("TM10"))
				codClient = "4119000075";

			else if (filiala.equals("VN10"))
				codClient = "4119000076";

			else if (filiala.equals("BU13"))
				codClient = "4119000065";

			else if (filiala.equals("BU10")) {
				if (!UserInfo.getInstance().getInitDivizie().equals("15"))
					codClient = "4119000064";
				else if (UserInfo.getInstance().getInitDivizie().equals("15"))
					codClient = "4119000437";
			} else if (filiala.equals("BU11"))
				codClient = "4119000063";

			else if (filiala.equals("BU12"))
				codClient = "4119000062";

			else if (filiala.equals("SB10"))
				codClient = "4119000410";

			else if (filiala.equals("SV10"))
				codClient = "4119001377";

		}

		return codClient;
	}

	public static String gedPJNeplatitorTVA(String filiala) {
		String codClient = "00";

		if (filiala.equals("AG10"))
			codClient = "4119000265";

		else if (filiala.equals("BC10"))
			codClient = "4119000267";

		else if (filiala.equals("BH10"))
			codClient = "4119000269";

		else if (filiala.equals("BV10"))
			codClient = "4119000272";

		else if (filiala.equals("BZ10"))
			codClient = "4119000273";

		else if (filiala.equals("CJ10"))
			codClient = "4119000274";

		else if (filiala.equals("CT10"))
			codClient = "4119000275";

		else if (filiala.equals("DJ10"))
			codClient = "4119000276";

		else if (filiala.equals("GL10"))
			codClient = "4119000277";

		else if (filiala.equals("HD10"))
			codClient = "4119000278";

		else if (filiala.equals("IS10"))
			codClient = "4119000279";

		else if (filiala.equals("MM10"))
			codClient = "4119000280";

		else if (filiala.equals("MS10"))
			codClient = "4119000281";

		else if (filiala.equals("NT10"))
			codClient = "4119000282";

		else if (filiala.equals("PH10"))
			codClient = "4119000283";

		else if (filiala.equals("TM10"))
			codClient = "4119000284";

		else if (filiala.equals("VN10"))
			codClient = "4119000285";

		else if (filiala.equals("BU13"))
			codClient = "4119000266";

		else if (filiala.equals("BU10"))
		{
			if (!UserInfo.getInstance().getInitDivizie().equals("15"))
				codClient = "4119000268";
			else if (UserInfo.getInstance().getInitDivizie().equals("15"))
				codClient = "4119000440";
		}

		else if (filiala.equals("BU11"))
			codClient = "4119000270";

		else if (filiala.equals("BU12"))
			codClient = "4119000271";

		else if (filiala.equals("SB10"))
			codClient = "4119000411";

		else if (filiala.equals("SV10"))
			codClient = "4119001383";

		return codClient;

	}

	public static String getClientGenericGed_CONSGED(String filiala, String tipClient) {
		String codClient = "00";

		if (tipClient.equals("PF")) {
			if (filiala.equals("AG10"))
				codClient = "4119000307";

			if (filiala.equals("BH10"))
				codClient = "4119000240";

			if (filiala.equals("CJ10"))
				codClient = "4119000235";

			if (filiala.equals("CT10"))
				codClient = "4119000173";

			if (filiala.equals("DJ10"))
				codClient = "4119000192";

			if (filiala.equals("GL10"))
				codClient = "4119000155";

		}

		if (tipClient.equals("PJ")) {
			if (filiala.equals("AG10"))
				codClient = "4119000308";

			if (filiala.equals("BH10"))
				codClient = "4119000241";

			if (filiala.equals("CJ10"))
				codClient = "4119000236";

			if (filiala.equals("CT10"))
				codClient = "4119000172";

			if (filiala.equals("DJ10"))
				codClient = "4119000193";

			if (filiala.equals("GL10"))
				codClient = "4119000156";

		}

		return codClient;
	}

	public static String gedPJNeplatitorTVA_CONSGED(String filiala) {
		String codClient = "00";

		if (filiala.equals("AG10"))
			codClient = "4119000330";

		else if (filiala.equals("BH10"))
			codClient = "4119000337";

		else if (filiala.equals("CJ10"))
			codClient = "4119000342";

		else if (filiala.equals("CT10"))
			codClient = "4119000343";

		else if (filiala.equals("DJ10"))
			codClient = "4119000344";

		else if (filiala.equals("GL10"))
			codClient = "4119000345";

		else if (filiala.equals("BU10"))
			codClient = "4119000433";

		return codClient;

	}


	public static String getTipUser(String tipUser) {
		String tipAcces = "NN";

		if (UserInfo.getInstance().getTipAcces().equals("9")) {
			tipAcces = "AV";
		}

		// SEFI DE DEPARTAMENT
		else if (UserInfo.getInstance().getTipAcces().equals("10")) {
			tipAcces = "SD";
		}

		// DIRECTORI DE VANZARI, DEPARTAMENT
		else if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")) {
			tipAcces = "DV";
		}

		// KEY ACCOUNTI
		else if (UserInfo.getInstance().getTipAcces().equals("27")) {
			tipAcces = "KA";
		}

		// DIRECTOR KA
		else if (UserInfo.getInstance().getTipAcces().equals("35")) {
			tipAcces = "DK";
		}

		// SD KA
		else if (UserInfo.getInstance().getTipAcces().equals("32")) {
			tipAcces = "SK";
		}

		// CONSILIERI
		else if (UserInfo.getInstance().getTipAcces().equals("17")) {
			tipAcces = "CV";
		}

		// SEFI DE MAGAZIN
		else if (UserInfo.getInstance().getTipAcces().equals("18")) {
			tipAcces = "SM";
		}

		// Consilier Vanzari Retail
		else if (UserInfo.getInstance().getTipAcces().equals("41")) {
			tipAcces = "CVR";
		}

		// Sef magazin Retail
		else if (UserInfo.getInstance().getTipAcces().equals("44")) {
			tipAcces = "SMR";
		}

		// SD CONS
		else if (UserInfo.getInstance().getTipAcces().equals("39")) {
			tipAcces = "SC";
		}

		else if (UserInfo.getInstance().getTipAcces().equals("51")) {
			tipAcces = "SSCM";
		}

		else if (UserInfo.getInstance().getTipAcces().equals("50")) {
			tipAcces = "CGED";
		}

		else if (UserInfo.getInstance().getTipAcces().equals("62")) {
			tipAcces = "OIVPD";
		}

		return tipAcces;
	}

	public static String getTipPlata(String codPlata) {

		String strPlata = "";

		if (codPlata.equals("B"))
			strPlata = "Bilet la ordin";

		if (codPlata.equals("C"))
			strPlata = "Cec";

		if (codPlata.equals("E"))
			strPlata = "Plata in numerar";

		if (codPlata.equals("O"))
			strPlata = "Ordin de plata";

		if (codPlata.equals("E1"))
			strPlata = "Numerar la sofer";

		return strPlata;

	}

	public static String getTipTransport(String codTrasnport) {

		String strTransport = "";

		if (codTrasnport.equals("TRAP"))
			strTransport = "Arabesque";

		if (codTrasnport.equals("TCLI"))
			strTransport = "Client";

		if (codTrasnport.equals("TFRN"))
			strTransport = "Furnizor";

		if (codTrasnport.equals("TERT"))
			strTransport = "Curier";

		return strTransport;

	}

	public static String getTipAprobare(String codAprobare) {

		String strAprobare = "";

		if (codAprobare.toUpperCase(Locale.getDefault()).equals("X"))
			strAprobare = "Da";
		else
			strAprobare = "Nu";

		return strAprobare;

	}

	public static String getDistribUnitLog(String gedUnitLog) {
		return gedUnitLog.substring(0, 2) + "1" + gedUnitLog.subSequence(3, 4);
	}

	public static boolean isMatTransport(String codArt) {
		boolean isMat = false;

		String[] articoleTransp = { "000000000030101220", "000000000030101221", "000000000030101223", "000000000030101224", "000000000030101225",
				"000000000030101226", "000000000030101227", "000000000030101228", "000000000030101230", "000000000030101222", "000000000030101111",
				"000000000030101240" };

		for (int i = 0; i < articoleTransp.length; i++) {
			if (articoleTransp[i].equals(codArt)) {
				isMat = true;
				break;
			}
		}

		return isMat;
	}

	public static boolean isMatTransportNume(String numeArt) {

		return numeArt.toLowerCase().contains("transport");

	}

}
