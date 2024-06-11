package beans;

import dao.VagaDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Predio;
import modelo.Vaga;
import sun.nio.cs.ext.ISCII91;

/**
 * Classe do menagedBean do apartamento, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "vagaMB")
@ViewScoped
public class VagaMB implements Serializable {

    private Vaga bean;
    private VagaDao dao = new VagaDao();
    private Predio predioSelecionado;
    private List<Vaga> vagas;
    private int de;
    private int ate;
    /*
     @ManagedProperty("#{param.apto}")
     int apto;
     */

    public Vaga getBean() {
        if (bean == null) {
            bean = new Vaga();
        }
        return bean;
    }

    public void setBean(Vaga bean) {
        this.bean = bean;
    }

    public int getDe() {
        return de;
    }

    public void setDe(int de) {
        this.de = de;
    }

    public int getAte() {
        return ate;
    }

    public void setAte(int ate) {
        this.ate = ate;
    }
    /*
     public int getApto() {
     return apto;
     }

     public void setApto(int apto) {
     this.apto = apto;
     }
     */

    public Predio getPredioSelecionado() {
        return predioSelecionado;
    }

    public void setPredioSelecionado(Predio predioSelecionado) {
        this.predioSelecionado = predioSelecionado;
        bean.setPredio(predioSelecionado);
    }

    public String salvar() {
        for (int i = de; i <= ate; i++) {
            bean.setName(Integer.toString(i));
            try {
                dao.save(bean);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar vaga. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            }
            bean.setId(0);
        }
        return "formPredio";
    }

    public String novo() {
        bean = new Vaga();
        return "formPredio";
    }

    public void editar(Vaga apto) {
        bean = apto;
        predioSelecionado = bean.getPredio();
        de = Integer.parseInt(bean.getName());
        ate = de;
    }

    public String deletar(Vaga apto) {
        try {
            dao.delete(apto);
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir apartamento. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public List<Vaga> getVagas() throws Exception {
        if (vagas == null || vagas.isEmpty()) {
            vagas = dao.listAll();
        }
        return vagas;
    }

    public List<Vaga> getVagasBloco() throws Exception {
        if (bean != null && bean.getPredio() != null) {
            return dao.listPredio(predioSelecionado.getId());
        }
        return new ArrayList<>();

    }
}
