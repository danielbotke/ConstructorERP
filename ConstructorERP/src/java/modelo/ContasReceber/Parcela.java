/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import dao.ContasReceber.BoletoGeradoDao;
import dao.IndiceDao;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import modelo.Indice;
import modelo.TipoIndice;

/**
 * Classe modelo para a criação do objeto parcela, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class Parcela implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "contratoParc_id", nullable = false, referencedColumnName = "id", insertable = true, updatable = true)
    private Contrato contrato;
    @ManyToOne
    @JoinColumn(name = "grupoParcelas_id")
    private GrupoParcelas grupoParcelas;
    @Column(nullable = false)
    private float fatorIndice;
    @Column
    private boolean valorFixo;
    @Column
    private String descricao;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;
    /**
     * Quando persistido caracterisa uma parcela de valor fixo (reforços,
     * valorFixo, chave, saldoBancario)
     *
     */
    @Column
    private float valorParcela;
    @Column(nullable = false)
    private boolean paga = false;
    /**
     * 0 - Aberta 1 - Parcial 2 - Paga 3-Renegociana 4-Cancelada 5-Renegociada
     * por Sessao de Direitos 6 - Protestada
     *
     */
    @Column
    private int situacao = 0;
    @Column
    private float fatorIndicePagos = 0;
    @Column(nullable = true, length = 10)
    private String numeracao = "";
    @Column
    private boolean boletoGerado = false;
    @OneToMany(mappedBy = "parcela", targetEntity = BoletoGerado.class, fetch= FetchType.LAZY)
    private List<BoletoGerado> boletos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "tipoIndice_id", nullable = false)
    private TipoIndice tipoIndice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public GrupoParcelas getGrupoParcelas() {
        return grupoParcelas;
    }

    public void setGrupoParcelas(GrupoParcelas grupoParcelas) {
        this.grupoParcelas = grupoParcelas;
    }

    public float getFatorIndice() {
        return fatorIndice;
    }

    public void setFatorIndice(float fatorIndice) {
        this.fatorIndice = fatorIndice;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public float getValorParcela() throws Exception {
        IndiceDao indiceDao = new IndiceDao();
        if (!this.isValorFixo() && dataVencimento != null) {
            Date aux = new Date(dataVencimento.getTime());
            Indice indice = indiceDao.getIndiceMonth(aux, this.getTipoIndice().getId());
            if (indice == null) {
                indice = indiceDao.getLast(this.getTipoIndice().getId());
            }
            BigDecimal bd1 = new BigDecimal(indice.getValorIndice()).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal bd = new BigDecimal(this.getFatorIndice() * bd1.floatValue()).setScale(2, RoundingMode.HALF_DOWN);
            return bd.floatValue();
        }
        return valorParcela;
    }

    public float getValorParcelaFixa() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isValorFixo() {
        return valorFixo;
    }

    public void setValorFixo(boolean valorFixo) {
        this.valorFixo = valorFixo;
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

    public float getFatorIndicePagos() {
        return fatorIndicePagos;
    }

    public void setFatorIndicePagos(float fatorIndicePagos) {
        this.fatorIndicePagos += fatorIndicePagos;
    }

    public String getNumeracao() {
        return numeracao;
    }

    public void setNumeracao(String numeracao) {
        this.numeracao = numeracao;
    }

    public boolean isBoletoGerado() {
        return boletoGerado;
    }

    public void setBoletoGerado(boolean boletoGerado) {
        this.boletoGerado = boletoGerado;
    }

    public List<BoletoGerado> getBoletos() throws Exception {
        boletos = (new BoletoGeradoDao()).listAllParcela(id);
        return boletos;
    }

    public void setBoletos(List<BoletoGerado> boletos) {
        this.boletos = boletos;
    }

    public TipoIndice getTipoIndice() {
        return tipoIndice;
    }

    public void setTipoIndice(TipoIndice tipoIndice) {
        this.tipoIndice = tipoIndice;
    }
    
    
    

    public double saldoParcela() throws Exception {
        Indice indice = null;
        if (this.isValorFixo()) {
            indice = new Indice();
            indice.setValorIndice(valorParcela / fatorIndice);
        } else {
            indice = (new IndiceDao()).getLast(this.getTipoIndice().getId());
        }
        BigDecimal bd = new BigDecimal((fatorIndice - fatorIndicePagos) * indice.getValorIndice()).setScale(2, RoundingMode.HALF_DOWN);
        return bd.floatValue();
    }

    public String parcelaDesc() throws Exception {
        return this.getContrato().apartamentosDesc() + " - " + this.getNumeracao();
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
        Parcela other = (Parcela) obj;
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
