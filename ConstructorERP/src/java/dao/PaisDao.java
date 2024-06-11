/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Pais;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos países
 *
 * @author Daniel
 */
public class PaisDao {
    
    private Pais pais;

    public Pais get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public Pais get(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from Pais p where p.name = '" + name + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Pais) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean exist(Pais p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select p from Pais p where p.name like '" + p.getName() + "' or p.id = " + p.getId());
        try {
            if (q.getResultList().size() > 0) {
                pais = (Pais) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Pais p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(p)) {
                em.persist(p);
            } else {
                p.setId(pais.getId());
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

    public boolean delete(Pais p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Pais pais = em.merge(p);
            em.remove(pais);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Pais> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Pais as p order by p.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
