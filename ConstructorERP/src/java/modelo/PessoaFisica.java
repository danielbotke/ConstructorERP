/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Classe modelo para a criação do objeto pessoa física, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
@Table(name = "pessoaFisica")
public class PessoaFisica extends Pessoa implements Serializable {

    @Column(nullable = false)
    private char sexo;
    @Column
    private String empresa;
    @Column
    private String enderecoCom;
    @Column
    private int CEPCom;
    @Column
    private String bairroCom;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Cidade cidadeCom;
    @Column(length = 30)
    private String TelCom;
    @Column(nullable = false)
    private String nacionalidade;
    @Column
    private String profissao;
    @Column(nullable = false, length = 20)
    private String RG;
    @Column(nullable = false, length = 20)
    private String CPF;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataNasc;
    @Column(nullable = false)
    private int estadoCivil;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataCasam;
    @Column
    private String nameConjuge;
    @Column
    private String sexoConjuge;
    @Column
    private String enderecoConjuge;
    @Column
    private int CEPConjuge;
    @Column
    private String bairroConjuge;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Cidade cidadeConjuge;
    @Column
    private String empresaConjuge;
    @Column
    private String enderecoComConjuge;
    @Column
    private int CEPComConjuge;
    @Column
    private String bairroComConjuge;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Cidade cidadeComConjuge;
    @Column(length = 30)
    private String telResConjuge;
    @Column(length = 30)
    private String celConjuge;
    @Column(length = 30)
    private String TelComConjuge;
    @Column
    private String nacionalidadeConjuge;
    @Column
    private String profissaoConjuge;
    @Column(length = 20)
    private String RGConjuge;
    @Column(length = 20)
    private String CPFConjuge;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataNascConjuge;
    @Column
    private int estadoCivilConjuge;
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataCasamConjuge;
    @Column
    private String emailConjuge;
    @ManyToOne
    @JoinColumn(name = "representada_id")
    private PessoaJuridica representada;
    @Column(length=2)
    private int regBens;

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEnderecoCom() {
        return enderecoCom;
    }

    public void setEnderecoCom(String enderecoCom) {
        this.enderecoCom = enderecoCom;
    }

    public int getCEPCom() {
        return CEPCom;
    }

    public void setCEPCom(int CEPCom) {
        this.CEPCom = CEPCom;
    }

    public String getBairroCom() {
        return bairroCom;
    }

    public void setBairroCom(String bairroCom) {
        this.bairroCom = bairroCom;
    }

    public Cidade getCidadeCom() {
        return cidadeCom;
    }

    public void setCidadeCom(Cidade cidadeCom) {
        this.cidadeCom = cidadeCom;
    }

    public String getTelCom() {
        return TelCom;
    }

    public void setTelCom(String TelCom) {
        this.TelCom = TelCom;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public Date getDataCasam() {
        return dataCasam;
    }

    public void setDataCasam(Date dataCasam) {
        this.dataCasam = dataCasam;
    }

    public String getNameConjuge() {
        return nameConjuge;
    }

    public void setNameConjuge(String nameConjuge) {
        this.nameConjuge = nameConjuge;
    }

    public String getEnderecoConjuge() {
        return enderecoConjuge;
    }

    public void setEnderecoConjuge(String enderecoConjuge) {
        this.enderecoConjuge = enderecoConjuge;
    }

    public int getCEPConjuge() {
        return CEPConjuge;
    }

    public void setCEPConjuge(int CEPConjuge) {
        this.CEPConjuge = CEPConjuge;
    }

    public String getBairroConjuge() {
        return bairroConjuge;
    }

    public void setBairroConjuge(String bairroConjuge) {
        this.bairroConjuge = bairroConjuge;
    }

    public Cidade getCidadeConjuge() {
        return cidadeConjuge;
    }

    public void setCidadeConjuge(Cidade cidadeConjuge) {
        this.cidadeConjuge = cidadeConjuge;
    }

    public String getEmpresaConjuge() {
        return empresaConjuge;
    }

    public void setEmpresaConjuge(String empresaConjuge) {
        this.empresaConjuge = empresaConjuge;
    }

    public String getEnderecoComConjuge() {
        return enderecoComConjuge;
    }

    public void setEnderecoComConjuge(String enderecoComConjuge) {
        this.enderecoComConjuge = enderecoComConjuge;
    }

    public int getCEPComConjuge() {
        return CEPComConjuge;
    }

    public void setCEPComConjuge(int CEPComConjuge) {
        this.CEPComConjuge = CEPComConjuge;
    }

    public String getBairroComConjuge() {
        return bairroComConjuge;
    }

    public void setBairroComConjuge(String bairroComConjuge) {
        this.bairroComConjuge = bairroComConjuge;
    }

    public Cidade getCidadeComConjuge() {
        return cidadeComConjuge;
    }

    public void setCidadeComConjuge(Cidade cidadeComConjuge) {
        this.cidadeComConjuge = cidadeComConjuge;
    }

    public String getTelResConjuge() {
        return telResConjuge;
    }

    public void setTelResConjuge(String telResConjuge) {
        this.telResConjuge = telResConjuge;
    }

    public String getCelConjuge() {
        return celConjuge;
    }

    public void setCelConjuge(String celConjuge) {
        this.celConjuge = celConjuge;
    }

    public String getTelComConjuge() {
        return TelComConjuge;
    }

    public void setTelComConjuge(String TelComConjuge) {
        this.TelComConjuge = TelComConjuge;
    }

    public String getNacionalidadeConjuge() {
        return nacionalidadeConjuge;
    }

    public void setNacionalidadeConjuge(String nacionalidadeConjuge) {
        this.nacionalidadeConjuge = nacionalidadeConjuge;
    }

    public String getProfissaoConjuge() {
        return profissaoConjuge;
    }

    public void setProfissaoConjuge(String profissaoConjuge) {
        this.profissaoConjuge = profissaoConjuge;
    }

    public String getRGConjuge() {
        return RGConjuge;
    }

    public void setRGConjuge(String RGConjuge) {
        this.RGConjuge = RGConjuge;
    }

    public String getCPFConjuge() {
        return CPFConjuge;
    }

    public void setCPFConjuge(String CPFConjuge) {
        this.CPFConjuge = CPFConjuge;
    }

    public Date getDataNascConjuge() {
        return dataNascConjuge;
    }

    public void setDataNascConjuge(Date dataNascConjuge) {
        this.dataNascConjuge = dataNascConjuge;
    }

    public int getEstadoCivilConjuge() {
        return estadoCivilConjuge;
    }

    public void setEstadoCivilConjuge(int estadoCivilConjuge) {
        this.estadoCivilConjuge = estadoCivilConjuge;
    }

    public Date getDataCasamConjuge() {
        return dataCasamConjuge;
    }

    public void setDataCasamConjuge(Date dataCasamConjuge) {
        this.dataCasamConjuge = dataCasamConjuge;
    }

    public String getEmailConjuge() {
        return emailConjuge;
    }

    public void setEmailConjuge(String emailConjuge) {
        this.emailConjuge = emailConjuge;
    }

    public String getSexoConjuge() {
        return sexoConjuge;
    }

    public void setSexoConjuge(String sexoConjuge) {
        this.sexoConjuge = sexoConjuge;
    }

    public PessoaJuridica getRepresentada() {
        return representada;
    }

    public void setRepresentada(PessoaJuridica representada) {
        this.representada = representada;
    }

    public int getRegBens() {
        return regBens;
    }

    public void setRegBens(int regBens) {
        this.regBens = regBens;
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
        PessoaFisica other = (PessoaFisica) obj;
        if (this.getId() == 0) {
            if (other.getId() != 0) {
                return false;
            }
        } else if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.getId();
        return hash;
    }
}
