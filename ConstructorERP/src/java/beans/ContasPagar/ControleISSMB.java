package beans.ContasPagar;

import dao.ContasPagar.NotaFiscalCompraDao;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.NotaFiscalCompra;
import modelo.ContasPagar.ParcelaAPagar;

/**
 * Classe do menagedBean utilizada para geração dos relatórios de recebimentos
 *
 * @author Daniel
 */
@ManagedBean(name = "controleISSMB")
@ViewScoped
public class ControleISSMB implements Serializable {
    
    private Date de;
    private Date ate;
    private List<NotaFiscalCompra> despesasFiltradas;
    private List<NotaFiscalCompra> despesasSelecionadas;
    private boolean pagas;
    private double total;
    private Date dataPagamento = new Date();
    private NotaFiscalCompraDao notaDao = new NotaFiscalCompraDao();
        
    public Date getDe() {
        return de;
    }
    
    public void setDe(Date de) {
        this.de = de;
    }
    
    public Date getAte() {
        return ate;
    }
    
    public void setAte(Date ate) {
        this.ate = ate;
    }
    
    public List<NotaFiscalCompra> getDespesasFiltradas() {
        return despesasFiltradas;
    }

    public void setDespesasFiltradas(List<NotaFiscalCompra> despesasFiltradas) {
        this.despesasFiltradas = despesasFiltradas;
    }

    public List<NotaFiscalCompra> getDespesasSelecionadas() {
        return despesasSelecionadas;
    }

    public void setDespesasSelecionadas(List<NotaFiscalCompra> despesasSelecionadas) {
        this.despesasSelecionadas = despesasSelecionadas;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    
    public boolean isPagas() {
        return pagas;
    }

    public void setPagas(boolean pagas) {
        this.pagas = pagas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
    
    public void carregar() throws Exception {
        despesasFiltradas = notaDao.listNotasComISS(de, ate, pagas);
        total = 0;
        for (int i = 0; i < despesasFiltradas.size(); i++) {
            total+= despesasFiltradas.get(i).getISS();
        }
    }
    
     public void paga(NotaFiscalCompra nota) {
        try {
            if (dataPagamento != null) {
                nota.setISSPago(true);
                notaDao.save(nota);
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao pagar ISS."));
        }
    }
     
      public void pagarTodas() {
        for (int i = 0; i < despesasSelecionadas.size(); i++) {
            this.paga(despesasSelecionadas.get(i));
        }
    }
    
    
}
