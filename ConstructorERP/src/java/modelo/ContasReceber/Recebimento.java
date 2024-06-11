/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import modelo.Indice;

/**
 * Classe modelo para a criação do objeto recebimento, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
@Table(name = "recebimento")
public class Recebimento implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Parcela parcela;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Indice CUB;
    @Column(nullable = false)
    private float valorRecebido;
    @Column(nullable = false)
    private int formaPagamento;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataPagamento;
    @Column
    private float valorMulta;
    @Column
    private float valorJuros;
    @Column
    private float valorDesconto;
    @Column
    private String observacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public Indice getCUB() {
        return CUB;
    }

    public void setCUB(Indice CUB) {
        this.CUB = CUB;
    }

    public float getValorRecebido() {
        return valorRecebido;
    }
    
    public void setValorRecebido(float valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public int getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public float getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(float valorMulta) {
        this.valorMulta = valorMulta;
    }

    public float getValorJuros() {
        return valorJuros;
    }

    public void setValorJuros(float valorJuros) {
        this.valorJuros = valorJuros;
    }

    public float getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(float valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public float valorTotal() {
        return getValorDesconto() + getValorJuros() + getValorMulta() + getValorRecebido();
    }
    
    public float valorTotalSemDesconto() {
        return getValorJuros() + getValorMulta() + getValorRecebido();
    }
    
    @Override
    public String toString(){
        try {
            return this.getParcela().getContrato().apartamentosDesc() + " - " + NumberFormat.getCurrencyInstance().format(this.getValorRecebido());
        } catch (Exception ex) {
            Logger.getLogger(Recebimento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        Recebimento other = (Recebimento) obj;
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
