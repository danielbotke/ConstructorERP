/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import modelo.Predio;

/**
 * Classe modelo para a criação do objeto historico de ganho financeiro, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class HistoricoGanhoFinanceiro implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date mesAno;
    @OneToOne(fetch = FetchType.EAGER  )
    @MapsId
    private Predio predio;
    @Column(nullable = false)
    private double valorMes;
    @Column(nullable = false)
    private double valorAtualizado;
    @Column(nullable = false)
    private double ganhoFinanceiro;
    @Column(nullable = false)
    private double percentual;
    
    private double totalAno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getMesAno() {
        return mesAno;
    }

    public void setMesAno(Date mesAno) {
        this.mesAno = mesAno;
    }

    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public double getValorMes() {
        return valorMes;
    }

    public void setValorMes(double valorMes) {
        this.valorMes = valorMes;
    }

    public double getValorAtualizado() {
        return valorAtualizado;
    }

    public void setValorAtualizado(double valorAtualizado) {
        this.valorAtualizado = valorAtualizado;
    }

    public double getGanhoFinanceiro() {
        return ganhoFinanceiro;
    }

    public void setGanhoFinanceiro(double ganhoFinanceiro) {
        this.ganhoFinanceiro = ganhoFinanceiro;
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }

    public double getTotalAno() {
        return totalAno;
    }

    public void setTotalAno(double totalAno) {
        this.totalAno = totalAno;
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
        HistoricoGanhoFinanceiro other = (HistoricoGanhoFinanceiro) obj;
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
