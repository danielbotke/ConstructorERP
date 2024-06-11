package beans.ContasReceber;

import dao.IndiceDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RecebimentoDao;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Cliente;
import modelo.Indice;
import modelo.ContasReceber.Parcela;
import modelo.ContasReceber.Recebimento;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * Classe do menagedBean do contrato, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "recebimentoMB")
@ViewScoped
public class RecebimentoMB implements Serializable {

    private Recebimento bean;
    private RecebimentoDao dao = new RecebimentoDao();
    private Cliente clienteSelecionado;
    private Date hoje = new Date();

    public Recebimento getBean() {
        if (bean == null) {
            bean = new Recebimento();
        }
        return bean;
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(Cliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public void setBean(Recebimento bean) {
        this.bean = bean;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }

    public String salvar() {
        if (bean.getValorRecebido() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Informe o valor do recebimento para que este possa ser salvo."));
            return "";
        }
        try {
            dao.save(bean);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar o recebimento. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        try {
            Indice indice = null;
            BigDecimal bd;
            if (bean.getParcela().isValorFixo()) {
                indice = new Indice();
                indice = (new IndiceDao()).getIndiceMonth(new Date(bean.getParcela().getDataVencimento().getTime()), bean.getParcela().getTipoIndice().getId());
                if (indice == null) {
                    indice.setValorIndice(bean.getParcela().getValorParcela() / bean.getParcela().getFatorIndice());
                }
                bean.setCUB(indice);
            } else {
                double saldoParcela = 0;
                if (bean.getParcela().getSituacao() == 0) {
                    saldoParcela = bean.getParcela().getValorParcela();
                } else {
                    saldoParcela = bean.getParcela().saldoParcela();
                }
                if (saldoParcela - bean.valorTotal() > 0.01) {
                    indice = (new IndiceDao()).getIndiceMonth(new Date(bean.getDataPagamento().getTime()), bean.getParcela().getTipoIndice().getId());
                    if (indice == null) {
                        indice = (new IndiceDao()).getLast(bean.getParcela().getTipoIndice().getId());
                    }
                    bean.setCUB(indice);
                } else {
                    indice = (new IndiceDao()).getIndiceMonth(new Date(bean.getParcela().getDataVencimento().getTime()), bean.getParcela().getTipoIndice().getId());
                    bean.setCUB(indice);
                    if (indice == null) {
                        indice = new Indice();
                        indice.setValorIndice(bean.getParcela().getValorParcela() / bean.getParcela().getFatorIndice());
                    }
                }
            }
            bd = new BigDecimal((bean.valorTotal()) / indice.getValorIndice()).setScale(4, RoundingMode.HALF_DOWN);
            if (bean.getParcela().getFatorIndice() - (bean.getParcela().getFatorIndicePagos() + bd.floatValue()) > 0.001) {
                bean.getParcela().setSituacao(1);
            } else {
                bean.getParcela().setSituacao(2);
                bean.getParcela().setPaga(true);
            }
            bean.getParcela().setFatorIndicePagos(bd.floatValue());
            new ParcelaDao().save(bean.getParcela());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao gravar recebimento da parcela, contacte o administrador do sistema."));
            return "";
        }
        bean = new Recebimento();
        return "";

    }

    public String novo() {
        bean = new Recebimento();
        return "formRecebimento";
    }

    public void editar(int receb) throws Exception {
        bean = dao.get(receb);
    }

    public String deletar(int receb) {
        try {
            bean = dao.get(receb);
            ParcelaDao parcelaDao = new ParcelaDao();
            Parcela parcela = parcelaDao.get(bean.getParcela().getId());
            dao.delete(dao.get(receb));
            if (dao.listParcela(parcela.getId()) == 0) {
                parcela.setPaga(false);
                parcela.setSituacao(0);
                parcela.setFatorIndicePagos(parcela.getFatorIndicePagos() * -1);
            } else if (parcela.saldoParcela() < parcela.getValorParcela()) {
                parcela.setPaga(false);
                parcela.setSituacao(1);
                Indice indice = bean.getCUB();
                if (indice == null) {
                    indice = (new IndiceDao()).getIndiceMonth(new Date(bean.getParcela().getDataVencimento().getTime()), bean.getParcela().getTipoIndice().getId());
                    if (indice == null) {
                        indice.setValorIndice(bean.getParcela().getValorParcela() / bean.getParcela().getFatorIndice());
                    }
                }
                float cubsRecebidos = (bean.valorTotal()) / indice.getValorIndice();
                BigDecimal bd = new BigDecimal(cubsRecebidos).setScale(4, RoundingMode.HALF_DOWN);
                parcela.setFatorIndicePagos(bd.floatValue() * -1);
            }
            parcelaDao.save(parcela);
            bean = new Recebimento();
            return "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir este recebimento. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public List<Recebimento> getRecebimentos() throws Exception {
        return dao.listAll();
    }

    public List<Recebimento> getRecebimentosClientes() throws Exception {
        if (clienteSelecionado != null) {
            return dao.recebClientes(clienteSelecionado.getId());
        } else {
            return new ArrayList<>();
        }
    }

    public List<Parcela> getParcelasCliente() throws Exception {
        if (clienteSelecionado != null) {
            return (new ParcelaDao()).listAbertasCliente(clienteSelecionado.getId());
        } else {
            return new ArrayList<>();
        }
    }

    public void onRowSelect(SelectEvent event) {
        if (event.getObject().getClass() == Cliente.class) {
            clienteSelecionado = (Cliente) event.getObject();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('cliDialog').hide();");
        }
        if (event.getObject().getClass() == Parcela.class) {
            bean.setParcela((Parcela) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('parcDialog').hide();");
        }
    }
}
