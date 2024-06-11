/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.ContasPagar.Unidade;
import modelo.ContasReceber.Contrato;
import utils.JpaUtil;
import utils.StringUtils;

/**
 * Classe que provê os métodos para persistência e acesso às unidades
 *
 * @author Daniel
 */
public class UnidadeDao {

    private Unidade unidade;

    public Unidade get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Unidade.class, id);
        } finally {
            em.close();
        }
    }

    public Unidade getByName(String name) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            name = StringUtils.tratadotCaractereEspeciais(name);
            Query q = em.createQuery("select u from Unidade as u where u.name = '" + name + "'");
            if (!q.getResultList().isEmpty()) {
                return (Unidade) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Unidade u) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            unidade = this.getByName(u.getName());
            if (unidade == null) {
                em.persist(u);
            } else {
                u.setId(unidade.getId());
                em.merge(u);
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

    public boolean delete(Unidade u) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            u = this.getByName(u.getName());
            unidade = em.merge(u);
            em.remove(unidade);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Unidade> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Unidade as p");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Unidade> listOutras(Unidade excecao) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select u from Unidade as u where u.id <> " + excecao.getId());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Unidade> listOutrasServico(int servicoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select u from Servico s JOIN s.outrasUnidades u where s.id = " + servicoID);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Unidade> listOutrasProduto(int produtoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select u from Produto p JOIN p.outrasUnidades u where p.id = " + produtoID);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
