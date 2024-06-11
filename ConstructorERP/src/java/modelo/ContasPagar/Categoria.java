/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Classe modelo para a criação do objeto Categoria, bem como para persistência
 * e acesso.
 *
 * @author Daniel
 */
@Entity
public class Categoria implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @ManyToOne
    @JoinColumn(name = "categoriaPai_id")
    private Categoria categoriaPai;
    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> filhas;
    @Column
    private boolean considerarCalculoValorM2 = true;
    

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

    public Categoria getCategoriaPai() {
        return categoriaPai;
    }

    public void setCategoriaPai(Categoria categoriaPai) {
        this.categoriaPai = categoriaPai;
    }

    public List<Categoria> getFilhas() {
        return filhas;
    }

    public void setFilhas(List<Categoria> filhas) {
        this.filhas = filhas;
    }

    public boolean isConsiderarCalculoValorM2() {
        return considerarCalculoValorM2;
    }

    public void setConsiderarCalculoValorM2(boolean considerarCalculoValorM2) {
        this.considerarCalculoValorM2 = considerarCalculoValorM2;
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
        Categoria other = (Categoria) obj;
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
