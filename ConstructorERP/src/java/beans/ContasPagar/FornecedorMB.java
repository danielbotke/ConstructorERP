package beans.ContasPagar;

import dao.ContasPagar.CategoriaDao;
import dao.ContasPagar.FornecedorDao;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.Fornecedor;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Classe do menagedBean do fornecedor, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "fornecedorMB")
@ViewScoped
public class FornecedorMB {

    private Fornecedor bean;
    private FornecedorDao dao = new FornecedorDao();
    private Date hoje = new Date();
    private TreeNode root;
    private TreeNode selectedNode;
    List<Fornecedor> fornecedores;
    private boolean editando = false;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Nome", null);
        List<Categoria> orfaos = null;
        try {
            orfaos = (new CategoriaDao()).listOrfaos();
        } catch (Exception ex) {
            Logger.getLogger(FornecedorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeNode auxNode = null;
        for (int i = 0; i < orfaos.size(); i++) {
            auxNode = new DefaultTreeNode("categoria", orfaos.get(i), root);
            if (isEditando() && bean != null && bean.getCategoria() != null && bean.getCategoria().getName().equalsIgnoreCase(orfaos.get(i).getName())) {
                auxNode.setSelected(true);
            }
            root.getChildren().add(auxNode);

            try {
                this.processar(auxNode);
            } catch (Exception ex) {
                Logger.getLogger(FornecedorMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void processar(TreeNode pai) throws Exception {
        CategoriaDao catDao = new CategoriaDao();
        Categoria aux = catDao.getByName(((Categoria) pai.getData()).getName());
        List<Categoria> filhas = catDao.listFilhas(aux.getId());
        TreeNode auxNode = null;
        for (int i = 0; i < filhas.size(); i++) {
            auxNode = new DefaultTreeNode("categoria", filhas.get(i), pai);
            if (isEditando() && bean != null && bean.getCategoria() != null && bean.getCategoria().getName().equalsIgnoreCase(filhas.get(i).getName())) {
                auxNode.setSelected(true);
            }
            pai.getChildren().add(auxNode);
            this.processar(auxNode);

        }
    }

    public Fornecedor getBean() {
        if (bean == null) {
            bean = new Fornecedor();
        }
        return bean;
    }

    public void setBean(Fornecedor bean) {
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

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    

    public String salvar() {
        try {
            bean.setCategoria((Categoria) selectedNode.getData());
            dao.save(bean);
            editando = false;
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar fornecedor. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }

    }

    public String novo() {
        bean = new Fornecedor();
        return "formFornecedor";
    }

    public String editar(int cli) throws Exception {
        bean = dao.get(cli);
        editando = true;
        this.init();
        return "";
    }

    public String deletar(int cli) {
        try {
            dao.delete(dao.get(cli));
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir fornecedor. Verifique se o fornecedor não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public List<Fornecedor> getFornecedores() throws Exception {
        if (fornecedores == null || fornecedores.isEmpty()) {
            fornecedores = dao.listAll();
        }
        return fornecedores;
    }
}
