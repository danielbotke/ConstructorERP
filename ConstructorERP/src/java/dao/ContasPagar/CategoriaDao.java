/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasPagar.Categoria;
import utils.JpaUtil;
import utils.StringUtils;

/** Classe que provê os métodos para persistência e acesso às categorias
 *
 * @author Daniel
 */
public class CategoriaDao {

    public Categoria get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }
    public Categoria getByName(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            name = StringUtils.tratadotCaractereEspeciais(name);
            Query q = em.createQuery("select c from Categoria as c where c.name = '" + name+"'");
            return (Categoria)q.getResultList().get(0);
        } finally {
            em.close();
        }
    }

    public boolean save(Categoria c) throws Exception {
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
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(Categoria c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Categoria bloco = em.merge(c);
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

    public List<Categoria> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Categoria as c order by c.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Categoria> listFilhas(int categoriaID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Categoria as c where c.categoriaPai.id = " + categoriaID+" order by c.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Categoria> listOrfaos() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Categoria as c where c.categoriaPai.id is null  order by c.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
