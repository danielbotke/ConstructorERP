package beans.ContasPagar;

import dao.ContasPagar.CategoriaDao;
import dao.ContasPagar.NotaFiscalCompraDao;
import dao.ContasPagar.ParcelaAPagarDao;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Bloco;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.Fornecedor;
import modelo.ContasPagar.NotaFiscalCompra;
import modelo.ContasPagar.ParcelaAPagar;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Classe do menagedBean utilizada para visualização e edição das parcelas à
 * pagar
 *
 * @author Daniel
 */
@ManagedBean(name = "parcelaAPagarMB")
@ViewScoped
public class ParcelaAPagarMB implements Serializable {

    private Fornecedor fornecedor;
    private ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
    private Date de;
    private Date ate;
    private Date dataPagamento = new Date();
    private List<ParcelaAPagar> parcelasAbertas;
    private List<ParcelaAPagar> parcelasSelecionadas;
    private double total;
    private boolean adicionandoParcela = false;
    private ParcelaAPagar parcelaEditando = new ParcelaAPagar();
    private boolean exibirParcelasPagas = false;
    private TreeNode selectedNode;
    private TreeNode selectedNodeFiltro;
    private TreeNode root;
    private Bloco blocoSelecionado;
    private int codNotaFiltro;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Nome", null);
        List<Categoria> orfaos = null;
        try {
            orfaos = (new CategoriaDao()).listOrfaos();
        } catch (Exception ex) {
            Logger.getLogger(ParcelaAPagarMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        TreeNode auxNode = null;
        for (int i = 0; i < orfaos.size(); i++) {
            auxNode = new DefaultTreeNode("categoria", orfaos.get(i), root);
            if (parcelaEditando != null && parcelaEditando.getCategoriaParcelaAvulsa() != null && parcelaEditando.getCategoriaParcelaAvulsa().getName().equalsIgnoreCase(orfaos.get(i).getName())) {
                auxNode.setSelected(true);
            }
            root.getChildren().add(auxNode);

            try {
                this.processar(auxNode);
            } catch (Exception ex) {
                Logger.getLogger(ParcelaAPagarMB.class.getName()).log(Level.SEVERE, null, ex);
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
            if (parcelaEditando != null && parcelaEditando.getCategoriaParcelaAvulsa() != null && parcelaEditando.getCategoriaParcelaAvulsa().getName().equalsIgnoreCase(filhas.get(i).getName())) {
                auxNode.setSelected(true);
            }
            pai.getChildren().add(auxNode);
            this.processar(auxNode);

        }
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Date getDe() {
        return de;
    }

    public void setDe(Date de) {
        this.de = de;
    }

    public Date getAte() {
        return ate;
    }

    public void setAte(Date ate) {
        this.ate = ate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<ParcelaAPagar> getParcelasAbertas() {
        return parcelasAbertas;
    }

    public List<ParcelaAPagar> getParcelasSelecionadas() {
        return parcelasSelecionadas;
    }

    public void setParcelasSelecionadas(List<ParcelaAPagar> parcelasSelecionadas) {
        this.parcelasSelecionadas = parcelasSelecionadas;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public boolean isAdicionandoParcela() {
        return adicionandoParcela;
    }

    public void setAdicionandoParcela(boolean adicionandoParcela) {
        this.adicionandoParcela = adicionandoParcela;
    }

    public ParcelaAPagar getParcelaEditando() {
        return parcelaEditando;
    }

    public void setParcelaEditando(ParcelaAPagar parcelaEditando) {
        this.parcelaEditando = parcelaEditando;
    }

    public boolean isExibirParcelasPagas() {
        return exibirParcelasPagas;
    }

    public void setExibirParcelasPagas(boolean exibirParcelasPagas) {
        this.exibirParcelasPagas = exibirParcelasPagas;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getSelectedNodeFiltro() {
        return selectedNodeFiltro;
    }

    public void setSelectedNodeFiltro(TreeNode selectedNodeFiltro) {
        this.selectedNodeFiltro = selectedNodeFiltro;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Bloco getBlocoSelecionado() {
        return blocoSelecionado;
    }

    public void setBlocoSelecionado(Bloco blocoSelecionado) {
        this.blocoSelecionado = blocoSelecionado;
    }

    public int getCodNotaFiltro() {
        return codNotaFiltro;
    }

    public void setCodNotaFiltro(int codNotaFiltro) {
        this.codNotaFiltro = codNotaFiltro;
    }

    public String novo() {
        return "formParcelaAPagar";
    }

    public void deletar(ParcelaAPagar parc) {
        try {

            parcelaDao.delete(parc);
            parcelasAbertas.remove(parc);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir parcela."));
        }
    }

    public void paga(ParcelaAPagar parcela) {
        try {
            if (dataPagamento != null) {
                parcela.setSituacao(2);
                parcela.setPaga(true);
                parcela.setDataPagamento(dataPagamento);
                parcelaDao.save(parcela);
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao pagar parcela."));
        }
    }

    public void cancelar(ParcelaAPagar parcela) {
        try {
            parcela.setSituacao(4);
            parcelaDao.save(parcela);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao calcelar parcela."));
        }
    }

    public void carregar() throws Exception {
        if (fornecedor == null) {
            fornecedor = new Fornecedor();
        }
        Categoria categoriaSelecionada = selectedNodeFiltro != null ? (Categoria) selectedNodeFiltro.getData() : new Categoria();
        Bloco blocoSelecionadoFiltro = blocoSelecionado != null ? blocoSelecionado : new Bloco();
        parcelasAbertas = parcelaDao.listParcelasFornecedoresPeriodo(fornecedor.getId(), categoriaSelecionada.getId(), blocoSelecionadoFiltro.getId(), codNotaFiltro, de, ate, exibirParcelasPagas);
        total = parcelaDao.totalParcelasFornecedorPeriodo(fornecedor.getId(), categoriaSelecionada.getId(), codNotaFiltro, de, ate, exibirParcelasPagas);
    }

    public void pagarTodas() {
        for (int i = 0; i < parcelasSelecionadas.size(); i++) {
            this.paga(parcelasSelecionadas.get(i));
        }
    }

    public void newParcela() {
        adicionandoParcela = true;
        parcelaEditando = new ParcelaAPagar();
    }

    public void addParcela() {
        try {
            parcelaEditando.setCategoriaParcelaAvulsa((Categoria) selectedNode.getData());
            parcelaEditando.setBloco(blocoSelecionado);
            if(parcelaEditando.isSalario()){
                parcelaEditando.setCustoFuncionario(parcelaEditando.getINSS() + parcelaEditando.getFGTS());
            }
            (new ParcelaAPagarDao()).save(parcelaEditando);
            selectedNode.setSelected(false);
            selectedNode = null;
            blocoSelecionado = null;
            if (parcelaEditando.getId() > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Parcela salva, para visualizá-la na lista de parcelas, pressione novamente o botão Carregar."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcela. Verifique se todos os dados foram preenchidos. Caso o erro persista consulte o administrador do sistema."));
        }
        adicionandoParcela = false;
        parcelaEditando = new ParcelaAPagar();
    }

    public void editParcela(ParcelaAPagar parcelaEdit) {
        Categoria aux = null;
        parcelaEditando = parcelaEdit;
        for (TreeNode node : root.getChildren()) {
            aux = (Categoria) node.getData();
            node.setSelected(false);
            if (aux.getId() == parcelaEditando.getCategoriaParcelaAvulsa().getId()) {
                node.setSelected(true);
                selectedNode = node;
            }
        }
        blocoSelecionado = parcelaEdit.getBloco();
        adicionandoParcela = true;
    }

    public void addParcelaNotasSem() throws Exception {
        parcelaEditando = new ParcelaAPagar();
        List<NotaFiscalCompra> notas = new NotaFiscalCompraDao().listSemParcelas();
        for (NotaFiscalCompra notaFiscalCompra : notas) {
            try {
                parcelaEditando.setCategoriaParcelaAvulsa(notaFiscalCompra.getCentroDeCusto());
                parcelaEditando.setDataPagamento(notaFiscalCompra.getDataContabil());
                parcelaEditando.setDataVencimento(notaFiscalCompra.getDataContabil());
                parcelaEditando.setNota(notaFiscalCompra);
                parcelaEditando.setFGTS(notaFiscalCompra.getFGTS());
                parcelaEditando.setINSS(notaFiscalCompra.getINSS());
                parcelaEditando.setISS(notaFiscalCompra.getISS());
                parcelaEditando.setMDO(notaFiscalCompra.getMDO());
                parcelaEditando.setPaga(true);
                parcelaEditando.setSituacao(2);
                parcelaEditando.setValorParcela((float) notaFiscalCompra.getValorTotal());
                parcelaEditando.setBloco(notaFiscalCompra.getBloco());
                parcelaEditando.setDescricao(notaFiscalCompra.getObservacao());
                (new ParcelaAPagarDao()).save(parcelaEditando);
                selectedNode = null;
                parcelaEditando = new ParcelaAPagar();
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcela. Verifique se todos os dados foram preenchidos. Caso o erro persista consulte o administrador do sistema."));
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Parcela salva, para visualizála na lista de parcelas, pressione novamente o botão Carregar."));
        adicionandoParcela = false;
    }

    public List listTotais() throws Exception {
        List totais = parcelaDao.listTotais(0, 0, 0, null, null);
        return totais;
    }

    public void ajustarEscritorioA() throws Exception {
        parcelaEditando = new ParcelaAPagar();
        List<ParcelaAPagar> parcelas = (new ParcelaAPagarDao()).listParcelasFornecedoresPeriodo(0, 15, 4, 0, null, null, true);
        ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
        try {
            for (ParcelaAPagar parcelaAPagar : parcelas) {
                if (parcelaAPagar.getINSS() > 0) {
                    parcelaEditando = new ParcelaAPagar();
                    parcelaEditando.setCategoriaParcelaAvulsa(parcelaAPagar.getCategoriaParcelaAvulsa());
                    parcelaEditando.setDataPagamento(parcelaAPagar.getDataPagamento());
                    parcelaEditando.setDataVencimento(parcelaAPagar.getDataVencimento());
                    parcelaEditando.setNota(parcelaAPagar.getNota());
                    parcelaEditando.setFGTS(0);
                    parcelaEditando.setINSS(0);
                    parcelaEditando.setISS(0);
                    parcelaEditando.setMDO(0);
                    parcelaEditando.setPaga(parcelaAPagar.isPaga());
                    parcelaEditando.setSituacao(parcelaAPagar.getSituacao());
                    parcelaEditando.setValorParcela((float) parcelaAPagar.getINSS());
                    parcelaEditando.setBloco(parcelaAPagar.getBloco());
                    parcelaEditando.setDescricao(parcelaAPagar.getDescricao() + " - INSS");
                    parcelaDao.save(parcelaEditando);
                    parcelaAPagar.setINSS(0);
                    parcelaDao.save(parcelaAPagar);
                }
                if (parcelaAPagar.getFGTS() > 0) {
                    parcelaEditando = new ParcelaAPagar();
                    parcelaEditando.setCategoriaParcelaAvulsa(parcelaAPagar.getCategoriaParcelaAvulsa());
                    parcelaEditando.setDataPagamento(parcelaAPagar.getDataPagamento());
                    parcelaEditando.setDataVencimento(parcelaAPagar.getDataVencimento());
                    parcelaEditando.setNota(parcelaAPagar.getNota());
                    parcelaEditando.setFGTS(0);
                    parcelaEditando.setINSS(0);
                    parcelaEditando.setISS(0);
                    parcelaEditando.setMDO(0);
                    parcelaEditando.setPaga(parcelaAPagar.isPaga());
                    parcelaEditando.setSituacao(parcelaAPagar.getSituacao());
                    parcelaEditando.setValorParcela((float) parcelaAPagar.getFGTS());
                    parcelaEditando.setBloco(parcelaAPagar.getBloco());
                    parcelaEditando.setDescricao(parcelaAPagar.getDescricao() + " - FGTS");
                    parcelaDao.save(parcelaEditando);
                    parcelaAPagar.setFGTS(0);
                    parcelaDao.save(parcelaAPagar);
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcela. Verifique se todos os dados foram preenchidos. Caso o erro persista consulte o administrador do sistema."));
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Parcela salva, para visualizála na lista de parcelas, pressione novamente o botão Carregar."));
        adicionandoParcela = false;
    }
     public void ajustarFuncionariosA() throws Exception {
        parcelaEditando = new ParcelaAPagar();
        List<ParcelaAPagar> parcelas = (new ParcelaAPagarDao()).listParcelasFornecedoresPeriodo(0, 18, 4, 0, null, null, true);
        ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
        try {
            for (ParcelaAPagar parcelaAPagar : parcelas) {
                if (parcelaAPagar.getINSS() > 0) {
                    parcelaEditando = new ParcelaAPagar();
                    parcelaEditando.setCategoriaParcelaAvulsa(parcelaAPagar.getCategoriaParcelaAvulsa());
                    parcelaEditando.setDataPagamento(parcelaAPagar.getDataPagamento());
                    parcelaEditando.setDataVencimento(parcelaAPagar.getDataVencimento());
                    parcelaEditando.setNota(parcelaAPagar.getNota());
                    parcelaEditando.setFGTS(0);
                    parcelaEditando.setINSS(0);
                    parcelaEditando.setISS(0);
                    parcelaEditando.setMDO(0);
                    parcelaEditando.setPaga(parcelaAPagar.isPaga());
                    parcelaEditando.setSituacao(parcelaAPagar.getSituacao());
                    parcelaEditando.setValorParcela((float) parcelaAPagar.getINSS());
                    parcelaEditando.setBloco(parcelaAPagar.getBloco());
                    parcelaEditando.setDescricao(parcelaAPagar.getDescricao() + " - INSS");
                    parcelaDao.save(parcelaEditando);
                    parcelaAPagar.setINSS(0);
                    parcelaDao.save(parcelaAPagar);
                }
                if (parcelaAPagar.getFGTS() > 0) {
                    parcelaEditando = new ParcelaAPagar();
                    parcelaEditando.setCategoriaParcelaAvulsa(parcelaAPagar.getCategoriaParcelaAvulsa());
                    parcelaEditando.setDataPagamento(parcelaAPagar.getDataPagamento());
                    parcelaEditando.setDataVencimento(parcelaAPagar.getDataVencimento());
                    parcelaEditando.setNota(parcelaAPagar.getNota());
                    parcelaEditando.setFGTS(0);
                    parcelaEditando.setINSS(0);
                    parcelaEditando.setISS(0);
                    parcelaEditando.setMDO(0);
                    parcelaEditando.setPaga(parcelaAPagar.isPaga());
                    parcelaEditando.setSituacao(parcelaAPagar.getSituacao());
                    parcelaEditando.setValorParcela((float) parcelaAPagar.getFGTS());
                    parcelaEditando.setBloco(parcelaAPagar.getBloco());
                    parcelaEditando.setDescricao(parcelaAPagar.getDescricao() + " - FGTS");
                    parcelaDao.save(parcelaEditando);
                    parcelaAPagar.setFGTS(0);
                    parcelaDao.save(parcelaAPagar);
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcela. Verifique se todos os dados foram preenchidos. Caso o erro persista consulte o administrador do sistema."));
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Parcela salva, para visualizála na lista de parcelas, pressione novamente o botão Carregar."));
        adicionandoParcela = false;
    }
     public void ajustarFuncionariosB() throws Exception {
        parcelaEditando = new ParcelaAPagar();
        List<ParcelaAPagar> parcelas = (new ParcelaAPagarDao()).listParcelasFornecedoresPeriodo(0, 18, 5, 0, null, null, true);
        ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
        try {
            for (ParcelaAPagar parcelaAPagar : parcelas) {
                if (parcelaAPagar.getINSS() > 0) {
                    parcelaEditando = new ParcelaAPagar();
                    parcelaEditando.setCategoriaParcelaAvulsa(parcelaAPagar.getCategoriaParcelaAvulsa());
                    parcelaEditando.setDataPagamento(parcelaAPagar.getDataPagamento());
                    parcelaEditando.setDataVencimento(parcelaAPagar.getDataVencimento());
                    parcelaEditando.setNota(parcelaAPagar.getNota());
                    parcelaEditando.setFGTS(0);
                    parcelaEditando.setINSS(0);
                    parcelaEditando.setISS(0);
                    parcelaEditando.setMDO(0);
                    parcelaEditando.setPaga(parcelaAPagar.isPaga());
                    parcelaEditando.setSituacao(parcelaAPagar.getSituacao());
                    parcelaEditando.setValorParcela((float) parcelaAPagar.getINSS());
                    parcelaEditando.setBloco(parcelaAPagar.getBloco());
                    parcelaEditando.setDescricao(parcelaAPagar.getDescricao() + " - INSS");
                    parcelaDao.save(parcelaEditando);
                    parcelaAPagar.setINSS(0);
                    parcelaDao.save(parcelaAPagar);
                }
                if (parcelaAPagar.getFGTS() > 0) {
                    parcelaEditando = new ParcelaAPagar();
                    parcelaEditando.setCategoriaParcelaAvulsa(parcelaAPagar.getCategoriaParcelaAvulsa());
                    parcelaEditando.setDataPagamento(parcelaAPagar.getDataPagamento());
                    parcelaEditando.setDataVencimento(parcelaAPagar.getDataVencimento());
                    parcelaEditando.setNota(parcelaAPagar.getNota());
                    parcelaEditando.setFGTS(0);
                    parcelaEditando.setINSS(0);
                    parcelaEditando.setISS(0);
                    parcelaEditando.setMDO(0);
                    parcelaEditando.setPaga(parcelaAPagar.isPaga());
                    parcelaEditando.setSituacao(parcelaAPagar.getSituacao());
                    parcelaEditando.setValorParcela((float) parcelaAPagar.getFGTS());
                    parcelaEditando.setBloco(parcelaAPagar.getBloco());
                    parcelaEditando.setDescricao(parcelaAPagar.getDescricao() + " - FGTS");
                    parcelaDao.save(parcelaEditando);
                    parcelaAPagar.setFGTS(0);
                    parcelaDao.save(parcelaAPagar);
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcela. Verifique se todos os dados foram preenchidos. Caso o erro persista consulte o administrador do sistema."));
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Parcela salva, para visualizála na lista de parcelas, pressione novamente o botão Carregar."));
        adicionandoParcela = false;
    }
}
