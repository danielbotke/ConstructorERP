package beans.ContasReceber;

import dao.ApartamentoDao;
import dao.BlocoDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Apartamento;
import modelo.Bloco;
import modelo.Predio;

/**
 * Classe do menagedBean do apartamento, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "apartamentoContratoMB")
@ViewScoped
public class ApartamentoContratoMB implements Serializable {

    private Apartamento bean = new Apartamento();
    private ApartamentoDao dao = new ApartamentoDao();
    private Bloco blocoSelecionado;
    private Predio predioSelecionado;
    private List<Bloco> blocosPredio;
    private List<Apartamento> apartamentosBlocoDisponiveis;

    public Apartamento getBean() {
        if (bean == null) {
            bean = new Apartamento();
        }
        return bean;
    }

    public void setBean(Apartamento bean) {
        this.bean = bean;
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

    public List<Apartamento> getApartamentosBloco() throws Exception {
        if (bean != null && bean.getBloco() != null) {
            return dao.listBloco(blocoSelecionado.getId());
        }
        return new ArrayList<>();

    }

    public List<Apartamento> getApartamentosBlocoDisponiveis() throws Exception {
        if (bean != null && bean.getBloco() != null) {
            this.apartamentosBlocoDisponiveis = dao.listBlocoDisponiveis(blocoSelecionado.getId());
        }
        return apartamentosBlocoDisponiveis;

    }
    
     public List<Apartamento> getApartamentosBlocoNaoVendidos() throws Exception {
        if (bean != null && bean.getBloco() != null) {
            this.apartamentosBlocoDisponiveis = dao.listBlocoNaoVendidos(blocoSelecionado.getId());
        }
        return apartamentosBlocoDisponiveis;

    }
    

    public List<Apartamento> getApartamentosBlocoDisponiveisEspecifico() throws Exception {
        this.apartamentosBlocoDisponiveis = dao.listBlocoDisponiveis(9);
        return apartamentosBlocoDisponiveis;

    }
}
