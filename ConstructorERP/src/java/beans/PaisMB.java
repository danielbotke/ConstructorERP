package beans;

import dao.PaisDao;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.Pais;

/**
 * Classe do menagedBean do pais, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "paisMB")
public class PaisMB {

    private Pais bean;
    private PaisDao dao = new PaisDao();

    public Pais getBean() {
        if (bean == null) {
            bean = new Pais();
        }
        return bean;
    }

    public void setBean(Pais bean) {
        this.bean = bean;
    }

    public String salvar() {
        try{
            dao.save(bean);
            return "formLocalidades";
        } catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro","Erro ao salvar país. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }

    }

    public String novo() {
        bean = new Pais();
        EstadoMB estadoBean = new EstadoMB();
        return estadoBean.novo();
    }


    public List<Pais> getPaises() throws Exception {
        return dao.listAll();
    }
}
