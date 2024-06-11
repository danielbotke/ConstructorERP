package beans.ContasReceber;

import dao.BlocoDao;
import dao.ContasReceber.BoletoGeradoDao;
import dao.ContasReceber.NumerosRemessaDao;
import dao.PessoaJuridicaDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import modelo.ContasReceber.BoletoGerado;
import modelo.ContasReceber.NumerosRemessa;
import utils.RemessaBancaria;

/**
 * Classe do menagedBean do bloco, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "remessaBancariaMB")
@ViewScoped
public class RemessaBancariaMB implements Serializable {

    private List<BoletoGerado> boletosNaoArremessados;
    private NumerosRemessaDao dao = new NumerosRemessaDao();
    private NumerosRemessa numeroSelecionado = null;
    private List<BoletoGerado> boletosFiltrados;

    public NumerosRemessa getNumeroSelecionado() {
        return numeroSelecionado;
    }

    public void setNumeroSelecionado(NumerosRemessa numeroSelecionado) {
        this.numeroSelecionado = numeroSelecionado;
    }

    public List<BoletoGerado> getBoletosFiltrados() {
        return boletosFiltrados;
    }

    public void setBoletosFiltrados(List<BoletoGerado> boletosFiltrados) {
        this.boletosFiltrados = boletosFiltrados;
    }

    public void onRemessaSelecionada() throws Exception {
        if (numeroSelecionado != null) {
            boletosFiltrados = dao.listBoletos(numeroSelecionado.getId());
        }
    }

    public void gerarRemessa() {
        try {
            File file = (new RemessaBancaria()).geraRemessa((new PessoaJuridicaDao()), boletosNaoArremessados);
            for (BoletoGerado boleto : boletosNaoArremessados) {
                new BoletoGeradoDao().save(boleto);
            }
            this.downloadArquivo(file);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao gerar remessa. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public void downloadArquivo(File file) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\""); //header e o nome q vai aparecer na hr do donwload  
        response.setContentLength((int) file.length()); // tamanho do arquivo  
        response.setContentType("text/plain"); // tipo  

        try {
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[(int) file.length()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException ex) {
            System.out.println("Error in downloadFile: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String novo() {
        return "formGerarRemessa";
    }

    public String deletar( ) {
        try {
            for (int i = 0; i < boletosFiltrados.size(); i++) {
                boletosFiltrados.get(i).setNumerosRemessa(null);
                (new BoletoGeradoDao()).save(boletosFiltrados.get(i));
            }
            boletosFiltrados = new ArrayList<>();
            dao.delete(numeroSelecionado);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir remessa. Verifique se a remessa não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public List<BoletoGerado> getBoletosNaoArremessados() throws Exception {
        boletosNaoArremessados = (new BoletoGeradoDao()).listBoletosNaoArremessados();
        return boletosNaoArremessados;
    }

    public List<NumerosRemessa> getRemessas() throws Exception {
        return dao.listAll();
    }
}
