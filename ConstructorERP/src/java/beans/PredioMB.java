package beans;

import dao.ContasPagar.ParcelaAPagarDao;
import dao.PredioDao;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.Apartamento;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.ParcelaAPagar;
import modelo.Predio;

/**
 * Classe do menagedBean do predio, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "predioMB")
@SessionScoped
public class PredioMB implements Serializable{

    private Predio bean;
    private PredioDao dao = new PredioDao();
    private int qntBlocos;
    
    public Predio getBean() {
        if (bean == null) {
            bean = new Predio();
        }
        return bean;
    }

    public void setBean(Predio bean) {
        this.bean = bean;
    }

    public int getQntBlocos() {
        return qntBlocos;
    }

    public void setQntBlocos(int qntBlocos) {
        this.qntBlocos = qntBlocos;
    }

    public String salvar() throws Exception {
        try{
            dao.save(bean);
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro","Erro ao salvar empreendimento. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        bean = dao.get(bean.getName());
        BlocoMB blocoMB = new BlocoMB();
        for (int i = 1; i <= qntBlocos; i++) {
            blocoMB.getBean().setIdentificacao(utils.StringUtils.numberToLeter(i));
            blocoMB.getBean().setPredio(bean);
            blocoMB.salvar();
            blocoMB.getBean().setId(0);
        }
        return "formPredio";
    }

    public String novo() {
        bean = new Predio();
        BlocoMB blocoMB = new BlocoMB();
        return blocoMB.novo();
    }

    public List<Predio> getPredios() throws Exception {
        return dao.listAll();
    }
    
     
   
}
