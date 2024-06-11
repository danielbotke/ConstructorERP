/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasReceber.BoletoGerado;
import modelo.ContasReceber.NumerosRemessa;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso aos blocos
 *
 * @author Daniel
 */
public class NumerosRemessaDao {

    public NumerosRemessa get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(NumerosRemessa.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(NumerosRemessa n) throws Exception {
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
    
        public boolean delete(NumerosRemessa n) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            NumerosRemessa numero = em.merge(n);
            em.remove(numero);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<NumerosRemessa> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select n from NumerosRemessa as n");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public NumerosRemessa getNext() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        NumerosRemessa novo = new NumerosRemessa();
        try {
            this.save(novo);
             Query q = em.createQuery("select n from NumerosRemessa as n where n.id = (select max(id) from NumerosRemessa)");
            return (NumerosRemessa)q.getResultList().get(0);
        } finally {
            em.close();
        }
    }
    
    public List<BoletoGerado> listBoletos(int numeroRemessa) throws Exception{
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from BoletoGerado as b where b.numerosRemessa.id = " + numeroRemessa);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    
}
