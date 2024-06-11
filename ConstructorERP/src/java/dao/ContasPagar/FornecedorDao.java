/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasPagar.Fornecedor;
import modelo.ContasReceber.Contrato;
import modelo.Pessoa;
import modelo.PessoaFisica;
import modelo.PessoaJuridica;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos fornecedors
 *
 * @author Daniel
 */
public class FornecedorDao {

    private Fornecedor fornecedor;

    public Fornecedor get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Fornecedor.class, id);
        } finally {
            em.close();
        }
    }

    public Fornecedor get(String cpf) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from Pessoa c where c.CPF = '" + cpf + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Fornecedor) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean exist(Fornecedor f) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q;
        if (f.getPessoaFornecedor() instanceof PessoaFisica) {
            q = em.createQuery("select f from Fornecedor as f where f.pessoa.name like '" + f.getPessoa().getName() + "' or f.id = " + f.getId() + " or f.pessoa.CPF = '" + ((PessoaFisica) f.getPessoa()).getCPF() + "'");
        } else {
            q = em.createQuery("select f from Fornecedor as f where f.pessoa.name like '" + f.getPessoa().getName() + "' or f.id = " + f.getId() + " or f.pessoa.CNPJ = '" + ((PessoaJuridica) f.getPessoa()).getCNPJ() + "'");
        }

        try {
            if (q.getResultList().size() > 0) {
                fornecedor = (Fornecedor) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Fornecedor f) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(f)) {
                em.persist(f);
            } else {
                f.setId(fornecedor.getId());
                em.merge(f);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(Fornecedor f) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            fornecedor = em.merge(f);
            em.remove(fornecedor);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Fornecedor> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select f from Fornecedor as f order by f.pessoa.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Contrato> listContratos(int fornecedorID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c2 from Fornecedor f JOIN f.contratos c2 where f.id = " + fornecedorID);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Fornecedor> listFornecedorPredio(int fornecedorID, int PredioID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();

        try {
            String condicaoFornecedor = "";
            String condicaoPredio = "";
            if (fornecedorID > 0) {
                condicaoFornecedor = " and c2.id = " + fornecedorID;
            }
            if(PredioID > 0){
                condicaoPredio = " and a.bloco.predio.id = " + PredioID;
            }
            Query q = em.createQuery("select Distinct(c2) from Contrato c JOIN c.fornecedors c2, Apartamento a where a.contrato.id = c.id " + condicaoFornecedor + condicaoPredio+" order by c2.pessoa.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
     public Pessoa getPessoaFornecedor(int fornecedorID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();

        try {
            Query q = em.createQuery("select f.pessoa from Fornecedor f where f.id = " + fornecedorID);
            return (Pessoa)q.getResultList().get(0);
        } finally {
            em.close();
        }
    }
}
