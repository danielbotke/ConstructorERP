/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import org.hibernate.annotations.IndexColumn;

/**
 * Classe modelo para a criação do objeto produto, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class Produto implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Categoria categoria;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Unidade unidadePadrao;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "unidade_produto", joinColumns = {
        @JoinColumn(name = "produto_id")}, inverseJoinColumns = {
        @JoinColumn(name = "outrasUnidades_id", insertable = true, updatable = true)})
    @IndexColumn(name = "outrasUnidades_order")
    private List<Unidade> outrasUnidades = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Unidade getUnidadePadrao() {
        return unidadePadrao;
    }

    public void setUnidadePadrao(Unidade unidadePadrao) {
        this.unidadePadrao = unidadePadrao;
    }

    public List<Unidade> getOutrasUnidades() {
        return outrasUnidades;
    }

    public void setOutrasUnidades(List<Unidade> outrasUnidades) {
        this.outrasUnidades = outrasUnidades;
    }
    
    public List<Unidade> getUnidades() {
        List<Unidade> unidades = new ArrayList<>();
        unidades.add(unidadePadrao);
        unidades.addAll(this.getOutrasUnidades());
        return unidades;
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
        Produto other = (Produto) obj;
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
