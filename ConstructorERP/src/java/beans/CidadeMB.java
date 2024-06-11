package beans;

import dao.CidadeDao;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.Cidade;

/**
 * Classe do menagedBean do cidade, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "cidadeMB")
public class CidadeMB {

    private Cidade bean;
    private CidadeDao dao = new CidadeDao();

    public Cidade getBean() {
        if (bean == null) {
            bean = new Cidade();
        }
        return bean;
    }

    public void setBean(Cidade bean) {
        this.bean = bean;
    }

    public String salvar() {
        try {
            dao.save(bean);
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar cidade. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public String novo() {
        bean = new Cidade();
        return "formLocalidades";
    }

    public List<Cidade> getCidades() throws Exception {
        return dao.listAll();
    }
}
