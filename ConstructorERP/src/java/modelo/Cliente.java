/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import dao.ClienteDao;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Classe modelo para a criação do objeto cliente, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId
    private Pessoa pessoa;
    
    /** 1 - Protestar; 2 - Não protestar; 3 - Carteira;
     * 
     */
    @Column
    private int formaPagamento;
    
    private double saldoDevedor = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
    
    public Pessoa getPessoaCliente() throws Exception {
        if(pessoa == null){
            pessoa = (new ClienteDao()).getPessoaCliente(id);
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public double getSaldoDevedor() {
        return saldoDevedor;
    }

    public void setSaldoDevedor(double saldoDevedor) {
        this.saldoDevedor = saldoDevedor;
    }

    /** 1 - Boleto Registrado; 2 - Boleto Sem registeo; 3 - Carteira
     * 
     */
    public int getFormaPagamento() {
        return formaPagamento;
    }

    /** 1 - Boleto Registrado; 2 - Boleto Sem registeo; 3 - Carteira
     * 
     */
    public void setFormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
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
        Cliente other = (Cliente) obj;
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
