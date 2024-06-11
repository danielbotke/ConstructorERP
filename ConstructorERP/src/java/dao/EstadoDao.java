/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Estado;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos estados
 *
 * @author Daniel
 */
public class EstadoDao {
    
    private Estado estado;

    public Estado get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }
    
    public boolean exist(Estado e) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select e from Estado e where e.name = '" + e.getName() + "' OR e.id = " + e.getId());
        try {
            if (q.getResultList().size() > 0) {
                estado = (Estado) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Estado e) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(e)) {
                em.persist(e);
            } else {
                e.setId(estado.getId());
                em.merge(e);
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

    public boolean delete(Estado e) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Estado estado = em.merge(e);
            em.remove(estado);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Estado> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select e from Estado as e order by e.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
