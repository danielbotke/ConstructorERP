/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Usuario;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos usuários
 *
 * @author Daniel
 */
public class UsuarioDao {

    private Usuario usuario;
    private static final Logger logger = Logger.getLogger(UsuarioDao.class.getName());

    public Usuario get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario getEmailSenha(String email, String senha) throws Exception {
        logger.info("UsuarioDa.getEmailSenha antes de criar entitymanager");
        EntityManager em = JpaUtil.get().getEntityManager();
        logger.info("UsuarioDa.getEmailSenha depois de criar entitymanager");
        Query q = em.createQuery("select u from Usuario u where u.email = '" + email + "' and u.senha = '" + senha + "'");
        logger.info("UsuarioDa.getEmailSenha query criada");
        try {
            if (q.getResultList().size() > 0) {
                logger.info("UsuarioDa.getEmailSenha query executada");
                return (Usuario) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public Usuario get(String email) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from Usuario c where c.email = '" + email + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Usuario) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean exist(Usuario c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from Usuario c where c.nome like '" + c.getNome() + "' or c.id = " + c.getId());
        try {
            if (q.getResultList().size() > 0) {
                usuario = (Usuario) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Usuario c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(c)) {
                c.setDataCadastro(new Date());
                em.persist(c);
            } else {
                c.setId(usuario.getId());
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

    public boolean delete(Usuario c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Usuario usuario = em.merge(c);
            em.remove(usuario);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Usuario> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Usuario as c order by c.nome");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
