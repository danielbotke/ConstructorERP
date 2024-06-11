/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Cidade;
import utils.JpaUtil;

/** Classe que provê os métodos para persistência e acesso às cidades
 *
 * @author Daniel
 */
public class CidadeDao {
    
    private Cidade cidade;

    public Cidade get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Cidade.class, id);
        } finally {
            em.close();
        }
    }
    
    public boolean exist(Cidade c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from Cidade c where c.name = '" + c.getName() + "' OR c.id = " + c.getId());
        try {
            if (q.getResultList().size() > 0) {
                cidade = (Cidade) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Cidade c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(c)) {
                em.persist(c);
            } else {
                c.setId(cidade.getId());
                em.merge(c);
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

    public boolean delete(Cidade c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Cidade cidade = em.merge(c);
            em.remove(cidade);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Cidade> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Cidade as c order by c.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
