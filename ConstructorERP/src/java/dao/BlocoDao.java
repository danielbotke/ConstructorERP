/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Bloco;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos blocos
 *
 * @author Daniel
 */
public class BlocoDao {

    public Bloco get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Bloco.class, id);
        } finally {
            em.close();
        }
    }
    
     public long getTotalApart(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select count(a.id) from Apartamento a where a.bloco.id = " + id );
        try {
            if (q.getResultList().size() > 0) {
                return (long) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Bloco b) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(b.getId()) == null) {
                em.persist(b);
            } else {
                em.merge(b);
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

    public boolean delete(Bloco b) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Bloco bloco = em.merge(b);
            em.remove(bloco);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Bloco> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from Bloco as b");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
        public List<Bloco> listDisponiveisVenda() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from Bloco as b where b.disponivelVenda = true");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Bloco> listPredio(int predioID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from Bloco as b where b.predio.id = " + predioID);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
