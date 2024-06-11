/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import dao.ReservaAptoDao;
import modelo.ContasReceber.Contrato;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Classe modelo para a criação do objeto apartaento, bem como para persistência
 * e acesso.
 *
 * @author Daniel
 */
@Entity
public class Apartamento implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "bloco_id", nullable = false)
    private Bloco bloco;
    @ManyToOne
    @JoinColumn(name = "contratoApart_id")
    private Contrato contrato;
    @Column(nullable = false)
    private float areaPrivativa;
    @Column(nullable = false)
    private float areaComum;
    @Column(nullable = false)
    private float areaTotal;
    @Column
    private float coeficienteProporc;
    @Column
    private float fracaoIdeal;
    @Column
    private float custoEstivativo;
    @Column
    private boolean vendido = false;
    @Column
    private boolean disponivelVenda = true;
    @Column
    private boolean distratado = false;

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

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

    public Bloco getBloco() {
        return bloco;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public float getAreaPrivativa() {
        return areaPrivativa;
    }

    public void setAreaPrivativa(float areaPrivativa) {
        this.areaPrivativa = areaPrivativa;
    }

    public float getAreaComum() {
        return areaComum;
    }

    public void setAreaComum(float areaComum) {
        this.areaComum = areaComum;
    }

    public float getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(float areaTotal) {
        this.areaTotal = areaTotal;
    }

    public float getCoeficienteProporc() {
        return coeficienteProporc;
    }

    public void setCoeficienteProporc(float coeficienteProporc) {
        this.coeficienteProporc = coeficienteProporc;
    }

    public float getFracaoIdeal() {
        return fracaoIdeal;
    }

    public void setFracaoIdeal(float fracaoIdeal) {
        this.fracaoIdeal = fracaoIdeal;
    }

    public float getCustoEstivativo() {
        return custoEstivativo;
    }

    public void setCustoEstivativo(float custoEstivativo) {
        this.custoEstivativo = custoEstivativo;
    }

    public boolean isDistratado() {
        return distratado;
    }

    public void setDistratado(boolean distratado) {
        this.distratado = distratado;
    }

    public boolean isDisponivelVenda() {
        return disponivelVenda;
    }

    public void setDisponivelVenda(boolean disponivelVenda) {
        this.disponivelVenda = disponivelVenda;
    }
    
    

    public boolean isReservado() throws Exception {
        if ((new ReservaAptoDao()).getLastApto(this) != null) {
            GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("America/Sao_Paulo"));
            long horas = (gc.getTime().getTime() - (new ReservaAptoDao()).getLastApto(this).getDataReserva().getTime()) / 3600000;
            if (horas <= 24) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public String getDescricao(){
        return this.getBloco().getPredio().getName() + " " + this.getName() + this.getBloco().getIdentificacao().toUpperCase();
    }

    public ReservaApto getUltimaReserva() throws Exception {
        return (new ReservaAptoDao()).getLastApto(this);
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
        Apartamento other = (Apartamento) obj;
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
