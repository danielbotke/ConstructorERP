/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.StringTokenizer;

/**
 *
 * @author daniel.botke
 */
public class StringUtils {

    /**
     * Substitui os caracteres reservados que podem gerar erro no html pelo
     * respectivo código.
     *
     * @param str
     * @return A String passada como parâmetro com os caracteres tratados.
     */
    public static String tratadorCaracteres(String str) {
        str = str.replaceAll("\\|", "\r\n");
        /**
         * alteração da 586: str = str.replaceAll("\\|", "<br/>"); Pula a linha
         * sem que seja necessário incluir <br/> no 0800
         *
         */
        str = str.replaceAll("–", "-");
        str = str.replaceAll("—", "-");
        str = str.replaceAll("-", "-");
        str = str.replaceAll("–", "-");
        str = str.replaceAll("&#8211;", "-");

        str = str.replace("|", "&brvbar;");

        str = tratadorAspas(str);

        return str;
    }
    
    public static String removerPipe(String str) {
        str = str.replace("|", "e");
        return str;
    }

    public static String tratadorAspas(String str) {


        str = str.replaceAll("“", "&quot;");
        str = str.replaceAll("”", "&quot;");
        str = str.replaceAll("\"", "&quot;");

        return str;
    }

    public static String tratadorRadicalVersoes(String str) {
        String versao = "";
        char aux[] = str.toCharArray();
        if (aux[0] == 'V') {
            for (int i = 0; i < aux.length; i++) {
                if (Character.isDigit(aux[i])) {
                    versao += aux[i];
                } else {
                    if (aux[i] == '_') {
                        i = aux.length;
                    }
                }
            }
        } else {
            StringTokenizer verToken = new StringTokenizer(str, ".");
            if (verToken.countTokens() > 1) {
                for (int i = 0; i < 3; i++) {
                    if (verToken.hasMoreTokens()) {
                        versao += verToken.nextToken();
                    }
                }
            } else {
                verToken = new StringTokenizer(str, "-");
                if (verToken.countTokens() > 1) {
                    for (int i = 0; i < 3; i++) {
                        if (verToken.hasMoreTokens()) {
                            versao += verToken.nextToken();
                        }
                    }
                }
            }
        }
        return versao;
    }

    public static String tratadorVersaoFechadaGP(String str) {
        if (str.charAt(0) != 'V') {
            str = str.replace('-', '.');
        }
        return str;
    }

    public static String tratadotCaractereEspeciais(String str) {
        str = str.replaceAll("ç", "c");
        str = str.replaceAll("ã", "a");
        str = str.replaceAll("â", "a");
        str = str.replaceAll("á", "a");
        str = str.replaceAll("à", "a");
        str = str.replaceAll("é", "e");
        str = str.replaceAll("ê", "e");
        str = str.replaceAll("í", "i");
        str = str.replaceAll("ó", "o");
        str = str.replaceAll("õ", "o");
        str = str.replaceAll("ú", "u");
        str = str.replaceAll("|", "");
        return str;
    }

    public static String substituicaoCaractereEspeciaisInverse(String str) {
        str = str.replaceAll("ç", "00");
        str = str.replaceAll("ã", "01");
        str = str.replaceAll("â", "02");
        str = str.replaceAll("á", "03");
        str = str.replaceAll("à", "04");
        str = str.replaceAll("é", "05");
        str = str.replaceAll("ê", "06");
        str = str.replaceAll("í", "07");
        str = str.replaceAll("ó", "08");
        str = str.replaceAll("õ", "09");
        str = str.replaceAll("ú", "10");
        return str;
    }

    public static String tratadotCaractereEspeciaisHTMLInverse(String str) {
        str = str.replaceAll("00", "ç");
        str = str.replaceAll("01", "ã");
        str = str.replaceAll("02", "â");
        str = str.replaceAll("03", "á");
        str = str.replaceAll("04", "à");
        str = str.replaceAll("05", "é");
        str = str.replaceAll("06", "ê");
        str = str.replaceAll("07", "í");
        str = str.replaceAll("08", "ó");
        str = str.replaceAll("09", "õ");
        str = str.replaceAll("10", "ú");
        return str;
    }

    public static String removerMascara(String str) {
        str = str.replaceAll("\\.", "");
        str = str.replaceAll("-", "");
        return str;
    }

    public static String numberToLeter(int i) {
        switch (i) {
            case 1:
                return "a";
            case 2:
                return "b";
            case 3:
                return "c";
            case 4:
                return "d";
            case 5:
                return "e";
            case 6:
                return "f";
            case 7:
                return "g";
            case 8:
                return "h";
            case 9:
                return "i";
            case 10:
                return "j";
            case 11:
                return "k";
            case 12:
                return "l";
            case 13:
                return "m";
            case 14:
                return "n";
            case 15:
                return "o";
            case 16:
                return "p";
            case 17:
                return "q";
            case 18:
                return "r";
            case 19:
                return "s";
            case 20:
                return "t";
            case 21:
                return "u";
            case 22:
                return "v";
            case 23:
                return "w";
            case 24:
                return "x";
            case 25:
                return "y";
            case 26:
                return "z";
            default:
                return "";
        }
    }
}
