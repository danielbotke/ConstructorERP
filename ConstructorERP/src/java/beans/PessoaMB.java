package beans;

import dao.PessoaFisicaDao;
import dao.PessoaJuridicaDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Pessoa;

/**
 * Classe do menagedBean da pessoa, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "pessoaMB")
@ViewScoped
public class PessoaMB implements Serializable {

    private PessoaFisicaDao daoFis = new PessoaFisicaDao();
    private PessoaJuridicaDao daoJur = new PessoaJuridicaDao();
    private List<Pessoa> pessoas;

    public List<Pessoa> getPessoas() throws Exception {
        if (pessoas == null || pessoas.isEmpty()) {
            pessoas = new ArrayList<>();
            pessoas.addAll(daoFis.listAll());
            pessoas.addAll(daoJur.listAll());
        }
        return pessoas;
    }

    public List<Pessoa> getPessoasNaoClientes() throws Exception {
        if (pessoas == null || pessoas.isEmpty()) {
            pessoas = new ArrayList<>();
            pessoas.addAll(daoFis.listNaoClientes());
            pessoas.addAll(daoJur.listNaoClientes());
        }
        return pessoas;
    }
    public List<Pessoa> getPessoasNaoFornecedores() throws Exception {
        if (pessoas == null || pessoas.isEmpty()) {
            pessoas = new ArrayList<>();
            pessoas.addAll(daoFis.listNaoFornecedores());
            pessoas.addAll(daoJur.listNaoFornecedores());
        }
        return pessoas;
    }
    
}
