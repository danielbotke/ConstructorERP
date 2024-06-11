package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.jboss.weld.bean.AbstractBean;

@Entity
@NamedQueries(value = {
    @NamedQuery(
        name = "Usuario.findByEmailSenha",
    query = "SELECT c FROM Usuario c " + "WHERE c.email = :email AND c.senha = :senha")})
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(name = "data_cadastro")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    @Column
    private boolean imobiliaria = false;
    @Column
    private boolean cliente = false;
    @Column
    private boolean contrato = false;
    @Column
    private boolean contratoAluguel = false;
    @Column
    private boolean indice = false;
    @Column
    private boolean localidades = false;
    @Column
    private boolean parcelas = false;
    @Column
    private boolean predio = false;
    @Column
    private boolean recebimento = false;
    @Column
    private boolean relParcela = false;
    @Column
    private boolean relReceb = false;
    @Column
    private boolean usuario = false;
    @Column
    private boolean pessoaFisica = false;
    @Column
    private boolean pessoaJuridica = false;
    @Column
    private boolean sessaoDireitos = false;
    @Column
    private boolean relSaldoDevedor = false;
    @Column
    private boolean relSaldoContrato = false;
    @Column
    private boolean renegociacao = false;
    @Column
    private boolean gerarBoleto = false;
    @Column
    private boolean gerarSegundaViaBoletos = false;
    @Column
    private boolean gerarRemessa = false;
    @Column
    private boolean gerenciarBoletos = false;
    @Column
    private boolean processarRetorno = false;
    @Column
    private boolean unidade = false;
    @Column
    private boolean categoria = false;
    @Column
    private boolean servico = false;
    @Column
    private boolean produto = false;
    @Column
    private boolean fornecedor = false;
    @Column
    private boolean cotacaoCompra = false;
    @Column
    private boolean notaFiscalCompra = false;
    @Column
    private boolean ParcelasAPagar = false;
    @Column
    private boolean gerencRemessas = false;
    @Column
    private boolean relDespesasPorCentroCusto = false;
    @Column
    private boolean relControleISS  = false;
    @Column
    private boolean relGanhoFinanceiro  = false;
    @Column
    private boolean enviarCampanha  = false;
    @Column
    private boolean reservarApto  = false;
    @Column
    private boolean administrarReservarApto  = false;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha.trim();
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isCliente() {
        return cliente;
    }

    public void setCliente(boolean cliente) {
        this.cliente = cliente;
    }

    public boolean isContrato() {
        return contrato;
    }

    public void setContrato(boolean contrato) {
        this.contrato = contrato;
    }

    public boolean isIndice() {
        return indice;
    }

    public void setIndice(boolean indice) {
        this.indice = indice;
    }

    public boolean isLocalidades() {
        return localidades;
    }

    public void setLocalidades(boolean localidades) {
        this.localidades = localidades;
    }

    public boolean isParcelas() {
        return parcelas;
    }

    public void setParcelas(boolean parcelas) {
        this.parcelas = parcelas;
    }

    public boolean isPredio() {
        return predio;
    }

    public void setPredio(boolean predio) {
        this.predio = predio;
    }

    public boolean isRecebimento() {
        return recebimento;
    }

    public void setRecebimento(boolean recebimento) {
        this.recebimento = recebimento;
    }

    public boolean isRelParcela() {
        return relParcela;
    }

    public void setRelParcela(boolean relParcela) {
        this.relParcela = relParcela;
    }

    public boolean isRelReceb() {
        return relReceb;
    }

    public void setRelReceb(boolean relReceb) {
        this.relReceb = relReceb;
    }

    public boolean isUsuario() {
        return usuario;
    }

    public void setUsuario(boolean usuario) {
        this.usuario = usuario;
    }

    public boolean isPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(boolean pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public boolean isPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(boolean pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public boolean isCessaoDireitos() {
        return sessaoDireitos;
    }

    public void setCessaoDireitos(boolean sessaoDireitos) {
        this.sessaoDireitos = sessaoDireitos;
    }

    public boolean isRelSaldoDevedor() {
        return relSaldoDevedor;
    }

    public void setRelSaldoDevedor(boolean relSaldoDevedor) {
        this.relSaldoDevedor = relSaldoDevedor;
    }

    public boolean isRelSaldoContrato() {
        return relSaldoContrato;
    }

    public void setRelSaldoContrato(boolean relSaldoContrato) {
        this.relSaldoContrato = relSaldoContrato;
    }

    public boolean isRenegociacao() {
        return renegociacao;
    }

    public void setRenegociacao(boolean renegociacao) {
        this.renegociacao = renegociacao;
    }

    public boolean isGerarBoleto() {
        return gerarBoleto;
    }

    public void setGerarBoleto(boolean gerarBoleto) {
        this.gerarBoleto = gerarBoleto;
    }

    public boolean isGerarRemessa() {
        return gerarRemessa;
    }

    public void setGerarRemessa(boolean gerarRemessa) {
        this.gerarRemessa = gerarRemessa;
    }

    public boolean isGerenciarBoletos() {
        return gerenciarBoletos;
    }

    public void setGerenciarBoletos(boolean gerenciarBoletos) {
        this.gerenciarBoletos = gerenciarBoletos;
    }

    public boolean isProcessarRetorno() {
        return processarRetorno;
    }

    public void setProcessarRetorno(boolean processarRetorno) {
        this.processarRetorno = processarRetorno;
    }

    public boolean isGerarSegundaViaBoletos() {
        return gerarSegundaViaBoletos;
    }

    public void setGerarSegundaViaBoletos(boolean gerarSegundaViaBoletos) {
        this.gerarSegundaViaBoletos = gerarSegundaViaBoletos;
    }

    public boolean isUnidade() {
        return unidade;
    }

    public void setUnidade(boolean unidade) {
        this.unidade = unidade;
    }

    public boolean isCategoria() {
        return categoria;
    }

    public void setCategoria(boolean categoria) {
        this.categoria = categoria;
    }

    public boolean isServico() {
        return servico;
    }

    public void setServico(boolean servico) {
        this.servico = servico;
    }

    public boolean isProduto() {
        return produto;
    }

    public void setProduto(boolean produto) {
        this.produto = produto;
    }

    public boolean isFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(boolean fornecedor) {
        this.fornecedor = fornecedor;
    }

    public boolean isCotacaoCompra() {
        return cotacaoCompra;
    }

    public void setCotacaoCompra(boolean cotacaoCompra) {
        this.cotacaoCompra = cotacaoCompra;
    }

    public boolean isNotaFiscalCompra() {
        return notaFiscalCompra;
    }

    public void setNotaFiscalCompra(boolean notaFiscalCompra) {
        this.notaFiscalCompra = notaFiscalCompra;
    }

    public boolean isParcelasAPagar() {
        return ParcelasAPagar;
    }

    public void setParcelasAPagar(boolean ParcelasAPagar) {
        this.ParcelasAPagar = ParcelasAPagar;
    }

    public boolean isGerencRemessas() {
        return gerencRemessas;
    }

    public void setGerencRemessas(boolean gerencRemessas) {
        this.gerencRemessas = gerencRemessas;
    }

    public boolean isRelDespesasPorCentroCusto() {
        return relDespesasPorCentroCusto;
    }

    public void setRelDespesasPorCentroCusto(boolean relDespesasPorCentroCusto) {
        this.relDespesasPorCentroCusto = relDespesasPorCentroCusto;
    }

    public boolean isRelControleISS() {
        return relControleISS;
    }

    public void setRelControleISS(boolean relControleISS) {
        this.relControleISS = relControleISS;
    }

    public boolean isEnviarCampanha() {
        return enviarCampanha;
    }

    public void setEnviarCampanha(boolean enviarCampanha) {
        this.enviarCampanha = enviarCampanha;
    }

    public boolean isReservarApto() {
        return reservarApto;
    }

    public void setReservarApto(boolean reservarApto) {
        this.reservarApto = reservarApto;
    }

    public boolean isAdministrarReservarApto() {
        return administrarReservarApto;
    }

    public void setAdministrarReservarApto(boolean administrarReservarApto) {
        this.administrarReservarApto = administrarReservarApto;
    }

    public boolean isContratoAluguel() {
        return contratoAluguel;
    }

    public void setContratoAluguel(boolean contratoAluguel) {
        this.contratoAluguel = contratoAluguel;
    }

    public boolean isRelGanhoFinanceiro() {
        return relGanhoFinanceiro;
    }

    public void setRelGanhoFinanceiro(boolean relGanhoFinanceiro) {
        this.relGanhoFinanceiro = relGanhoFinanceiro;
    }

    public boolean isImobiliaria() {
        return imobiliaria;
    }

    public void setImobiliaria(boolean imobiliaria) {
        this.imobiliaria = imobiliaria;
    }
    
    
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return (obj instanceof AbstractBean) ? (this.getId() == null ? this == obj : this.getId().equals(((AbstractBean) obj).getId())) : false;
    }
}