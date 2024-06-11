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
import modelo.Bloco;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos apartamentos
 *
 * @author Daniel
 */
public class ApartamentoDao {

    private Apartamento apartamento;

    public Apartamento get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Apartamento.class, id);
        } finally {
            em.close();
        }
    }
    
       public Bloco getBlocoApto(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a.bloco from Apartamento as a where a.id = " + id);
            return (Bloco)q.getResultList().get(0);
        } finally {
            em.close();
        }
    }

    public boolean exist(Apartamento a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select a from Apartamento a where (a.name = '" + a.getName() + "' and a.bloco.id = " + a.getBloco().getId() + ") or a.id = " + a.getId());
        try {
            if (q.getResultList().size() > 0) {
                apartamento = (Apartamento) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Apartamento a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(a)) {
                em.persist(a);
            } else {
                a.setId(apartamento.getId());
                em.merge(a);
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

    public boolean delete(Apartamento a) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            Apartamento apartamento = em.merge(this.get(a.getId()));
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

    public List<Apartamento> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Apartamento as a order by a.bloco.predio.name, a.bloco.identificacao, a.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Apartamento> listBloco(int blocoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Apartamento as a where a.bloco.id = " + blocoID + " order by (a.name * 1.0) ");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Apartamento> listBlocoDisponiveis(int blocoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Apartamento as a where a.bloco.id = " + blocoID + " and a.vendido = false and a.disponivelVenda = true order by (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
        public List<Apartamento> listBlocoNaoVendidos(int blocoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Apartamento as a where a.bloco.id = " + blocoID + " and a.vendido = false order by (a.name * 1.0)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Apartamento> listDisponiveisPredio(int predioID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Apartamento as a where a.bloco.predio.id = " + predioID + " and a.vendido = false order by (a.name * 1.0), a.bloco.identificacao");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    public List<Apartamento> listFromContrato(int contratoId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Apartamento as a where a.contrato.id = " + contratoId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
