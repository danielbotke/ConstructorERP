package beans;

import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RecebimentoDao;
import dao.TotaisRecebimentosDao;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.TotaisRecebimentos;

/**
 * Classe do menagedBean do bloco, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "totaisRecebimentosMB")
@ViewScoped
public class TotaisRecebimentosMB implements Serializable {

    private TotaisRecebimentos bean;
    private TotaisRecebimentosDao dao = new TotaisRecebimentosDao();

    public TotaisRecebimentos getBean() {
        if (bean == null) {
            bean = new TotaisRecebimentos();
        }
        return bean;
    }

    public void setBean(TotaisRecebimentos bean) {
        this.bean = bean;
    }

    public void salvar() {
        try {
            dao.save(bean);
            bean = new TotaisRecebimentos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar totais. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public List<TotaisRecebimentos> getTotaisRecebimentos() throws Exception {
        return dao.listAll();
    }

    public TotaisRecebimentos getLast() throws Exception {
        return dao.getLast();
    }
    
    public void atualizar() throws Exception{
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
        double[] recebidos = new double[31];
        double[] aReceber = new double[31];
        for (int i = 0; i <= 30; i++) {
            recebidos[i] = (new RecebimentoDao()).totalRecebido(0, 0, gc.getTime(), gc.getTime());
           // aReceber[i] = (new ParcelaDao()).totalFatorIndiceAbertosParcelas(0, 0, gc.getTime(), gc.getTime(), true);
        }
        
        (new RecebimentoDao()).listRecebimentosClientePredio(0, 0, null, null);
        this.getLast();
    }
}
