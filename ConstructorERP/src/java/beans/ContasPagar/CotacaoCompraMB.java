package beans.ContasPagar;

import dao.ContasPagar.CotacaoCompraDao;
import dao.ContasPagar.ItemCompraDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasPagar.CotacaoCompra;
import modelo.ContasPagar.Fornecedor;
import modelo.ContasPagar.ItemCompra;
import modelo.ContasPagar.NotaFiscalCompra;
import modelo.ContasPagar.Produto;
import modelo.ContasPagar.Servico;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * Classe do menagedBean da cotação de compra, utilizada para interar as
 * informações deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "cotacaoCompraMB")
@ViewScoped
public class CotacaoCompraMB implements Serializable {

    private CotacaoCompra bean;
    private CotacaoCompraDao dao = new CotacaoCompraDao();
    private Fornecedor fornecedorSelecionado;
    private List<ItemCompra> itensCompra = new ArrayList<>();
    private ItemCompra editando;

    public CotacaoCompra getBean() {
        if (bean == null) {
            bean = new CotacaoCompra();
        }
        return bean;
    }

    public void setBean(CotacaoCompra bean) {
        this.bean = bean;
    }

    public Fornecedor getFornecedorSelecionado() {
        return fornecedorSelecionado;
    }

    public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
        this.fornecedorSelecionado = fornecedorSelecionado;
    }

    public List<ItemCompra> getItensCompra() {
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompra> itensCompra) {
        this.itensCompra = itensCompra;
    }

    public ItemCompra getEditando() {
        return editando;
    }

    public void setEditando(ItemCompra editando) {
        this.editando = editando;
    }

    public String salvar() {
        try {
            bean.setFornecedor(fornecedorSelecionado);
            dao.save(bean);
            ItemCompraDao itemDao = new ItemCompraDao();
            for (int i = 0; i < itensCompra.size(); i++) {
                itensCompra.get(i).setCotacao(bean);
                itemDao.save(itensCompra.get(i));
            }
            bean.setItensCompra(itensCompra);
            dao.save(bean);
            bean = new CotacaoCompra();
            itensCompra = new ArrayList<>();
            fornecedorSelecionado = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar cotação. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";

    }

    public void editar(CotacaoCompra cotac) throws Exception {
        bean = cotac;
        itensCompra = bean.getItensCompra();
        editando = itensCompra.get(0);
        fornecedorSelecionado = bean.getFornecedor();
    }

    public void aprovar(CotacaoCompra cotac) {
        String cotacaoFaturada = "";
        try {
            if(cotac.getSituacao() != 2 ){
            cotac.setSituacao(1);
            dao.save(cotac);
            } else {
                cotacaoFaturada = " Não é possível aprovar uma cotação já faturada.";
                throw new Exception();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao aprovar cotação." + cotacaoFaturada));
        }
    }

    public void faturar(CotacaoCompra cotac) {
        String cotacaoNaoAprovada = "";
        try {
            if (cotac.getSituacao() == 1) {
                NotaFiscalCompraMB notaMb = new NotaFiscalCompraMB();
                notaMb.novo();
                NotaFiscalCompra nota = notaMb.getBean();
//                nota.setCotacaoGeradora(cotac);
                nota.setDataContabil(new Date());
                nota.setDataInicioParcelas(cotac.getDataInicioParcelas());
                nota.setDiaMesVencimento(cotac.getDiaMesVencimento());
                nota.setDiasEntreParcelas(cotac.getDiasEntreParcelas());
                notaMb.setFornecedorSelecionado(cotac.getFornecedor());
                notaMb.setItensCompra(cotac.getItensCompra());
                nota.setPercentualDesconto(cotac.getPercentualDesconto());
                nota.setPrimeiroDiaDaSemana(cotac.getPrimeiroDiaDaSemana());
                nota.setQntParcelas(cotac.getQntParcelas());
                nota.setTipoParcelamento(cotac.getTipoParcelamento());
                nota.setValorDesconto(cotac.getValorDesconto());
                nota.setValorFrete(cotac.getValorFrete());
                notaMb.salvar(false);
                
                cotac.setSituacao(2);
                dao.save(cotac);
            } else {
                cotacaoNaoAprovada = " Não é possível faturar uma cotação não aprovada.";
                throw new Exception();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao faturar cotação." + cotacaoNaoAprovada));
        }
    }

    public void encerrar(CotacaoCompra cotac) {
        String cotacaoAprovada = "";
        try {
            if (cotac.getSituacao() != 2) {
                cotac.setSituacao(3);
                dao.save(cotac);
            } else {
                cotacaoAprovada = " Não é possível encerrar uma cotação faturada.";
                throw new Exception();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao encerrar cotação." + cotacaoAprovada));
        }
    }

    public String novo() {
        bean = new CotacaoCompra();
        bean.setDataCriacao(new Date());
        return "formCotacaoCompra";
    }

    public void deletar(CotacaoCompra cotacao) {
        try {

            dao.delete(cotacao);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir cotacao. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
    }

    public void itemEditado(ItemCompra item) {
        editando = item;
    }

    public List<CotacaoCompra> getCotacaoCompras() throws Exception {
        return dao.listAll();
    }

    public void newItem() {
        editando = new ItemCompra();
    }

    public void addItem() {
        // editando.setValorTotal();
        itensCompra.add(editando);
        editando = new ItemCompra();
    }
    
    public void removerItem(ItemCompra item){
        itensCompra.remove(item);
    }

    public double valorTotal() {
        double valorTotal = 0;
        for (int i = 0; i < itensCompra.size(); i++) {
            valorTotal += (itensCompra.get(i).getValorTotal());
        }
        valorTotal += bean.getValorFrete();
        valorTotal = valorTotal * (1 - (bean.getPercentualDesconto() / 100));
        valorTotal -= bean.getValorDesconto();
        return valorTotal;
    }

    public void onRowSelect(SelectEvent event) {
        if (event.getObject().getClass() == Produto.class) {
          //  editando = new ItemCompra();
            editando.setProduto((Produto) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('prodDialog').hide();");
        }
        if (event.getObject().getClass() == Servico.class) {
         //   editando = new ItemCompra();
            editando.setServico((Servico) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('servDialog').hide();");
        }
    }
}
