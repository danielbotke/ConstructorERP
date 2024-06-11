/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.Indice;
import modelo.TipoIndice;
import utils.DateUtils;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos índices
 *
 * @author Daniel
 */
public class IndiceDao {

    public Indice get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Indice.class, id);
        } finally {
            em.close();
        }
    }

    public Indice getLast(int tipoIndiceId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select i from Indice i where i.tipoIndice.id = '" + tipoIndiceId + "' order by i.dataIndice DESC");
        try {
            if (q.getResultList().size() > 0) {
                return (Indice) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public Indice getIndiceMonth(Date data, int tipoIndiceId) throws Exception {
        data.setDate(1);
        Date ultimoDiaMes = DateUtils.ultimoDiaMes(data);
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select i from Indice i where i.tipoIndice.id = " + tipoIndiceId + " and i.dataIndice >= :ini and i.dataIndice <= :fim");
        q.setParameter("ini", data, TemporalType.DATE);
        q.setParameter("fim", ultimoDiaMes, TemporalType.DATE);
        try {
            if (q.getResultList().size() > 0) {
                return (Indice) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Indice i) throws Exception {
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

    public boolean delete(Indice i) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Indice indice = em.merge(i);
            em.remove(indice);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Indice> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select i from Indice as i order by i.dataIndice DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
