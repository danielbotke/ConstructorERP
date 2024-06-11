package beans.ContasPagar;

import dao.BlocoDao;
import dao.ContasPagar.HistoricoDespesasDao;
import dao.ContasPagar.NotaFiscalCompraDao;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Bloco;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.HistoricoDespesas;
import modelo.ContasPagar.NotaFiscalCompra;
import modelo.Predio;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.PieChartModel;

/**
 * Classe do menagedBean utilizada para geração dos relatórios de recebimentos
 *
 * @author Daniel
 */
@ManagedBean(name = "controleDespesasMB")
@ViewScoped
public class ControleDespesasMB implements Serializable {

    private Predio predio;
    private Bloco bloco;
    private Categoria centroCusto;
    private Date de;
    private Date ate;
    private List<NotaFiscalCompra> despesasFiltradas;
    private List<Object[]> totais;
    private List<Object[]> despesasPorBloco;
    private List<Date> mesesHistorico;
    private Date dataGraficoSelecionada;
    private TreeNode selectedNode;
    private long numAptos;
    private double totalDespesas;
    private String totaisBloco = "";
    private Map<String, Double> totaisMes = new HashMap<String, Double>();
    private PieChartModel graficoPizzaTotalGeral = new PieChartModel();
    private PieChartModel graficoPizzaTotalMes = new PieChartModel();

    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public Categoria getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(Categoria centroCusto) {
        this.centroCusto = centroCusto;
    }

    public List<Object[]> getTotais() {
        return totais;
    }

    public void setTotais(List<Object[]> totais) {
        this.totais = totais;
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

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public long getNumAptos() {
        return numAptos;
    }

    public void setNumAptos(long numAptos) {
        this.numAptos = numAptos;
    }

    public double getTotalDespesas() {
        return totalDespesas;
    }

    public void setTotalDespesas(double totalDespesas) {
        this.totalDespesas = totalDespesas;
    }

    public List<Object[]> getDespesasPorBloco() {
        return despesasPorBloco;
    }

    public void setDespesasPorBloco(List<Object[]> despesasPorBloco) {
        this.despesasPorBloco = despesasPorBloco;
    }

    public String getTotaisBloco() {
        return totaisBloco;
    }

    public void setTotaisBloco(String totaisBloco) {
        this.totaisBloco = totaisBloco;
    }

    public List<NotaFiscalCompra> getDespesasFiltradas() {
        return despesasFiltradas;
    }

    public void setDespesasFiltradas(List<NotaFiscalCompra> despesasFiltradas) {
        this.despesasFiltradas = despesasFiltradas;
    }

    public Map getTotaisMes() {
        return totaisMes;
    }

    public void setTotaisMes(HashMap totaisMes) {
        this.totaisMes = totaisMes;
    }

    public PieChartModel getGraficoPizzaTotalGeral() {
        return graficoPizzaTotalGeral;
    }

    public void setGraficoPizzaTotalGeral(PieChartModel graficoPizzaTotalGeral) {
        this.graficoPizzaTotalGeral = graficoPizzaTotalGeral;
    }

    public PieChartModel getGraficoPizzaTotalMes() {
        return graficoPizzaTotalMes;
    }

    public void setGraficoPizzaTotalMes(PieChartModel graficoPizzaTotalMes) {
        this.graficoPizzaTotalMes = graficoPizzaTotalMes;
    }

    public List<Date> getMesesHistorico() throws Exception {
        return (new HistoricoDespesasDao()).listDatas();
    }

    public void setMesesHistorico(List<Date> mesesHistorico) {
        this.mesesHistorico = mesesHistorico;
    }

    public Date getDataGraficoSelecionada() {
        return dataGraficoSelecionada;
    }

    public void setDataGraficoSelecionada(Date dataGraficoSelecionada) {
        this.dataGraficoSelecionada = dataGraficoSelecionada;
    }
    
    
    
    

    public void carregar() throws Exception {
        totaisBloco = "";
        totais = (new NotaFiscalCompraDao()).listTotais(
                predio != null ? predio.getId() : 0,
                bloco != null ? bloco.getId() : 0,
                selectedNode != null ? ((Categoria) selectedNode.getData()).getId() : 0,
                de, ate);
        totalDespesas = (new NotaFiscalCompraDao()).totalDespesas(
                predio != null ? predio.getId() : 0,
                bloco != null ? bloco.getId() : 0,
                de, ate);
        despesasPorBloco = (new NotaFiscalCompraDao()).listTotaisPorBloco(
                predio != null ? predio.getId() : 0,
                bloco != null ? bloco.getId() : 0,
                selectedNode != null ? ((Categoria) selectedNode.getData()).getId() : 0,
                de, ate);
        despesasFiltradas = (new NotaFiscalCompraDao()).listPorBloco(
                predio != null ? predio.getId() : 0,
                bloco != null ? bloco.getId() : 0,
                selectedNode != null ? ((Categoria) selectedNode.getData()).getId() : 0,
                de, ate);
        BigDecimal bd1;
        BigDecimal bd2;
        BigDecimal bd3;
        BlocoDao blocoDao = new BlocoDao();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        for (Object[] object : despesasPorBloco) {
            if ((double) object[3] > 0) {
                bd1 = new BigDecimal((double) object[3]).setScale(2, RoundingMode.HALF_DOWN);
                if ((double) object[4] > 0) {
                    bd2 = new BigDecimal((double) object[3] / (double) object[4]).setScale(2, RoundingMode.HALF_DOWN);
                } else {
                    bd2 = new BigDecimal(0.0);
                }

                if ((int) object[5] > 0 && blocoDao.getTotalApart((int) object[5]) > 0) {
                    bd3 = new BigDecimal((double) object[3] / blocoDao.getTotalApart((int) object[5])).setScale(2, RoundingMode.HALF_DOWN);
                } else {
                    bd3 = new BigDecimal(0.0);
                }
                totaisBloco += (String) object[1] + " - " + (String) object[2] + "\n   - Total de Despesas: R$ " + df.format(bd1.doubleValue()) + "\n   - Custo apartamento: R$ " + df.format(bd3.doubleValue()) + "\n   - Custo m²: R$ " + df.format(bd2.doubleValue()) + "; \n \n";
            }
        }
        String aux = "";
        double valor = 0;
        for (NotaFiscalCompra notaFiscal : despesasFiltradas) {
            aux = "";
            valor = 0;
            aux += notaFiscal.getCentroDeCusto().getId() + "-";
            if (notaFiscal != null) {
                aux += notaFiscal.getDataContabil().getYear() + "-";
                aux += notaFiscal.getDataContabil().getMonth() + "-";
                valor = notaFiscal.getValorTotal();
            } else {
                aux += notaFiscal.getDataContabil().getYear() + "-";
                aux += notaFiscal.getDataContabil().getMonth() + "-";
                valor = notaFiscal.getAjusteContabil() + notaFiscal.getAlmoco() + notaFiscal.getExtra() + notaFiscal.getValorTotal();
            }
            if (totaisMes.get(aux) == null) {
                totaisMes.put(aux, valor);
            } else {
                valor += totaisMes.get(aux);
                totaisMes.put(aux, valor);
            }
            aux = "";
        }
    }

    public void atualizarHistoricosDesesas() throws Exception {
        (new HistoricoDespesasMB()).atualizar(
                predio != null ? predio.getId() : 0,
                bloco != null ? bloco.getId() : 0,
                selectedNode != null ? ((Categoria) selectedNode.getData()).getId() : 0,
                de, ate);
    }

    public double porAP(double valor, int blocoId) throws Exception {
        numAptos = numAptos = (new BlocoDao()).getTotalApart(blocoId);
        if (numAptos > 0) {
            return valor / numAptos;
        } else {
            return 0.0;
        }
    }

    public double percent(double valor, int blocoId) {
        if (totalDespesas > 0) {
            for (Object[] objects : despesasPorBloco) {
                if ((int) objects[5] == blocoId) {
                    return (valor * 100) / (double) objects[3];
                }
            }
            return 0.0;
        } else {
            return 0.0;
        }
    }

    public void createGraficoPizzaTotalGeral() {
        graficoPizzaTotalGeral = new PieChartModel();
        HashMap<String, Double> totaisCentroCusto = new HashMap<>();
        String aux = "";
        double valor = 0;
        if(totais != null){
        for (Object[] total : totais) {
            valor = (double) total[3];
            if (totaisCentroCusto.get((String) total[0]) != null) {
                valor += totaisCentroCusto.get((String) total[0]);
                totaisCentroCusto.put((String) total[0], valor);
            } else {
                totaisCentroCusto.put((String) total[0], valor);
            }
        }
        for (Map.Entry<String, Double> entry : totaisCentroCusto.entrySet()) {
            graficoPizzaTotalGeral.set(entry.getKey(), entry.getValue());
        }
        }

        graficoPizzaTotalGeral.setLegendPosition("e");
        graficoPizzaTotalGeral.setShowDataLabels(true);
        graficoPizzaTotalGeral.setDiameter(450);
        graficoPizzaTotalGeral.setLegendCols(2);
        graficoPizzaTotalGeral.setMouseoverHighlight(true);
        graficoPizzaTotalGeral.setExtender("pieExtender");
        
        
    }
    
     public void createGraficoPizzaTotalMes() throws Exception {
        graficoPizzaTotalMes = new PieChartModel();
        List<HistoricoDespesas> despesas = new ArrayList<>();
        HistoricoDespesasDao daoDespesas = new HistoricoDespesasDao();
        despesas = daoDespesas.despesasPorData(dataGraficoSelecionada);
         for (HistoricoDespesas historicoDespesas : despesas) {
            graficoPizzaTotalMes.set(historicoDespesas.getCentroCusto().getName(), historicoDespesas.getValor());
        }

        graficoPizzaTotalMes.setLegendPosition("e");
        graficoPizzaTotalMes.setShowDataLabels(true);
        graficoPizzaTotalMes.setDiameter(450);
        graficoPizzaTotalMes.setLegendCols(2);
        graficoPizzaTotalMes.setMouseoverHighlight(true);
        graficoPizzaTotalMes.setExtender("pieExtender");
        
        
    }
}
