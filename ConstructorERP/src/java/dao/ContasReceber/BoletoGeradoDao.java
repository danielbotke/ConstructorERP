/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.ContasReceber.BoletoGerado;
import modelo.ContasReceber.Parcela;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos blocos
 *
 * @author Daniel
 */
public class BoletoGeradoDao {

    public BoletoGerado get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(BoletoGerado.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(BoletoGerado b) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(b.getId()) == null) {
                em.persist(b);
            } else {
                em.merge(b);
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

    public boolean delete(BoletoGerado b) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            BoletoGerado bloco = em.merge(b);
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

    public List<BoletoGerado> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from BoletoGerado as b");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<BoletoGerado> listAllParcela(int parcelaId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from BoletoGerado as b where b.parcela.id = " + parcelaId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<BoletoGerado> listBoletosParcela(int parcelaId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from BoletoGerado as b where b.parcela.id = " + parcelaId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public BoletoGerado getNumeroTitulo(int numeroTitulo) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from BoletoGerado as b where b.numerosTitulo.id = " + numeroTitulo);
            List<BoletoGerado> results = q.getResultList();
            if (results.isEmpty()) {
                return null;
            } else {
                return (BoletoGerado)results.get(0);
            }
        } finally {
            em.close();
        }
    }

    public List<BoletoGerado> listBoletosNaoArremessados() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select b from BoletoGerado as b where b.numerosRemessa is null and b.carteira = 1");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<BoletoGerado> listBoletosClientePredio(int clienteID, int predioID, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            if (clienteID > 0) {
                condicaoCliente = " and c.id = " + clienteID;
            }
            if (predioID > 0) {
                condicaoPredio = " and a.bloco.predio.id = " + predioID;
            }
            Query q = em.createQuery("select DISTINCT(b) from BoletoGerado b, Parcela as p, Contrato as x JOIN x.clientes c, Apartamento a where b.parcela.id = p.id and  p.contrato.id = x.id and a.contrato.id = p.contrato.id " + condicaoCliente + condicaoPredio + " and b.dataVencimento >= :de and b.dataVencimento <= :ate order by a.bloco.predio.id, a.bloco.identificacao, (a.name * 1.0), p.dataVencimento, p.numeracao");
            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
