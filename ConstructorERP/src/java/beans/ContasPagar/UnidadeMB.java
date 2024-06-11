package beans.ContasPagar;

import dao.ContasPagar.UnidadeDao;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.Unidade;

/**
 * Classe do menagedBean da unidade, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "unidadeMB")
@SessionScoped
public class UnidadeMB implements Serializable {

    private Unidade bean;
    private UnidadeDao dao = new UnidadeDao();

    public Unidade getBean() {
        if (bean == null) {
            bean = new Unidade();
        }
        return bean;
    }

    public void setBean(Unidade bean) {
        this.bean = bean;
    }

    public void salvar() {
        try {
            dao.save(bean);
            bean = new Unidade();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public void editar(Unidade uni) {
        bean = uni;
    }

    public String deletar(Unidade unit) {
        try {
            dao.delete(unit);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir unidade. Verifique se a parcela não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public String novo() {
        bean = new Unidade();
        return "formUnidade";
    }

    public List<Unidade> getUnidades() throws Exception {
        return dao.listAll();
    }
}
