/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

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
import modelo.Cliente;
import org.hibernate.annotations.IndexColumn;

/**
 * Classe modelo para a criação do objeto sessão de direitos, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class SessaoDireitos implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clientesAntigos_sessaoDireitos", joinColumns = {
        @JoinColumn(name = "sessaoDireito_id")}, inverseJoinColumns = {
        @JoinColumn(name = "clienteAntigo_id", insertable = true, updatable = true)})
    @IndexColumn(name = "clientes_order")
    private List<Cliente> clientesAntigos = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Contrato contrato;
    @Column(nullable = false)
    private boolean saldoRenegociado = true;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataSessao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cliente> getClientesAntigos() {
        return clientesAntigos;
    }

    public void setClientesAntigos(List<Cliente> clientesAntigos) {
        this.clientesAntigos = clientesAntigos;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public boolean isSaldoRenegociado() {
        return saldoRenegociado;
    }

    public void setSaldoRenegociado(boolean saldoRenegociado) {
        this.saldoRenegociado = saldoRenegociado;
    }

    public Date getDataSessao() {
        return dataSessao;
    }

    public void setDataSessao(Date dataSessao) {
        this.dataSessao = dataSessao;
    }

    public String nomePrimeiroClienteAntigo() {
        if (this.getClientesAntigos() != null && !this.getClientesAntigos().isEmpty()) {
            return this.getClientesAntigos().get(0).getPessoa().getName();
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
        SessaoDireitos other = (SessaoDireitos) obj;
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
