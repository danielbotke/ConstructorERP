/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasPagar;

import java.io.Serializable;
import java.util.Date;
import org.primefaces.model.TreeNode;

/**
 * Classe modelo para a criação do objeto Categoria, bem como para persistência
 * e acesso.
 *
 * @author Daniel
 */
public class DiaFluxoCaixa implements Serializable {

    private Date diaFluxo;
    private double recebimento;
    private double pagamentos;
    private double saldo;
    private TreeNode recebido;
    private TreeNode pago;

    public DiaFluxoCaixa(Date diaFluxo, double recebimento, double pagamentos, double saldo, TreeNode recebido, TreeNode pago) {
        this.diaFluxo = diaFluxo;
        this.recebimento = recebimento;
        this.pagamentos = pagamentos;
        this.saldo = saldo;
        this.recebido = recebido;
        this.pago = pago;
    }
    
    

    public Date getDiaFluxo() {
        return diaFluxo;
    }

    public void setDiaFluxo(Date diaFluxo) {
        this.diaFluxo = diaFluxo;
    }

    public double getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(double recebimento) {
        this.recebimento = recebimento;
    }

    public double getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(double pagamentos) {
        this.pagamentos = pagamentos;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public TreeNode getRecebido() {
        return recebido;
    }

    public void setRecebido(TreeNode recebido) {
        this.recebido = recebido;
    }

    public TreeNode getPago() {
        return pago;
    }

    public void setPago(TreeNode pago) {
        this.pago = pago;
    }
    
    
    
    

}
