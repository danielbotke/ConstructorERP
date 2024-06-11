/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.ContasPagar.Categoria;
import modelo.ContasPagar.HistoricoDespesas;
import utils.JpaUtil;
import utils.StringUtils;

/**
 * Classe que provê os métodos para persistência e acesso às HistoricoDespesass
 *
 * @author Daniel
 */
public class HistoricoDespesasDao {

    public HistoricoDespesas get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(HistoricoDespesas.class, id);
        } finally {
            em.close();
        }
    }

    public HistoricoDespesas get(int centrocusto, int bloco, Date data) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoDespesas as h where h.mesAno = :data and h.centroCusto.id =  " + centrocusto + " and h.bloco.id = " + bloco);
            q.setParameter("data", data, TemporalType.DATE);
            if (!q.getResultList().isEmpty()) {
                return (HistoricoDespesas) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(HistoricoDespesas h) throws Exception {
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
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(HistoricoDespesas h) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            HistoricoDespesas hist = em.merge(h);
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
            Query q = em.createQuery("DELETE FROM HistoricoDespesas");
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

    public List<HistoricoDespesas> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoDespesas as h order by h.mesAno DESC, h.centroCusto.name, h.bloco.identificacao");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Date> listDatas() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select Distinct(h.mesAno) from HistoricoDespesas as h order by h.mesAno DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<HistoricoDespesas> despesasPorData(Date dataFiltro) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistoricoDespesas as h where h.mesAno = :data order by h.mesAno DESC, h.centroCusto.name, h.bloco.identificacao");
            q.setParameter("data", dataFiltro, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
