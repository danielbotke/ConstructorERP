/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.SaldoCaixa;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos índices
 *
 * @author Daniel
 */
public class SaldoCaixaDao {

    public SaldoCaixa get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(SaldoCaixa.class, id);
        } finally {
            em.close();
        }
    }

    public SaldoCaixa getLast() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select i from SaldoCaixa i order by i.dataSaldo DESC");
        try {
            if (q.getResultList().size() > 0) {
                return (SaldoCaixa) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

   public boolean save(SaldoCaixa i) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(i.getId()) == null) {
                em.persist(i);
            } else {
                em.merge(i);
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

    public boolean delete(SaldoCaixa i) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            SaldoCaixa SaldoCaixa = em.merge(i);
            em.remove(SaldoCaixa);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<SaldoCaixa> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select i from SaldoCaixa as i order by i.dataSaldo DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
