/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author daniel.botke
 */
public class DateUtils {

    /**
     * Retorna a mesta data, ou a mais próxima existente, no mês seguinte.
     *
     * @param dt
     */
    public static Date proximoMes(Date dt) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dt);
        gc.add(Calendar.MONTH, 1);

        return gc.getTime();
    }

    public static Date formatedDate(Date dt) {
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getDefault());
        gc.setTime(dt);
        return gc.getTime();
    }

    /**
     * Retorna data passada como parâmetro com mais a quantidade de dias por
     * parâmetro
     */
    public static Date adicionarDias(Date data, int qntDias) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        gc.add(Calendar.DAY_OF_MONTH, qntDias);
        return gc.getTime();
    }

    /**
     * Retorna a data da primeira ocorrência do dia da semana passado por
     * parâmetro no mês da data passada por parâmetro
     */
    public static Date primeiroDiaSemana(Date data, int diaSemana) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        while (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            gc.roll(Calendar.DAY_OF_MONTH, 1);
        }
        return gc.getTime();
    }

    /**
     * Retorna a data do primeiro dia útil do mês da data passada por parâmetro
     */
    public static Date primeiroDiaUtilMes(Date data) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        while (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            gc.roll(Calendar.DAY_OF_MONTH, 1);
        }
        return gc.getTime();
    }

    /**
     * Retorna a data do último dia útil do mês da data passada por parâmetro
     */
    public static Date ultimoDiaMes(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return calendar.getTime();
    }
    
     public static int diferencaEmDias(Date dataInicial, Date dataFinal){  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        double diferencaEmDias = (diferenca /1000) / 60 / 60 /24; //resultado é diferença entre as datas em dias  
        return (int)diferencaEmDias;  
    }  
}
