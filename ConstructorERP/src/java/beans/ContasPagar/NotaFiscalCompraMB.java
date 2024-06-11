package beans.ContasPagar;

import dao.ContasPagar.ItemCompraDao;
import dao.ContasPagar.NotaFiscalCompraDao;
import dao.ContasPagar.ParcelaAPagarDao;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Bloco;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.Fornecedor;
import modelo.ContasPagar.ItemCompra;
import modelo.ContasPagar.NotaFiscalCompra;
import modelo.ContasPagar.ParcelaAPagar;
import modelo.ContasPagar.Produto;
import modelo.ContasPagar.Servico;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;
import utils.DateUtils;

/**
 * Classe do menagedBean da nota fiscal de compra, utilizada para interar as
 * informações deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "notaFiscalCompraMB")
@ViewScoped
public class NotaFiscalCompraMB implements Serializable {

    private NotaFiscalCompra bean;
    private NotaFiscalCompraDao dao = new NotaFiscalCompraDao();
    private Fornecedor fornecedorSelecionado;
    private Bloco blocoSelecionado;
    private List<ItemCompra> itensCompra = new ArrayList<>();
    private ItemCompra itemEditando;
    private ParcelaAPagar parcelaEditando = new ParcelaAPagar();
    private boolean editandoNota = false;
    private boolean adicionandoParcela = false;
    private boolean parcelaUnica = false;
    private TreeNode selectedNode;
    private Date dataContabil;
    private Date dataDe;
    private Date dataAte;

    public NotaFiscalCompra getBean() {
        if (bean == null) {
            bean = new NotaFiscalCompra();
        }
        return bean;
    }

    public void setBean(NotaFiscalCompra bean) {
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
        return itemEditando;
    }

    public void setEditando(ItemCompra editando) {
        this.itemEditando = editando;
    }

    public ParcelaAPagar getParcelaEditando() {
        return parcelaEditando;
    }

    public void setParcelaEditando(ParcelaAPagar parcelaEditando) {
        this.parcelaEditando = parcelaEditando;
    }

    public boolean isAdicionandoParcela() {
        return adicionandoParcela;
    }

    public void setAdicionandoParcela(boolean adicionandoParcela) {
        this.adicionandoParcela = adicionandoParcela;
    }

    public boolean isEditandoNota() {
        return editandoNota;
    }

    public void setEditandoNota(boolean editandoNota) {
        this.editandoNota = editandoNota;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Bloco getBlocoSelecionado() {
        return blocoSelecionado;
    }

    public void setBlocoSelecionado(Bloco blocoSelecionado) {
        this.blocoSelecionado = blocoSelecionado;
    }

    public Date getDataContabil() {
        return dataContabil;
    }

    public void setDataContabil(Date dataContabil) {
        this.dataContabil = dataContabil;
    }

    public Date getDataDe() {
        return dataDe;
    }

    public void setDataDe(Date dataDe) {
        this.dataDe = dataDe;
    }

    public Date getDataAte() {
        return dataAte;
    }

    public void setDataAte(Date dataAte) {
        this.dataAte = dataAte;
    }

    public boolean isParcelaUnica() {
        return parcelaUnica;
    }

    public void setParcelaUnica(boolean parcelaUnica) {
        this.parcelaUnica = parcelaUnica;
    }

    public String salvar(boolean editar) {
        try {
            bean.setFornecedor(fornecedorSelecionado);
            bean.setBloco(blocoSelecionado);
            bean.setDataContabil(dataContabil);
            if (bean.isSalario()) {
                bean.setCustoFuncionario(bean.getINSS() + bean.getFGTS());
            }
            if (selectedNode != null) {
                bean.setCentroDeCusto((Categoria) selectedNode.getData());
            }
            if (bean.getDataNota() == null) {
                bean.setDataNota(dataContabil);
            }
            dao.save(bean);
            /*  ItemCompraDao itemDao = new ItemCompraDao();
             for (int i = 0; i < itensCompra.size(); i++) {
             itensCompra.get(i).setNotaFiscal(bean);
             itemDao.save(itensCompra.get(i));
             }
             itensCompra = new ArrayList<>();
             */
            if (parcelaUnica) {
                ParcelaAPagar parcela = new ParcelaAPagar();
                parcela.setNota(bean);
                parcela.setAjusteContabil(bean.getAjusteContabil());
                parcela.setAlmoco(bean.getAlmoco());
                parcela.setBloco(bean.getBloco());
                parcela.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
                parcela.setCustoFuncionario(bean.getCustoFuncionario());
                parcela.setDataVencimento(bean.getDataContabil());
                parcela.setDescricao(bean.getObservacao());
                parcela.setExtra(bean.getExtra());
                parcela.setFGTS(bean.getFGTS());
                parcela.setINSS(bean.getINSS());
                parcela.setISS(bean.getISS());
                parcela.setMDO(bean.getMDO());
                parcela.setSalario(bean.isSalario());
                parcela.setValorParcela((float) bean.getTotal());
                (new ParcelaAPagarDao()).save(parcela);
                parcelaUnica = false;
            }
            if (!editar) {
                bean = new NotaFiscalCompra();
                fornecedorSelecionado = null;
                blocoSelecionado = null;
                editandoNota = false;
                dataContabil = null;
                parcelaUnica = false;
                selectedNode.setSelected(false);
                selectedNode = null;
            } else {
                editandoNota = true;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar despesa. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";

    }

    public void editar(NotaFiscalCompra nota) {
        bean = nota;
//        itensCompra = bean.getItensCompra();
//        parcelasNota = bean.getParcelas();
//        editando = itensCompra.get(0);
//        parcelaEditando = parcelasNota.get(0);
        fornecedorSelecionado = bean.getFornecedor();
        blocoSelecionado = bean.getBloco();
        dataContabil = bean.getDataContabil();
    }

    public void editar2(NotaFiscalCompra nota) throws Exception {
        bean = nota;
//        itensCompra = bean.getItensCompra();

//        editando = itensCompra.get(0);
        if (bean.getParcelas() != null && !bean.getParcelas().isEmpty()) {
            parcelaEditando = bean.getParcelas().get(0);
        }
        fornecedorSelecionado = bean.getFornecedor();
        editandoNota = true;
        blocoSelecionado = bean.getBloco();
        dataContabil = bean.getDataContabil();
    }

    public void calcelar(NotaFiscalCompra nota) {
        try {
            nota.setSituacao(1);
            dao.save(nota);
            ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
            List<ParcelaAPagar> parcelas = nota.getParcelas();
            for (ParcelaAPagar parcelaAPagar : parcelas) {
                parcelaAPagar.setPaga(true);
                parcelaAPagar.setSituacao(4);
                parcelaDao.save(parcelaAPagar);
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao cancelar despesa."));
        }
    }

    public String novo() {
        bean = new NotaFiscalCompra();
        dataContabil = new Date();
        return "formNotaFiscalCompraProv";
    }

    public String novoDespesas() {
        bean = new NotaFiscalCompra();
        dataContabil = new Date();
        return "formDespesas";
    }

    public void deletar(NotaFiscalCompra notaFiscal) {
        try {
            dao.delete(notaFiscal);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir despesa. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
    }

    public void itemEditado(ItemCompra item) {
        itemEditando = item;
    }

    public void parcelaEditada(ParcelaAPagar parcela) {
        parcelaEditando = parcela;
    }

    public List<NotaFiscalCompra> getNotaFiscalCompras() throws Exception {
        if (fornecedorSelecionado == null && selectedNode == null && bean.getDataNota() == null && bean.getDataContabil() == null && bean.getCodigo() == 0 && blocoSelecionado == null) {
            return dao.listAll();
        } else {
            if (fornecedorSelecionado == null) {
                fornecedorSelecionado = new Fornecedor();
            }

            if (blocoSelecionado == null) {
                blocoSelecionado = new Bloco();
            }
            Categoria categoriaFiltro = selectedNode != null ? (Categoria) selectedNode.getData() : new Categoria();
            return dao.listFiltros(fornecedorSelecionado.getId(), categoriaFiltro.getId(), bean.getCodigo(), blocoSelecionado.getId(), dataContabil, bean.getDataNota(), dataDe, dataAte);
        }
    }

    public void newItem() {
        itemEditando = new ItemCompra();
    }

    public void newParcela() {
        adicionandoParcela = true;
        parcelaEditando = new ParcelaAPagar();
    }

    public void addItem() {
        // editando.setValorTotal();
        itensCompra.add(itemEditando);
        itemEditando = new ItemCompra();
    }

    public void addParcela() throws Exception {
        parcelaEditando.setNota(bean);
        parcelaEditando.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
        parcelaEditando.setBloco(bean.getBloco());
        if ("".equalsIgnoreCase(parcelaEditando.getDescricao())) {
            parcelaEditando.setDescricao(bean.getObservacao());
        }
        ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
        try {
            parcelaDao.save(parcelaEditando);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        bean.getParcelas().add(parcelaEditando);
        adicionandoParcela = false;
        parcelaEditando = new ParcelaAPagar();
    }

    public void removerItem(ItemCompra item) {
        itensCompra.remove(item);
    }

    public void removerParcela(ParcelaAPagar parcela) {
        try {
            (new ParcelaAPagarDao()).delete(parcela);
            bean.getParcelas().remove(parcela);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao remover parcela."));
        }
    }

    public void gerarNotasDeParcelas() throws Exception {
        ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
        List<ParcelaAPagar> parcelas = parcelaDao.listSemNota();
        for (ParcelaAPagar parcelaAPagar : parcelas) {
            bean = new NotaFiscalCompra();
            bean.setAjusteContabil(parcelaAPagar.getAjusteContabil());
            bean.setAlmoco(parcelaAPagar.getAlmoco());
            bean.setBloco(parcelaAPagar.getBloco());
            bean.setCentroDeCusto(parcelaAPagar.getCategoriaParcelaAvulsa());
            bean.setCustoFuncionario(parcelaAPagar.getCustoFuncionario());
            bean.setDataContabil(parcelaAPagar.getDataVencimento());
            bean.setExtra(parcelaAPagar.getExtra());
            bean.setFGTS(parcelaAPagar.getFGTS());
            bean.setINSS(parcelaAPagar.getINSS());
            bean.setISS(parcelaAPagar.getISS());
            bean.setMDO(parcelaAPagar.getMDO());
            bean.setObservacao(parcelaAPagar.getDescricao());
            bean.setSalario(parcelaAPagar.isSalario());
            bean.setValorTotal(parcelaAPagar.getValorParcela());
            dao.save(bean);
            parcelaAPagar.setNota(bean);
            parcelaDao.save(parcelaAPagar);
        }
    }

    public void ajustarINSSeFGTS() throws Exception {
        String impostos[] = {"FGTS", "INSS"};
        int categorias[] = {15, 18};
        String funcionarios[] = {"cleiso", "guilherme", "dico", "cleber", "airton", "eduardo", "vadinho", "edemir", "jucemar", "amarildo", "alex", "anderson", "bruno", "cristiano", "leocir", "gelson", "pedro", "rodrigo", "eliazar", "daniel", "dani", "samara", "pedro", "tanise", "angelo"};
        List<NotaFiscalCompra> listNotas;
        NotaFiscalCompra aux;
        for (String imposto : impostos) {
            for (int categoria : categorias) {
                for (String funcionario : funcionarios) {
                    listNotas = dao.listNotasFuncionarioIposto(funcionario, imposto, categoria);
                    for (NotaFiscalCompra notaFiscalCompra : listNotas) {
                        if (notaFiscalCompra.getDataContabil().getMonth() != 12) {
                            aux = dao.getNotasFuncionarioDataSemImposto(funcionario, notaFiscalCompra.getDataContabil(), categoria);
                            if (aux != null) {
                                if (imposto.equalsIgnoreCase("INSS")) {
                                    aux.setINSS(notaFiscalCompra.getValorTotal());
                                } else if (imposto.equalsIgnoreCase("FGTS")) {
                                    aux.setFGTS(notaFiscalCompra.getValorTotal());
                                }
                                aux.setSalario(true);
                                fornecedorSelecionado = aux.getFornecedor();
                                blocoSelecionado = aux.getBloco();
                                dataContabil = aux.getDataContabil();
                                bean = aux;
                                this.salvar(false);
                                dao.delete(notaFiscalCompra);
                            }
                            aux = null;
                        }
                    }
                }
            }
        }
    }

    public String gerarParcelas() {
        ParcelaAPagar parcela;
        ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
        parcela = new ParcelaAPagar();
        parcela.setNota(bean);
        parcela.setBloco(bean.getBloco());
        parcela.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
        parcela.setDescricao(bean.getObservacao());
        Date aux;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        BigDecimal bd = new BigDecimal((bean.getValorTotal()) / bean.getQntParcelas()).setScale(2, RoundingMode.HALF_DOWN);
        //parcela.setValorParcela(bd.floatValue());
        if (bean != null && bean.getId() > 0) {
            switch (bean.getTipoParcelamento()) {
                case 1: //diaMesVencimento
                    if (bean.getDiaMesVencimento() == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar o dia do mês de vencimento da parcela."));
                        //return "";
                    }
                    aux = new Date();
                    gc.setTime(bean.getDataInicioParcelas());
                    gc.set(Calendar.HOUR_OF_DAY, 12);
                    gc.set(Calendar.DAY_OF_MONTH, bean.getDiaMesVencimento());
                    /*while (gc.getTime().before(aux)) {
                     gc.add(Calendar.MONTH, 1);
                     }*/
                    aux = gc.getTime();
                    for (int i = 0; i < bean.getQntParcelas(); i++) {
                        parcela.setDataVencimento(aux);
                        parcela.setValorParcela(bd.floatValue());
                        try {
                            parcelaDao.save(parcela);
                            bean.getParcelas().add(parcela);
                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                            return "";
                        }
                        parcela = new ParcelaAPagar();
                        parcela.setNota(bean);
                        parcela.setBloco(bean.getBloco());
                        parcela.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
                        parcela.setDescricao(bean.getObservacao());
                        gc.add(Calendar.MONTH, 1);
                        if (bean.getDiaMesVencimento() > 28) {
                            gc.set(Calendar.DAY_OF_MONTH, 1);
                            if (gc.getActualMaximum(Calendar.DAY_OF_MONTH) < bean.getDiaMesVencimento()) {
                                gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
                            } else {
                                gc.set(Calendar.DAY_OF_MONTH, bean.getDiaMesVencimento());
                            }
                        }
                        aux = gc.getTime();
                    }
                    break;
                case 2: //diasEntreParcelas
                    if (bean.getDiasEntreParcelas() == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar a quantidade de dias entre parcelas."));
                        return "";
                    }
                    aux = new Date();
                    gc.setTime(bean.getDataInicioParcelas());
                    while (gc.getTime().before(aux)) {
                        bean.setDataInicioParcelas(DateUtils.adicionarDias(bean.getDataInicioParcelas(), bean.getDiasEntreParcelas()));
                    }
                    aux = bean.getDataInicioParcelas();
                    for (int i = 0; i < bean.getQntParcelas(); i++) {
                        parcela.setDataVencimento(aux);
                        parcela.setValorParcela(bd.floatValue());
                        try {
                            parcelaDao.save(parcela);
                            bean.getParcelas().add(parcela);
                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                            return "";
                        }
                        parcela = new ParcelaAPagar();
                        parcela.setNota(bean);
                        parcela.setBloco(bean.getBloco());
                        parcela.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
                        parcela.setDescricao(bean.getObservacao());
                        aux = DateUtils.adicionarDias(aux, bean.getDiasEntreParcelas());
                    }
                    break;
                case 3: //primeiroDiaDaSemana
                    if (bean.getPrimeiroDiaDaSemana() == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar o dia da semana em que a parcela deve vencer."));
                        return "";
                    }
                    aux = new Date();
                    bean.setDataInicioParcelas(DateUtils.primeiroDiaSemana(bean.getDataInicioParcelas(), bean.getPrimeiroDiaDaSemana()));
                    while (bean.getDataInicioParcelas().before(aux)) {
                        gc.setTime(bean.getDataInicioParcelas());
                        gc.add(Calendar.MONTH, 1);
                        bean.setDataInicioParcelas(gc.getTime());
                        bean.setDataInicioParcelas(DateUtils.primeiroDiaSemana(bean.getDataInicioParcelas(), bean.getPrimeiroDiaDaSemana()));
                    }
                    aux = bean.getDataInicioParcelas();
                    for (int i = 0; i < bean.getQntParcelas(); i++) {
                        parcela.setDataVencimento(aux);
                        parcela.setValorParcela(bd.floatValue());
                        try {
                            parcelaDao.save(parcela);
                            bean.getParcelas().add(parcela);
                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                            return "";
                        }
                        parcela = new ParcelaAPagar();
                        parcela.setNota(bean);
                        parcela.setBloco(bean.getBloco());
                        parcela.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
                        parcela.setDescricao(bean.getObservacao());
                        aux = DateUtils.proximoMes(aux);
                        aux = DateUtils.primeiroDiaSemana(aux, bean.getPrimeiroDiaDaSemana());
                    }
                    break;
                case 4: //primeiroDiaUtil
                    if (bean.getDiasEntreParcelas() == 0) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar a quantidade de dias entre parcelas."));
                        return "";
                    }
                    aux = new Date();
                    bean.setDataInicioParcelas(DateUtils.primeiroDiaUtilMes(bean.getDataInicioParcelas()));
                    while (bean.getDataInicioParcelas().before(aux)) {
                        bean.setDataInicioParcelas(DateUtils.primeiroDiaUtilMes(bean.getDataInicioParcelas()));
                    }
                    aux = DateUtils.formatedDate(bean.getDataInicioParcelas());
                    for (int i = 0; i < bean.getQntParcelas(); i++) {
                        parcela.setDataVencimento(aux);
                        parcela.setValorParcela(bd.floatValue());
                        try {
                            parcelaDao.save(parcela);
                            bean.getParcelas().add(parcela);
                        } catch (Exception e) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                            return "";
                        }
                        parcela = new ParcelaAPagar();
                        parcela.setNota(bean);
                        parcela.setBloco(bean.getBloco());
                        parcela.setCategoriaParcelaAvulsa(bean.getCentroDeCusto());
                        parcela.setDescricao(bean.getObservacao());
                        aux = DateUtils.proximoMes(aux);
                        aux = DateUtils.primeiroDiaUtilMes(aux);
                    }
                    break;
            }
        }
        return "";
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
            itemEditando.setProduto((Produto) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('prodDialog').hide();");
        }
        if (event.getObject().getClass() == Servico.class) {
            //   editando = new ItemCompra();
            itemEditando.setServico((Servico) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('servDialog').hide();");
        }
    }
}
