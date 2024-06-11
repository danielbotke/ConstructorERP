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
import modelo.ContasReceber.ContratoAluguel;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos contratos
 *
 * @author Daniel
 */
public class ContratoAluguelDao {

    private ContratoAluguel contrato;

    public ContratoAluguel get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(ContratoAluguel.class, id);
        } finally {
            em.close();
        }
    }

    public ContratoAluguel getApartamento(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from ContratoAluguel c where c.apartamento.id = " + id);
        try {
            if (q.getResultList().size() > 0) {
                return (ContratoAluguel) q.getResultList().get(0);
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

    public boolean save(ContratoAluguel c) throws Exception {
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

    public boolean delete(ContratoAluguel c) throws Exception {
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

    public List<ContratoAluguel> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from ContratoAluguel as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ContratoAluguel> listEmAndamento() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from ContratoAluguel as c where c.status = 0");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cliente> listClientes(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c2 from ContratoAluguel c JOIN c.clientes c2 where c.id = " + contratoID + " order by c2.pessoa.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ContratoAluguel> listClientePredio(int clienteID, int predioID, int status) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        String clausulaCliente = "";
        String clausulaPredio = "";
        if (clienteID > 0) {
            clausulaCliente = " and cli.id = " + clienteID;
        }
        if (predioID > 0) {
            clausulaPredio = " and a.bloco.predio.id = " + predioID;
        }

        try {
            Query q = em.createQuery("select Distinct(c) from ContratoAluguel c JOIN c.clientes cli, Apartamento a where a.contrato.id = c.id and status = "+ status + clausulaCliente + clausulaPredio + " order by a.bloco.predio.name, a.bloco.identificacao, (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
