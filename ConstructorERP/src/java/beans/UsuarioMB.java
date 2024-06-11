package beans;

import dao.UsuarioDao;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import login.SessionContext;
import modelo.Usuario;

/**
 * Classe do menagedBean do pais, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "usuarioMB")
@ViewScoped
public class UsuarioMB {

    private Usuario bean;
    private UsuarioDao dao = new UsuarioDao();
    private List<Usuario> usuarios;
    private boolean editando = false;

    public Usuario getBean() {
        if (bean == null) {
            bean = new Usuario();
        }
        return bean;
    }

    public void setBean(Usuario bean) {
        this.bean = bean;
    }

    public boolean isEditando() {
        return editando;
    }

    public String salvar() {
        try {
            if (!"".equalsIgnoreCase(bean.getSenha())) {
                bean.setSenha(UsuarioLogadoMBImpl.convertStringToMd5(bean.getSenha()));
            } else {
                bean.setSenha(dao.get(bean.getEmail()).getSenha());
            }
            dao.save(bean);
            return "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar usuário. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }

    }

    public String novo() {
        bean = new Usuario();
        return "formUsuario";
    }

    public void editar(Usuario usu) {
        editando = true;
        bean = usu;
    }

    public void deletar(Usuario usu) {
        try {
            dao.delete(usu);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir parcela. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
    }

    public List<Usuario> getUsuarios() throws Exception {
        if (usuarios == null || usuarios.isEmpty()) {
            usuarios = dao.listAll();
        }
        return usuarios;
    }

    public boolean temPermissao(String tela) {
        switch (tela) {
            case "formClientes":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isCliente();
            case "formContrato":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isContrato();
            case "formContratoAluguel":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isContratoAluguel();
            case "formIndices":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isIndice();
            case "formLocalidades":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isLocalidades();
            case "formParcelas":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isParcelas();
            case "formPredio":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isPredio();
            case "formRecebimento":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRecebimento();
            case "formRelatorioParcelas":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelParcela();
            case "formRelatorioRecebimentos":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelReceb();
            case "formUsuario":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isUsuario();
            case "formPessoaFisica":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isPessoaFisica();
            case "formPessoaJuridica":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isPessoaJuridica();
            case "formCessaoDireitos":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isCessaoDireitos();
            case "formRelatorioSaldoDevedor":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelSaldoDevedor();
            case "formRelatorioSaldoContrato":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelSaldoContrato();
            case "formRenegociacao":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRenegociacao();
            case "formGerarBoletos":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isGerarBoleto();
            case "formGerarRemessa":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isGerarRemessa();
            case "formGerencBoletos":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isGerenciarBoletos();
            case "formProcessarRetorno":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isProcessarRetorno();
            case "formGerarSegundaViaBoletos":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isGerarSegundaViaBoletos();
            case "formUnidade":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isUnidade();
            case "formCategoria":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isCategoria();
            case "formServico":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isServico();
            case "formProduto":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isProduto();
            case "formFornecedor":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isFornecedor();
            case "formCotacaoCompra":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isCotacaoCompra();
            case "formNotaFiscalCompra":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isNotaFiscalCompra();
            case "formParcelaAPagar":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isParcelasAPagar();
            case "formGerencRemessas":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isGerencRemessas();
            case "formRelatorioDespesasPorCentroCusto":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelDespesasPorCentroCusto();
            case "formRelatorioGanhoFinanceiro":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelGanhoFinanceiro();
            case "formRelatorioControleISS":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isRelControleISS();
            case "formEnviarCampanha":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isEnviarCampanha();
            case "formReservarApto":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isReservarApto();
            case "formAdmReservarApto":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isAdministrarReservarApto();
            case "isImobiliaria":
                return ((Usuario) SessionContext.getInstance().getUsuarioLogado()).isImobiliaria();
            default:
                return false;
        }

    }
}
