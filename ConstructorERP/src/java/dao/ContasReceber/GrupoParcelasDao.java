/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasReceber.GrupoParcelas;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos grupos de parcelas
 *
 * @author Daniel
 */
public class GrupoParcelasDao {
    GrupoParcelas grupoParcelas;

    public GrupoParcelas get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(GrupoParcelas.class, id);
        } finally {
            em.close();
        }
    }
    
    public GrupoParcelas get(GrupoParcelas grupo) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select g from GrupoParcelas g where g.contrato.id = " + grupo.getContrato().getId() + " and g.identificador = '" + grupo.getIdentificador() + "'");
        try {
            if (q.getResultList().size() > 0) {
                grupoParcelas = (GrupoParcelas) q.getResultList().get(0);
                return grupoParcelas;
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }
    
    public boolean save(GrupoParcelas g) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(g) == null) {
                em.persist(g);
            } else {
                g.setId(grupoParcelas.getId());
                em.merge(g);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(GrupoParcelas g) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            GrupoParcelas grupoParcelas = em.merge(g);
            em.remove(grupoParcelas);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<GrupoParcelas> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select g from GrupoParcelas as g");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    public List<GrupoParcelas> listGruposContrato(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        String condicaoContrato = "";
        if(contratoID > 0){
            condicaoContrato = " where g.contrato.id = " + contratoID;
        }
        try {
            Query q = em.createQuery("select g from GrupoParcelas as g" + condicaoContrato);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
