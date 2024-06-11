package beans.ContasPagar;

import dao.ContasPagar.CategoriaDao;
import dao.ContasPagar.ProdutoDao;
import dao.ContasPagar.UnidadeDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.Produto;
import modelo.ContasPagar.Unidade;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Classe do menagedBean do serviço, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "produtoMB")
@ViewScoped
public class ProdutoMB implements Serializable {

    private Produto bean;
    private ProdutoDao dao = new ProdutoDao();
    private TreeNode selectedNode;
    private TreeNode root;
    private List<Unidade> outrasUnidadesSelecionadas;

    @PostConstruct
    public void init()  {
        root = new DefaultTreeNode("Nome", null);
        List<Categoria> orfaos = null;
        try {
            orfaos = (new CategoriaDao()).listOrfaos();
        } catch (Exception ex) {
            Logger.getLogger(ProdutoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeNode auxNode = null;
        for (int i = 0; i < orfaos.size(); i++) {
            auxNode = new DefaultTreeNode("categoria", orfaos.get(i), root);
            if (bean != null && bean.getCategoria() != null && bean.getCategoria().getName().equalsIgnoreCase(orfaos.get(i).getName())) {
                auxNode.setSelected(true);
            }
            root.getChildren().add(auxNode);
            
            try {
                this.processar(auxNode);
            } catch (Exception ex) {
                Logger.getLogger(ProdutoMB.class.getName()).log(Level.SEVERE, null, ex);
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
            if (bean != null && bean.getCategoria() != null && bean.getCategoria().getName().equalsIgnoreCase(filhas.get(i).getName())) {
                auxNode.setSelected(true);
            }
            pai.getChildren().add(auxNode);
            this.processar(auxNode);

        }
    }

    public Produto getBean() {
        if (bean == null) {
            bean = new Produto();
        }
        return bean;
    }

    public void setBean(Produto bean) {
        this.bean = bean;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<Unidade> getOutrasUnidadesSelecionadas() {
        return outrasUnidadesSelecionadas;
    }

    public void setOutrasUnidadesSelecionadas(List<Unidade> outrasUnidadesSelecionadas) {
        this.outrasUnidadesSelecionadas = outrasUnidadesSelecionadas;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void salvar() {
        try {
            bean.setCategoria((Categoria) selectedNode.getData());
            dao.save(bean);
            bean.setOutrasUnidades(outrasUnidadesSelecionadas);
            dao.save(bean);
            bean = new Produto();
            outrasUnidadesSelecionadas = new ArrayList<>();
            selectedNode = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar bloco. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public void editar(Produto serv) throws Exception {
        bean = serv;
        outrasUnidadesSelecionadas = (new UnidadeDao()).listOutrasProduto(bean.getId());
        this.init();
    }

    public String novo() {
        bean = new Produto();
        return "formProduto";
    }

    public List<Produto> getProdutos() throws Exception {
        return dao.listAll();
    }

    public List<Unidade> getUnidadesNaoSelecionadas() throws Exception {
        if (bean.getUnidadePadrao() != null) {
            return (new UnidadeDao()).listOutras(bean.getUnidadePadrao());
        } else {
            return new ArrayList<>();
        }
    }
}
