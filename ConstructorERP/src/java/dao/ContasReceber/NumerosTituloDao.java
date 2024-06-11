/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Bloco;
import modelo.ContasReceber.NumerosTitulo;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos blocos
 *
 * @author Daniel
 */
public class NumerosTituloDao {

    public NumerosTitulo get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(NumerosTitulo.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(NumerosTitulo n) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(n.getId()) == null) {
                em.persist(n);
            } else {
                em.merge(n);
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

    public List<NumerosTitulo> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select n from NumerosTitulo as n");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public NumerosTitulo getNext() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        NumerosTitulo novo = new NumerosTitulo();
        try {
            this.save(novo);
             Query q = em.createQuery("select n from NumerosTitulo as n where n.id = (select max(id) from NumerosTitulo)");
            return (NumerosTitulo)q.getResultList().get(0);
        } finally {
            em.close();
        }
    }
    
    
}
