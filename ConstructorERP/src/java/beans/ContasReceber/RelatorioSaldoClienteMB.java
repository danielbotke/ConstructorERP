package beans.ContasReceber;

import dao.ClienteDao;
import dao.IndiceDao;
import dao.ContasReceber.ParcelaDao;
import dao.TipoIndiceDao;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Cliente;
import modelo.Indice;
import modelo.Predio;
import modelo.TipoIndice;

/**
 * Classe do menagedBean utilizada para geração do srelatórios de parcelas deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "relatorioSaldoClienteMB")
@ViewScoped
public class RelatorioSaldoClienteMB implements Serializable {

    private Predio predio;
    private Cliente cliente;
    private ParcelaDao parcelaDao = new ParcelaDao();
    private List<Cliente> clientes;

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void carregar() throws Exception {
        ClienteDao clienteDao = new ClienteDao();
        ParcelaDao parcelaDao = new ParcelaDao();
        if (cliente == null) {
            cliente = new Cliente();
        }
        if (predio == null) {
            predio = new Predio();
        }
        clientes = clienteDao.listClientePredio(cliente.getId(), predio.getId());
        double aux = 0;
        double fator = 0;
        for (int i = 0; i < clientes.size(); i++) {

            List<TipoIndice> tiposIndices = (new TipoIndiceDao()).listAll();
            for (TipoIndice tipoIndice : tiposIndices) {
                fator = parcelaDao.totalCUBAbertosParcelasClientePredio(clientes.get(i).getId(), predio.getId(), tipoIndice.getId());
                aux += fator * (new IndiceDao()).getLast(tipoIndice.getId()).getValorIndice();
            }

            clientes.get(i).setSaldoDevedor(aux);
            if (clientes.get(i).getSaldoDevedor() == 0) {
                clientes.remove(i);
                i--;
            }
        }
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
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

    public String novo() {
        return "formRelatorioSaldoDevedor";
    }
}
