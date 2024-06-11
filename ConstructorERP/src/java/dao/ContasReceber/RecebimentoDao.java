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
import modelo.ContasReceber.Recebimento;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos recebimentos
 *
 * @author Daniel
 */
public class RecebimentoDao {

    private Recebimento recebimento;

    public Recebimento get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Recebimento.class, id);
        } finally {
            em.close();
        }
    }

    public boolean exist(Recebimento r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select r from Recebimento r where r.id = " + r.getId());
        try {
            if (q.getResultList().size() > 0) {
                recebimento = (Recebimento) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Recebimento r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(r)) {
                em.persist(r);
            } else {
                r.setId(recebimento.getId());
                em.merge(r);
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

    public boolean delete(Recebimento r) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            recebimento = em.merge(r);
            em.remove(recebimento);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Recebimento> recebParcela(int parcelaID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select r from Recebimento r where r.parcela.id = " + parcelaID + " order by r.dataPagamento");
        try {
            if (q.getResultList().size() > 0) {
                return q.getResultList();
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public List<Recebimento> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select r from Recebimento as r order by r.dataPagamento, r.formaPagamento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Recebimento> recebClientes(int clienteID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select r from Recebimento as r, Contrato as c JOIN c.clientes c2 where r.parcela.contrato.id = c.id and c2.id = " + clienteID + " order by r.dataPagamento DESC, r.formaPagamento, r.parcela.numeracao");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public double listParcela(int parcelaID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select SUM(r.valorRecebido) + SUM(r.valorDesconto) + SUM(r.valorMulta) + SUM(r.valorJuros) from Recebimento as r where r.parcela.id = " + parcelaID + " order by r.dataPagamento, r.formaPagamento");
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }

    public List<Recebimento> listRecebimentosClientePredio(int clienteID, int predioID, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            if (clienteID > 0) {
                condicaoCliente = " and c.id = " + clienteID;
            }
            if (predioID > 0) {
                condicaoPredio = " and a.bloco.predio = " + predioID;
            }
            Query q = em.createQuery("select DISTINCT(r) from Recebimento as r, Contrato as x JOIN x.clientes c, Apartamento a where x.id = r.parcela.contrato.id and a.contrato.id = r.parcela.contrato.id and r.dataPagamento >= :de and r.dataPagamento <= :ate " + condicaoCliente + condicaoPredio + " order by r.dataPagamento, a.bloco.predio.name, (a.name * 1.0)");
            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public double totalRecebidoContrato(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select SUM(r.valorRecebido) + SUM(r.valorJuros) + SUM(r.valorMulta) from Recebimento as r where r.parcela.contrato.id = " + contratoID);
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }

    public double totalRecebido(int clienteID, int predioID, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            if (clienteID > 0) {
                condicaoCliente = " and c.id = " + clienteID;
            }
            if (predioID > 0) {
                condicaoPredio = " and a.bloco.predio = " + predioID;
            }

            Query q = em.createQuery("select SUM(valorRecebido) + SUM(valorJuros) + SUM(valorMulta) from Recebimento where id in (select distinct(r.id) from Recebimento as r, Contrato as x JOIN x.clientes c, Apartamento a where x.id = r.parcela.contrato.id and a.contrato.id = r.parcela.contrato.id and r.dataPagamento >= :de and r.dataPagamento <= :ate " + condicaoCliente + condicaoPredio + ")");
            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
    
        public double totalRecebidoParcelas(int clienteID, int predioID, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            if (clienteID > 0) {
                condicaoCliente = " and c.id = " + clienteID;
            }
            if (predioID > 0) {
                condicaoPredio = " and a.bloco.predio = " + predioID;
            }

            Query q = em.createQuery("select SUM(valorRecebido) + SUM(valorJuros) + SUM(valorMulta) from Recebimento where id in (select distinct(r.id) from Recebimento as r, Contrato as x JOIN x.clientes c, Apartamento a where x.id = r.parcela.contrato.id and a.contrato.id = r.parcela.contrato.id and r.parcela.dataVencimento >= :de and r.parcela.dataVencimento <= :ate " + condicaoCliente + condicaoPredio + ")");
            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
        
        
}
