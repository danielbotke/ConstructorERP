/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import dao.ContasReceber.ContratoDao;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import modelo.Apartamento;
import modelo.Cliente;
import org.hibernate.annotations.IndexColumn;

/**
 * Classe modelo para a criação do objeto contrato, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class ContratoAluguel implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "cliente_contratoAluguel", joinColumns = {
        @JoinColumn(name = "contratosAluguel_id")}, inverseJoinColumns = {
        @JoinColumn(name = "clientesAluguel_id", insertable = true, updatable = true)})
    @IndexColumn(name = "clientesAluguel_order")
    private List<Cliente> clientes = new ArrayList<>();
    @Column(nullable = false)
    private double valorAluguel;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFim;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Apartamento unidade;
    /**
     * 0 - Em andamento 1 - Encerrado
     *
     */
    @Column(nullable = false)
    private int status = 0;
    
    @Column(nullable = false)
    private int diaMesVencimento = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cliente> getClientes() throws Exception {
        if (clientes == null || clientes.isEmpty()) {
            ContratoDao contratoDao = new ContratoDao();
            clientes = contratoDao.listClientes(id);
        }
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    /**
     * 0 - Em andamento 1 - Encerrado
     *
     */
    public int getStatus() {
        return status;
    }

    /**
     * 0 - Em andamento 1 - Encerrado
     *
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }


    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Apartamento getUnidade() {
        return unidade;
    }

    public void setUnidade(Apartamento unidade) {
        this.unidade = unidade;
    }

    public int getDiaMesVencimento() {
        return diaMesVencimento;
    }

    public void setDiaMesVencimento(int diaMesVencimento) {
        this.diaMesVencimento = diaMesVencimento;
    }
    
    
    
    public String unidadeDesc() {
        String aptos = "";
            aptos += unidade.getBloco().getPredio().getName() + " - " + unidade.getName() + unidade.getBloco().getIdentificacao().toUpperCase();

        return aptos;
    }


    public String nomePrimeiroCliente() throws Exception {
        if (this.getClientes() != null && !this.getClientes().isEmpty()) {
            return this.getClientes().get(0).getPessoa().getName();
        } else {
            return "";
        }
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
        ContratoAluguel other = (ContratoAluguel) obj;
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
