package beans.ContasReceber;

import dao.BlocoDao;
import dao.VagaDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Bloco;
import modelo.Predio;
import modelo.Vaga;

/**
 * Classe do menagedBean da vaga, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "vagaContratoMB")
@ViewScoped
public class VagaContratoMB implements Serializable {

    private Vaga bean = new Vaga();
    private VagaDao dao = new VagaDao();
    private Predio predioSelecionado;
    private List<Bloco> blocosPredio;
    private List<Vaga> vagasPredioDisponiveis;
   
    public Vaga getBean() {
        if (bean == null) {
            bean = new Vaga();
        }
        return bean;
    }

    public void setBean(Vaga bean) {
        this.bean = bean;
    }

    public void onPredioSelecionado() throws Exception {
        BlocoDao blocoDao = new BlocoDao();
        blocosPredio = new ArrayList<>();
        if (predioSelecionado != null) {
            blocosPredio = blocoDao.listPredio(predioSelecionado.getId());
        }
    }

    public List<Bloco> getBlocosPredio() {
        return blocosPredio;
    }

    public Predio getPredioSelecionado() {
        return predioSelecionado;
    }

    public void setPredioSelecionado(Predio predioSelecionado) {
        this.predioSelecionado = predioSelecionado;
        bean.setPredio(predioSelecionado);
    }

    public List<Vaga> getVagasPredio() throws Exception {
        if (bean != null && bean.getPredio() != null) {
            return dao.listPredio(predioSelecionado.getId());
        }
        return new ArrayList<>();

    }
    public List<Vaga> getVagasPredioDisponiveis() throws Exception {
        if (bean != null && bean.getPredio() != null) {
            this.vagasPredioDisponiveis =  dao.listPredioDisponiveis(predioSelecionado.getId());
        }
        return vagasPredioDisponiveis;

    }
  
}
