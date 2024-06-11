package beans.ContasReceber;

import beans.ApartamentoMB;
import beans.VagaMB;
import dao.ApartamentoDao;
import dao.ContasReceber.ContratoDao;
import dao.ContasReceber.ParcelaDao;
import dao.VagaDao;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Apartamento;
import modelo.Cliente;
import modelo.ContasReceber.Contrato;
import modelo.ContasReceber.Parcela;
import modelo.Vaga;
import org.primefaces.context.RequestContext;

/**
 * Classe do menagedBean do contrato, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "contratoMB")
@ViewScoped
public class ContratoMB implements Serializable {

    private Contrato bean;
    private ContratoDao dao = new ContratoDao();
    private String motivoDistrato;
    private List<Vaga> vagasSelecionadas;
    private List<Cliente> clientesSelecionados;
    private List<Apartamento> apartamentosSelecionados;
    private String vagas;
    private String clientes;
    private String aptos;
    private List<Contrato> contratos;
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

    public Contrato getBean() {
        if (bean == null) {
            bean = new Contrato();
        }
        return bean;
    }

    public void setBean(Contrato bean) {
        this.bean = bean;
    }

    public String getMotivoDistrato() {
        return motivoDistrato;
    }

    public void setMotivoDistrato(String motivoDistrato) {
        this.motivoDistrato = motivoDistrato;
    }

    public List<Vaga> getVagasSelecionadas() {
        return vagasSelecionadas;
    }

    public void setVagasSelecionadas(List<Vaga> vagasSelecionadas) {
        this.vagasSelecionadas = vagasSelecionadas;
    }

    public List<Apartamento> getApartamentosSelecionados() {
        return apartamentosSelecionados;
    }

    public void setApartamentosSelecionados(List<Apartamento> apartamentosSelecionados) {
        this.apartamentosSelecionados = apartamentosSelecionados;
    }

    public String getVagas() {
        vagas = "";
        if (vagasSelecionadas != null) {
            for (int i = 0; i < vagasSelecionadas.size(); i++) {
                if (!"".equalsIgnoreCase(vagas)) {
                    vagas += " - ";
                }
                vagas += vagasSelecionadas.get(i).getName();
            }
        }
        return vagas;
    }

    public String getAptos() {
        aptos = "";
        if (apartamentosSelecionados != null) {
            for (int i = 0; i < apartamentosSelecionados.size(); i++) {
                if (!"".equalsIgnoreCase(aptos)) {
                    aptos += " - ";
                }
                aptos += apartamentosSelecionados.get(i).getName();
            }
        }
        return aptos;
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

    public String salvar() throws Exception {
        Contrato old = dao.get(bean.getId());
        if (dao.save(bean)) {
            ApartamentoMB apartamentoMB = new ApartamentoMB();
            VagaMB vagaMB = new VagaMB();
            if (old != null) {
                boolean existe;
                if (vagasSelecionadas.size() > 0) {
                    for (int i = 0; i < old.getVagas().size(); i++) {
                        existe = false;
                        for (int j = 0; j < vagasSelecionadas.size(); j++) {
                            if (old.getVagas().get(i).getId() == vagasSelecionadas.get(j).getId()) {
                                existe = true;
                            }
                        }
                        if (!existe) {
                            old.getVagas().get(i).setVendido(false);
                            vagaMB.setBean(old.getVagas().get(i));
                            vagaMB.setDe(Integer.parseInt(old.getVagas().get(i).getName()));
                            vagaMB.setAte(Integer.parseInt(old.getVagas().get(i).getName()));
                            vagaMB.salvar();
                        }

                    }
                }
                if (apartamentosSelecionados.size() > 0) {
                    for (int i = 0; i < old.getApartamentos().size(); i++) {
                        existe = false;
                        for (int j = 0; j < apartamentosSelecionados.size(); j++) {
                            if (old.getApartamentos().get(i).getId() == apartamentosSelecionados.get(j).getId()) {
                                existe = true;
                            }
                        }
                        if (!existe) {
                            old.getApartamentos().get(i).setVendido(false);
                            apartamentoMB.setBean(old.getApartamentos().get(i));
                            apartamentoMB.setDe(Integer.parseInt(old.getApartamentos().get(i).getName()));
                            apartamentoMB.setAte(Integer.parseInt(old.getApartamentos().get(i).getName()));
                            apartamentoMB.salvar();
                        }

                    }
                }
            }
            for (int i = 0; i < apartamentosSelecionados.size(); i++) {
                apartamentosSelecionados.get(i).setVendido(true);
                apartamentosSelecionados.get(i).setContrato(bean);
                apartamentoMB.setBean(apartamentosSelecionados.get(i));
                apartamentoMB.setDe(Integer.parseInt(apartamentosSelecionados.get(i).getName()));
                apartamentoMB.setAte(Integer.parseInt(apartamentosSelecionados.get(i).getName()));
                apartamentoMB.salvar();
            }
            for (int i = 0; i < vagasSelecionadas.size(); i++) {
                vagasSelecionadas.get(i).setVendido(true);
                vagasSelecionadas.get(i).setContrato(bean);
                vagaMB.setBean(vagasSelecionadas.get(i));
                vagaMB.setDe(Integer.parseInt(vagasSelecionadas.get(i).getName()));
                vagaMB.setAte(Integer.parseInt(vagasSelecionadas.get(i).getName()));
                vagaMB.salvar();
            }
            bean = dao.get(bean.getId());
            if (clientesSelecionados.size() > 0) {
                bean.setClientes(clientesSelecionados);
            }
            dao.save(bean);
            return "formContrato";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar contrato. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";

    }

    public String novo() {
        bean = new Contrato();
        return "formContrato";
    }

    public void editar(Contrato contract) throws Exception {
        bean = contract;
        vagasSelecionadas = bean.getVagas();
        clientesSelecionados = bean.getClientes();
        apartamentosSelecionados = bean.getApartamentos();
//        return "formContrato";
    }

    public void deletar(Contrato contract) {
        try {
            ApartamentoDao apartamentoDao = new ApartamentoDao();
            for (int i = 0; i < bean.getApartamentos().size(); i++) {
                bean.getApartamentos().get(i).setVendido(false);
                try {
                    apartamentoDao.save(bean.getApartamentos().get(i));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao distratar contrato, contacte o administrador do sistema."));
                }
            }

            VagaDao vagaDao = new VagaDao();
            for (int i = 0; i < bean.getVagas().size(); i++) {
                bean.getVagas().get(i).setVendido(false);
                try {
                    vagaDao.save(bean.getVagas().get(i));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao distratar contrato, contacte o administrador do sistema."));
                }

            }
            dao.delete(contract);
            //return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir contrato. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            //return "";
        }
    }

    public String distrato() throws Exception {
        bean = dao.get(contract);
        bean.setMotivoDistrato(motivoDistrato);
        bean.setStatus(2);
        ParcelaDao parcelaDao = new ParcelaDao();
        List<Parcela> parcelas = parcelaDao.listContrato(bean.getId());
        for (int i = 0; i < parcelas.size(); i++) {
            parcelas.get(i).setPaga(true);
            parcelas.get(i).setSituacao(4);
            try {
                parcelaDao.save(parcelas.get(i));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao distratar contrato, contacte o administrador do sistema."));
                return "";
            }

        }

        ApartamentoDao apartamentoDao = new ApartamentoDao();
        for (int i = 0; i < bean.getApartamentos().size(); i++) {
            bean.getApartamentos().get(i).setVendido(false);
            bean.getApartamentos().get(i).setDistratado(true);
            try {
                apartamentoDao.save(bean.getApartamentos().get(i));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao distratar contrato, contacte o administrador do sistema."));
                return "";
            }
        }

        VagaDao vagaDao = new VagaDao();
        for (int i = 0; i < bean.getVagas().size(); i++) {
            bean.getVagas().get(i).setVendido(false);
            bean.getVagas().get(i).setDistratado(true);
            try {
                vagaDao.save(bean.getVagas().get(i));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao distratar contrato, contacte o administrador do sistema."));
                return "";
            }

        }

        try {
            if ("".equalsIgnoreCase(motivoDistrato) || null == bean.getMotivoDistrato()) {
                throw new EmptyStackException();
            }
            dao.save(bean);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao distratar contrato, contacte o administrador do sistema."));
            return "";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('distratoDialog').hide();");
        return novo();
    }

    public List<Contrato> getContratos() throws Exception {
        if (contratos == null || contratos.isEmpty()) {
            contratos = dao.listAll();
        }
        return contratos;
    }

    public List<Contrato> getContratosEmAndamento() throws Exception {
        return dao.listEmAndamento();
    }
}
