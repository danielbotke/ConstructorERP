/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Predio;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos prédios
 *
 * @author Daniel
 */
public class PredioDao {
    
    private Predio predio;

    public Predio get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Predio.class, id);
        } finally {
            em.close();
        }
    }
    
    public Predio get(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from Predio p where p.name = '" + name + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Predio) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
    public long getTotalApart(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select count(a.id) from Apartamento a where a.bloco.predio.id = " + id );
        try {
            if (q.getResultList().size() > 0) {
                return (Long) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
    
    
        public boolean exist(Predio p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from Predio p where p.name like '" + p.getName() + "' or p.id = " + p.getId());
        try {
            if (q.getResultList().size() > 0) {
                predio = (Predio) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Predio p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(p)) {
                em.persist(p);
            } else {
                p.setId(predio.getId());
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

    public boolean delete(Predio p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Predio predio = em.merge(p);
            em.remove(predio);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Predio> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Predio as p order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
