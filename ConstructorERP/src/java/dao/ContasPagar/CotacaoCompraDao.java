/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasPagar.CotacaoCompra;
import modelo.ContasPagar.ItemCompra;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso às cotações de compra
 *
 * @author Daniel
 */
public class CotacaoCompraDao {

    public CotacaoCompra get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(CotacaoCompra.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(CotacaoCompra c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(c.getId()) == null) {
                em.persist(c);
            } else {
                em.merge(c);
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

    public boolean delete(CotacaoCompra c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            CotacaoCompra bloco = em.merge(c);
            em.remove(bloco);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<CotacaoCompra> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from CotacaoCompra as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ItemCompra> listItens(int cotacaoId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select i from ItemCompra as i where i.cotacao.id = " + cotacaoId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    

}
