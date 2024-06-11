/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Classe modelo para a criação do objeto pessoa jurídica, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
@Table(name = "pessoaJuridica")
public class PessoaJuridica extends Pessoa implements Serializable {

    @Column(nullable = false, length = 20)
    private String CNPJ;
    @Column(length = 20)
    private String inscricaoEstadual;
    @OneToMany(mappedBy = "representada", targetEntity = PessoaFisica.class, fetch = FetchType.EAGER)
    private List<PessoaFisica> representantes = new ArrayList<>();

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public List<PessoaFisica> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<PessoaFisica> representantes) {
        this.representantes = representantes;
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
        PessoaJuridica other = (PessoaJuridica) obj;
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
