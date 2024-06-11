/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasReceber.Renegociacao;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos contratos
 *
 * @author Daniel
 */
public class RenegociacaoDao {

    private Renegociacao renegociacao;

    public Renegociacao get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Renegociacao.class, id);
        } finally {
            em.close();
        }
    }

    public boolean exist(int id) throws Exception {
        renegociacao = this.get(id);
        if (renegociacao != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean save(Renegociacao r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(r.getId())) {
                em.persist(r);
            } else {
                r.setId(renegociacao.getId());
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

    public boolean delete(Renegociacao r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            renegociacao = em.merge(r);
            em.remove(renegociacao);
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

    public Renegociacao renegociacaoContrato(int idContrato) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select Distinct(r) from Renegociacao r JOIN r.parcelas p where r.encerrada = false and p.contrato.id = " + idContrato);
            if (!q.getResultList().isEmpty()) {
                return (Renegociacao) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public List<Renegociacao> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select r from Renegociacao as r ");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Renegociacao> listAbertas() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select r from Renegociacao as r where r.encerrada = false");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
