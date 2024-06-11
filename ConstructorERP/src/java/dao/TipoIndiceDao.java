/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.TipoIndice;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos tipos de índice
 *
 * @author Daniel
 */
public class TipoIndiceDao {

    private TipoIndice tipoIndice;

    public TipoIndice get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(TipoIndice.class, id);
        } finally {
            em.close();
        }
    }

    public TipoIndice get(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select t from TipoIndice t where t.name = '" + name + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (TipoIndice) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean exist(TipoIndice t) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select t from TipoIndice t where t.name like '" + t.getName() + "' or t.id = " + t.getId());
        try {
            if (q.getResultList().size() > 0) {
                tipoIndice = (TipoIndice) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(TipoIndice t) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(t)) {
                em.persist(t);
            } else {
                t.setId(tipoIndice.getId());
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

    public boolean delete(TipoIndice t) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            TipoIndice tipoIndice = em.merge(t);
            em.remove(tipoIndice);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<TipoIndice> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select t from TipoIndice as t");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
