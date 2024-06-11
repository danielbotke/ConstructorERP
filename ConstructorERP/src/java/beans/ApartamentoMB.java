package beans;

import dao.ApartamentoDao;
import dao.BlocoDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Apartamento;
import modelo.Bloco;
import modelo.ContasPagar.ParcelaAPagar;
import modelo.Predio;

/**
 * Classe do menagedBean do apartamento, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "apartamentoMB")
@ViewScoped
public class ApartamentoMB implements Serializable {

    private Apartamento bean;
    private ApartamentoDao dao = new ApartamentoDao();
    private Bloco blocoSelecionado;
    private Predio predioSelecionado;
    private List<Bloco> blocosPredio;
    private List<Apartamento> apartamentos;
    private int de;
    private int ate;
    private Apartamento apartamentoEditando = new Apartamento();

    /*
     @ManagedProperty("#{param.apto}")
     int apto;
     */
    public Apartamento getBean() {
        if (bean == null) {
            bean = new Apartamento();
        }
        return bean;
    }

    public void setBean(Apartamento bean) {
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

    public Apartamento getApartamentoEditando() {
        return apartamentoEditando;
    }

    public void setApartamentoEditando(Apartamento apartamentoEditando) {
        this.apartamentoEditando = apartamentoEditando;
    }

    public void onPredioSelecionado() throws Exception {
        BlocoDao blocoDao = new BlocoDao();
        blocosPredio = new ArrayList<>();
        if (predioSelecionado != null) {
            blocosPredio = blocoDao.listPredio(predioSelecionado.getId());
        }
    }

    public Bloco getBlocoSelecionado() {
        return blocoSelecionado;
    }

    public void setBlocoSelecionado(Bloco blocoSelecionado) {
        this.blocoSelecionado = blocoSelecionado;
        bean.setBloco(blocoSelecionado);
    }

    public List<Bloco> getBlocosPredio() {
        return blocosPredio;
    }

    public Predio getPredioSelecionado() {
        return predioSelecionado;
    }

    public void setPredioSelecionado(Predio predioSelecionado) {
        this.predioSelecionado = predioSelecionado;
    }

    public String salvar() {
        for (int i = de; i <= ate; i++) {
            bean.setName(Integer.toString(i));
            try {
                if (bean.getBloco() == null && bean.getId() > 0){
                    bean.setBloco(dao.getBlocoApto(bean.getId()));
                }
                dao.save(bean);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar apartamento. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            }
            bean.setId(0);
        }
        return "formPredio";
    }

    public String novo() {
        bean = new Apartamento();
        return "formPredio";
    }

    public void editar(Apartamento apto) {
        bean = apto;
        blocoSelecionado = bean.getBloco();
        predioSelecionado = bean.getBloco().getPredio();
        de = Integer.parseInt(bean.getName());
        ate = de;
    }

    public String deletar(Apartamento apto) {
        try {
            dao.delete(apto);
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir apartamento. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public void newUnidade() {
        apartamentoEditando = new Apartamento();
    }

    public void addUnidade() {
        try {
            apartamentoEditando.setBloco(blocoSelecionado);
            dao.save(apartamentoEditando);

            if (apartamentoEditando.getId() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Unidade salva."));
            }
            apartamentoEditando = new Apartamento();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar unidade. Verifique se todos os dados foram preenchidos. Caso o erro persista consulte o administrador do sistema."));
        }
    }

    public List<Apartamento> getApartamentos() throws Exception {
        if (apartamentos == null || apartamentos.isEmpty()) {
            apartamentos = dao.listAll();
        }
        return apartamentos;
    }

    public List<Apartamento> getApartamentosBloco() throws Exception {
        if (bean != null && bean.getBloco() != null) {
            return dao.listBloco(blocoSelecionado.getId());
        }
        return new ArrayList<>();

    }

    public List<Apartamento> getApartamentosDisponiveisPredio() throws Exception {
        if (bean != null) {
            return dao.listDisponiveisPredio(bean.getId());
        }
        return new ArrayList<>();

    }
}
