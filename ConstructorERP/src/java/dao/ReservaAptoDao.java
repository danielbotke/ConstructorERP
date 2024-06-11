/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Apartamento;
import modelo.ReservaApto;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos reservaAptos
 *
 * @author Daniel
 */
public class ReservaAptoDao {

    private ReservaApto reservaApto;

    public ReservaApto get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(ReservaApto.class, id);
        } finally {
            em.close();
        }
    }

    public boolean exist(int id) throws Exception {
        reservaApto = this.get(id);
        if (reservaApto != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean save(ReservaApto r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(r.getId())) {
                em.persist(r);
            } else {
                r.setId(reservaApto.getId());
                em.merge(r);
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

    public boolean delete(ReservaApto r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            reservaApto = em.merge(r);
            em.remove(reservaApto);
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

    public List<ReservaApto> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select r from ReservaApto as r");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ReservaApto getLastApto(Apartamento a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select r from ReservaApto r where r.ativa = true and  r.apartamento.id = " + a.getId() + " order by r.id DESC");
            if (q.getResultList().size() > 0) {
                return (ReservaApto) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
}
