package beans.ContasReceber;

import dao.ContasReceber.HistoricoDao;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.Apartamento;
import modelo.Historico;

/**
 * Classe do menagedBean do bloco, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "historicoMB")
@SessionScoped
public class HistoricoMB implements Serializable {

    private Historico bean;
    private HistoricoDao dao = new HistoricoDao();

    public Historico getBean() {
        if (bean == null) {
            bean = new Historico();
        }
        return bean;
    }

    public void setBean(Historico bean) {
        this.bean = bean;
    }

    public String deletar(Historico hist) {
        try {
            dao.delete(hist);
            return "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir histórico. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public void salvar() {
        try {
            dao.save(bean);
            bean = new Historico();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar historico. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public List<Historico> getHistoricos(String tipo, int idObjetoHistorico) throws Exception {
        return dao.listAll(tipo, idObjetoHistorico);
    }
}
