/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import beans.IndiceMB;
import modelo.ContasReceber.Parcela;
import dao.ApartamentoDao;
import dao.ContasReceber.ContratoDao;
import dao.IndiceDao;
import dao.ContasReceber.ParcelaDao;
import dao.TipoIndiceDao;
import dao.VagaDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import modelo.Apartamento;
import modelo.Cliente;
import modelo.Indice;
import modelo.Vaga;
import org.hibernate.annotations.IndexColumn;

/**
 * Classe modelo para a criação do objeto contrato, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class Contrato implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "cliente_contrato", joinColumns = {
        @JoinColumn(name = "contratos_id")}, inverseJoinColumns = {
        @JoinColumn(name = "clientes_id", insertable = true, updatable = true)})
    @IndexColumn(name = "clientes_order")
    private List<Cliente> clientes = new ArrayList<>();
    private float totalCUBs;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Indice CUB;
    @Column(nullable = false)
    private float valorTotal;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataContrato;
    @OneToMany(targetEntity = Parcela.class)
    @IndexColumn(name = "contratoParc_id")
    private List<Parcela> parcelas = new ArrayList<>();
    @OneToMany(targetEntity = Vaga.class, fetch = FetchType.EAGER)
    @IndexColumn(name = "contratoVaga_id")
    private List<Vaga> vagas = new ArrayList<>();
    @OneToMany(targetEntity = Apartamento.class, fetch = FetchType.EAGER)
    @IndexColumn(name = "contratoApart_id")
    private List<Apartamento> apartamentos;
    @Column
    private String motivoDistrato;
    @Column
    private boolean naPlanta;
    private double totalPago = 0;
    private double totalEmAberto = 0;
    private double totalCUBEmAberto = 0;
    private double totalEmAbertoCUBContrato = 0;
    /**
     * 0 - Em andamento 1 - Encerrado 2 - Distratado
     *
     */
    @Column(nullable = false)
    private int status = 0;

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

    public List<Apartamento> getApartamentos() throws Exception {
        if (apartamentos == null || apartamentos.isEmpty()) {
            apartamentos = (new ApartamentoDao()).listFromContrato(id);
        }
        return apartamentos;
    }

    public void setApartamentos(List<Apartamento> apartamentos) {
        this.apartamentos = apartamentos;
    }

    public float getTotalCUBs() throws Exception {
        totalCUBs = this.valorTotal / this.getCUB().getValorIndice();
        return totalCUBs;
    }

    public Indice getCUB() throws Exception {
        if (dataContrato != null) {
            CUB = new IndiceDao().getIndiceMonth(new Date(dataContrato.getTime()), (new TipoIndiceDao()).get("CUB").getId());
        } else {
            CUB = new IndiceDao().getIndiceMonth(new Date(), (new TipoIndiceDao()).get("CUB").getId());
        }
        if (CUB == null) {
            CUB = new IndiceDao().getLast((new TipoIndiceDao()).get("CUB").getId());
        }
        return CUB;
    }

    public void setCUB(Indice CUB) {
        this.CUB = CUB;
    }

    public Date getDataContrato() {
        return dataContrato;
    }

    public void setDataContrato(Date dataContrato) {
        this.dataContrato = dataContrato;
    }

    public List<Parcela> getParcelas() throws Exception {
        if (parcelas.isEmpty()) {
            ParcelaDao parcelaDao = new ParcelaDao();
            parcelas = parcelaDao.listContrato(id);
        }
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getMotivoDistrato() {
        return motivoDistrato;
    }

    public void setMotivoDistrato(String motivoDistrato) {
        this.motivoDistrato = motivoDistrato;
    }

    /**
     * 0 - Em andamento 1 - Encerrado 2 - Distratado
     *
     */
    public int getStatus() {
        return status;
    }

    /**
     * 0 - Em andamento 1 - Encerrado 2 - Distratado
     *
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public List<Vaga> getVagas() throws Exception {
        if (vagas == null || vagas.isEmpty()) {
            VagaDao vagaDao = new VagaDao();
            vagas = vagaDao.getFromContrato(id);
        }
        return vagas;
    }

    public void setVagas(List<Vaga> vagas) {
        this.vagas = vagas;
    }

    public boolean isNaPlanta() {
        return naPlanta;
    }

    public void setNaPlanta(boolean naPlanta) {
        this.naPlanta = naPlanta;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }

    public double getTotalEmAberto() {
        return totalEmAberto;
    }

    public void setTotalEmAberto(double totalEmAberto) {
        this.totalEmAberto = totalEmAberto;
    }

    public double getTotalCUBEmAberto() {
        return totalCUBEmAberto;
    }

    public void setTotalCUBEmAberto(double totalCUBEmAberto) {
        this.totalCUBEmAberto = totalCUBEmAberto;
    }

    public double getTotalEmAbertoCUBContrato() {
        return totalEmAbertoCUBContrato;
    }

    public void setTotalEmAbertoCUBContrato(double totalEmAbertoCUBContrato) {
        this.totalEmAbertoCUBContrato = totalEmAbertoCUBContrato;
    }
    
    public Indice getCUBContrato() throws Exception {
        IndiceMB indiceMB = new IndiceMB();
        return indiceMB.getCUBMes(dataContrato);
    }

    public String nomePrimeiroCliente() throws Exception {
        if (this.getClientes() != null && !this.getClientes().isEmpty()) {
            return this.getClientes().get(0).getPessoa().getName();
        } else {
            return "";
        }
    }

    public String apartamentosDesc() throws Exception {
        String aptos = "";
        this.getApartamentos();
        for (int i = 0; i < apartamentos.size(); i++) {
            Apartamento apto = apartamentos.get(i);
            if (!"".equalsIgnoreCase(aptos)) {
                aptos += " | ";
            }
            aptos += apto.getBloco().getPredio().getName() + " - " + apto.getName() + apto.getBloco().getIdentificacao().toUpperCase();

        }
        return aptos;
    }
    
    public String unidadesDesc() throws Exception {
        String aptos = "";
        this.getApartamentos();
        for (int i = 0; i < apartamentos.size(); i++) {
            Apartamento apto = apartamentos.get(i);
            if (!"".equalsIgnoreCase(aptos)) {
                aptos += " | ";
            }
            aptos += apto.getName();
            if(!(apto.getBloco().getPredio().getName().equalsIgnoreCase("Arboreto") && apto.getBloco().getIdentificacao().equalsIgnoreCase("A"))){
                aptos += apto.getBloco().getIdentificacao().toUpperCase();
            }

        }
        return aptos;
    }
    
    public String PredioDesc() throws Exception {
        String predio = "";
        this.getApartamentos();
        StringTokenizer sttk;
        for (Apartamento apto : apartamentos) {
            String predios[] = apto.getBloco().getPredio().getName().split(" ");
            predio = predios[0];
        }
        return predio;
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
        Contrato other = (Contrato) obj;
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
