package beans.ContasReceber;

import dao.ApartamentoDao;
import dao.BlocoDao;
import dao.IndiceDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RecebimentoDao;
import dao.TipoIndiceDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Apartamento;
import modelo.Bloco;
import modelo.Cliente;
import modelo.ContasReceber.Parcela;
import modelo.Predio;
import modelo.TipoIndice;
import org.primefaces.model.chart.PieChartModel;

/**
 * Classe do menagedBean utilizada para geração do srelatórios de parcelas deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "relatorioParcelasMB")
@ViewScoped
public class RelatorioParcelasMB implements Serializable {

    private Predio predio;
    private Bloco bloco;
    private Apartamento apartamento;
    private Cliente cliente;
    private ParcelaDao parcelaDao = new ParcelaDao();
    private Date de;
    private Date ate;
    private List<Parcela> parcelasPagas;
    private List<Parcela> parcelasAbertas;
    private PieChartModel totalizadores;
    private double totalParcelasPagas = 0;
    private double totalParcelasAbertas = 0;
    private double totalFatorIndiceParcelasPagas = 0;
    private double totalFatorIndiceParcelasAbertas = 0;
    private List<Bloco> blocosPredio;
    private List<Apartamento> apartamentosBloco;

    @PostConstruct
    public void init() {
        try {
            createTotalizadores();
        } catch (Exception ex) {
            Logger.getLogger(RelatorioParcelasMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onPredioSelecionado() throws Exception {
        BlocoDao blocoDao = new BlocoDao();
        blocosPredio = new ArrayList<>();
        if (predio != null) {
            blocosPredio = blocoDao.listPredio(predio.getId());
        }
    }

    public void onBlocoSelecionado() throws Exception {
        if (bloco != null) {
            this.apartamentosBloco = (new ApartamentoDao()).listBloco(bloco.getId());
        }

    }

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

    public ParcelaDao getParcelaDao() {
        return parcelaDao;
    }

    public void setParcelaDao(ParcelaDao parcelaDao) {
        this.parcelaDao = parcelaDao;
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

    public double getTotalParcelasPagas() {
        return totalParcelasPagas;
    }

    public void setTotalParcelasPagas(double totalParcelasPagas) {
        this.totalParcelasPagas = totalParcelasPagas;
    }

    public double getTotalParcelasAbertas() {
        return totalParcelasAbertas;
    }

    public void setTotalParcelasAbertas(double totalParcelasAbertas) {
        this.totalParcelasAbertas = totalParcelasAbertas;
    }

    public double getTotalFatorIndiceParcelasPagas() {
        return totalFatorIndiceParcelasPagas;
    }

    public void setTotalFatorIndiceParcelasPagas(double totalFatorIndiceParcelasPagas) {
        this.totalFatorIndiceParcelasPagas = totalFatorIndiceParcelasPagas;
    }

    public double getTotalCUBParcelasAbertas() {
        return totalFatorIndiceParcelasAbertas;
    }

    public void setTotalCUBParcelasAbertas(double totalCUBParcelasAbertas) {
        this.totalFatorIndiceParcelasAbertas = totalCUBParcelasAbertas;
    }

    public List<Parcela> getParcelasPagas() {
        return parcelasPagas;
    }

    public List<Parcela> getParcelasAbertas() {
        return parcelasAbertas;
    }

    public List<Bloco> getBlocosPredio() {
        return blocosPredio;
    }

    public void setBlocosPredio(List<Bloco> blocosPredio) {
        this.blocosPredio = blocosPredio;
    }

    public List<Apartamento> getApartamentosBloco() {
        return apartamentosBloco;
    }

    public void setApartamentosBloco(List<Apartamento> apartamentosBloco) {
        this.apartamentosBloco = apartamentosBloco;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public Apartamento getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamento apartamento) {
        this.apartamento = apartamento;
    }

    public String novo() {
        return "formRelatorioParcelas";
    }

    public void carregar() throws Exception {
        if (cliente == null) {
            cliente = new Cliente();
        }
        if (predio == null) {
            predio = new Predio();
        }
        if (bloco == null) {
            bloco = new Bloco();
        }
        if (apartamento == null) {
            apartamento = new Apartamento();
        }


        if (de != null && ate != null) {
            parcelasAbertas = parcelaDao.listParcelasClientePredio(cliente.getId(), predio.getId(), de, ate, false, bloco.getId(), apartamento.getId());
        }
        if (de != null && ate != null) {
            parcelasPagas = parcelaDao.listParcelasClientePredio(cliente.getId(), predio.getId(), de, ate, true, bloco.getId(), apartamento.getId());
        }
        createTotalizadores();
    }

    public void createTotalizadores() throws Exception {
        if (parcelasAbertas != null && parcelasPagas != null) {
            RecebimentoDao recebDao = new RecebimentoDao();
            totalFatorIndiceParcelasPagas = parcelaDao.totalCUBPagosParcelas(cliente.getId(), predio.getId(), de, ate);
            totalParcelasPagas = recebDao.totalRecebidoParcelas(cliente.getId(), predio.getId(), de, ate);
            List<TipoIndice> tiposIndices = (new TipoIndiceDao()).listAll();
            totalParcelasAbertas = 0;
            for (TipoIndice tipoIndice : tiposIndices) {
                totalFatorIndiceParcelasAbertas = parcelaDao.totalFatorIndiceAbertosParcelas(cliente.getId(), predio.getId(), de, ate, false, tipoIndice.getId());
                totalFatorIndiceParcelasAbertas += parcelaDao.totalFatorIndiceAbertosParcelas(cliente.getId(), predio.getId(), de, ate, true, tipoIndice.getId());
                totalParcelasAbertas += totalFatorIndiceParcelasAbertas * (new IndiceDao()).getLast(tipoIndice.getId()).getValorIndice();
            }
        }
        totalizadores = new PieChartModel();

        totalizadores.set("Pago", totalParcelasPagas);
        totalizadores.set("Em aberto", totalParcelasAbertas);

        totalizadores.setTitle("Totalizador parcelas");
        totalizadores.setLegendPosition("e");
        totalizadores.setShowDataLabels(true);
        totalizadores.setDiameter(150);
    }
}
