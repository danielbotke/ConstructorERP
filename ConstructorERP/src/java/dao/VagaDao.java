/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Vaga;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos apartamentos
 *
 * @author Daniel
 */
public class VagaDao {

    private Vaga vaga;

    public Vaga get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Vaga.class, id);
        } finally {
            em.close();
        }
    }

    public boolean exist(Vaga a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select a from Vaga a where (a.name = '" + a.getName() + "' and a.predio.id = " + a.getPredio().getId() + ") or a.id = " + a.getId());
        try {
            if (q.getResultList().size() > 0) {
                vaga = (Vaga) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Vaga a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(a)) {
                em.persist(a);
            } else {
                a.setId(vaga.getId());
                em.merge(a);
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

    public boolean delete(Vaga a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            Vaga apartamento = em.merge(this.get(a.getId()));
            em.remove(apartamento);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Vaga> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Vaga as a order by a.predio.name, (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vaga> listPredio(int predioID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Vaga as a where a.predio.id = " + predioID + " order by (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vaga> listPredioDisponiveis(int predioID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Vaga as a where a.predio.id = " + predioID + " and a.vendido = false order by (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Vaga> getFromContrato(int contratoId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select a from Vaga as a where a.contrato.id = " + contratoId);
        try {
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
