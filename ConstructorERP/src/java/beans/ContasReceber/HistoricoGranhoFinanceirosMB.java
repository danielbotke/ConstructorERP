package beans.ContasReceber;

import dao.ContasReceber.ContratoDao;
import dao.ContasReceber.HistoricoGanhoFinanceiroDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RecebimentoDao;
import dao.IndiceDao;
import dao.TipoIndiceDao;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasReceber.Contrato;
import modelo.ContasReceber.HistoricoGanhoFinanceiro;
import modelo.Predio;
import modelo.TipoIndice;

/**
 * Classe do menagedBean da histórido de ganho financeiro, utilizada para
 * interar as informações deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "historicoGanhoFinanceiroMB")
@ViewScoped
public class HistoricoGranhoFinanceirosMB implements Serializable {

    private HistoricoGanhoFinanceiro bean = new HistoricoGanhoFinanceiro();
    private HistoricoGanhoFinanceiroDao dao = new HistoricoGanhoFinanceiroDao();
    private Predio predioSelecionado;
    private Map<Integer, Double> totais = new HashMap<Integer, Double>();
    private List<HistoricoGanhoFinanceiro> historicos;

    public HistoricoGanhoFinanceiro getBean() {
        if (bean == null) {
            bean = new HistoricoGanhoFinanceiro();
        }
        return bean;
    }

    public void setBean(HistoricoGanhoFinanceiro bean) {
        this.bean = bean;
    }

    public Predio getPredioSelecionado() {
        return predioSelecionado;
    }

    public void setPredioSelecionado(Predio predioSelecionado) {
        this.predioSelecionado = predioSelecionado;
    }

    public void newHistorico() {
        bean = new HistoricoGanhoFinanceiro();
    }

    public double getCalcularAtual() throws Exception {
        List<Contrato> contratos;
        ParcelaDao parcelaDao = new ParcelaDao();
        ContratoDao contratoDao = new ContratoDao();
        if (predioSelecionado == null) {
            predioSelecionado = new Predio();
        }
        contratos = contratoDao.listClientePredio(0, predioSelecionado.getId(),0);
        RecebimentoDao recebDao = new RecebimentoDao();
        double aux = 0;
        double fator = 0;
        for (int i = 0; i < contratos.size(); i++) {
            List<TipoIndice> tiposIndices = (new TipoIndiceDao()).listAll();
            for (TipoIndice tipoIndice : tiposIndices) {
                fator = parcelaDao.totalCUBAbertosContrato(contratos.get(i).getId(), tipoIndice.getId());
                aux += fator * (new IndiceDao()).getLast(tipoIndice.getId()).getValorIndice();
            }
            fator = 0;
        }
        return aux;
    }

    public void salvar() {
        try {
            bean.setGanhoFinanceiro(bean.getValorAtualizado() - bean.getValorMes());
            bean.setPercentual((bean.getGanhoFinanceiro() * 100) / bean.getValorMes());
            dao.save(bean);
            bean = new HistoricoGanhoFinanceiro();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public void editar(HistoricoGanhoFinanceiro hist) {
        bean = hist;
    }

    public String deletar(HistoricoGanhoFinanceiro hist) {
        try {
            dao.delete(hist);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir caegoria. Verifique se a parcela não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public List<HistoricoGanhoFinanceiro> getHistoricosDespesas() throws Exception {
        if (predioSelecionado == null) {
            historicos = dao.listAll();
        } else {
            historicos = dao.despesasPorPredio(predioSelecionado.getId());
        }
        double total;
        totais = new HashMap<Integer, Double>();
        for (HistoricoGanhoFinanceiro historicoGanhoFinanc : historicos) {
            if (totais.get(historicoGanhoFinanc.getMesAno().getYear() + 1900) != null) {
                total = totais.get(historicoGanhoFinanc.getMesAno().getYear() + 1900);
                total += historicoGanhoFinanc.getGanhoFinanceiro();
                totais.put(historicoGanhoFinanc.getMesAno().getYear() + 1900, total);
            } else {
                totais.put(historicoGanhoFinanc.getMesAno().getYear() + 1900, historicoGanhoFinanc.getGanhoFinanceiro());
            }
        }
        for (HistoricoGanhoFinanceiro historicoGanhoFinanc : historicos) {
            historicoGanhoFinanc.setTotalAno(totais.get(historicoGanhoFinanc.getMesAno().getYear() + 1900));
        }
        return historicos;

    }
}
