package beans;

import dao.PessoaJuridicaDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.PessoaFisica;
import modelo.PessoaJuridica;

/**
 * Classe do menagedBean da pessoa jurídica, utilizada para interar as
 * informações deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "pessoaJuridicaMB")
@ViewScoped
public class PessoaJuridicaMB implements Serializable {

    private PessoaJuridica bean = new PessoaJuridica();
    private PessoaJuridicaDao dao = new PessoaJuridicaDao();
    private String representantes;

    public PessoaJuridica getBean() {
        if (bean == null) {
            bean = new PessoaJuridica();
        }
        return bean;
    }

    public void setBean(PessoaJuridica bean) {
        this.bean = bean;
    }

    public String getRepresentantes() {
        representantes = "";
        for (int i = 0; i < bean.getRepresentantes().size(); i++) {
            if (!"".equalsIgnoreCase(representantes)) {
                representantes += " - ";
            }
            representantes += bean.getRepresentantes().get(i).getName();
        }
        return representantes;
    }

    public String salvar() {
        try {
            dao.save(bean);
            ArrayList<PessoaFisica> repre = new ArrayList<>();
            repre.addAll(bean.getRepresentantes());
            bean = dao.getCNPJ(bean.getCNPJ());
            PessoaFisicaMB pessoaFisicaMB = new PessoaFisicaMB();
            for (int i = 0; i < repre.size(); i++) {
                repre.get(i).setRepresentada(bean);
                pessoaFisicaMB.setBean(repre.get(i));
                pessoaFisicaMB.salvar();
            }
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar pessoa jurídica. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }

    }

    public String novo() {
        bean = new PessoaJuridica();
        return "formPessoaJuridica";
    }

    public void editar(PessoaJuridica parc) {
        bean = parc;
    }

    public void deletar(PessoaJuridica parc) {
        try {
            dao.delete(parc);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir pessoa jurídica. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
    }

    public List<PessoaJuridica> getPessoaJuridicas() throws Exception {
        return dao.listAll();
    }
}
