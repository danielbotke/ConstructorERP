package beans;

import dao.IndiceDao;
import dao.TipoIndiceDao;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import modelo.Indice;

/**
 * Classe do menagedBean do indice, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "indiceMB")
public class IndiceMB {

    private Indice bean;
    private IndiceDao dao = new IndiceDao();
    private Date hoje = new Date();
    private List<Indice> indices;
    @ManagedProperty("#{param.indice}")
    int indice;

    public Indice getBean() {
        if (bean == null) {
            bean = new Indice();
        }
        return bean;
    }

    public void setBean(Indice bean) {
        this.bean = bean;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public IndiceDao getDao() {
        return dao;
    }

    public void setDao(IndiceDao dao) {
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

            System.out.println(bean.getDataIndice());
            dao.save(bean);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar índice. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        bean = new Indice();
        return "formIndices";
    }

    public String novo() {
        bean = new Indice();
        return "formIndices";
    }

    public String editar() throws Exception {
        bean = dao.get(indice);
        return "formIndices";
    }

    public String deletar() {
        try {
            dao.delete(dao.get(indice));
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir índice. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public List<Indice> getIndices() throws Exception {
        if (indices == null || indices.isEmpty()){
            indices = dao.listAll();
        }
        return indices;
    }

    public Indice getLastCUB() throws Exception {
        bean = dao.getLast((new TipoIndiceDao()).get("CUB").getId());
        return bean;
    }
    
        public Indice getCUBMes(Date data) throws Exception {
        bean = dao.getIndiceMonth(data, (new TipoIndiceDao()).get("CUB").getId());
        return bean;
    }
}
