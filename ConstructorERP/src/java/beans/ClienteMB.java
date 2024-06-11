package beans;

import dao.ClienteDao;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Cliente;

/**
 * Classe do menagedBean do cliente, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "clienteMB")
@ViewScoped
public class ClienteMB {

    private Cliente bean;
    private ClienteDao dao = new ClienteDao();
    private Date hoje = new Date();
    List<Cliente> clientes;
    private boolean editando = false;

    public Cliente getBean() {
        if (bean == null) {
            bean = new Cliente();
        }
        return bean;
    }

    public void setBean(Cliente bean) {
        this.bean = bean;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }
    
    public String salvar() {
        try {
            dao.save(bean);
            editando = false;
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar cliente. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        
    }

    public String novo() {
        bean = new Cliente();
        return "formClientes";
    }

    public String editar(int cli) throws Exception {
        bean = dao.get(cli);
        editando = true;
        return "";
    }

    public String deletar(int cli) {
        try {
            dao.delete(dao.get(cli));
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir cliente. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public List<Cliente> getClientes() throws Exception {
        if (clientes == null || clientes.isEmpty()) {
            clientes = dao.listAll();
        }
        return clientes;
    }
}
