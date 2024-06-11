package beans;

import dao.BlocoDao;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Bloco;

/**
 * Classe do menagedBean do bloco, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "blocoMB")
@ViewScoped
public class BlocoMB implements Serializable {

    private Bloco bean;
    private BlocoDao dao = new BlocoDao();

    public Bloco getBean() {
        if (bean == null) {
            bean = new Bloco();
        }
        return bean;
    }

    public void setBean(Bloco bean) {
        this.bean = bean;
    }

    public void salvar() {
        try {
            dao.save(bean);
            bean = new Bloco();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public String novo() {
        ApartamentoMB apartamentoMB = (ApartamentoMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("apartamentoMB");
        if (apartamentoMB == null) {
            apartamentoMB = new ApartamentoMB();
        }
        return apartamentoMB.novo();
    }

    public void editar(Bloco bloco) {
        bean = bloco;
        bean.setId(bloco.getId());
    }

    public String deletar(Bloco bloco) {
        try {
            dao.delete(bloco);
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir bloco. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public List<Bloco> getBlocos() throws Exception {
        return dao.listAll();
    }
    
     public List<Bloco> getBlocosDisponiveisVenda() throws Exception {
        return dao.listDisponiveisVenda();
    }
}
