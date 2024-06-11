package beans.ContasPagar;

import dao.ContasPagar.ItemCompraDao;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.ItemCompra;
import modelo.ContasPagar.ParcelaAPagar;

/**
 * Classe do menagedBean do item compra, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "itemCompraMB")
@SessionScoped
public class ItemCompraMB implements Serializable {

    private ItemCompra bean;
    private ItemCompraDao dao = new ItemCompraDao();

    public ItemCompra getBean() {
        if (bean == null) {
            bean = new ItemCompra();
        }
        return bean;
    }

    public void setBean(ItemCompra bean) {
        this.bean = bean;
    }

    public void salvar() {
        try {
            dao.save(bean);
            bean = new ItemCompra();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }
    
    public void editar(ItemCompra item) {
        bean = item;
    }

    public String novo() {
        bean = new ItemCompra();
        return "formItemCompra";
    }

    public List<ItemCompra> getItemCompras() throws Exception {
        return dao.listAll();
    }
}
