/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import dao.ContasPagar.CotacaoCompraDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Classe modelo para a criação do objeto cotação de compra, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class CotacaoCompra implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Fornecedor fornecedor;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCriacao = new Date();
    @Column
    private String observacao;
    @Column(nullable = false)
    private int situacao = 0;
    @OneToMany(mappedBy="cotacao",targetEntity = ItemCompra.class)
    private List<ItemCompra> itensCompra = new ArrayList<>();
    @Column
    private double valorDesconto;
    @Column
    private double valorFrete;
    @Column
    private double percentualDesconto;
    @Column
    private int qntParcelas;
    @Column
    private double valorEntrada;
    @Column
    private int tipoParcelamento;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicioParcelas;
    @Column
    private int diaMesVencimento;
    @Column
    private int diasEntreParcelas;
    /**
     * Variável utilizada para armazenar o dia da semana em que sempre deve
     * vencer a parela, sendo sempre a primeira ocorrência do dia no mês. 0 =
     * domingo 1 = segunda 2 = terça 3 = quarta 4 = quinta 5 = sexta 6 = sábado
     *
     */
    @Column
    private int primeiroDiaDaSemana;
    @Column
    private boolean primeiroDiaUtil = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public List<ItemCompra> getItensCompra() throws Exception {
            itensCompra = (new CotacaoCompraDao()).listItens(id);
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompra> itensCompra) {
        this.itensCompra = itensCompra;
    }

    public double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public int getQntParcelas() {
        return qntParcelas;
    }

    public void setQntParcelas(int qntParcelas) {
        this.qntParcelas = qntParcelas;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public int getTipoParcelamento() {
        return tipoParcelamento;
    }

    public void setTipoParcelamento(int tipoParcelamento) {
        this.tipoParcelamento = tipoParcelamento;
    }

    public Date getDataInicioParcelas() {
        return dataInicioParcelas;
    }

    public void setDataInicioParcelas(Date dataInicioParcelas) {
        this.dataInicioParcelas = dataInicioParcelas;
    }

    public int getDiaMesVencimento() {
        return diaMesVencimento;
    }

    public void setDiaMesVencimento(int diaMesVencimento) {
        this.diaMesVencimento = diaMesVencimento;
    }

    public int getDiasEntreParcelas() {
        return diasEntreParcelas;
    }

    public void setDiasEntreParcelas(int diasEntreParcelas) {
        this.diasEntreParcelas = diasEntreParcelas;
    }

    public int getPrimeiroDiaDaSemana() {
        return primeiroDiaDaSemana;
    }

    public void setPrimeiroDiaDaSemana(int primeiroDiaDaSemana) {
        this.primeiroDiaDaSemana = primeiroDiaDaSemana;
    }

    public boolean isPrimeiroDiaUtil() {
        return primeiroDiaUtil;
    }

    public void setPrimeiroDiaUtil(boolean primeiroDiaUtil) {
        this.primeiroDiaUtil = primeiroDiaUtil;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    
    public double getValorTotal() throws Exception{
        double valorTotal = 0;
        for (int i = 0; i < this.getItensCompra().size(); i++) {
            valorTotal += (itensCompra.get(i).getValorTotal());
        }
        valorTotal += this.getValorFrete();
        valorTotal = valorTotal * (1 - (this.getPercentualDesconto() / 100));
        valorTotal -= this.getValorDesconto();
        return valorTotal;
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
        CotacaoCompra other = (CotacaoCompra) obj;
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
