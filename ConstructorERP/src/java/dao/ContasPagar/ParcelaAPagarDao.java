/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.ContasPagar.ParcelaAPagar;
import modelo.ContasReceber.Parcela;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso às parcelas à pagar
 *
 * @author Daniel
 */
public class ParcelaAPagarDao {

    public ParcelaAPagar get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(ParcelaAPagar.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(ParcelaAPagar p) throws Exception {
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

    public boolean delete(ParcelaAPagar p) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            ParcelaAPagar bloco = em.merge(p);
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

    public List<ParcelaAPagar> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from ParcelaAPagar as p order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ParcelaAPagar> listSemNota() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from ParcelaAPagar as p where p.nota is null order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ParcelaAPagar> listParcelasFornecedoresPeriodo(int fornecedorId, int ccId, int blocoid, int codNF, Date de, Date ate, boolean paga) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoFornec = "";
            String condicaoDe = "";
            String condicaoAte = "";
            String condicaoCc = "";
            String condicaoBloco = "";
            String condicaoNf = "";
            if (fornecedorId > 0) {
                condicaoFornec = " and p.nota.fornecedor.id = " + fornecedorId;
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId;
            }
            if (blocoid > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoid;
            }
            if (codNF > 0) {
                condicaoNf = " and p.nota.codigo = " + codNF;
            }
            if (de != null) {
                condicaoDe = " and p.dataVencimento >= :de ";
            }
            if (ate != null) {
                condicaoAte = " and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select DISTINCT(p) from ParcelaAPagar as p where p.paga = " + paga + condicaoNf + condicaoDe + condicaoAte + condicaoFornec + condicaoCc + condicaoBloco + " order by p.dataVencimento");
            if (de != null) {
                q.setParameter("de", de, TemporalType.DATE);
            }
            if (ate != null) {
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ParcelaAPagar> listParcelasFornecedoresDataPagamento(int fornecedorId, int ccId, int blocoid, int codNF, Date de, Date ate, boolean paga) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoFornec = "";
            String condicaoDe = "";
            String condicaoAte = "";
            String condicaoCc = "";
            String condicaoBloco = "";
            String condicaoNf = "";
            if (fornecedorId > 0) {
                condicaoFornec = " and p.nota.fornecedor.id = " + fornecedorId;
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId;
            }
            if (blocoid > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoid;
            }
            if (codNF > 0) {
                condicaoNf = " and p.nota.codigo = " + codNF;
            }
            if (de != null) {
                condicaoDe = " and p.dataPagamento >= :de ";
            }
            if (ate != null) {
                condicaoAte = " and p.dataPagamento <= :ate ";
            }
            Query q = em.createQuery("select DISTINCT(p) from ParcelaAPagar as p where p.paga = " + paga + condicaoNf + condicaoDe + condicaoAte + condicaoFornec + condicaoCc + condicaoBloco + " order by p.dataVencimento");
            if (de != null) {
                q.setParameter("de", de, TemporalType.DATE);
            }
            if (ate != null) {
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    public List<ParcelaAPagar> listParcelasFornecedoresPeriodo(int fornecedorId, int ccId, int blocoid, int codNF, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoFornec = "";
            String condicaoDe = "";
            String condicaoAte = "";
            String condicaoCc = "";
            String condicaoBloco = "";
            String condicaoNf = "";
            if (fornecedorId > 0) {
                condicaoFornec = " and p.nota.fornecedor.id = " + fornecedorId;
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId;
            }
            if (blocoid > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoid;
            }
            if (codNF > 0) {
                condicaoNf = " and p.nota.codigo = " + codNF;
            }
            if (de != null) {
                condicaoDe = " and p.dataVencimento >= :de ";
            }
            if (ate != null) {
                condicaoAte = " and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select DISTINCT(p) from ParcelaAPagar as p where " +  condicaoNf + condicaoDe + condicaoAte + condicaoFornec + condicaoCc + condicaoBloco + " order by p.dataVencimento");
            if (de != null) {
                q.setParameter("de", de, TemporalType.DATE);
            }
            if (ate != null) {
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    

    public double totalParcelasFornecedorPeriodo(int fornecedorId, int ccId, int codNF, Date de, Date ate, boolean paga) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoFornec = "";
            String condicaoDe = "";
            String condicaoAte = "";
            String condicaoCc = "";
            String condicaoNf = "";
            if (fornecedorId > 0) {
                condicaoFornec = " and p.nota.fornecedor.id = " + fornecedorId;
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId;
            }
            if (codNF > 0) {
                condicaoNf = " and p.nota.codigo = " + codNF;
            }
            if (de != null) {
                condicaoDe = " and p.dataVencimento >= :de ";
            }
            if (ate != null) {
                condicaoAte = " and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select SUM(p.custoFuncionario) + SUM(p.ajusteContabil) + SUM(p.almoco)+ SUM(p.Extra) + SUM(p.MDO) + SUM(p.FGTS) + SUM(p.INSS) + SUM(p.ISS) + SUM(p.valorParcela)   from  ParcelaAPagar as p where p.paga =  " + paga + condicaoNf + condicaoDe + condicaoAte + condicaoFornec + condicaoCc);
            if (de != null) {
                q.setParameter("de", de, TemporalType.DATE);
            }
            if (ate != null) {
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
    
     public double totalParcelasFornecedorDataPagamento(int fornecedorId, int ccId, int codNF, Date de, Date ate, boolean paga) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoFornec = "";
            String condicaoDe = "";
            String condicaoAte = "";
            String condicaoCc = "";
            String condicaoNf = "";
            if (fornecedorId > 0) {
                condicaoFornec = " and p.nota.fornecedor.id = " + fornecedorId;
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId;
            }
            if (codNF > 0) {
                condicaoNf = " and p.nota.codigo = " + codNF;
            }
            if (de != null) {
                condicaoDe = " and p.dataPagamento >= :de ";
            }
            if (ate != null) {
                condicaoAte = " and p.dataPagamento <= :ate ";
            }
            Query q = em.createQuery("select SUM(p.custoFuncionario) + SUM(p.ajusteContabil) + SUM(p.almoco)+ SUM(p.Extra) + SUM(p.MDO) + SUM(p.FGTS) + SUM(p.INSS) + SUM(p.ISS) + SUM(p.valorParcela)   from  ParcelaAPagar as p where p.paga =  " + paga + condicaoNf + condicaoDe + condicaoAte + condicaoFornec + condicaoCc);
            if (de != null) {
                q.setParameter("de", de, TemporalType.DATE);
            }
            if (ate != null) {
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            if (q.getResultList().size() > 0 && q.getResultList().get(0) != null) {
                return (Double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
    

    public List listTotais(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and p.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and p.dataVencimento >= :de and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select DISTINCT(p.categoriaParcelaAvulsa.name), p.bloco.predio.name, p.bloco.identificacao, SUM(p.custoFuncionario) + SUM(p.ajusteContabil) + SUM(p.almoco)+ SUM(p.Extra) +  SUM(p.valorParcela)  , p.bloco.id from ParcelaAPagar as p where p.id > 0  " + condicaoBloco + condicaoCc + condicaoPredio + condicaoPeriodo + " group by p.categoriaParcelaAvulsa.name , p.bloco.id order by p.categoriaParcelaAvulsa.name ");
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List listTotaisPorBloco(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and p.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and p.dataVencimento >= :de and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select p.bloco.id, p.bloco.predio.name, p.bloco.identificacao, SUM(p.custoFuncionario) + SUM(p.ajusteContabil) + SUM(p.almoco)+ SUM(p.Extra) + SUM(p.valorParcela), p.bloco.area , p.bloco.id from ParcelaAPagar as p where p.id > 0   " + condicaoBloco + condicaoCc + " and p.categoriaParcelaAvulsa.considerarCalculoValorM2 = 1 " + condicaoPredio + condicaoPeriodo + " group by p.bloco.id order by p.bloco.id ");
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List listTotaisParcelasSemNota(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and p.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and p.dataVencimento >= :de and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select p.dataVencimento, p.categoriaParcelaAvulsa, p.bloco, p.custoFuncionario + p.ajusteContabil + p.almoco + p.Extra + p.valorParcela from ParcelaAPagar as p where p.nota = " + null + condicaoBloco + condicaoCc + condicaoPredio + condicaoPeriodo + " order by p.dataVencimento ");
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ParcelaAPagar> listPorBloco(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and p.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and p.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and p.categoriaParcelaAvulsa.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and p.dataVencimento >= :de and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select p from ParcelaAPagar as p where p.id > 0   " + condicaoBloco + condicaoCc  + condicaoPredio + condicaoPeriodo + " group by p.bloco.id order by p.bloco.id ");
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public double totalDespesas(int predioId, int blocoId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and p.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and p.bloco.id = " + predioId + " ";
                condicaoPredio = "";
            }
            if (de != null && ate != null) {
                condicaoPeriodo = " and p.dataVencimento >= :de and p.dataVencimento <= :ate ";
            }
            Query q = em.createQuery("select SUM(p.custoFuncionario) + SUM(p.ajusteContabil) + SUM(p.almoco) + SUM(p.Extra) + SUM(p.MDO) + SUM(p.FGTS) + SUM(p.INSS) + SUM(p.ISS) + SUM(p.valorParcela)  from ParcelaAPagar as p where p.id > 0  " + condicaoBloco + condicaoPredio + condicaoPeriodo);
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            if (q.getResultList().size() > 0) {
                return (double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
}
