/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasPagar.Produto;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos produtos
 *
 * @author Daniel
 */
public class ProdutoDao {

    public Produto get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(Produto p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(p.getId()) == null) {
                em.persist(p);
            } else {
                em.merge(p);
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

    public boolean delete(Produto p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Produto bloco = em.merge(p);
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

    public List<Produto> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Produto as p");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    

}
