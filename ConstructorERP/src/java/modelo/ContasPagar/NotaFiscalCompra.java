/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import dao.ContasPagar.NotaFiscalCompraDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import modelo.Bloco;

/**
 * Classe modelo para a criação do objeto nota de compra, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class NotaFiscalCompra implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Fornecedor fornecedor;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataContabil;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNota;
    @Column(nullable = false)
    private int codigo;
    @Column
    private String observacao;
    @Column(nullable = false)
    private int situacao = 0;
    /*
     @OneToMany(mappedBy = "notaFiscal", targetEntity = ItemCompra.class)
     private List<ItemCompra> itensCompra = new ArrayList<>();*/
    @Column
    private double valorDesconto;
    @Column
    private double valorFrete;
    @Column
    private double percentualDesconto;
    @Column
    private double valorTotal;
    @Column
    private double ISS;
    @Column
    private double INSS;
    @Column
    private double FGTS;
    @Column
    private double MDO;
    @Column
    private double extra;
    @Column
    private double almoco;
    @Column
    private double ajusteContabil;
    @Column
    private double custoFuncionario;
    @Column
    private boolean salario;
    @Column
    private int qntParcelas;
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
    @OneToMany(mappedBy = "nota", targetEntity = ParcelaAPagar.class, cascade = CascadeType.REMOVE)
    private List<ParcelaAPagar> parcelas = new ArrayList<>();
    /* @OneToOne(fetch = FetchType.EAGER)
     @MapsId
     private CotacaoCompra cotacaoGeradora;
     */
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Categoria centroDeCusto;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Bloco bloco;
    @Column
    private boolean ISSPago = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataContabil() {
        return dataContabil;
    }

    public void setDataContabil(Date dataContabil) {
        this.dataContabil = dataContabil;
    }

    public Date getDataNota() {
        return dataNota;
    }

    public void setDataNota(Date dataNota) {
        this.dataNota = dataNota;
    }
    
    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
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

    /*    public List<ItemCompra> getItensCompra() {
     itensCompra = (new NotaFiscalCompraDao()).listItens(id);
     return itensCompra;
     }

     public void setItensCompra(List<ItemCompra> itensCompra) {
     this.itensCompra = itensCompra;
     }*/
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
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
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

    public int getQntParcelas() {
        return qntParcelas;
    }

    public void setQntParcelas(int qntParcelas) {
        this.qntParcelas = qntParcelas;
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

    public List<ParcelaAPagar> getParcelas() throws Exception {
        parcelas = (new NotaFiscalCompraDao()).listParcelas(id);
        return parcelas;
    }

    public void setParcelas(List<ParcelaAPagar> parcelas) {
        this.parcelas = parcelas;
    }

    /*  public CotacaoCompra getCotacaoGeradora() {
     return cotacaoGeradora;
     }

     public void setCotacaoGeradora(CotacaoCompra cotacaoGeradora) {
     this.cotacaoGeradora = cotacaoGeradora;
     }*/
    public Categoria getCentroDeCusto() {
        return centroDeCusto;
    }

    public void setCentroDeCusto(Categoria centroDeCusto) {
        this.centroDeCusto = centroDeCusto;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public double getMDO() {
        return MDO;
    }

    public void setMDO(double MDO) {
        this.MDO = MDO;
    }

    public double getAjusteContabil() {
        return ajusteContabil;
    }

    public void setAjusteContabil(double ajusteContabil) {
        this.ajusteContabil = ajusteContabil;
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

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    public double getAlmoco() {
        return almoco;
    }

    public void setAlmoco(double almoco) {
        this.almoco = almoco;
    }

    public double getTotal() {
        return this.valorTotal + valorFrete - valorDesconto + MDO + ajusteContabil + almoco + extra + custoFuncionario;
    }

    public boolean isISSPago() {
        return ISSPago;
    }

    public void setISSPago(boolean ISSPago) {
        this.ISSPago = ISSPago;
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
        NotaFiscalCompra other = (NotaFiscalCompra) obj;
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
