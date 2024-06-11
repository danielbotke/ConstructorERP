/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasReceber;

import dao.IndiceDao;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.ContasReceber.Parcela;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso às parcelas
 *
 * @author Daniel
 */
public class ParcelaDao {

    public Parcela get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Parcela.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(Parcela p) throws Exception {
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
            ex.printStackTrace();
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(Parcela p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Parcela parcela = em.merge(p);
            em.remove(parcela);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Parcela> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Parcela as p order by p.contrato.id, p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Parcela parcelaBoleto(int codigoboleto) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Parcela p, BoletoGerado b where p.id = b.parcela.id and b.numerosTitulo.id = " + codigoboleto);
            return (Parcela) q.getResultList().get(0);
        } finally {
            em.close();
        }
    }

    public double totalParcelasContrato(int contratoID) throws Exception {
        double totalParcelas = 0;
        List<Parcela> parcelas = this.listContrato(contratoID);
        for (int i = 0; i < parcelas.size(); i++) {
            if (parcelas.get(i).getValorParcela() > 0) {
                totalParcelas += parcelas.get(i).getValorParcela();
            } else if (parcelas.get(i).getFatorIndice() > 0) {
                totalParcelas += parcelas.get(i).getFatorIndice() * (new IndiceDao()).getLast(parcelas.get(i).getTipoIndice().getId()).getValorIndice();
            }

        }

        return totalParcelas;
    }

    public double totalCUBParcelasContrato(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select SUM(p.fatorIndice) from Parcela as p where p.situacao < 3 and p.contrato.id = " + contratoID);
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }

    public List<Parcela> listContrato(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Parcela as p where p.contrato.id = " + contratoID + " and p.situacao in (0,1,2,3,6) order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Parcela> listAbertasCliente(int clienteID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Parcela as p, Contrato as c JOIN c.clientes c2 where p.contrato.id = c.id and c2.id = " + clienteID + " and p.paga = false order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Parcela> listAbertasContrato(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from Parcela as p where p.contrato.id = " + contratoID + " and p.paga = false order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Parcela> listParcelasGrupo(int grupoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        String condicaoGrupo = "";
        if (grupoID > 0) {
            condicaoGrupo = " where p.grupoParcelas.id = " + grupoID;
        }
        try {
            Query q = em.createQuery("select p from Parcela as p " + condicaoGrupo + " order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Parcela> listReforcos(int contratoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        String condicaoContrato = "";
        if (contratoID > 0) {
            condicaoContrato = " and p.contrato.id = " + contratoID;
        }
        try {
            Query q = em.createQuery("select p from Parcela as p where p.grupoParcelas.id = NULL and p.valorFixo = false " + condicaoContrato + " order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Parcela> listParcelasClientePredio(int clienteID, int predioID, Date de, Date ate, boolean paga, int blocoID, int aptoID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoApto = "";
            
            if (clienteID > 0) {
                condicaoCliente = " and c.id = " + clienteID;
            }
            if (predioID > 0) {
                condicaoPredio = " and a.bloco.predio.id = " + predioID;
            }
            if (blocoID > 0) {
                condicaoBloco = " and a.bloco.id = " + blocoID;
            }
            if (aptoID > 0) {
                condicaoApto = " and a.id = " + aptoID;
            }
            Query q = em.createQuery("select DISTINCT(p) from Parcela as p, Contrato as x JOIN x.clientes c, Apartamento a where p.contrato.id = x.id and a.contrato.id = p.contrato.id and p.situacao not in (3, 4, 5) and p.paga = " + paga + condicaoCliente + condicaoPredio + condicaoBloco + condicaoApto +" and p.dataVencimento >= :de and p.dataVencimento <= :ate order by a.bloco.predio.id, a.bloco.identificacao, (a.name * 1.0), p.dataVencimento, p.numeracao");
            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Parcela> listParcelasBoletos(int clienteID, int predioID, Date de, Date ate, boolean boletosGerados) throws Exception {
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
            Query q = em.createQuery("select DISTINCT(p) from Parcela as p, Contrato as x JOIN x.clientes c, Apartamento a where (p.contrato.id = x.id and a.contrato.id = p.contrato.id and p.situacao not in (3, 4, 5) and c.formaPagamento not in (3) and x.naPlanta = false and p.paga = false and ((boletoGerado = 1 and (select count(bg.id) from BoletoGerado bg where bg.parcela.id = p.id and bg.dataVencimento >= CURRENT_DATE) = 0) or (p.boletoGerado = " + boletosGerados + "))" + condicaoCliente + condicaoPredio + " and p.dataVencimento >= :de and p.dataVencimento <= :ate) order by a.bloco.predio.id, a.bloco.identificacao, (a.name * 1), p.dataVencimento");

            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public double totalCUBParcelasContrato(int contratoID, boolean atrasadas) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String filtroAtrasadas = "";
            if (atrasadas) {
                filtroAtrasadas = " and p.dataVencimento < CURRENT_DATE";
            } else {
                filtroAtrasadas = " and p.dataVencimento >= CURRENT_DATE";
            }
            Query q = em.createQuery("select SUM(fatorIndice) from Parcela where id in (select distinct(p.id) from Parcela as p where p.situacao < 3 and p.contrato.id = " + contratoID + filtroAtrasadas + ")");
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }

    public double totalCUBPagosParcelas(int clienteID, int predioID, Date de, Date ate) throws Exception {
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
            Query q = em.createQuery("select SUM(fatorIndicePagos) from Parcela where id in (SELECT Distinct(p.id) from Parcela as p, Contrato as x JOIN x.clientes c, Apartamento a where p.contrato.id = x.id and a.contrato.id = p.contrato.id and p.situacao not in (3, 4, 5)" + condicaoCliente + condicaoPredio + " and p.dataVencimento >= :de and p.dataVencimento <= :ate)");
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

    public double totalFatorIndiceAbertosParcelas(int clienteID, int predioID, Date de, Date ate, boolean atrasada, int tipoIndiceId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            String condicaoAtrasada = "";
            if (clienteID > 0) {
                condicaoCliente = " and c.id = " + clienteID;
            }
            if (predioID > 0) {
                condicaoPredio = " and a.bloco.predio.id = " + predioID;
            }
            if (atrasada) {
                condicaoAtrasada = " and p.dataVencimento < CURRENT_DATE ";
            } else {
                condicaoAtrasada = " and p.dataVencimento >= CURRENT_DATE ";
            }
            Query q = em.createQuery("select SUM(fatorIndice) - SUM(fatorIndicePagos) from  Parcela where id in (select distinct(p.id) from Parcela as p, Contrato as x JOIN x.clientes c, Apartamento a where p.contrato.id = x.id and a.contrato.id = p.contrato.id and p.paga = false and p.tipoIndice.id = " + tipoIndiceId + condicaoCliente + condicaoPredio + " and p.dataVencimento >= :de and p.dataVencimento <= :ate " + condicaoAtrasada + " )");
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

    public double totalCUBAbertosParcelas(int clienteID, int predioID, Date de, Date ate) throws Exception {
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
            Query q = em.createQuery("select SUM(fatorIndice) - SUM(faorIndicePagos) from  Parcela where id in (select distinct(p.id) from Parcela as p, Contrato as x JOIN x.clientes c, Apartamento a where p.contrato.id = x.id and a.contrato.id = p.contrato.id and p.paga = false " + condicaoCliente + condicaoPredio + " and p.dataVencimento >= :de and p.dataVencimento <= :ate )");
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

    public double totalCUBAbertosParcelasClientePredio(int clienteID, int predioID, int tipoIndiceId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            if (predioID > 0) {
                condicaoPredio = " or p.contrato.id in (select distinct(xx.id) from Contrato as xx INNER JOIN xx.apartamentos a where a.bloco.predio.id = " + predioID + ")";

            }
            Query q = em.createQuery("select SUM(p2.fatorIndice) - SUM(p2.fatorIndicePagos) from  Parcela as p2 where p2.id in (select distinct(p.id) from Parcela as p where p.paga = false and p.tipoIndice.id = " + tipoIndiceId + " and (p.contrato.id in (select distinct(x.id) from Contrato as x INNER JOIN x.clientes c where c.id = " + clienteID + ") " + condicaoPredio + "))");

            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }

    public double totalCUBAbertosContrato(int contratoID, int tipoInndiceId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select SUM(p2.fatorIndice) - SUM(p2.fatorIndicePagos) from  Parcela as p2 where  p2.paga = false and  p2.contrato.id = " + contratoID + " and p2.tipoIndice.id = " + tipoInndiceId);
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
