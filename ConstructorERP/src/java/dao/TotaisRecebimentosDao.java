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
import modelo.TotaisRecebimentos;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos blocos
 *
 * @author Daniel
 */
public class TotaisRecebimentosDao {

    public TotaisRecebimentos get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(TotaisRecebimentos.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(TotaisRecebimentos t) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(t.getId()) == null) {
                em.persist(t);
            } else {
                em.merge(t);
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

    public boolean delete(TotaisRecebimentos t) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            TotaisRecebimentos totais = em.merge(t);
            em.remove(totais);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<TotaisRecebimentos> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select t from Bloco as t");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TotaisRecebimentos getLast() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select t from TotaisRecebimentos as t where t.id = MAX(t.id)");
            return (TotaisRecebimentos) q.getResultList().get(0);
        } finally {
            em.close();
        }
    }
}
