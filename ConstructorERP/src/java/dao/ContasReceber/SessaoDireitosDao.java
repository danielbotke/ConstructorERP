/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasReceber.SessaoDireitos;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos prédios
 *
 * @author Daniel
 */
public class SessaoDireitosDao {
    
    private SessaoDireitos sessao;

    public SessaoDireitos get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(SessaoDireitos.class, id);
        } finally {
            em.close();
        }
    }
    
    public SessaoDireitos getContrato(int contrato) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select s from SessaoDireitos s where s.contrato.id = " + contrato);
        try {
            if (q.getResultList().size() > 0) {
                return (SessaoDireitos) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    public boolean save(SessaoDireitos s) throws Exception {
        sessao = this.get(s.getId());
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (sessao == null) {
                em.persist(s);
            } else {
                s.setId(sessao.getId());
                em.merge(s);
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

    public boolean delete(SessaoDireitos s) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            sessao = em.merge(s);
            em.remove(sessao);
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

    public List<SessaoDireitos> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select s from SessaoDireitos as s order by s.dataSessao DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<SessaoDireitos> listContrato(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select s from SessaoDireitos as s where s.contrato.id = " + contratoID +" order by s.dataSessao DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
