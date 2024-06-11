/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import modelo.Bloco;

/**
 * Classe modelo para a criação do objeto parcela a pagar, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class ParcelaAPagar implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column
    private String descricao;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPagamento;
    @Column
    private float valorParcela;
    @Column(nullable = false)
    private boolean paga = false;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Categoria categoriaParcelaAvulsa;
    /**
     * 0 - Aberta 1 - Parcial 2 - Paga 3-Renegociana 4-Cancelada 6 - Protestada
     *
     */
    @Column
    private int situacao = 0;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Bloco bloco;
    @ManyToOne
    @JoinColumn(name = "notaCompraParcela_id", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
    private NotaFiscalCompra nota;
    @Column
    private double ISS;
    @Column
    private double INSS;
    @Column
    private double FGTS;
    @Column
    private double MDO;
    @Column
    private double Extra;
    @Column
    private double almoco;
    @Column
    private double ajusteContabil;
    @Column
    private double custoFuncionario;
    @Column
    private boolean salario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 0 - Aberta 1 - Parcial 2 - Paga 3-Renegociana 4-Cancelada 5-Renegociada
     * por Sessao de Direitos 6 - Protestada
     */
    public int getSituacao() {
        return situacao;
    }

    /**
     * 0 - Aberta 1 - Parcial 2 - Paga 3-Renegociana 4-Cancelada 5-Renegociada
     * por Sessao de Direitos 6 - Protestada
     *
     * @param situacao
     */
    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public float getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(float valorParcela) {
        this.valorParcela = valorParcela;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }

    public NotaFiscalCompra getNota() {
        return nota;
    }

    public void setNota(NotaFiscalCompra nota) {
        this.nota = nota;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Categoria getCategoriaParcelaAvulsa() {
        return categoriaParcelaAvulsa;
    }

    public void setCategoriaParcelaAvulsa(Categoria categoriaParcelaAvulsa) {
        this.categoriaParcelaAvulsa = categoriaParcelaAvulsa;
    }

    public double getISS() {
        return ISS;
    }

    public void setISS(double ISS) {
        this.ISS = ISS;
    }

    public double getINSS() {
        return INSS;
    }

    public void setINSS(double INSS) {
        this.INSS = INSS;
    }

    public double getFGTS() {
        return FGTS;
    }

    public void setFGTS(double FGTS) {
        this.FGTS = FGTS;
    }

    public double getMDO() {
        return MDO;
    }

    public void setMDO(double MDO) {
        this.MDO = MDO;
    }

    public double getExtra() {
        return Extra;
    }

    public void setExtra(double Extra) {
        this.Extra = Extra;
    }

    public double getAlmoco() {
        return almoco;
    }

    public void setAlmoco(double almoco) {
        this.almoco = almoco;
    }

    public double getAjusteContabil() {
        return ajusteContabil;
    }

    public void setAjusteContabil(double ajusteContabil) {
        this.ajusteContabil = ajusteContabil;
    }

    public double getValorTotal() {
        if (this.isSalario()) {
            return valorParcela + ISS + MDO + INSS + FGTS + Extra + almoco + ajusteContabil;
        } else {
            return valorParcela + MDO + Extra + almoco + ajusteContabil;

        }
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public double getCustoFuncionario() {
        return custoFuncionario;
    }

    public void setCustoFuncionario(double custoFuncionario) {
        this.custoFuncionario = custoFuncionario;
    }

    public boolean isSalario() {
        return salario;
    }

    public void setSalario(boolean salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        if (this.nota != null && this.nota.getFornecedor() != null) {
            try {
                return this.nota.getFornecedor().getPessoaFornecedor().getName() + " - " + NumberFormat.getCurrencyInstance().format(this.getValorTotal());
            } catch (Exception ex) {
                Logger.getLogger(ParcelaAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return this.getDescricao() + " - " + NumberFormat.getCurrencyInstance().format(this.getValorTotal());
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
        ParcelaAPagar other = (ParcelaAPagar) obj;
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
