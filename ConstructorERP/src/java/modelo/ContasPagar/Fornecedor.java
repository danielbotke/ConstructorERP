/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import dao.ContasPagar.FornecedorDao;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import modelo.Pessoa;


/**
 * Classe modelo para a criação do objeto fornecedor, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId
    private Pessoa pessoa;
    
    @Column(nullable = false)
    private String observacao;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Categoria categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
    
    public Pessoa getPessoaFornecedor() throws Exception {
        if(pessoa == null){
            pessoa = (new FornecedorDao()).getPessoaFornecedor(id);
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
        Fornecedor other = (Fornecedor) obj;
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
