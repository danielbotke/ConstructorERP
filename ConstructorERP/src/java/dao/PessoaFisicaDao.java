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
import modelo.PessoaFisica;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso às pessoaFisicas
 *
 * @author Daniel
 */
public class PessoaFisicaDao {

    private PessoaFisica pessoaFisica;

    public PessoaFisica get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(PessoaFisica.class, id);
        } finally {
            em.close();
        }
    }
    

    public PessoaFisica get(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from PessoaFisica p where p.name = '" + name + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (PessoaFisica) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    public boolean exist(PessoaFisica p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from PessoaFisica as p where (p.name like '" + p.getName() + "' and p.CPF = '" + p.getCPF() + "') or p.id = " + p.getId());
        try {
            if (q.getResultList().size() > 0) {
                pessoaFisica = (PessoaFisica) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(PessoaFisica p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(p)) {
                em.persist(p);
            } else {
                p.setId(pessoaFisica.getId());
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

    public boolean delete(PessoaFisica p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            PessoaFisica pessoaFisica = em.merge(p);
            em.remove(pessoaFisica);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<PessoaFisica> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from PessoaFisica as p order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<PessoaFisica> listNaoClientes() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from PessoaFisica as p where p.id  NOT IN (select c.pessoa.id from Cliente as c) order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<PessoaFisica> listNaoFornecedores() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from PessoaFisica as p where p.id  NOT IN (select f.pessoa.id from Fornecedor as f) order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Pessoa> listPessoaClientePredioBloco(int clienteID, int predioID, int blocoID, boolean apenasContratosAtivos, boolean somenteClientes, boolean somenteFornecedores) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();

        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoContrato = "";
            String condicaoApartamento = "";
            String condicaoSomenteClientes = "";
            String condicaoSomenteFornecedores = "";
            String fromApartamento = "";
            String fromContrato = "";
            String fromCliente = "";
            if (clienteID > 0) {
                condicaoCliente = " and c2.pessoa.id = p.id and c2.id = " + clienteID;
                fromCliente = ", Cliente c2 ";
            }
            if(predioID > 0){
                condicaoPredio = " and a.bloco.predio.id = " + predioID;
                condicaoApartamento = " and a.contrato.id = c.id and c2.pessoa.id = p.id and c2.id in (select cli.id from Contrato contr JOIN contr.clientes cli where contr.id = c.id) ";
                fromCliente = ", Cliente c2 ";
                fromContrato = ", Contrato c";
                fromApartamento = ", Apartamento a";
            }
            
            if(blocoID > 0){
                condicaoBloco = " and a.bloco.id = " + blocoID;
                condicaoApartamento = " and a.contrato.id = c.id and c2.pessoa.id = p.id and c2.id in (select cli.id from Contrato contr JOIN contr.clientes cli where contr.id = c.id)";
                fromCliente = ", Cliente c2 ";
                fromContrato = ", Contrato c";
                fromApartamento = ", Apartamento a";
            }
            if(apenasContratosAtivos){
                condicaoContrato = " and c.status = 0 ";
                fromContrato = ", Contrato c";
            }
            if(somenteClientes){
                condicaoSomenteClientes = " and p.id in (select c3.pessoa.id from Cliente c3) ";
            }
            if(somenteFornecedores){
                condicaoSomenteFornecedores = " and p.id in (select f.pessoa.id from Fornecedor f) ";
            }
            
            Query q = em.createQuery("select Distinct(p) From Pessoa p " + fromApartamento + fromCliente + fromContrato + " where p.id > 0  " + condicaoSomenteClientes + condicaoSomenteFornecedores + condicaoApartamento + condicaoContrato + condicaoCliente + condicaoPredio + condicaoBloco + " order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    
}
