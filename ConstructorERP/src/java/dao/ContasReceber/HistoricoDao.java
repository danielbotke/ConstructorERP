/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Cliente;
import modelo.Historico;
import modelo.Historico;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos contratos
 *
 * @author Daniel
 */
public class HistoricoDao {

    private Historico historico;

    public Historico get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Historico.class, id);
        } finally {
            em.close();
        }
    }

    public boolean exist(int id) throws Exception {
        historico = this.get(id);
        if (historico != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean save(Historico h) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(h.getId())) {
                em.persist(h);
            } else {
                h.setId(historico.getId());
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

    public boolean delete(Historico h) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            historico = em.merge(h);
            em.remove(historico);
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

    public List<Historico> listAll(String tipo, int idObjetoHistorico) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            if("".equalsIgnoreCase(tipo)){
                tipo = "Vazio";
            }
            Query q = em.createQuery("select h from Historico as h where h.tipo = '" + tipo + "' and h.idObjetoHistorico = " + idObjetoHistorico);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    
}
