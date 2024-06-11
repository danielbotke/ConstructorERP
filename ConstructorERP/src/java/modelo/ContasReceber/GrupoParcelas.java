/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import modelo.ContasReceber.Parcela;
import java.io.Serializable;
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
import modelo.TipoIndice;

/** Classe modelo para a criação do objeto grupo de parcelas, bem como para persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class GrupoParcelas implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;
    
    @OneToMany(mappedBy = "grupoParcelas", targetEntity = Parcela.class, fetch = FetchType.LAZY)
    private List<Parcela> parcelas = new ArrayList<>();
    
    @Column(nullable = false)
    private float fatorIndicePorParcela;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicioParcelas;
    
    @Column(nullable = false)
    private int qntParcelas;
    
    @Column(nullable = false)
    private char identificador;
    
    @Column
    private int diaMesVencimento;
    
    @Column
    private int diasEntreParcelas;
    
    /** Variável utilizada para armazenar o dia da semana em que sempre deve vencer a parela, sendo sempre a primeira ocorrência do dia no mês.
     *  0 = domingo
     *  1 = segunda
     *  2 = terça
     *  3 = quarta
     *  4 = quinta
     *  5 = sexta
     *  6 = sábado
     * 
     */
    @Column
    private int primeiroDiaDaSemana;
    
    @Column
    private boolean primeiroDiaUtil = false;
    
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

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public float getFatorIndicePorParcela() {
        return fatorIndicePorParcela;
    }

    public void setFatorIndicePorParcela(float fatorIndicePorParcela) {
        this.fatorIndicePorParcela = fatorIndicePorParcela;
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

    public int getQntParcelas() {
        return qntParcelas;
    }

    public void setQntParcelas(int qntParcelas) {
        this.qntParcelas = qntParcelas;
    }

    public char getIdentificador() {
        return identificador;
    }

    public void setIdentificador(char identificador) {
        this.identificador = identificador;
    }

    public TipoIndice getTipoIndice() {
        return tipoIndice;
    }

    public void setTipoIndice(TipoIndice tipoIndice) {
        this.tipoIndice = tipoIndice;
    }
    
    
    
}