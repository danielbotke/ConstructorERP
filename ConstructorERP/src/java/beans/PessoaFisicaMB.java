package beans;

import dao.PessoaFisicaDao;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.PessoaFisica;

/**
 * Classe do menagedBean da pessoa física, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "pessoaFisicaMB")
@ViewScoped
public class PessoaFisicaMB implements Serializable {

    private PessoaFisica bean = new PessoaFisica();
    private PessoaFisicaDao dao = new PessoaFisicaDao();
    private Date hoje = new Date();
    private Date maiorIdade = new Date();

    public PessoaFisica getBean() {
        if (bean == null) {
            bean = new PessoaFisica();
        }
        return bean;
    }

    public void setBean(PessoaFisica bean) {
        this.bean = bean;
    }

    public PessoaFisicaDao getDao() {
        return dao;
    }

    public void setDao(PessoaFisicaDao dao) {
        this.dao = dao;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }

    public String salvar() {
        try {
            dao.save(bean);
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar pessoa física. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }

    }

    public String novo() {
        bean = new PessoaFisica();
        return "formPessoaFisica";
    }

    public void editar(PessoaFisica parc) {
        bean = parc;
    }

    public void deletar(PessoaFisica parc) {
        try {
            dao.delete(parc);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir pessoa física. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
    }

    public List<PessoaFisica> getPessoaFisicas() throws Exception {
        return dao.listAll();
    }

    public Date getMaiorIdade() {
        maiorIdade = hoje;
        maiorIdade.setYear(maiorIdade.getYear() - 18);
        return maiorIdade;
    }
}
