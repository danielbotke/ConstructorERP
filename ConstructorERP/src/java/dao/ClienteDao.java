/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import modelo.Cliente;
import modelo.ContasReceber.Contrato;
import modelo.Pessoa;
import modelo.PessoaFisica;
import modelo.PessoaJuridica;
import utils.JpaUtil;

/**
 * Classe que provê os métodos para persistência e acesso aos clientes
 *
 * @author Daniel
 */
public class ClienteDao {

    private Cliente cliente;

    public Cliente get(int id) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public Cliente get(String cpf) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select c from Pessoa c where c.CPF = '" + cpf + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Cliente) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean exist(Cliente c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q;
        if (c.getPessoaCliente() instanceof PessoaFisica) {
            q = em.createQuery("select c from Cliente as c where c.pessoa.name like '" + c.getPessoa().getName() + "' or c.id = " + c.getId() + " or c.pessoa.CPF = '" + ((PessoaFisica) c.getPessoa()).getCPF() + "'");
        } else {
            q = em.createQuery("select c from Cliente as c where c.pessoa.name like '" + c.getPessoa().getName() + "' or c.id = " + c.getId() + " or c.pessoa.CNPJ = '" + ((PessoaJuridica) c.getPessoa()).getCNPJ() + "'");
        }

        try {
            if (q.getResultList().size() > 0) {
                cliente = (Cliente) q.getResultList().get(0);
                return true;
            } else {
                return false;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Cliente c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (!this.exist(c)) {
                em.persist(c);
            } else {
                c.setId(cliente.getId());
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

    public boolean delete(Cliente c) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            cliente = em.merge(c);
            em.remove(cliente);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Cliente> listAll() throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Cliente as c order by c.pessoa.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Contrato> listContratos(int clienteID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c2 from Cliente c JOIN c.contratos c2 where c.id = " + clienteID);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cliente> listClientePredio(int clienteID, int PredioID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();

        try {
            String condicaoCliente = "";
            String condicaoPredio = "";
            if (clienteID > 0) {
                condicaoCliente = " and c2.id = " + clienteID;
            }
            if(PredioID > 0){
                condicaoPredio = " and a.bloco.predio.id = " + PredioID;
            }
            Query q = em.createQuery("select Distinct(c2) from Contrato c JOIN c.clientes c2, Apartamento a where a.contrato.id = c.id " + condicaoCliente + condicaoPredio+" order by c2.pessoa.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
     public Pessoa getPessoaCliente(int clienteID) throws Exception {
        EntityManager em = JpaUtil.get().getEntityManager();

        try {
            Query q = em.createQuery("select c.pessoa from Cliente c where c.id = " + clienteID);
            return (Pessoa)q.getResultList().get(0);
        } finally {
            em.close();
        }
    }
}
