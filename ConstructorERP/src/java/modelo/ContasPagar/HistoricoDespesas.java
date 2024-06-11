/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

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
import modelo.Bloco;

/**
 * Classe modelo para a criação do objeto historico de despesa, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class HistoricoDespesas implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date mesAno;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Categoria centroCusto;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Bloco bloco;
    @Column(nullable = false)
    private double valor;
    @Column(nullable = false)
    private double totalPeriodo;

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

    public Categoria getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(Categoria centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getTotalPeriodo() {
        return totalPeriodo;
    }

    public void setTotalPeriodo(double totalPeriodo) {
        this.totalPeriodo = totalPeriodo;
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
        HistoricoDespesas other = (HistoricoDespesas) obj;
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
