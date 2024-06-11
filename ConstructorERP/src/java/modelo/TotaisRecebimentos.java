/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 * Classe modelo para a criação do objeto bloco, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class TotaisRecebimentos implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;
    @Column
    private double aReceber1 = 0;
    @Column
    private double aReceber2 = 0;
    @Column
    private double aReceber3 = 0;
    @Column
    private double aReceber4 = 0;
    @Column
    private double aReceber5 = 0;
    @Column
    private double aReceber6 = 0;
    @Column
    private double aReceber7 = 0;
    @Column
    private double aReceber8 = 0;
    @Column
    private double aReceber9 = 0;
    @Column
    private double aReceber10 = 0;
    @Column
    private double aReceber11 = 0;
    @Column
    private double aReceber12 = 0;
    @Column
    private double aReceber13 = 0;
    @Column
    private double aReceber14 = 0;
    @Column
    private double aReceber15 = 0;
    @Column
    private double aReceber16 = 0;
    @Column
    private double aReceber17 = 0;
    @Column
    private double aReceber18 = 0;
    @Column
    private double aReceber19 = 0;
    @Column
    private double aReceber20 = 0;
    @Column
    private double aReceber21 = 0;
    @Column
    private double aReceber22 = 0;
    @Column
    private double aReceber23 = 0;
    @Column
    private double aReceber24 = 0;
    @Column
    private double aReceber25 = 0;
    @Column
    private double aReceber26 = 0;
    @Column
    private double aReceber27 = 0;
    @Column
    private double aReceber28 = 0;
    @Column
    private double aReceber29 = 0;
    @Column
    private double aReceber30 = 0;
    @Column
    private double aReceber31 = 0;
    @Column
    private double recebido1 = 0;
    @Column
    private double recebido2 = 0;
    @Column
    private double recebido3 = 0;
    @Column
    private double recebido4 = 0;
    @Column
    private double recebido5 = 0;
    @Column
    private double recebido6 = 0;
    @Column
    private double recebido7 = 0;
    @Column
    private double recebido8 = 0;
    @Column
    private double recebido9 = 0;
    @Column
    private double recebido10 = 0;
    @Column
    private double recebido11 = 0;
    @Column
    private double recebido12 = 0;
    @Column
    private double recebido13 = 0;
    @Column
    private double recebido14 = 0;
    @Column
    private double recebido15 = 0;
    @Column
    private double recebido16 = 0;
    @Column
    private double recebido17 = 0;
    @Column
    private double recebido18 = 0;
    @Column
    private double recebido19 = 0;
    @Column
    private double recebido20 = 0;
    @Column
    private double recebido21 = 0;
    @Column
    private double recebido22 = 0;
    @Column
    private double recebido23 = 0;
    @Column
    private double recebido24 = 0;
    @Column
    private double recebido25 = 0;
    @Column
    private double recebido26 = 0;
    @Column
    private double recebido27 = 0;
    @Column
    private double recebido28 = 0;
    @Column
    private double recebido29 = 0;
    @Column
    private double recebido30 = 0;
    @Column
    private double recebido31 = 0;

    public TotaisRecebimentos() {
    }

    
    
    public TotaisRecebimentos(Date dataInicio, double[] aReceber, double[] recebido) {
        this.dataInicio = dataInicio;
        this.setRecebido(recebido);
        this.setaReceber(aReceber);
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public double[] getaReceber() {
        double aReceber[] = {aReceber1, aReceber2, aReceber3, aReceber4, aReceber5, aReceber6, aReceber7, aReceber8, aReceber9, aReceber10, aReceber11, aReceber12, aReceber13, aReceber14, aReceber15, aReceber16, aReceber17, aReceber18, aReceber19, aReceber20, aReceber21, aReceber22, aReceber23, aReceber24, aReceber25, aReceber26, aReceber27, aReceber28, aReceber29, aReceber30, aReceber31};
        return aReceber;
    }

    public void setaReceber(double[] aReceber) {
        if (aReceber.length == 31) {
            this.aReceber1 = aReceber[0];
            this.aReceber2 = aReceber[1];
            this.aReceber3 = aReceber[2];
            this.aReceber3 = aReceber[3];
            this.aReceber5 = aReceber[4];
            this.aReceber6 = aReceber[5];
            this.aReceber6 = aReceber[6];
            this.aReceber8 = aReceber[7];
            this.aReceber9 = aReceber[8];
            this.aReceber10 = aReceber[9];
            this.aReceber11 = aReceber[10];
            this.aReceber12 = aReceber[11];
            this.aReceber13 = aReceber[12];
            this.aReceber14 = aReceber[13];
            this.aReceber15 = aReceber[14];
            this.aReceber16 = aReceber[15];
            this.aReceber17 = aReceber[16];
            this.aReceber18 = aReceber[17];
            this.aReceber19 = aReceber[18];
            this.aReceber20 = aReceber[19];
            this.aReceber21 = aReceber[20];
            this.aReceber22 = aReceber[21];
            this.aReceber23 = aReceber[22];
            this.aReceber24 = aReceber[23];
            this.aReceber25 = aReceber[24];
            this.aReceber26 = aReceber[25];
            this.aReceber27 = aReceber[26];
            this.aReceber28 = aReceber[27];
            this.aReceber29 = aReceber[28];
            this.aReceber30 = aReceber[29];
            this.aReceber31 = aReceber[30];

        }
    }

    public double[] getRecebido() {
        double recebido[] = {recebido1, recebido2, recebido3, recebido4, recebido5, recebido6, recebido7, recebido8, recebido9, recebido10, recebido11, recebido12, recebido13, recebido14, recebido15, recebido16, recebido17, recebido18, recebido19, recebido20, recebido21, recebido22, recebido23, recebido24, recebido25, recebido26, recebido27, recebido28, recebido29, recebido30, recebido31};
        return recebido;
    }

    public void setRecebido(double[] recebido) {
        if (recebido.length == 31) {
            this.recebido1 = recebido[0];
            this.recebido2 = recebido[1];
            this.recebido3 = recebido[2];
            this.recebido3 = recebido[3];
            this.recebido5 = recebido[4];
            this.recebido6 = recebido[5];
            this.recebido6 = recebido[6];
            this.recebido8 = recebido[7];
            this.recebido9 = recebido[8];
            this.recebido10 = recebido[9];
            this.recebido11 = recebido[10];
            this.recebido12 = recebido[11];
            this.recebido13 = recebido[12];
            this.recebido14 = recebido[13];
            this.recebido15 = recebido[14];
            this.recebido16 = recebido[15];
            this.recebido17 = recebido[16];
            this.recebido18 = recebido[17];
            this.recebido19 = recebido[18];
            this.recebido20 = recebido[19];
            this.recebido21 = recebido[20];
            this.recebido22 = recebido[21];
            this.recebido23 = recebido[22];
            this.recebido24 = recebido[23];
            this.recebido25 = recebido[24];
            this.recebido26 = recebido[25];
            this.recebido27 = recebido[26];
            this.recebido28 = recebido[27];
            this.recebido29 = recebido[28];
            this.recebido30 = recebido[29];
            this.recebido31 = recebido[30];

        }
    }

    public double getaReceber1() {
        return aReceber1;
    }

    public void setaReceber1(double aReceber1) {
        this.aReceber1 = aReceber1;
    }

    public double getaReceber2() {
        return aReceber2;
    }

    public void setaReceber2(double aReceber2) {
        this.aReceber2 = aReceber2;
    }

    public double getaReceber3() {
        return aReceber3;
    }

    public void setaReceber3(double aReceber3) {
        this.aReceber3 = aReceber3;
    }

    public double getaReceber4() {
        return aReceber4;
    }

    public void setaReceber4(double aReceber4) {
        this.aReceber4 = aReceber4;
    }

    public double getaReceber5() {
        return aReceber5;
    }

    public void setaReceber5(double aReceber5) {
        this.aReceber5 = aReceber5;
    }

    public double getaReceber6() {
        return aReceber6;
    }

    public void setaReceber6(double aReceber6) {
        this.aReceber6 = aReceber6;
    }

    public double getaReceber7() {
        return aReceber7;
    }

    public void setaReceber7(double aReceber7) {
        this.aReceber7 = aReceber7;
    }

    public double getaReceber8() {
        return aReceber8;
    }

    public void setaReceber8(double aReceber8) {
        this.aReceber8 = aReceber8;
    }

    public double getaReceber9() {
        return aReceber9;
    }

    public void setaReceber9(double aReceber9) {
        this.aReceber9 = aReceber9;
    }

    public double getaReceber10() {
        return aReceber10;
    }

    public void setaReceber10(double aReceber10) {
        this.aReceber10 = aReceber10;
    }

    public double getaReceber11() {
        return aReceber11;
    }

    public void setaReceber11(double aReceber11) {
        this.aReceber11 = aReceber11;
    }

    public double getaReceber12() {
        return aReceber12;
    }

    public void setaReceber12(double aReceber12) {
        this.aReceber12 = aReceber12;
    }

    public double getaReceber13() {
        return aReceber13;
    }

    public void setaReceber13(double aReceber13) {
        this.aReceber13 = aReceber13;
    }

    public double getaReceber14() {
        return aReceber14;
    }

    public void setaReceber14(double aReceber14) {
        this.aReceber14 = aReceber14;
    }

    public double getaReceber15() {
        return aReceber15;
    }

    public void setaReceber15(double aReceber15) {
        this.aReceber15 = aReceber15;
    }

    public double getaReceber16() {
        return aReceber16;
    }

    public void setaReceber16(double aReceber16) {
        this.aReceber16 = aReceber16;
    }

    public double getaReceber17() {
        return aReceber17;
    }

    public void setaReceber17(double aReceber17) {
        this.aReceber17 = aReceber17;
    }

    public double getaReceber18() {
        return aReceber18;
    }

    public void setaReceber18(double aReceber18) {
        this.aReceber18 = aReceber18;
    }

    public double getaReceber19() {
        return aReceber19;
    }

    public void setaReceber19(double aReceber19) {
        this.aReceber19 = aReceber19;
    }

    public double getaReceber20() {
        return aReceber20;
    }

    public void setaReceber20(double aReceber20) {
        this.aReceber20 = aReceber20;
    }

    public double getaReceber21() {
        return aReceber21;
    }

    public void setaReceber21(double aReceber21) {
        this.aReceber21 = aReceber21;
    }

    public double getaReceber22() {
        return aReceber22;
    }

    public void setaReceber22(double aReceber22) {
        this.aReceber22 = aReceber22;
    }

    public double getaReceber23() {
        return aReceber23;
    }

    public void setaReceber23(double aReceber23) {
        this.aReceber23 = aReceber23;
    }

    public double getaReceber24() {
        return aReceber24;
    }

    public void setaReceber24(double aReceber24) {
        this.aReceber24 = aReceber24;
    }

    public double getaReceber25() {
        return aReceber25;
    }

    public void setaReceber25(double aReceber25) {
        this.aReceber25 = aReceber25;
    }

    public double getaReceber26() {
        return aReceber26;
    }

    public void setaReceber26(double aReceber26) {
        this.aReceber26 = aReceber26;
    }

    public double getaReceber27() {
        return aReceber27;
    }

    public void setaReceber27(double aReceber27) {
        this.aReceber27 = aReceber27;
    }

    public double getaReceber28() {
        return aReceber28;
    }

    public void setaReceber28(double aReceber28) {
        this.aReceber28 = aReceber28;
    }

    public double getaReceber29() {
        return aReceber29;
    }

    public void setaReceber29(double aReceber29) {
        this.aReceber29 = aReceber29;
    }

    public double getaReceber30() {
        return aReceber30;
    }

    public void setaReceber30(double aReceber30) {
        this.aReceber30 = aReceber30;
    }

    public double getaReceber31() {
        return aReceber31;
    }

    public void setaReceber31(double aReceber31) {
        this.aReceber31 = aReceber31;
    }

    public double getRecebido1() {
        return recebido1;
    }

    public void setRecebido1(double recebido1) {
        this.recebido1 = recebido1;
    }

    public double getRecebido2() {
        return recebido2;
    }

    public void setRecebido2(double recebido2) {
        this.recebido2 = recebido2;
    }

    public double getRecebido3() {
        return recebido3;
    }

    public void setRecebido3(double recebido3) {
        this.recebido3 = recebido3;
    }

    public double getRecebido4() {
        return recebido4;
    }

    public void setRecebido4(double recebido4) {
        this.recebido4 = recebido4;
    }

    public double getRecebido5() {
        return recebido5;
    }

    public void setRecebido5(double recebido5) {
        this.recebido5 = recebido5;
    }

    public double getRecebido6() {
        return recebido6;
    }

    public void setRecebido6(double recebido6) {
        this.recebido6 = recebido6;
    }

    public double getRecebido7() {
        return recebido7;
    }

    public void setRecebido7(double recebido7) {
        this.recebido7 = recebido7;
    }

    public double getRecebido8() {
        return recebido8;
    }

    public void setRecebido8(double recebido8) {
        this.recebido8 = recebido8;
    }

    public double getRecebido9() {
        return recebido9;
    }

    public void setRecebido9(double recebido9) {
        this.recebido9 = recebido9;
    }

    public double getRecebido10() {
        return recebido10;
    }

    public void setRecebido10(double recebido10) {
        this.recebido10 = recebido10;
    }

    public double getRecebido11() {
        return recebido11;
    }

    public void setRecebido11(double recebido11) {
        this.recebido11 = recebido11;
    }

    public double getRecebido12() {
        return recebido12;
    }

    public void setRecebido12(double recebido12) {
        this.recebido12 = recebido12;
    }

    public double getRecebido13() {
        return recebido13;
    }

    public void setRecebido13(double recebido13) {
        this.recebido13 = recebido13;
    }

    public double getRecebido14() {
        return recebido14;
    }

    public void setRecebido14(double recebido14) {
        this.recebido14 = recebido14;
    }

    public double getRecebido15() {
        return recebido15;
    }

    public void setRecebido15(double recebido15) {
        this.recebido15 = recebido15;
    }

    public double getRecebido16() {
        return recebido16;
    }

    public void setRecebido16(double recebido16) {
        this.recebido16 = recebido16;
    }

    public double getRecebido17() {
        return recebido17;
    }

    public void setRecebido17(double recebido17) {
        this.recebido17 = recebido17;
    }

    public double getRecebido18() {
        return recebido18;
    }

    public void setRecebido18(double recebido18) {
        this.recebido18 = recebido18;
    }

    public double getRecebido19() {
        return recebido19;
    }

    public void setRecebido19(double recebido19) {
        this.recebido19 = recebido19;
    }

    public double getRecebido20() {
        return recebido20;
    }

    public void setRecebido20(double recebido20) {
        this.recebido20 = recebido20;
    }

    public double getRecebido21() {
        return recebido21;
    }

    public void setRecebido21(double recebido21) {
        this.recebido21 = recebido21;
    }

    public double getRecebido22() {
        return recebido22;
    }

    public void setRecebido22(double recebido22) {
        this.recebido22 = recebido22;
    }

    public double getRecebido23() {
        return recebido23;
    }

    public void setRecebido23(double recebido23) {
        this.recebido23 = recebido23;
    }

    public double getRecebido24() {
        return recebido24;
    }

    public void setRecebido24(double recebido24) {
        this.recebido24 = recebido24;
    }

    public double getRecebido25() {
        return recebido25;
    }

    public void setRecebido25(double recebido25) {
        this.recebido25 = recebido25;
    }

    public double getRecebido26() {
        return recebido26;
    }

    public void setRecebido26(double recebido26) {
        this.recebido26 = recebido26;
    }

    public double getRecebido27() {
        return recebido27;
    }

    public void setRecebido27(double recebido27) {
        this.recebido27 = recebido27;
    }

    public double getRecebido28() {
        return recebido28;
    }

    public void setRecebido28(double recebido28) {
        this.recebido28 = recebido28;
    }

    public double getRecebido29() {
        return recebido29;
    }

    public void setRecebido29(double recebido29) {
        this.recebido29 = recebido29;
    }

    public double getRecebido30() {
        return recebido30;
    }

    public void setRecebido30(double recebido30) {
        this.recebido30 = recebido30;
    }

    public double getRecebido31() {
        return recebido31;
    }

    public void setRecebido31(double recebido31) {
        this.recebido31 = recebido31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TotaisRecebimentos other = (TotaisRecebimentos) obj;
        if (id == 0) {
            if (other.id != 0) {
                return false;
            }
        } else if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        return hash;
    }
}
