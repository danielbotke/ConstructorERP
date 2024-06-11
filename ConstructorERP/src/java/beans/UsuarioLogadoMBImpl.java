package beans;

import dao.UsuarioDao;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import login.SessionContext;


/**
 * Classe do menagedBean do login, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
/**
 * * Controla o LOGIN e LOGOUT do Usuário *
 */
@ManagedBean(name = "usuarioLogadoMB")
@SessionScoped
public class UsuarioLogadoMBImpl {

    private UsuarioDao dao = new UsuarioDao();
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UsuarioLogadoMBImpl.class.getName());
    @ManagedProperty(value = "#{this}")
    private String email;
    private String login;
    private String senha;

    /**
     * * Retorna usuario logado *
     */
    public Usuario getUsuario() {
        return (Usuario) SessionContext.getInstance().getUsuarioLogado();
    }

    public String doLogin() {
        try {
            logger.info("Tentando logar com usuário " + login);
            Usuario user = this.isUsuarioReadyToLogin(login, senha);
            if (user == null) {
                logger.log(Level.SEVERE, "Login ou Senha errado, tente novamente !");
                FacesContext.getCurrentInstance().validationFailed();
                return "";
            }
            Usuario usuario = (Usuario) dao.get(user.getId());
            logger.info("Login efetuado com sucesso");
            SessionContext.getInstance().setAttribute("usuarioLogado", usuario);
            if (!usuario.isImobiliaria()) {
                return "/telas/index.jsf?faces-redirect=true";
            } else {
                return "/telas/formReservaApto.jsf?faces-redirect=true";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            FacesContext.getCurrentInstance().validationFailed();
            e.printStackTrace();
            return "";
        }
    }

    public String doLogout() {
        logger.info("Fazendo logout com usuário " + ((Usuario) SessionContext.getInstance().getUsuarioLogado()).getNome());
        SessionContext.getInstance().encerrarSessao();
        logger.info("Logout realizado com sucesso !");
        return "/login.jsf?faces-redirect=true";
    }

    public void solicitarNovaSenha() {
        try {
            this.gerarNovaSenha();
            logger.info("Nova Senha enviada para o email " + email);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Verifica se usuário existe ou se pode logar 
    public Usuario isUsuarioReadyToLogin(String email, String senha) throws Exception {
        try {
            email = email.toLowerCase().trim();
            logger.info("Verificando login do usuário " + email);
            return dao.getEmailSenha(email.trim(), convertStringToMd5(senha));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String convertStringToMd5(String valor) {
        MessageDigest mDigest;
        try {
            //Instanciamos o nosso HASH MD5, poderíamos usar outro como 
            //SHA, por exemplo, mas optamos por MD5. 
            mDigest = MessageDigest.getInstance("MD5");
            //Convert a String valor para um array de bytes em MD5 
            byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));
            //Convertemos os bytes para hexadecimal, assim podemos salvar 
            //no banco para posterior comparação se senhas 
            StringBuffer sb = new StringBuffer();
            for (byte b : valorMD5) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
            return null;
        }
    }

    public String gerarNovaSenha() {
        String[] carct = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String senha = "";
        for (int x = 0; x < 10; x++) {
            int j = (int) (Math.random() * carct.length);
            senha += carct[j];
        }
        return senha;
    }
}
