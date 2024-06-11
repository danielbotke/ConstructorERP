package beans.ContasReceber;

import dao.PessoaFisicaDao;
import dao.PessoaJuridicaDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Bloco;
import modelo.Cliente;
import modelo.Pessoa;
import modelo.Predio;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Classe do menagedBean do contrato, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "enviarEmailMB")
@ViewScoped
public class EnviarEmailMB implements Serializable {

    private HtmlEmail email;
    private Cliente clienteFiltro;
    private Predio predioFiltro;
    private Bloco blocoFiltro;
    private boolean apenasContratosAtivos = false;
    private boolean somenteClientes = false;
    private boolean somenteFornecedores = false;
    private String emailDeEnvio;
    private String nomeRemetente;
    private String password;
    private boolean colocarAssinatura = false;
    private List<Pessoa> pessoasFiltradas;
    private List<Pessoa> pessoasSelecionados;
    private String htmlConteudoEmail;
    private String assuntoEmail;
    private String errosDestinatários = "";
    private String errosRemetente = "";

    public HtmlEmail getEmail() {
        return email;
    }

    public void setEmail(HtmlEmail email) {
        this.email = email;
    }

    public Cliente getClienteFiltro() {
        return clienteFiltro;
    }

    public void setClienteFiltro(Cliente clienteFiltro) {
        this.clienteFiltro = clienteFiltro;
    }

    public Predio getPredioFiltro() {
        return predioFiltro;
    }

    public void setPredioFiltro(Predio predioFiltro) {
        this.predioFiltro = predioFiltro;
    }

    public Bloco getBlocoFiltro() {
        return blocoFiltro;
    }

    public void setBlocoFiltro(Bloco blocoFiltro) {
        this.blocoFiltro = blocoFiltro;
    }

    public String getEmailDeEnvio() {
        return emailDeEnvio;
    }

    public void setEmailDeEnvio(String emailDeEnvio) {
        this.emailDeEnvio = emailDeEnvio;
    }

    public String getNomeRemetente() {
        return nomeRemetente;
    }

    public void setNomeRemetente(String nomeRemetente) {
        this.nomeRemetente = nomeRemetente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Pessoa> getPessoasFiltradas() {
        return pessoasFiltradas;
    }

    public void setPessoasFiltradas(List<Pessoa> pessoasFiltradas) {
        this.pessoasFiltradas = pessoasFiltradas;
    }

    public List<Pessoa> getPessoasSelecionados() {
        return pessoasSelecionados;
    }

    public void setPessoasSelecionados(List<Pessoa> pessoasSelecionados) {
        this.pessoasSelecionados = pessoasSelecionados;
    }

    public String getHtmlConteudoEmail() {
        return htmlConteudoEmail;
    }

    public void setHtmlConteudoEmail(String htmlConteudoEmail) {
        this.htmlConteudoEmail = htmlConteudoEmail;
    }

    public String getAssuntoEmail() {
        return assuntoEmail;
    }

    public void setAssuntoEmail(String assuntoEmail) {
        this.assuntoEmail = assuntoEmail;
    }

    public String getErrosDestinatários() {
        return errosDestinatários;
    }

    public void setErrosDestinatários(String errosDestinatários) {
        this.errosDestinatários = errosDestinatários;
    }

    public String getErrosRemetente() {
        return errosRemetente;
    }

    public void setErrosRemetente(String errosRemetente) {
        this.errosRemetente = errosRemetente;
    }

    public boolean isApenasContratosAtivos() {
        return apenasContratosAtivos;
    }

    public void setApenasContratosAtivos(boolean apenasContratosAtivos) {
        this.apenasContratosAtivos = apenasContratosAtivos;
    }

    public boolean isSomenteClientes() {
        return somenteClientes;
    }

    public void setSomenteClientes(boolean somenteClientes) {
        this.somenteClientes = somenteClientes;
    }

    public boolean isSomenteFornecedores() {
        return somenteFornecedores;
    }

    public void setSomenteFornecedores(boolean somenteFornecedores) {
        this.somenteFornecedores = somenteFornecedores;
    }

    public boolean isColocarAssinatura() {
        return colocarAssinatura;
    }

    public void setColocarAssinatura(boolean colocarAssinatura) {
        this.colocarAssinatura = colocarAssinatura;
    }

    public void filtrarPessoas() throws Exception {
        PessoaFisicaDao pessoaFisicaDao = new PessoaFisicaDao();
        pessoasFiltradas = new ArrayList<>();

        int clienteID = clienteFiltro != null ? clienteFiltro.getId() : 0;
        int predioID = predioFiltro != null ? predioFiltro.getId() : 0;
        int blocoID = blocoFiltro != null ? blocoFiltro.getId() : 0;

        pessoasFiltradas.addAll(pessoaFisicaDao.listPessoaClientePredioBloco(clienteID, predioID, blocoID, apenasContratosAtivos, somenteClientes, somenteFornecedores));
    }

    public void selecionarTodas() {
        pessoasSelecionados = new ArrayList<>();
        pessoasSelecionados.addAll(pessoasFiltradas);
    }

    public void enviarEmail() throws EmailException {
        for (Pessoa pessoa : pessoasSelecionados) {
            email = new HtmlEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(emailDeEnvio, password));
            email.setSSLOnConnect(true);
            email.setFrom(emailDeEnvio, nomeRemetente);
            email.setSubject(assuntoEmail);
            if (colocarAssinatura) {
                email.setHtmlMsg(htmlConteudoEmail + "");
            } else {
                email.setHtmlMsg(htmlConteudoEmail);
            }

            try {
                if (!"".equalsIgnoreCase(pessoa.getEmail())) {
                    email.addTo(pessoa.getEmail());
                } else {
                    if ("".equalsIgnoreCase(errosDestinatários)) {
                        errosDestinatários = "Erro ao enviar email para: \n";
                    }
                    errosDestinatários += pessoa.getName() + " (Sem e-mail cadastrado);\n";
                }

                email.send();
                pessoasSelecionados = new ArrayList<>();
            } catch (EmailException e) {
                e.printStackTrace();
                if (e.getCause() != null && e.getCause().getClass().equals(javax.mail.AuthenticationFailedException.class)) {
                    errosRemetente = "E-mail de envio ou senha inválido.";

                } else if (e.getCause() != null && e.getCause().getClass().equals(javax.mail.internet.AddressException.class)) {
                    if ("".equalsIgnoreCase(errosDestinatários)) {
                        errosDestinatários = "Erro ao enviar email para: \n";
                    }
                    errosDestinatários += pessoa.getName() + " (E-mail cadastrado inválido);\n";
                }


            }
        }
        if (!"".equalsIgnoreCase(errosDestinatários)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", errosDestinatários));
        }
        if (!"".equalsIgnoreCase(errosRemetente)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", errosRemetente));
        }
    }
}
