package beans.ContasReceber;

import dao.ContasReceber.HistoricoDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RenegociacaoDao;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.Cliente;
import modelo.Historico;
import modelo.ContasReceber.Parcela;
import modelo.ContasReceber.Renegociacao;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import utils.JpaUtil;

/**
 * Classe do menagedBean do bloco, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "renegociacaoMB")
@ViewScoped
public class RenegociacaoMB implements Serializable {

    private Renegociacao bean;
    private RenegociacaoDao dao = new RenegociacaoDao();
    private Historico historico = new Historico();
    private Cliente cliente;
    private List<Parcela> parcelasCliente;
    private List<Parcela> parcelasSelecionadas;
    private Date hoje = new Date();

    public Renegociacao getBean() {
        if (bean == null) {
            bean = new Renegociacao();
        }
        return bean;
    }

    public void setBean(Renegociacao bean) {
        this.bean = bean;
    }

    public Historico getHistorico() {
        return historico;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Parcela> getParcelasCliente() throws Exception {
        if (cliente != null) {
            parcelasCliente = (new ParcelaDao()).listAbertasCliente(cliente.getId());
        }
        return parcelasCliente;
    }

    public void setParcelasCliente(List<Parcela> parcelasCliente) {
        this.parcelasCliente = parcelasCliente;
    }

    public List<Parcela> getParcelasSelecionadas() {
        return parcelasSelecionadas;
    }

    public void setParcelasSelecionadas(List<Parcela> parcelasSelecionadas) {
        this.parcelasSelecionadas = parcelasSelecionadas;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }

    public String salvar() {
        try {
            bean.setDescricao("");
            dao.save(bean);
            bean.setParcelas(parcelasSelecionadas);
            bean.getDescricao();
            dao.save(bean);
            bean = new Renegociacao();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar Negociação. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return this.novo();

    }

    public String adicionarParcelas() {
        try {
            Renegociacao renegociacao = null;
            if (parcelasSelecionadas != null && !parcelasSelecionadas.isEmpty()) {
                renegociacao = dao.renegociacaoContrato(parcelasSelecionadas.get(0).getContrato().getId());
            }
            if (renegociacao == null) {
                return this.salvar();
            }
            this.setBean(renegociacao);
            bean.getParcelas().addAll(parcelasSelecionadas);
            bean.setDescricao(null);
            bean.getDescricao();
            dao.save(bean);
            bean = new Renegociacao();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar Negociação. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return this.novo();

    }

    public void imprimeRelatorio() throws IOException, SQLException {


        HashMap parameters = new HashMap();

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            facesContext.responseComplete();

            JasperPrint jasperPrint = JasperFillManager.fillReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/reports/negociacoes/negociacoes.jasper", parameters, JpaUtil.getConexao());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

            exporter.exportReport();

            byte[] bytes = baos.toByteArray();

            if (bytes != null && bytes.length > 0) {

                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

                response.setContentType("application/pdf");

                response.setHeader("Content-disposition", "inline; filename=\"negociacoesAbertas.pdf\"");

                response.setContentLength(bytes.length);

                ServletOutputStream outputStream = response.getOutputStream();

                outputStream.write(bytes, 0, bytes.length);

                outputStream.flush();

                outputStream.close();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public String encerrar() {
        try {
            bean.setEncerrada(true);
            dao.save(bean);
            bean = new Renegociacao();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao encerrar negociação. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return this.novo();

    }

    public void salvarHistoricoRenegociacao() {
        try {
            if (historico.getDataHistorico() != null && !"".equalsIgnoreCase(historico.getDescricao())) {
                historico.setTipo(bean.getClass().toString());
                historico.setIdObjetoHistorico(bean.getId());
            }
            HistoricoMB historicoMB = new HistoricoMB();
            historicoMB.setBean(historico);
            historicoMB.salvar();
            historico = new Historico();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar histórico da renegociação. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }

    }

    public String novo() {
        bean = new Renegociacao();
        historico = new Historico();
        return "formRenegociacao";
    }

    public List<Renegociacao> getRenegociacoes() throws Exception {
        return dao.listAll();
    }

    public List<Renegociacao> getRenegociacoesAbertas() throws Exception {
        return dao.listAbertas();
    }

    public List<Historico> getHistoricoRenegociacao() throws Exception {
        return (new HistoricoDao()).listAll(bean.getClass().toString(), bean.getId());
    }

    public String parcelasToString() {
        String aux = "";
        if (parcelasSelecionadas != null) {
            for (int i = 0; i < parcelasSelecionadas.size(); i++) {
                aux += parcelasSelecionadas.get(i).getNumeracao();
                if (i < (parcelasSelecionadas.size() - 1)) {
                    aux += " - ";
                }
            }
        }
        return aux;
    }
}
