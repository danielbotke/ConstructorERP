package beans;

import dao.TipoIndiceDao;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.TipoIndice;

/**
 * Classe do menagedBean do tipoIndice, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "tipoIndiceMB")
public class TipoIndiceMB {

    private TipoIndice bean;
    private TipoIndiceDao dao = new TipoIndiceDao();

    public TipoIndice getBean() {
        if (bean == null) {
            bean = new TipoIndice();
        }
        return bean;
    }

    public void setBean(TipoIndice bean) {
        this.bean = bean;
    }

    public String salvar() {
        try {
            dao.save(bean);
            return "formIndices";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar tipo de índice. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public String novo() {
        bean = new TipoIndice();
        IndiceMB indiceMB = new IndiceMB();
        return indiceMB.novo();
    }

    public List<TipoIndice> getTipoIndices() throws Exception {
        return dao.listAll();
    }
}
