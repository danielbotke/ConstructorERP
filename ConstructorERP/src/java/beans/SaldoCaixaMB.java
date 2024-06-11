package beans;

import dao.ContasPagar.ParcelaAPagarDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RecebimentoDao;
import dao.IndiceDao;
import dao.SaldoCaixaDao;
import dao.TipoIndiceDao;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import modelo.ContasPagar.DiaFluxoCaixa;
import modelo.ContasPagar.ParcelaAPagar;
import modelo.ContasReceber.Parcela;
import modelo.ContasReceber.Recebimento;
import modelo.SaldoCaixa;
import modelo.TipoIndice;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 * Classe do menagedBean do indice, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "saldoCaixaMB")
@ViewScoped
public class SaldoCaixaMB {

    private SaldoCaixaDao dao = new SaldoCaixaDao();
    private SaldoCaixa bean = null;
    private Date hoje = new Date();
    private double previsaoReceb;
    private double recebFut;
    private double recebido;
    private double aPagar;
    private double saldo;
    private int periodo = 0;
    private Date de;
    private Date ate;
    private LineChartModel grafico;
    private List<DiaFluxoCaixa> tabelaFluxoCaixa = new ArrayList<>();

    /**
     *
     * @throws Exception
     */
    @PostConstruct
    public void init()  {
        try {
            this.alterarPeriodo(null);
            this.calcular();
        } catch (Exception ex) {
            Logger.getLogger(SaldoCaixaMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SaldoCaixa getBean() {
        if (bean == null) {
            bean = new SaldoCaixa();
        }
        return bean;
    }

    public void setBean(SaldoCaixa bean) {
        this.bean = bean;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }

    public LineChartModel getGrafico() {
        return grafico;
    }

    public void setGrafico(LineChartModel grafico) {
        this.grafico = grafico;
    }

    public double getRecebFut() throws Exception {
        RecebimentoDao recebDao = new RecebimentoDao();
        GregorianCalendar amanha = new GregorianCalendar();
        amanha.setTime(new Date());
        amanha.roll(Calendar.DAY_OF_YEAR, 1);
        GregorianCalendar depois = new GregorianCalendar();
        depois.setTime(new Date());
        depois.roll(Calendar.DAY_OF_YEAR, 10);
        recebFut = recebDao.totalRecebido(0, 0, amanha.getTime(), depois.getTime());
        return recebFut;
    }

    public void setRecebFut(double recebFut) {
        this.recebFut = recebFut;
    }

    public double getPrevisaoReceb() throws Exception {
        ParcelaDao parcelaDao = new ParcelaDao();
        List<TipoIndice> tiposIndices = (new TipoIndiceDao()).listAll();
        double fator = 0;
        previsaoReceb = 0;
        for (TipoIndice tipoIndice : tiposIndices) {
            fator = parcelaDao.totalFatorIndiceAbertosParcelas(0, 0, de, ate, false, tipoIndice.getId());
            previsaoReceb += fator * (new IndiceDao()).getLast(tipoIndice.getId()).getValorIndice();
        }
        return previsaoReceb + this.getRecebFut();
    }

    public void setPrevisaoReceb(double previsaoReceb) {
        this.previsaoReceb = previsaoReceb;
    }

    public void setaPagar(double aPagar) {
        this.aPagar = aPagar;
    }

    public double getAPagar() throws Exception {
        double atrasado = (new ParcelaAPagarDao()).totalParcelasFornecedorPeriodo(0, 0, 0, new Date(100, 01, 01), de, false);
        double futuro = (new ParcelaAPagarDao()).totalParcelasFornecedorPeriodo(0, 0, 0, de, ate, false);
        aPagar = atrasado + futuro;
        return aPagar;
    }

    public void setAPagar(double aPagar) {
        this.aPagar = aPagar;
    }

    public double getSaldo() {
        if (bean != null && bean.getSaldo() > 0) {
            saldo = bean.getSaldo() + previsaoReceb + recebFut - aPagar;
        }
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public List<DiaFluxoCaixa> getTabelaFluxoCaixa() {
        return tabelaFluxoCaixa;
    }

    public void setTabelaFluxoCaixa(List<DiaFluxoCaixa> tabelaFluxoCaixa) {
        this.tabelaFluxoCaixa = tabelaFluxoCaixa;
    }

    public void alterarPeriodo(ValueChangeEvent event) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(hoje);
        if (event != null && event.getNewValue() instanceof Integer) {
            periodo = (Integer) event.getNewValue();
        }
        switch (periodo) {
            case 0:
                gc.set(GregorianCalendar.DAY_OF_MONTH, gc.getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
                de = gc.getTime();
                gc.set(GregorianCalendar.DAY_OF_MONTH, gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
                ate = gc.getTime();
                break;
            case 1:
                de = gc.getTime();
                gc.add(GregorianCalendar.DAY_OF_YEAR, 30);
                ate = gc.getTime();
                break;
            case 2:
                de = gc.getTime();
                gc.add(GregorianCalendar.DAY_OF_YEAR, 15);
                ate = gc.getTime();
                break;
            case 3:
                gc.set(GregorianCalendar.DAY_OF_WEEK, Calendar.SUNDAY);
                gc.roll(GregorianCalendar.DAY_OF_YEAR, -1);
                de = gc.getTime();
                gc.add(GregorianCalendar.DAY_OF_MONTH, 6);
                ate = gc.getTime();
                break;
        }
        this.calcular();
    }

    public void calcular() throws Exception {
        if (bean.getSaldo() > 0) {
            this.salvar();
            saldo = bean.getSaldo() + this.getPrevisaoReceb() - this.getAPagar();
            this.montarGrafico();
        }
    }

    public void montarGrafico() throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(hoje);
        GregorianCalendar amanha = new GregorianCalendar();
        amanha.setTime(hoje);
        amanha.roll(Calendar.DAY_OF_YEAR, 1);
        ParcelaAPagarDao parcelaPagarDao = new ParcelaAPagarDao();
        RecebimentoDao recebDao = new RecebimentoDao();
        List<ParcelaAPagar> pagoList;
        List<Recebimento> recebList;
        TreeNode pago;
        TreeNode recebido;
        List<Parcela> aReceber;
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        gc.setTime(de);
        gc.roll(GregorianCalendar.DAY_OF_YEAR, -1);
        if (hoje.getMonth() == 0) {
            gc.roll(GregorianCalendar.YEAR, -1);
        }
        Date limite = gc.getTime();
        gc.set(Calendar.YEAR, 1900);
        Date temposRemotos = gc.getTime();
        List<ParcelaAPagar> atrasadas = parcelaPagarDao.listParcelasFornecedoresPeriodo(0, 0, 0, 0, temposRemotos, limite, false);


        grafico = new LineChartModel();
        grafico.setAnimate(true);
        ChartSeries caixa = new ChartSeries();
        caixa.setLabel("Caixa");
        ChartSeries contas = new ChartSeries();
        contas.setLabel("À Pagar");
        ChartSeries receber = new ChartSeries();
        receber.setLabel("À Receber");

        double saldoAux = bean.getSaldo();
        double pagarAux = 0;
        double pagoAux = 0;
        double receberAux = 0;
        double recebidoAux = 0;
        for (ParcelaAPagar parcelaAPagar : atrasadas) {
            saldoAux = saldoAux - parcelaAPagar.getValorTotal();
        }
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        caixa.set(df.format(de), saldoAux);
        Date ateAux = ate;
        gc.setTime(ateAux);
        gc.roll(Calendar.DAY_OF_YEAR, 1);
        ateAux = gc.getTime();
        Date dataProcessando = de;
        gc.setTime(dataProcessando);

        tabelaFluxoCaixa = new ArrayList();

        while (dataProcessando.before(ateAux)) {
            pagoList = parcelaPagarDao.listParcelasFornecedoresDataPagamento(0, 0, 0, 0, dataProcessando, dataProcessando, true);
            recebList = recebDao.listRecebimentosClientePredio(0, 0, dataProcessando, dataProcessando);
            // aReceber = parcelaReceberDao.listParcelasClientePredio(0, 0, dataProcessando, dataProcessando, false);
            ParcelaDao parcelaDao = new ParcelaDao();
            List<TipoIndice> tiposIndices = (new TipoIndiceDao()).listAll();
            double fator = 0;
            receberAux = 0;
            pago = new DefaultTreeNode("Root", null);
            recebido = new DefaultTreeNode("Root", null);
            for (TipoIndice tipoIndice : tiposIndices) {
                fator = parcelaDao.totalFatorIndiceAbertosParcelas(0, 0, dataProcessando, dataProcessando, false, tipoIndice.getId());
                receberAux += fator * (new IndiceDao()).getLast(tipoIndice.getId()).getValorIndice();
            }

            pagarAux = (new ParcelaAPagarDao()).totalParcelasFornecedorPeriodo(0, 0, 0, dataProcessando, dataProcessando, false);
            pagoAux = (new ParcelaAPagarDao()).totalParcelasFornecedorDataPagamento(0, 0, 0, dataProcessando, dataProcessando, true);
            if (pagoAux > 0) {
                TreeNode totalPago = new DefaultTreeNode(nf.format(pagoAux), pago);
                for (ParcelaAPagar parcelaAPagar : pagoList) {
                    totalPago.getChildren().add(new DefaultTreeNode(parcelaAPagar.toString()));
                }
            } else {
                pago = null;
            }
            saldoAux = saldoAux - pagarAux;


            recebidoAux = recebDao.totalRecebido(0, 0, dataProcessando, dataProcessando);
            if (recebidoAux > 0) {
                TreeNode totalRecebido = new DefaultTreeNode(nf.format(recebidoAux), recebido);
                for (Recebimento receb : recebList) {
                    totalRecebido.getChildren().add(new DefaultTreeNode(receb.toString()));
                }
            } else {
                recebido = null;
            }





            if (!dataProcessando.after(amanha.getTime()) && !dataProcessando.before(amanha.getTime())) {
                saldoAux = saldoAux + receberAux + recebFut;
            } else {
                saldoAux = saldoAux + receberAux;
            }
            contas.set(df.format(dataProcessando), pagarAux);
            receber.set(df.format(dataProcessando), receberAux);
            caixa.set(df.format(dataProcessando), saldoAux);
            tabelaFluxoCaixa.add(new DiaFluxoCaixa(dataProcessando, receberAux, pagarAux, saldoAux, recebido, pago));
            gc.roll(Calendar.DAY_OF_YEAR, 1);
            dataProcessando = gc.getTime();
            pagarAux = 0;
            receberAux = 0;
        }


        grafico.addSeries(caixa);
        grafico.addSeries(contas);
        grafico.addSeries(receber);


        grafico.setTitle("Projeção de caixa");
        grafico.setLegendPosition("e");
        grafico.setShowPointLabels(false);
        grafico.getAxes().put(AxisType.X, new CategoryAxis("Data"));
        Axis yAxis = grafico.getAxis(AxisType.Y);
        Axis xAxis = grafico.getAxis(AxisType.X);
        yAxis = grafico.getAxis(AxisType.Y);
        yAxis.setLabel("R$");
        yAxis.setMin(-100000);
        yAxis.setMax(200000);
        xAxis.setTickAngle(270);
    }

    public String salvar() {
        try {
            if (bean.getSaldo() > 0) {
                bean.setDataSaldo(hoje);
                dao.save(bean);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar saldo. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        return "";
    }

    public String novo() {
        bean = new SaldoCaixa();
        return "formIndices";
    }

    public String editar(SaldoCaixa saldo) throws Exception {
        bean = dao.get(saldo.getId());
        return "formIndices";
    }

    public String deletar(SaldoCaixa saldo) {
        try {
            dao.delete(dao.get(saldo.getId()));
            return this.novo();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir índice. Verifique se o cliente não está sendo utilizado em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
    }

    public SaldoCaixa getLast() throws Exception {
        bean = dao.getLast();
        return bean;
    }
}
