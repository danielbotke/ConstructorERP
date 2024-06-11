package beans.ContasReceber;

import dao.ContasReceber.ContratoDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.SessaoDireitosDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Cliente;
import modelo.ContasReceber.Parcela;
import modelo.ContasReceber.SessaoDireitos;

/**
 * Classe do menagedBean da sessao de direitos, utilizada para interar as
 * informações deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "sessaoDireitosMB")
@ViewScoped
public class SessaoDireitosMB implements Serializable {

    private SessaoDireitos bean;
    private SessaoDireitosDao dao = new SessaoDireitosDao();
    private List<Cliente> clientesSelecionados;
    private String clientes;
    private Date hoje = new Date();

    public SessaoDireitos getBean() {
        if (bean == null) {
            bean = new SessaoDireitos();
        }
        return bean;
    }

    public void setBean(SessaoDireitos bean) {
        this.bean = bean;
    }

    public String getClientes() {
        clientes = "";
        if (clientesSelecionados != null) {
            for (int i = 0; i < clientesSelecionados.size(); i++) {
                if (!"".equalsIgnoreCase(clientes)) {
                    clientes += " - ";
                }
                clientes += clientesSelecionados.get(i).getPessoa().getName();
            }
        }
        return clientes;
    }

    public List<Cliente> getClientesSelecionados() {
        return clientesSelecionados;
    }

    public void setClientesSelecionados(List<Cliente> clientesSelecionados) {
        this.clientesSelecionados = clientesSelecionados;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }
    

    public String salvar() {
        try {
            ContratoDao contratoDao = new ContratoDao();
            List<Cliente> clientesAntigos = new ArrayList<>();
            clientesAntigos.addAll(bean.getContrato().getClientes());
            bean.setClientesAntigos(clientesAntigos);
            bean.getContrato().setClientes(clientesSelecionados);
            contratoDao.save(bean.getContrato());
            dao.save(bean);
            if (bean.isSaldoRenegociado()) {
                List<Parcela> parcelasAbertas = (new ParcelaDao()).listAbertasContrato(bean.getContrato().getId());
                ParcelaDao parcelaDao = new ParcelaDao();
                for (int i = 0; i < parcelasAbertas.size(); i++) {
                    parcelasAbertas.get(i).setSituacao(5);
                    parcelasAbertas.get(i).setPaga(true);
                    parcelaDao.save(parcelasAbertas.get(i));
                }
            }
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar sessão de direitos. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return this.novo();

    }

    public String novo() {
        bean = new SessaoDireitos();
        clientesSelecionados = new ArrayList<>();
        clientes = "";
        return "formCessaoDireitos";
    }

    public void deletar(SessaoDireitos sessao) throws Exception {
        boolean sessaoMaisAtual = false;
        List<SessaoDireitos> sessoes = dao.listContrato(bean.getContrato().getId());
        for (int i = 0; i < sessoes.size(); i++) {
            if (sessoes.get(i).getDataSessao().after(bean.getDataSessao())) {
                sessaoMaisAtual = true;
            }

        }
        try {
            if (!sessaoMaisAtual) {
                ContratoMB contratoMB = new ContratoMB();
                bean.getContrato().setClientes(bean.getClientesAntigos());
                contratoMB.setBean(bean.getContrato());
                contratoMB.salvar();
                dao.delete(sessao);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir contrato. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
    }

    public List<SessaoDireitos> getSessoesDireito() throws Exception {
        return dao.listAll();
    }
}
