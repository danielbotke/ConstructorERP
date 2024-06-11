/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.ContasPagar;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.ContasPagar.ItemCompra;
import modelo.ContasPagar.NotaFiscalCompra;
import modelo.ContasPagar.ParcelaAPagar;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso às notas de compra
 *
 * @author Daniel
 */
public class NotaFiscalCompraDao {
    
    private static final Logger logger = Logger.getLogger(JpaUtil.class.getName());


    public NotaFiscalCompra get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(NotaFiscalCompra.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(NotaFiscalCompra nf) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (nf.getParcelas().isEmpty()) {
                nf.setParcelas(null);
            }
            if (this.get(nf.getId()) == null) {
                em.persist(nf);
            } else {
                em.merge(nf);
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

    public boolean delete(NotaFiscalCompra nf) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            /*
             ParcelaAPagarDao parcelaDao = new ParcelaAPagarDao();
             List<ParcelaAPagar> parcelas = nf.getParcelas();
             int size = parcelas.size();
             for (int i = 0; i < size; i++) {
             parcelaDao.delete(parcelas.get(i));
             i++;
             }*/
            NotaFiscalCompra nfCompra = em.merge(nf);
            em.remove(nfCompra);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<NotaFiscalCompra> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        if (em == null){
            logger.info("Após get Entity Notas compras");
        }
        try {
            Query q = em.createQuery("select nf from NotaFiscalCompra as nf  order by  nf.dataContabil DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<NotaFiscalCompra> listFiltros(int fornecId, int ccId, int codNf, int blocoId, Date dataCriacao, Date dataNota, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoFornec = "";
            String condicaoBloco = "";
            String condicaoDataContabil = "";
            String condicaoDataNota = "";
            String condicaoCc = "";
            String condicaoNf = "";
            String condicaoPeriodo = "";

            if (fornecId > 0) {
                condicaoFornec = " and nf.fornecedor.id = " + fornecId;
            }
            if (blocoId > 0) {
                condicaoBloco = " and nf.bloco.id = " + blocoId;
            }
            if (ccId > 0) {
                condicaoCc = " and nf.centroDeCusto.id = " + ccId;
            }
            if (codNf > 0) {
                condicaoNf = " and nf.codigo = " + codNf;
            }
            if (dataCriacao != null) {
                condicaoDataContabil = " and  nf.dataContabil = :dataContabil ";
            }
            if (dataCriacao != null) {
                condicaoDataNota = " and  nf.dataNota = :dataNota ";
            }
            if (de != null && ate != null) {
                condicaoPeriodo = " and nf.dataContabil >= :de and nf.dataContabil <= :ate ";
            }
            Query q = em.createQuery("select nf from NotaFiscalCompra as nf where nf.id > 0 " + condicaoNf + condicaoDataContabil + condicaoDataNota + condicaoFornec + condicaoBloco + condicaoCc + condicaoPeriodo + " order by nf.dataContabil DESC ");
            if (dataCriacao != null) {
                q.setParameter("dataContabil", dataCriacao, TemporalType.DATE);
            }
            if (dataCriacao != null) {
                q.setParameter("dataNota", dataNota, TemporalType.DATE);
            }
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<NotaFiscalCompra> listNotasComISS(Date de, Date ate, boolean pago) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select nf from NotaFiscalCompra as nf where nf.ISS > 0 and nf.ISSPago = " + pago + "  and nf.dataContabil >= :de and nf.dataContabil <= :ate  order by nf.dataContabil DESC ");
            q.setParameter("de", de, TemporalType.DATE);
            q.setParameter("ate", ate, TemporalType.DATE);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<NotaFiscalCompra> listSemParcelas() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select nf from NotaFiscalCompra as nf where 0 = (select count(p.id) from ParcelaAPagar p where p.nota.id = nf.id)");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<NotaFiscalCompra> listNotasFuncionarioIposto(String funcionario, String imposto, int categoriaId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select nf from NotaFiscalCompra as nf where nf.centroDeCusto.id = " + categoriaId + " and nf.observacao like '%" + funcionario + "%" + imposto + "'");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public NotaFiscalCompra getNotasFuncionarioDataSemImposto(String funcionario, Date data, int categoriaId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select nf from NotaFiscalCompra as nf where nf.centroDeCusto.id = " + categoriaId + " and nf.dataContabil = :data and nf.observacao NOT LIKE 'INSS' and nf.observacao NOT LIKE 'FGTS' and  nf.observacao like '%" + funcionario + "%'");
            q.setParameter("data", data, TemporalType.DATE);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (NotaFiscalCompra) q.getResultList().get(0);
            }
        } finally {
            em.close();
        }
    }

    public List listTotaisNota(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and n.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and n.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and n.categoriaParcelaAvulsa.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and n.dataContabil >= :de and n.dataContabil <= :ate ";
            }
            Query q = em.createQuery("select n.dataContabil, n.centroDeCusto, n.bloco,  n.custoFuncionario + n.ajusteContabil + n.almoco+ n.extra +  n.valorTotal from NotaFiscalCompra as n where n.id > 0   " + condicaoBloco + condicaoCc + condicaoPredio + condicaoPeriodo + " order by n.dataContabil ");
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ItemCompra> listItens(int notaId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select i from  ItemCompra as i where i.notaFiscal.id = " + notaId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ParcelaAPagar> listParcelas(int notaId) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select p from  ParcelaAPagar as p where p.nota.id = " + notaId + " order by p.dataVencimento");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------
    public List listTotais(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and n.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and n.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and n.centroDeCusto.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and n.dataContabil >= :de and n.dataContabil <= :ate ";
            }
            Query q = em.createQuery("select DISTINCT(n.centroDeCusto.name), n.bloco.predio.name, n.bloco.identificacao, SUM(n.custoFuncionario) + SUM(n.ajusteContabil) + SUM(n.almoco)+ SUM(n.extra) +  SUM(n.valorTotal)  , n.bloco.id from NotaFiscalCompra as n where n.id > 0  " + condicaoBloco + condicaoCc + condicaoPredio + condicaoPeriodo + " group by n.centroDeCusto.name , n.bloco.id order by n.centroDeCusto.name ");
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
                condicaoPredio = " and n.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and n.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and n.centroDeCusto.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and n.dataContabil >= :de and n.dataContabil <= :ate ";
            }
            Query q = em.createQuery("select n.bloco.id, n.bloco.predio.name, n.bloco.identificacao, SUM(n.custoFuncionario) + SUM(n.ajusteContabil) + SUM(n.almoco)+ SUM(n.extra) + SUM(n.valorTotal), n.bloco.area , n.bloco.id from NotaFiscalCompra as n where n.id > 0   " + condicaoBloco + condicaoCc + " and n.centroDeCusto.considerarCalculoValorM2 = 1 " + condicaoPredio + condicaoPeriodo + " group by n.bloco.id order by n.bloco.id ");
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<NotaFiscalCompra> listPorBloco(int predioId, int blocoId, int ccId, Date de, Date ate) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            String condicaoPredio = "";
            String condicaoBloco = "";
            String condicaoCc = "";
            String condicaoPeriodo = "";
            if (predioId > 0) {
                condicaoPredio = " and n.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and n.bloco.id = " + blocoId + " ";
                condicaoPredio = "";
            }
            if (ccId > 0) {
                condicaoCc = " and n.centroDeCusto.id = " + ccId + " ";
            }

            if (de != null && ate != null) {
                condicaoPeriodo = " and n.dataContabil >= :de and n.dataContabil <= :ate ";
            }
            Query q = em.createQuery("select n from NotaFiscalCompra as n where n.id > 0   " + condicaoBloco + condicaoCc + condicaoPredio + condicaoPeriodo + " group by n.bloco.id order by n.bloco.id ");
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
                condicaoPredio = " and n.bloco.predio.id = " + predioId + " ";
            }
            if (blocoId > 0) {
                condicaoBloco = " and n.bloco.id = " + predioId + " ";
                condicaoPredio = "";
            }
            if (de != null && ate != null) {
                condicaoPeriodo = " and n.dataContabil >= :de and n.dataContabil <= :ate ";
            }
            Query q = em.createQuery("select SUM(n.custoFuncionario) + SUM(n.ajusteContabil) + SUM(n.almoco) + SUM(n.extra) + SUM(n.MDO) + SUM(n.FGTS) + SUM(n.INSS) + SUM(n.valorTotal)  from NotaFiscalCompra as n where n.id > 0  " + condicaoBloco + condicaoPredio + condicaoPeriodo);
            if (!"".equalsIgnoreCase(condicaoPeriodo)) {
                q.setParameter("de", de, TemporalType.DATE);
                q.setParameter("ate", ate, TemporalType.DATE);
            }
            if (q.getResultList().size() > 0 && q.getResultList().get(0) !=null) {
                return (double) q.getResultList().get(0);
            } else {
                return 0;
            }
        } finally {
            em.close();
        }
    }
}
