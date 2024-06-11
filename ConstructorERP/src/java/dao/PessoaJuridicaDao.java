/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Pessoa;
import modelo.PessoaJuridica;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso às pessoaJuridicas
 *
 * @author Daniel
 */
public class PessoaJuridicaDao {
    
    private PessoaJuridica pessoaJuridica;
    
    public PessoaJuridica get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(PessoaJuridica.class, id);
        } finally {
            em.close();
        }
    }
    
    public PessoaJuridica get(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from PessoaJuridica p where p.name = '" + name + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (PessoaJuridica) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    public PessoaJuridica getCNPJ(String cnpj) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from PessoaJuridica p where p.CNPJ = '" + cnpj + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (PessoaJuridica) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    
    public boolean exist(PessoaJuridica p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from PessoaJuridica as p where p.name like '" + p.getName() + "' or p.id = " + p.getId() + "or p.CNPJ = '" + p.getCNPJ() + "'");
        try {
            if (q.getResultList().size() > 0) {
                pessoaJuridica = (PessoaJuridica) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }
    
    public boolean save(PessoaJuridica p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        
        try {
            if (!this.exist(p)) {
                em.persist(p);
            } else {
                p.setId(pessoaJuridica.getId());
                em.merge(p);
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
    
    public boolean delete(PessoaJuridica p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            PessoaJuridica pessoaJuridica = em.merge(p);
            em.remove(pessoaJuridica);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
    
    public List<PessoaJuridica> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from PessoaJuridica as p order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<PessoaJuridica> listNaoClientes() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from PessoaJuridica as p where p.id  NOT IN (select c.pessoa.id from Cliente as c) order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Pessoa> listPessoaClientePredioBloco(int clienteID, int predioID, int blocoID ) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();

        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            String condicaoBloco = "";
            
            if (clienteID > 0) {
                condicaoCliente = " and c2.id = " + clienteID;
            }
            if(predioID > 0){
                condicaoPredio = " and a.bloco.predio.id = " + predioID;
            }
            
            if(blocoID > 0){
                condicaoBloco = " and a.bloco.id = " + blocoID;
            }
            
            Query q = em.createQuery("select Distinct(p) from Contrato c JOIN c.clientes c2 JOIN PessoaJuridica p, Apartamento a where a.contrato.id = c.id " + condicaoCliente + condicaoPredio + condicaoBloco + " order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<PessoaJuridica> listNaoFornecedores() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from PessoaJuridica as p where p.id  NOT IN (select f.pessoa.id from Fornecedor as f) order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
