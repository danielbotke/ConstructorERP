/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

/**
 * Classe modelo para a criação do objeto item compra, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class ItemCompra implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Produto produto;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Servico servico;
    @Column(nullable = false)
    private float quantidade;
    @Column(nullable = false)
    private double valor;
    @Column
    private double valorDesconto;
    @Column
    private double percentualDesconto;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Unidade unidade;
    @ManyToOne
    @JoinColumn(name = "cotacaoItens_id", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
    private CotacaoCompra cotacao;
    @ManyToOne
    @JoinColumn(name = "notaItens_id", nullable = true, referencedColumnName = "id", insertable = true, updatable = true)
    private NotaFiscalCompra notaFiscal;
    
    private double valorTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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

    public double getValorTotal() {
        valorTotal = valor * quantidade;
        valorTotal = valorTotal * (1 - (percentualDesconto / 100));
        valorTotal -= valorDesconto;
        return valorTotal;
    }

    public void setValorTotal() {
        valorTotal = valor * quantidade;
        valorTotal = valorTotal * (1 - (percentualDesconto / 100));
        valorTotal -= valorDesconto;
    }


    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public CotacaoCompra getCotacao() {
        return cotacao;
    }

    public void setCotacao(CotacaoCompra cotacao) {
        this.cotacao = cotacao;
    }

    public NotaFiscalCompra getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(NotaFiscalCompra notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
    
    
    
    public String nomeProdServ(){
        if (produto != null){
            return produto.getName();
        }else if (servico != null){
            return servico.getName();
        }
        return "";
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
        ItemCompra other = (ItemCompra) obj;
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
