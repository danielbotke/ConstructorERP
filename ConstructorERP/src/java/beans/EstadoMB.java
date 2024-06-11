package beans;

import dao.EstadoDao;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.Estado;

/**
 * Classe do menagedBean do estado, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "estadoMB")
public class EstadoMB {

    private Estado bean;
    private EstadoDao dao = new EstadoDao();

    public Estado getBean() {
        if (bean == null) {
            bean = new Estado();
        }
        return bean;
    }

    public void setBean(Estado bean) {
        this.bean = bean;
    }

    public String salvar() {
        try{
            dao.save(bean);
            return "formLocalidades";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar estado. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        
    }

    public String novo() {
        bean = new Estado();
        CidadeMB cidadeBean = new CidadeMB();
        return cidadeBean.novo();
    }

    public List<Estado> getEstados() throws Exception {
        return dao.listAll();
    }
}
