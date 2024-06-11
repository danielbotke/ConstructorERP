/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasPagar.ItemCompra;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos itens de compra
 *
 * @author Daniel
 */
public class ItemCompraDao {

    public ItemCompra get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(ItemCompra.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(ItemCompra i) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(i.getId()) == null) {
                em.persist(i);
            } else {
                em.merge(i);
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

    public boolean delete(ItemCompra i) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            ItemCompra bloco = em.merge(i);
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

    public List<ItemCompra> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select i from ItemCompra as i");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    

}
