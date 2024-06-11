/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import modelo.ContasReceber.Parcela;
import modelo.ContasReceber.NumerosTitulo;
import modelo.ContasReceber.NumerosRemessa;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Classe modelo para a criação do objeto bloco, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class BoletoGerado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private NumerosTitulo numerosTitulo;
    @Column(nullable=false)
    private double valorBoleto;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataDocto;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;
    @Column(nullable=false)
    String instrucao;
    @ManyToOne
    @JoinColumn(name = "parcela_id", nullable = false)
    private Parcela parcela;
    @Column(nullable=false, length= 1)
    String carteira;
    @Column(nullable=false, length=2)
    String modalidade;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private NumerosRemessa numerosRemessa;
    @Column
    int situacao = 0;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NumerosTitulo getNumerosTitulo() {
        return numerosTitulo;
    }

    public void setNumerosTitulo(NumerosTitulo numerosTitulo) {
        this.numerosTitulo = numerosTitulo;
    }

    public double getValorBoleto() {
        return valorBoleto;
    }

    public void setValorBoleto(double valorBoleto) {
        this.valorBoleto = valorBoleto;
    }

    public Date getDataDocto() {
        return dataDocto;
    }

    public void setDataDocto(Date dataDocto) {
        this.dataDocto = dataDocto;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getInstrucao() {
        return instrucao;
    }

    public void setInstrucao(String instrucao) {
        this.instrucao = instrucao;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public NumerosRemessa getNumerosRemessa() {
        return numerosRemessa;
    }

    public void setNumerosRemessa(NumerosRemessa numerosRemessa) {
        this.numerosRemessa = numerosRemessa;
    }

    /**0 - Aberto, 1 - Entrada Confirmada, 2 - Pago, 3 - Cancelado, 4 - Remessa a Cartório, 5 - Protestado, 6 - Entrada rejeitada, 99 - Situação desconhecida
     * 
     */
    public int getSituacao() {
        return situacao;
    }

    /**0 - Aberto, 1 - Entrada Confirmada, 2 - Pago, 3 - Cancelado, 4 - Remessa a Cartório, 5 - Protestado, 6 - Entrada rejeitada, 99 - Situação desconhecida
     * 
     */
    public void setSituacao(int situacao) {
        this.situacao = situacao;
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
        BoletoGerado other = (BoletoGerado) obj;
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
