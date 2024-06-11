package beans.ContasPagar;

import dao.ContasPagar.HistoricoDespesasDao;
import dao.ContasPagar.NotaFiscalCompraDao;
import dao.ContasPagar.ParcelaAPagarDao;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Bloco;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.HistoricoDespesas;

/**
 * Classe do menagedBean da histórido de despesas, utilizada para interar as
 * informações deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "historicoDespesasMB")
@ViewScoped
public class HistoricoDespesasMB implements Serializable {

    private HistoricoDespesas bean;
    private HistoricoDespesasDao dao = new HistoricoDespesasDao();

    public HistoricoDespesas getBean() {
        if (bean == null) {
            bean = new HistoricoDespesas();
        }
        return bean;
    }

    public void setBean(HistoricoDespesas bean) {
        this.bean = bean;
    }

    public void atualizar(int predioId, int blocoId, int categoriaId, Date de, Date ate) throws Exception {
        Date aux;
        List<Object[]> despesas = (new NotaFiscalCompraDao()).listTotaisNota(predioId, blocoId, blocoId, de, ate);
        dao.deleteAll();
        for (Object[] despesa : despesas) {
            aux = (Date) despesa[0];
            aux.setDate(1);
            bean = dao.get(((Categoria) despesa[1]).getId(), ((Bloco) despesa[2]).getId(), aux);
            if (bean != null && despesa[3] != null) {
                bean.setValor(bean.getValor() + (double) despesa[3]);
            } else if (despesa[2] != null && despesa[3] != null){
                bean = new HistoricoDespesas();
                bean.setCentroCusto((Categoria) despesa[1]);
                bean.setBloco((Bloco) despesa[2]);
                bean.setMesAno(aux);
                bean.setValor((double) despesa[3]);
            }
            this.salvar();
        }
        List<HistoricoDespesas> historicos = this.getHistoricosDespesas();
        Map<Date, Double> totais = new HashMap<Date, Double>();
        double total;
        for (HistoricoDespesas historicoDespesa : historicos) {
            if (totais.get(historicoDespesa.getMesAno()) != null) {
                total = totais.get(historicoDespesa.getMesAno());
                total += historicoDespesa.getValor();
                totais.put(historicoDespesa.getMesAno(), total);
            } else {
                totais.put(historicoDespesa.getMesAno(), historicoDespesa.getValor());
            }
        }
        for (HistoricoDespesas historicoDespesa : historicos) {
            historicoDespesa.setTotalPeriodo(totais.get(historicoDespesa.getMesAno()));
            dao.save(historicoDespesa);
        }

    }

    public void salvar() {
        try {

            dao.save(bean);
            bean = new HistoricoDespesas();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public void editar(HistoricoDespesas hist) {
        bean = hist;
    }

    public String deletar(HistoricoDespesas hist) {
        try {
            dao.delete(hist);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir caegoria. Verifique se a parcela não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public List<HistoricoDespesas> getHistoricosDespesas() throws Exception {
        return dao.listAll();
    }
}
