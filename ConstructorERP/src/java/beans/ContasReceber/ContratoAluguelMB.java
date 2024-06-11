package beans.ContasReceber;

import dao.ContasReceber.ContratoAluguelDao;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Apartamento;
import modelo.Cliente;
import modelo.ContasReceber.ContratoAluguel;

/**
 * Classe do menagedBean do contrato de aluguel, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "contratoAluguelMB")
@ViewScoped
public class ContratoAluguelMB implements Serializable {

    private ContratoAluguel bean = new ContratoAluguel();
    private ContratoAluguelDao dao = new ContratoAluguelDao();
    private List<Cliente> clientesSelecionados;
    private Apartamento apartamentoSelecionado;
    private String clientes;
    private String aptos;
    private List<ContratoAluguel> contratos;
    int contract;

    public int getContract() {
        return contract;
    }

    public void setContract(int contract) {
        this.contract = contract;
    }

    public void selecionarContract(int contract) {
        this.contract = contract;
    }

    public ContratoAluguel getBean() {
        return bean;
    }

    public void setBean(ContratoAluguel bean) {
        this.bean = bean;
    }

    public ContratoAluguelDao getDao() {
        return dao;
    }

    public void setDao(ContratoAluguelDao dao) {
        this.dao = dao;
    }

    public String getAptos() {
        return aptos;
    }

    public void setAptos(String aptos) {
        this.aptos = aptos;
    }

    public List<ContratoAluguel> getContratos() throws Exception {
        contratos = dao.listAll();
        return contratos;
    }

    public void setContratos(List<ContratoAluguel> contratos) {
        this.contratos = contratos;
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

    public Apartamento getApartamentoSelecionado() {
        return apartamentoSelecionado;
    }

    public void setApartamentoSelecionado(Apartamento apartamentoSelecionado) {
        this.apartamentoSelecionado = apartamentoSelecionado;
    }
    
    

    public String salvar() {
        bean.setUnidade(apartamentoSelecionado);
                try{
            dao.save(bean);
            bean.setClientes(clientesSelecionados);
            dao.save(bean);
        } catch(Exception e){
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar contrato. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";

    }

    public String novo() {
        return "formContratoAluguel";
    }

    public void editar(ContratoAluguel contract) throws Exception {
        bean = contract;
        clientesSelecionados = bean.getClientes();
        apartamentoSelecionado = bean.getUnidade();
//        return "formContrato";
    }

    public void deletar(ContratoAluguel contract) {
        try {
            dao.delete(contract);
            //return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir contrato. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            //return "";
        }
    }

    public String encerrar(ContratoAluguel contrato) {
        if(contrato.getStatus() == 0){
            contrato.setStatus(1);
        }else{
            contrato.setStatus(0);
        }
        try {
            dao.save(contrato);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao encerrar contrato, contacte o administrador do sistema."));
            return "";
        }
        return novo();
    }

    public List<ContratoAluguel> getContratosAluguel() throws Exception {
        if (contratos == null || contratos.isEmpty()) {
            contratos = dao.listAll();
        }
        return contratos;
    }

    public List<ContratoAluguel> getContratosEmAndamento() throws Exception {
        return dao.listEmAndamento();
    }
}
