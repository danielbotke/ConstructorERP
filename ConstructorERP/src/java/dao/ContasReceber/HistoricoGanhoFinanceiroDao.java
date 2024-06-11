/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.ContasReceber.HistoricoGanhoFinanceiro;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso às HistoricoGanhoFinanceiros
 *
 * @author Daniel
 */
public class HistoricoGanhoFinanceiroDao {

    public HistoricoGanhoFinanceiro get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(HistoricoGanhoFinanceiro.class, id);
        } finally {
            em.close();
        }
    }

    public HistoricoGanhoFinanceiro get(int predio, Date data) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoGanhoFinanceiro as h where h.mesAno = :data and h.predio =  " + predio );
            q.setParameter("data", data, TemporalType.DATE);
            if (!q.getResultList().isEmpty()) {
                return (HistoricoGanhoFinanceiro) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(HistoricoGanhoFinanceiro h) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(h.getId()) == null) {
                em.persist(h);
            } else {
                em.merge(h);
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

    public boolean delete(HistoricoGanhoFinanceiro h) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            HistoricoGanhoFinanceiro hist = em.merge(h);
            em.remove(hist);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean deleteAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Query q = em.createQuery("DELETE FROM HistoricoGanhoFinanceiro");
            q.executeUpdate();
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

    public List<HistoricoGanhoFinanceiro> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoGanhoFinanceiro as h order by h.mesAno DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Date> listDatas() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select Distinct(h.mesAno) from HistoricoGanhoFinanceiro as h order by h.mesAno DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<HistoricoGanhoFinanceiro> despesasPorData(Date dataFiltro) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoGanhoFinanceiro as h where h.mesAno = :data order by h.mesAno DESC");
            q.setParameter("data", dataFiltro, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }


    public List<HistoricoGanhoFinanceiro> despesasPorPredio(int predioId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoGanhoFinanceiro as h where h.predio.id = " + predioId + " order by h.mesAno DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
