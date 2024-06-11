/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import dao.ContasReceber.HistoricoDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import modelo.Historico;
import org.hibernate.annotations.IndexColumn;

/**
 * Classe modelo para a criação do objeto renegociação, bem como para
 * persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class Renegociacao implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "parcela_renegociacao", joinColumns = {
        @JoinColumn(name = "renegociacao_id")}, inverseJoinColumns = {
        @JoinColumn(name = "parcela_id", insertable = true, updatable = true)})
    @IndexColumn(name = "parcela_order")
    private List<Parcela> parcelas = new ArrayList<>();
    @OneToMany(targetEntity = Historico.class, fetch = FetchType.EAGER)
    private List<Historico> historicos = new ArrayList<>();
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private boolean encerrada = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public List<Historico> getHistoricos() throws Exception {
        historicos = (new HistoricoDao()).listAll(this.getClass().toString(), this.getId());
        return historicos;
    }

    public void setHistoricos(List<Historico> historicos) {
        this.historicos = historicos;
    }

    public String getDescricao() {
        if (descricao == null || "".equalsIgnoreCase(descricao)) {
            this.setDescricao(this.toString());
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isEncerrada() {
        return encerrada;
    }

    public void setEncerrada(boolean encerrada) {
        this.encerrada = encerrada;
    }

    public String parcelasToString() {
        String aux = "";
        for (int i = 0; i < parcelas.size(); i++) {
            aux += parcelas.get(i).getNumeracao();
            if (i < (parcelas.size() - 1)) {
                aux += " - ";
            }
        }
        return aux;
    }

    public String apartamentos() throws Exception {
        String aux = "";
        ArrayList<Contrato> contracts = new ArrayList<>();
        for (int i = 0; i < parcelas.size(); i++) {
            if (!contracts.contains(parcelas.get(i).getContrato())) {
                contracts.add(parcelas.get(i).getContrato());
                aux += parcelas.get(i).getContrato().apartamentosDesc() + " - ";
            }
        }
        return aux;
    }

    public String cliente() throws Exception {
        if (parcelas != null) {
            return parcelas.get(0).getContrato().nomePrimeiroCliente();
        } else {
            return "";

        }
    }

    @Override
    public String toString() {
        if (parcelas != null && !parcelas.isEmpty()) {
            try {
                return this.cliente() + " | " + this.apartamentos() + " | " + this.parcelasToString();
            } catch (Exception ex) {
                Logger.getLogger(Renegociacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return "";

        }
        return null;
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
        Renegociacao other = (Renegociacao) obj;
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
