/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Cliente;
import modelo.ContasReceber.Contrato;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos contratos
 *
 * @author Daniel
 */
public class ContratoDao {

    private Contrato contrato;

    public Contrato get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public Contrato getApartamento(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from Contrato c where c.apartamento.id = " + id);
        try {
            if (q.getResultList().size() > 0) {
                return (Contrato) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean exist(int id) throws Exception {
        contrato = this.get(id);
        if (contrato != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean save(Contrato c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(c.getId())) {
                em.persist(c);
            } else {
                c.setId(contrato.getId());
                em.merge(c);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(Contrato c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            contrato = em.merge(c);
            em.remove(contrato);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Contrato> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Contrato as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Contrato> listEmAndamento() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Contrato as c where c.status = 0");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cliente> listClientes(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c2 from Contrato c JOIN c.clientes c2 where c.id = " + contratoID + " order by c2.pessoa.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Contrato> listClientePredio(int clienteID, int predioID, int apartID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        String clausulaCliente = "";
        String clausulaPredio = "";
        String clausulaApartamento = "";
        if (clienteID > 0) {
            clausulaCliente = " and cli.id = " + clienteID;
        }
        if (predioID > 0) {
            clausulaCliente = " and a.bloco.predio.id = " + predioID;
        }
        
        if (apartID > 0) {
            clausulaCliente = " and a.id = " + apartID;
        }

        try {
            Query q = em.createQuery("select Distinct(c) from Contrato c JOIN c.clientes cli, Apartamento a where a.contrato.id = c.id " + clausulaCliente + clausulaPredio + clausulaApartamento +" order by a.bloco.predio.name, a.bloco.identificacao, (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Contrato> listClientePredioStatus(int clienteID, int predioID, int apartID, int status) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        String clausulaCliente = "";
        String clausulaPredio = "";
        String clausulaApartamento = "";
        if (clienteID > 0) {
            clausulaCliente = " and cli.id = " + clienteID;
        }
        if (predioID > 0) {
            clausulaCliente = " and a.bloco.predio.id = " + predioID;
        }
        
        if (apartID > 0) {
            clausulaCliente = " and a.id = " + apartID;
        }

        try {
            Query q = em.createQuery("select Distinct(c) from Contrato c JOIN c.clientes cli, Apartamento a where a.contrato.id = c.id and a.contrato.status = " + status + clausulaCliente + clausulaPredio + clausulaApartamento +" order by a.bloco.predio.name, a.bloco.identificacao, (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
