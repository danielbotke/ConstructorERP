package beans.ContasReceber;

import dao.ApartamentoDao;
import dao.ContasReceber.ContratoDao;
import dao.IndiceDao;
import dao.ContasReceber.ParcelaDao;
import dao.ContasReceber.RecebimentoDao;
import dao.TipoIndiceDao;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Apartamento;
import modelo.Cliente;
import modelo.ContasReceber.Contrato;
import modelo.Indice;
import modelo.Predio;
import modelo.TipoIndice;

/**
 * Classe do menagedBean utilizada para geração do srelatórios de parcelas deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "relatorioSaldoContratoMB")
@ViewScoped
public class RelatorioSaldoContratoMB implements Serializable {

    private Predio predio = new Predio();
    private Cliente cliente = new Cliente();
    private List<Contrato> contratos;
    private Apartamento apartamento = new Apartamento();
    private List<Apartamento> apartamentos;

    public void carregar() throws Exception {
        ContratoDao contratoDao = new ContratoDao();
        ParcelaDao parcelaDao = new ParcelaDao();
        RecebimentoDao recebDao = new RecebimentoDao();
        if (cliente == null) {
            cliente = new Cliente();
        }
        if (predio == null) {
            predio = new Predio();
        }
        if (apartamento == null) {
            apartamento = new Apartamento();
        }
        contratos = contratoDao.listClientePredioStatus(cliente.getId(), predio.getId(), apartamento.getId(), 0);
        double aux = 0;
        double fator = 0;
        double fatorAux = 0;
        double valorOriginal = 0;
        int tipoIndiceId = 0;
        for (int i = 0; i < contratos.size(); i++) {
            List<TipoIndice> tiposIndices = (new TipoIndiceDao()).listAll();
            for (TipoIndice tipoIndice : tiposIndices) {
                fator = parcelaDao.totalCUBAbertosContrato(contratos.get(i).getId(), tipoIndice.getId());
                aux += fator * (new IndiceDao()).getLast(tipoIndice.getId()).getValorIndice();
                if (fator > 0) {
                    tipoIndiceId = tipoIndice.getId();
                }
                fatorAux += fator;
            }
            if (contratos.get(i).getDataContrato() != null && (new IndiceDao()).getIndiceMonth(contratos.get(i).getDataContrato(), tipoIndiceId) !=null) {
                valorOriginal = fatorAux * ((new IndiceDao()).getIndiceMonth(contratos.get(i).getDataContrato(), tipoIndiceId)).getValorIndice();
            }
            contratos.get(i).setTotalEmAberto(aux);
            contratos.get(i).setTotalCUBEmAberto(fatorAux);
            contratos.get(i).setTotalEmAbertoCUBContrato(valorOriginal);
            if (contratos.get(i).getTotalEmAberto() == 0) {
                contratos.remove(i);
                i--;
            } else {
                aux = recebDao.totalRecebidoContrato(contratos.get(i).getId());
                contratos.get(i).setTotalPago(aux);
            }
            aux = 0;
            fator = 0;
            fatorAux = 0;
            valorOriginal = 0;
        }
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
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

    public Apartamento getApartamento() {
        return apartamento;
    }

    public void setApartamento(Apartamento apartamento) {
        this.apartamento = apartamento;
    }

    public List<Apartamento> getApartamentos() throws Exception {
        return (new ApartamentoDao()).listAll();
    }

    public void setApartamentos(List<Apartamento> apartamentos) {
        this.apartamentos = apartamentos;
    }

    public String novo() {
        return "formRelatorioSaldoContrato";
    }
}
