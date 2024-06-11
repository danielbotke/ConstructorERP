package beans.ContasReceber;

import br.com.nordestefomento.jrimum.bopepo.BancoSuportado;
import br.com.nordestefomento.jrimum.bopepo.Boleto;
import br.com.nordestefomento.jrimum.bopepo.view.BoletoViewer;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.endereco.CEP;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.endereco.Endereco;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import br.com.nordestefomento.jrimum.domkee.comum.pessoa.id.cprf.CNPJ;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Banco;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Agencia;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Carteira;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Cedente;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.CodigoDeCompensacaoBACEN;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Modalidade;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Sacado;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.TipoDeCobranca;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Titulo;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;
import com.sun.mail.util.MailSSLSocketFactory;
import dao.ContasReceber.BoletoGeradoDao;
import dao.ContasReceber.ContratoAluguelDao;
import dao.ContasReceber.NumerosTituloDao;
import dao.ContasReceber.ParcelaDao;
import dao.IndiceDao;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import modelo.Apartamento;
import modelo.ContasReceber.BoletoGerado;
import modelo.Cliente;
import modelo.ContasReceber.Contrato;
import modelo.ContasReceber.ContratoAluguel;
import modelo.ContasReceber.NumerosTitulo;
import modelo.ContasReceber.Parcela;
import modelo.Indice;
import modelo.Pessoa;
import modelo.PessoaFisica;
import modelo.PessoaJuridica;
import modelo.Predio;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import utils.DateUtils;
import utils.StringUtils;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Classe do menagedBean do grupoParcelas, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "gerarBoletosMB")
@ViewScoped
public class GerarBoletosMB {

    private Cedente cedente;
    private static ContaBancaria contaBancaria;
    private Sacado sacado;
    private Titulo titulo;
    private Boleto boleto;
    private BoletoGerado boletoSelecionado;
    private boolean protestar;
    private List<Parcela> parcelasSelecionadas;
    private Date de;
    private Date ate;
    private Date dataSegundaVia;
    private Cliente clienteSelecionado;
    private Parcela parcelaSelecionada;
    private Predio predioSelecionado;
    private List<Parcela> parcelasFiltradas;
    private List<Parcela> parcelasCliente;
    private List<ContratoAluguel> contratosAluguel;
    private boolean boletosGerados = false;
    private Modalidade modalidade;
    private List<BoletoGerado> boletosFiltrados;
    private List<BoletoGerado> boletosParcela;
    private BoletoGeradoDao dao = new BoletoGeradoDao();
    private boolean gerandoSegundaVia = false;
    private boolean semMultaJuros = false;
    private double especificarValor = 0;
    private boolean enviarPorEmail = true;
    private int banco = 0;

    public int getBanco() {
        return banco;
    }

    public void setBanco(int banco) {
        this.banco = banco;
    }

    public Cedente getCedente() {
        if (cedente == null) {
            cedente = new Cedente("", "");
        }
        return cedente;
    }

    public Sacado getSacado() {
        return sacado;
    }

    public void setSacado(Sacado sacado) {
        this.sacado = sacado;
    }

    public ContaBancaria getContaBancaria() {
        if (contaBancaria == null) {
            contaBancaria = new ContaBancaria(BancoSuportado.CAIXA_ECONOMICA_FEDERAL.create());
            contaBancaria.setNumeroDaConta(new NumeroDaConta(538862, "7"));
            contaBancaria.setAgencia(new Agencia(1660, "0"));
        }
        contaBancaria.setCarteira(this.getCarteira());
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public boolean isProtestar() {
        return protestar;
    }

    public void setProtestar(boolean protestar) {
        this.protestar = protestar;
    }

    public Carteira getCarteira() {
        this.setModalidade(new Modalidade(14, "Registrada"));
        return new Carteira(1, TipoDeCobranca.COM_REGISTRO);
    }

    public int getCodigoCarteira() {
        return this.getCarteira().getCodigo();
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    public List<Parcela> getParcelasSelecionadas() {
        return parcelasSelecionadas;
    }

    public void setParcelasSelecionadas(List<Parcela> parcelasSelecionadas) {
        this.parcelasSelecionadas = parcelasSelecionadas;
    }

    public Date getDe() {
        return de;
    }

    public void setDe(Date de) {
        this.de = de;
    }

    public Date getAte() {
        return ate;
    }

    public void setAte(Date ate) {
        this.ate = ate;
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(Cliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public Predio getPredioSelecionado() {
        return predioSelecionado;
    }

    public void setPredioSelecionado(Predio predioSelecionado) {
        this.predioSelecionado = predioSelecionado;
    }

    public List<Parcela> getParcelasFiltradas() {
        return parcelasFiltradas;
    }

    public void setParcelasFiltradas(List<Parcela> parcelasFiltradas) {
        this.parcelasFiltradas = parcelasFiltradas;
    }

    public void selecionarTodas() {
        this.setParcelasSelecionadas(this.getParcelasFiltradas());
    }

    public boolean isBoletosGerados() {
        return boletosGerados;
    }

    public void setBoletosGerados(boolean boletosGerados) {
        this.boletosGerados = boletosGerados;
    }

    public List<BoletoGerado> getBoletosFiltrados() {
        return boletosFiltrados;
    }

    public void setBoletosFiltrados(List<BoletoGerado> boletosFiltrados) {
        this.boletosFiltrados = boletosFiltrados;
    }

    public Parcela getParcelaSelecionada() {
        if (parcelaSelecionada == null) {
            parcelaSelecionada = new Parcela();
        }
        return parcelaSelecionada;
    }

    public void setParcelaSelecionada(Parcela parcelaSelecionada) {
        this.parcelaSelecionada = parcelaSelecionada;
    }

    public List<Parcela> getParcelasCliente() throws Exception {
        if (clienteSelecionado != null) {
            parcelasCliente = (new ParcelaDao()).listAbertasCliente(clienteSelecionado.getId());
        } else {
            parcelasCliente = new ArrayList<>();
        }
        return parcelasCliente;
    }

    public void setParcelasCliente(List<Parcela> parcelasCliente) {
        this.parcelasCliente = parcelasCliente;
    }

    public BoletoGerado getBoletoSelecionado() {
        return boletoSelecionado;
    }

    public void setBoletoSelecionado(BoletoGerado boletoSelecionado) {
        this.boletoSelecionado = boletoSelecionado;
    }

    public List<BoletoGerado> getBoletosParcela() throws Exception {
        if (parcelaSelecionada != null) {
            boletosParcela = (new BoletoGeradoDao()).listBoletosParcela(parcelaSelecionada.getId());
        } else {
            boletosParcela = new ArrayList<>();
        }
        return boletosParcela;
    }

    public void setBoletosParcela(List<BoletoGerado> boletosParcela) {
        this.boletosParcela = boletosParcela;
    }

    public Date getDataSegundaVia() {
        return dataSegundaVia;
    }

    public void setDataSegundaVia(Date dataSegundaVia) {
        this.dataSegundaVia = dataSegundaVia;
    }

    public boolean isGerandoSegundaVia() {
        return gerandoSegundaVia;
    }

    public void setGerandoSegundaVia(boolean gerandoSegundaVia) {
        this.gerandoSegundaVia = gerandoSegundaVia;
    }

    public boolean isSemMultaJuros() {
        return semMultaJuros;
    }

    public void setSemMultaJuros(boolean semMultaJuros) {
        this.semMultaJuros = semMultaJuros;
    }

    public double getEspecificarValor() {
        return especificarValor;
    }

    public void setEspecificarValor(double especificarValor) {
        this.especificarValor = especificarValor;
    }

    public boolean isEnviarPorEmail() {
        return enviarPorEmail;
    }

    public void setEnviarPorEmail(boolean enviarPorEmail) {
        this.enviarPorEmail = enviarPorEmail;
    }

    public List<ContratoAluguel> getContratosAluguel() {
        return contratosAluguel;
    }

    public void setContratosAluguel(List<ContratoAluguel> contratosAluguel) {
        this.contratosAluguel = contratosAluguel;
    }

    public void carregarBoletos() throws Exception {
        if (clienteSelecionado == null) {
            clienteSelecionado = new Cliente();
        }
        if (predioSelecionado == null) {
            predioSelecionado = new Predio();
        }
        if (de != null && ate != null) {
            boletosFiltrados = dao.listBoletosClientePredio(clienteSelecionado.getId(), predioSelecionado.getId(), de, ate);
        }
    }

    public void carregar() throws Exception {
        if (clienteSelecionado == null) {
            clienteSelecionado = new Cliente();
        }
        if (predioSelecionado == null) {
            predioSelecionado = new Predio();
        }
        if (de != null && ate != null) {
            parcelasFiltradas = (new ParcelaDao()).listParcelasBoletos(clienteSelecionado.getId(), predioSelecionado.getId(), de, ate, boletosGerados);
        }
        this.carregarAluguel();

    }

    public void carregarAluguel() throws Exception {
        if (clienteSelecionado == null) {
            clienteSelecionado = new Cliente();
        }
        if (predioSelecionado == null) {
            predioSelecionado = new Predio();
        }
        if (de != null && ate != null) {
            contratosAluguel = (new ContratoAluguelDao()).listClientePredio(clienteSelecionado.getId(), predioSelecionado.getId(), 0);
        }
        this.gerarBoletosAluguel();

    }

    public String novo() {
        parcelasSelecionadas = new ArrayList<>();
        parcelasFiltradas = new ArrayList<>();
        contratosAluguel = new ArrayList<>();
        clienteSelecionado = new Cliente();
        predioSelecionado = new Predio();
        de = null;
        ate = null;
        return "formGerarBoletos";
    }

    public String deletar(BoletoGerado boleto) {
        try {
            if (boleto.getParcela().getBoletos().size() == 1) {
                boleto.getParcela().setBoletoGerado(false);
                (new ParcelaDao()).save(boleto.getParcela());
            }
            dao.delete(boleto);
            this.carregarBoletos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir parcela. Verifique se a parcela não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public String gerenciarBoletos() {
        return "formGerencBoletos";
    }

    public void botaoGerarBoletos() throws ParseException, IOException, EmailException, Exception {
        if (!parcelasSelecionadas.isEmpty()) {
            this.gerarBoletos(parcelasSelecionadas);
        }
        parcelasSelecionadas = new ArrayList<>();
        this.carregar();

    }

    public void botaoGerarSegundaVia() throws ParseException, IOException, EmailException, Exception {
        if (parcelaSelecionada != null) {
            gerandoSegundaVia = true;
            parcelasSelecionadas = new ArrayList<>();
            parcelasSelecionadas.add(parcelaSelecionada);
            this.gerarBoletos(parcelasSelecionadas);
        }
        gerandoSegundaVia = false;
        semMultaJuros = false;
    }

    public void gerarBoletosAluguel() throws Exception {
        Parcela auxParcela;
        Contrato auxContrato;
        List<Apartamento> auxAptos;
        Indice indiceAtual = (new IndiceDao()).getLast(1);
        for (ContratoAluguel contrato : contratosAluguel) {
            auxContrato = new Contrato();
            auxAptos = new ArrayList<>();
            auxAptos.add(contrato.getUnidade());
            auxContrato.setApartamentos(auxAptos);
            auxContrato.setClientes(contrato.getClientes());
            auxContrato.setCUB(indiceAtual);
            auxParcela = new Parcela();
            auxParcela.setContrato(auxContrato);
            GregorianCalendar gc = new GregorianCalendar();
            gc.set(Calendar.DAY_OF_MONTH, contrato.getDiaMesVencimento());
            auxParcela.setDataVencimento(gc.getTime());
            auxParcela.setValorParcela((float) contrato.getValorAluguel());
            auxParcela.setValorFixo(true);
            auxParcela.setNumeracao("Aluguel");
            auxParcela.setFatorIndice((float) contrato.getValorAluguel() / indiceAtual.getValorIndice());
            auxParcela.setDescricao("Aluguel");
            auxParcela.setTipoIndice(indiceAtual.getTipoIndice());
            parcelasFiltradas.add(auxParcela);
        }
    }

    public void gerarBoletos(List<Parcela> parcelas) throws ParseException, IOException, EmailException, Exception {
        Pessoa pessoaParcela;
        BoletoGerado boletoGerado = new BoletoGerado();

        HashMap files = new HashMap();
        for (int i = 0; i < parcelas.size(); i++) {

            pessoaParcela = parcelas.get(i).getContrato().getClientes().get(0).getPessoa();
            String docto = "";
            double valorDocto;
            if (this.isGerandoSegundaVia() && !this.semMultaJuros) {

                valorDocto = boletoSelecionado.getValorBoleto() + ((boletoSelecionado.getValorBoleto() * 0.02)) + ((boletoSelecionado.getValorBoleto() * 0.0003) * DateUtils.diferencaEmDias(boletoSelecionado.getDataVencimento(), dataSegundaVia));
            } else if (especificarValor > 0) {
                valorDocto = especificarValor;
                especificarValor = 0;
            } else {
                valorDocto = parcelas.get(i).saldoParcela();
            }
            MaskFormatter mf;
            if (pessoaParcela instanceof PessoaFisica) {
                mf = new MaskFormatter("AAA.AAA.AAA-AA");
                mf.setValueContainsLiteralCharacters(false);
                docto = mf.valueToString(((PessoaFisica) pessoaParcela).getCPF());
            } else {
                mf = new MaskFormatter("AA.AAA.AAA/AAAA-AA");
                mf.setValueContainsLiteralCharacters(false);
                docto = ((PessoaJuridica) pessoaParcela).getCNPJ();
            }
            sacado = new Sacado(pessoaParcela.getName(), docto);

            // Informando o endereço do sacado.
            Endereco enderecoSac = new Endereco();
            enderecoSac.setUF(UnidadeFederativa.valueOf(pessoaParcela.getCidade().getEstado().getSigla()));
            enderecoSac.setLocalidade(pessoaParcela.getCidade().getName());
            enderecoSac.setCep(new CEP(pessoaParcela.getCEP() + ""));
            enderecoSac.setBairro(pessoaParcela.getBairro());
            enderecoSac.setLogradouro(pessoaParcela.getEndereco());
            sacado.addEndereco(enderecoSac);

            //Informando os dados do título
            NumerosTitulo numerosTitulo = (new NumerosTituloDao()).getNext();

            protestar = false;
            for (Cliente cliente : parcelas.get(i).getContrato().getClientes()) {
                if (cliente.getFormaPagamento() == 1) {
                    protestar = true;
                }
            }
            titulo = new Titulo(this.getContaBancaria(), this.getSacado(), this.getCedente());
           /* boleto = new Boleto(titulo);
            Banco bc = BancoSuportado.CAIXA_ECONOMICA_FEDERAL.create();
            contaBancaria = new ContaBancaria(bc);
            if (banco == 136) {
                CodigoDeCompensacaoBACEN ccb = new CodigoDeCompensacaoBACEN(136);
                bc.setCodigoDeCompensacaoBACEN(ccb);
                bc.setSegmento("Unicred");
                bc.setNomeFantasia("Unicred");
                bc.setCNPJ(new CNPJ("00315557000111"));
                contaBancaria.setNumeroDaConta(new NumeroDaConta(400988, "6"));
                contaBancaria.setAgencia(new Agencia(1302, "1"));
            }*/
            titulo = new Titulo(this.getContaBancaria(), this.getSacado(), this.getCedente());
            titulo.setNumeroDoDocumento(numerosTitulo.getNumeroDocto());
            titulo.setNossoNumero(this.getCodigoCarteira() + "4" + numerosTitulo.getNossoNumero());
            titulo.setDigitoDoNossoNumero(numerosTitulo.getDigitoVerificador(this.getCodigoCarteira() + "4"));
            titulo.setValor(BigDecimal.valueOf(valorDocto));
            titulo.setDataDoDocumento(new Date());
            titulo.setDataDoVencimento(this.dataSegundaVia != null ? dataSegundaVia : parcelas.get(i).getDataVencimento());
            titulo.setTipoDeDocumento(TipoDeTitulo.DMI_DUPLICATA_MERCANTIL_PARA_INDICACAO);
            titulo.setAceite(EnumAceite.N);

            //Informando os dados do boleto
            boleto = new Boleto(titulo);
            boleto.setLocalPagamento("PAGAR EM QUALQUER BANCO");
            NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date(dataSegundaVia != null ? dataSegundaVia.getTime() : parcelas.get(i).getDataVencimento().getTime()));
            gc.add(GregorianCalendar.DAY_OF_MONTH, 1);
            boleto.setInstrucao1("MULTA DE R$: " + nf.format(titulo.getValor().floatValue() * 0.02) + " APOS: " + dt.format(gc.getTime()).toString());
            double valorMulta = titulo.getValor().floatValue() * 0.0003;
            boleto.setInstrucao2("JUROS DE R$: " + nf.format(valorMulta >= 0.01 ? valorMulta : 0.01) + " AO DIA");
            if (isProtestar()) {
                boleto.setInstrucao3("PROTESTAR APOS 5 DIAS DO VENCIMENTO");
            } else {
                boleto.setInstrucao3("NAO RECEBER APOS 5 DIAS DO VENCIMENTO");
            }
            boleto.setInstrucao4("Parcela " + parcelas.get(i).getNumeracao() + " - " + parcelas.get(i).getContrato().apartamentosDesc());

            //Gerando o boleto em pdf
            String reforco = "";
            if (parcelas.get(i).getNumeracao() != null && parcelas.get(i).getNumeracao().length() > 0 && parcelas.get(i).getNumeracao().charAt(0) == 'R') {
                reforco = "R";
            } else if (parcelas.get(i).getNumeracao() == null || parcelas.get(i).getNumeracao().length() == 0) {
                reforco = "RN";
            }
            BoletoViewer boletoViewer = new BoletoViewer(boleto);
            URI myURI = URI.create(this.getClass().getResource("").toString() + "/LayoutBoleto/BoletoTemplatePersonalizado.pdf");
            boletoViewer.setTemplate(myURI.getPath());
            StringTokenizer sttk = new StringTokenizer(parcelas.get(i).getNumeracao(), "/");
            File file = boletoViewer.getPdfAsFile(pessoaParcela.getName() + " - " + StringUtils.removerPipe(parcelas.get(i).getContrato().apartamentosDesc()) + " - P." + (sttk.hasMoreTokens() ? sttk.nextToken() : "") + ".pdf");
            int dif = 1;
            while (files.get(file.getName()) != null) {
                file = boletoViewer.getPdfAsFile(pessoaParcela.getName() + " - " + StringUtils.removerPipe(parcelas.get(i).getContrato().apartamentosDesc()) + " - P." + (sttk.hasMoreTokens() ? sttk.nextToken() : "") + dif + ".pdf");
                dif++;
            }
            if (enviarPorEmail) {
                try {
                    this.enviarEmail(pessoaParcela.getEmail(), file);
                } catch (EmailException e) {
                    e.printStackTrace();
                    dao.delete(boletoGerado);
                    continue;
                }
            }
            files.put(file.getName(), file);

            //Salvando Boleto
            boletoGerado.setParcela(parcelas.get(i));
            boletoGerado.setCarteira(this.getCarteira().getCodigo() + "");
            boletoGerado.setModalidade(this.getModalidade().getCodigo() + "");
            boletoGerado.setDataDocto(titulo.getDataDoDocumento());
            boletoGerado.setDataVencimento(titulo.getDataDoVencimento());
            boletoGerado.setValorBoleto(titulo.getValor().doubleValue());
            boletoGerado.setInstrucao(boleto.getInstrucao4());
            boletoGerado.setNumerosTitulo(numerosTitulo);
            try {
                (new BoletoGeradoDao()).save(boletoGerado);
                boletoGerado.setId(0);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar boleto. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            }

            //Salvando parcela com o campo boleto gerado como true
            try {
                parcelas.get(i).setBoletoGerado(true);
                (new ParcelaDao()).save(parcelas.get(i));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar parcela. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            }

        }
        //Código para download do pdf
        if (files.size() == 1) {
            this.downloadUnico(files);
        } else if (files.size() > 0) {
            this.downloadMultiplos(files);
        }

    }

    public void baixarBoletoExistente(BoletoGerado boletoABaixar) throws ParseException, IOException, Exception {
        Pessoa pessoaParcela = boletoABaixar.getParcela().getContrato().getClientes().get(0).getPessoa();;
        HashMap files = new HashMap();
        String docto = "";
        double valorDocto = boletoABaixar.getValorBoleto();
        MaskFormatter mf;
        if (pessoaParcela instanceof PessoaFisica) {
            mf = new MaskFormatter("AAA.AAA.AAA-AA");
            mf.setValueContainsLiteralCharacters(false);
            docto = mf.valueToString(((PessoaFisica) pessoaParcela).getCPF());
        } else {
            mf = new MaskFormatter("AA.AAA.AAA/AAAA-AA");
            mf.setValueContainsLiteralCharacters(false);
            docto = ((PessoaJuridica) pessoaParcela).getCNPJ();
        }
        sacado = new Sacado(pessoaParcela.getName(), docto);

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.valueOf(pessoaParcela.getCidade().getEstado().getSigla()));
        enderecoSac.setLocalidade(pessoaParcela.getCidade().getName());
        enderecoSac.setCep(new CEP(pessoaParcela.getCEP() + ""));
        enderecoSac.setBairro(pessoaParcela.getBairro());
        enderecoSac.setLogradouro(pessoaParcela.getEndereco());
        sacado.addEndereco(enderecoSac);

        //Informando os dados do título
        protestar = false;
        for (Cliente cliente : boletoABaixar.getParcela().getContrato().getClientes()) {
            if (cliente.getFormaPagamento() == 1) {
                protestar = true;
            }
        }
        titulo = new Titulo(this.getContaBancaria(), this.getSacado(), this.getCedente());
        titulo.setNumeroDoDocumento(boletoABaixar.getNumerosTitulo().getNumeroDocto());
        titulo.setNossoNumero(this.getCodigoCarteira() + "4" + boletoABaixar.getNumerosTitulo().getNossoNumero());
        titulo.setDigitoDoNossoNumero(boletoABaixar.getNumerosTitulo().getDigitoVerificador(this.getCodigoCarteira() + "4"));
        titulo.setValor(BigDecimal.valueOf(valorDocto));
        titulo.setDataDoDocumento(boletoABaixar.getDataDocto());
        titulo.setDataDoVencimento(boletoABaixar.getDataVencimento());
        titulo.setTipoDeDocumento(TipoDeTitulo.DMI_DUPLICATA_MERCANTIL_PARA_INDICACAO);
        titulo.setAceite(EnumAceite.N);

        //Informando os dados do boleto
        boleto = new Boleto(titulo);
        boleto.setLocalPagamento("PAGAR PREFERENCIALMENTE NAS CASAS LOTÉRICAS ATÉ A DATA LIMITE");
        NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(boletoABaixar.getDataVencimento());
        gc.add(GregorianCalendar.DAY_OF_MONTH, 1);
        boleto.setInstrucao1("MULTA DE R$: " + nf.format(titulo.getValor().floatValue() * 0.02) + " APOS: " + dt.format(gc.getTime()).toString());
        double valorMulta = titulo.getValor().floatValue() * 0.0003;
        boleto.setInstrucao2("JUROS DE R$: " + nf.format(valorMulta >= 0.01 ? valorMulta : 0.01) + " AO DIA");
        if (isProtestar()) {
            boleto.setInstrucao3("PROTESTAR APOS 5 DIAS DO VENCIMENTO");
        } else {
            boleto.setInstrucao3("NAO RECEBER APOS 5 DIAS DO VENCIMENTO");
        }
        boleto.setInstrucao4("Parcela " + boletoABaixar.getParcela().getNumeracao() + " - " + boletoABaixar.getParcela().getContrato().apartamentosDesc());

        //Gerando o boleto em pdf
        String reforco = "";
        if (boletoABaixar.getParcela().getNumeracao() != null && boletoABaixar.getParcela().getNumeracao().length() > 0 && boletoABaixar.getParcela().getNumeracao().charAt(0) == 'R') {
            reforco = "R";
        } else if (boletoABaixar.getParcela().getNumeracao() == null || boletoABaixar.getParcela().getNumeracao().length() == 0) {
            reforco = "RN";
        }
        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        URI myURI = URI.create(this.getClass().getResource("").toString() + "/LayoutBoleto/BoletoTemplatePersonalizado.pdf");
        boletoViewer.setTemplate(myURI.getPath());
        File file = boletoViewer.getPdfAsFile(pessoaParcela.getName() + " " + boletoABaixar.getParcela().getContrato().apartamentosDesc() + reforco + ".pdf");
        int dif = 1;
        while (files.get(file.getName()) != null) {
            file = boletoViewer.getPdfAsFile(pessoaParcela.getName() + " " + boletoABaixar.getParcela().getContrato().apartamentosDesc() + reforco + dif + ".pdf");
            dif++;
        }
        files.put(file.getName(), file);

        //Código para download do pdf
        if (files.size() == 1) {
            this.downloadUnico(files);
        } else {
            this.downloadMultiplos(files);
        }

    }

    public void downloadUnico(HashMap files) {
        Iterator i = files.keySet().iterator();
        File file = (File) files.get((String) i.next());
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\""); //header e o nome q vai aparecer na hr do donwload  
        response.setContentLength((int) file.length()); // tamanho do arquivo  
        response.setContentType("application/pdf"); // tipo  

        try {
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[(int) file.length()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException ex) {
            System.out.println("Error in downloadFile: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void downloadMultiplos(HashMap files) throws FileNotFoundException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipfile = new ZipOutputStream(bos);
        FileInputStream fis;
        Iterator i = files.keySet().iterator();
        String fileName = null;
        ZipEntry zipentry = null;
        while (i.hasNext()) {
            fileName = (String) i.next();
            fis = new FileInputStream(fileName);
            zipentry = new ZipEntry(fileName);
            zipfile.putNextEntry(zipentry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipfile.write(bytes, 0, length);
            }
            zipfile.closeEntry();
            fis.close();
        }

        bos.flush();
        zipfile.flush();
        bos.close();
        zipfile.close();

        byte[] zip = bos.toByteArray();

        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) context.getResponse();

            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename='Boletos.zip'");
            sos.write(zip);
            sos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowSelect(SelectEvent event) {
        if (event.getObject().getClass() == Cliente.class) {
            clienteSelecionado = (Cliente) event.getObject();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('cliDialog').hide();");
        }
        if (event.getObject().getClass() == Parcela.class) {
            parcelaSelecionada = ((Parcela) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('parcDialog').hide();");
        }
        if (event.getObject().getClass() == Parcela.class) {
            parcelaSelecionada = ((Parcela) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('parcDialog').hide();");
        }
        if (event.getObject().getClass() == BoletoGerado.class) {
            boletoSelecionado = ((BoletoGerado) event.getObject());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('boletoDialog').hide();");
        }
    }

    public void enviarEmail(String to, File boleto) throws EmailException, GeneralSecurityException {

        // cria algumas propriedades e obtem uma sessao padrao
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.googlemail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.googlemail.com");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", "");
            }
        });

        try {
            // cria a mensagem
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(""));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("");

            // cria a primeira parte da mensagem
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText("Olá,\nSegue anexo o boleto para pagamento da parcela do seu imóvel. \n \nFicamos à disposição para qualquer dúvida.");

            // cria a segunda parte da mensage
            MimeBodyPart mbp2 = new MimeBodyPart();

            // anexa o arquivo na mensagem
            FileDataSource fds = new FileDataSource(boleto);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());

            // cria a Multipart
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // adiciona a Multipart na mensagem
            msg.setContent(mp);

            // configura a data: cabecalho
            msg.setSentDate(new Date());

            // envia a mensagem
            Transport.send(msg);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }

    }

    public void enviarEmail2(String destinatario, File boleto) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("", ""));
        email.setSSLOnConnect(true);
        email.setFrom("", "");
        email.setSubject("");
        email.setHtmlMsg("");
        email.setTextMsg("Olá,\nSegue anexo o boleto para pagamento da parcela do seu imóvel. \n \nFicamos à disposição para qualquer dúvida.");
        email.addTo(destinatario);
        email.attach(boleto);
        email.send();

    }
}
