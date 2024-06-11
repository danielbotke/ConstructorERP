package beans.ContasPagar;

import dao.ContasPagar.CategoriaDao;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.Categoria;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Classe do menagedBean da categoria, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "categoriaMB")
@ViewScoped
public class CategoriaMB implements Serializable {

    private Categoria bean;
    private CategoriaDao dao = new CategoriaDao();
    private TreeNode root;
    private TreeNode selectedNode;

    @PostConstruct
    public void init()  {
        root = new DefaultTreeNode("Nome", null);
        List<Categoria> orfaos = null;
        try {
            orfaos = dao.listOrfaos();
        } catch (Exception ex) {
            Logger.getLogger(CategoriaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeNode auxNode = null;
        for (int i = 0; i < orfaos.size(); i++) {
            auxNode = new DefaultTreeNode("categoria", orfaos.get(i), root);
            root.getChildren().add(auxNode);
            try {
                this.processar(auxNode);
            } catch (Exception ex) {
                Logger.getLogger(CategoriaMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void processar(TreeNode pai) throws Exception {
        Categoria aux = dao.getByName(((Categoria)pai.getData()).getName());
        List<Categoria> filhas = dao.listFilhas(aux.getId());
        TreeNode auxNode = null;
        for (int i = 0; i < filhas.size(); i++) {
            auxNode = new DefaultTreeNode("categoria", filhas.get(i), pai);
            pai.getChildren().add(auxNode);
            this.processar(auxNode);

        }
    }

    public Categoria getBean() {
        if (bean == null) {
            bean = new Categoria();
        }
        return bean;
    }

    public void setBean(Categoria bean) {
        this.bean = bean;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
    
    public void salvar() {
        try {
            if(selectedNode != null){
                bean.setCategoriaPai((Categoria) selectedNode.getData());
            }
            dao.save(bean);
            bean = new Categoria();
            this.init();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public void editar(Categoria cat) {
        bean = cat;
    }

    public String deletar(Categoria cat) {
        try {
            dao.delete(cat);
            this.init();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir caegoria. Verifique se a parcela não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public String novo() {
        bean = new Categoria();
        return "formCategoria";
    }

    public List<Categoria> getCategorias() throws Exception {
        return dao.listAll();
    }
}
