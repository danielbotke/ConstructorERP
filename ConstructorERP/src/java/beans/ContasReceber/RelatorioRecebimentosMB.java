package beans.ContasReceber;

import dao.ContasReceber.RecebimentoDao;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.Cliente;
import modelo.Predio;
import modelo.ContasReceber.Recebimento;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.model.chart.PieChartModel;
import utils.JpaUtil;

/**
 * Classe do menagedBean utilizada para geração dos relatórios de recebimentos
 *
 * @author Daniel
 */
@ManagedBean(name = "relatorioRecebimentosMB")
@ViewScoped
public class RelatorioRecebimentosMB implements Serializable {

    private Predio predio;
    private Cliente cliente;
    private RecebimentoDao recebimentoDao = new RecebimentoDao();
    private Date de;
    private Date ate;
    private List<Recebimento> recebimentos;
    private PieChartModel totalizadores;
    private double totalPago = 0;

    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getDe() {
        return de;
    }

    public void setDe(Date de) {
        this.de = de;
    }

    public Date getAte() {
        return ate;
    }

    public void setAte(Date ate) {
        this.ate = ate;
    }

    public PieChartModel getTotalizadores() {
        return totalizadores;
    }

    public void setTotalizadores(PieChartModel totalizadores) {
        this.totalizadores = totalizadores;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }

    public List<Recebimento> getRecebimentos() {
        return recebimentos;
    }

    public void setRecebimentos(List<Recebimento> recebimentos) {
        this.recebimentos = recebimentos;
    }

    public String novo() {
        return "formRelatorioRecebimentos";
    }

    public void carregar() throws Exception {
        if (cliente == null) {
            cliente = new Cliente();
        }
        if (predio == null) {
            predio = new Predio();
        }
        if (de != null && ate != null) {
            recebimentos = recebimentoDao.listRecebimentosClientePredio(cliente.getId(), predio.getId(), de, ate);
            totalPago = recebimentoDao.totalRecebido(cliente.getId(), predio.getId(), de, ate);
        }
        if (de.equals(ate)) {
            this.textoRecebimentos();
        }
    }

    public void imprimeRelatorio() throws IOException, SQLException {


        HashMap parameters = new HashMap();
        parameters.put("dataIni", this.de);
        parameters.put("dataFim", this.ate);

        try {

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=fechamentoMes.xlsx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRXlsxExporter xlsExporter = new JRXlsxExporter();
            JasperPrint jasperPrint = JasperFillManager.fillReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/reports/fechamentoMes/fechamentoMes.jasper", parameters, JpaUtil.getConexao());
            xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
            xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            xlsExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{"Recebimentos"});
            xlsExporter.exportReport();
            servletOutputStream.flush();
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void textoRecebimentos() throws Exception {

        String predio = recebimentos != null && recebimentos.size() > 0 ? recebimentos.get(0).getParcela().getContrato().PredioDesc() : "";
        String textoReceb = predio + " ";
        StringTokenizer sttk;

        for (Recebimento recebimento : recebimentos) {
            if (recebimento.getFormaPagamento() == 1) {
                if (!recebimento.getParcela().getContrato().PredioDesc().equalsIgnoreCase(predio)) {
                    predio = recebimento.getParcela().getContrato().PredioDesc();
                    textoReceb += predio + " ";
                }
                if (null != recebimento.getParcela().getNumeracao() && !recebimento.getParcela().getNumeracao().equalsIgnoreCase("")){
                  sttk = new StringTokenizer(recebimento.getParcela().getNumeracao(), "/");
                  textoReceb += recebimento.getParcela().getContrato().unidadesDesc() + "/" + sttk.nextToken();
                } else{
                    textoReceb += recebimento.getParcela().getContrato().unidadesDesc() + "/";
                }
                //textoReceb += recebimento.getParcela().getContrato().unidadesDesc() + "/" + sttk.nextToken();
                if (!recebimento.getParcela().isPaga()) {
                    textoReceb += " Parcial";
                }
                textoReceb += "; ";
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", textoReceb));
    }
}
