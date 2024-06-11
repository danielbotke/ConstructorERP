package utils;

import beans.ContasReceber.GerarBoletosMB;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import dao.ContasReceber.NumerosRemessaDao;
import dao.PessoaJuridicaDao;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import modelo.Cliente;
import modelo.ContasReceber.BoletoGerado;
import modelo.ContasReceber.NumerosRemessa;
import modelo.Pessoa;
import modelo.PessoaFisica;
import modelo.PessoaJuridica;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;
import org.jrimum.texgit.Texgit;

public class RemessaBancaria {

    private PessoaJuridica empresa;
    private ContaBancaria conta;
    private NumerosRemessa nrRemessa;
    private int i = 0;
    private List<BoletoGerado> titulos;
    private double totalTitulos = 0;

    public File geraRemessa(PessoaJuridica empresa, List<BoletoGerado> listaTitulos) throws IOException, URISyntaxException, Exception {


        this.empresa = new PessoaJuridicaDao();
        this.conta = new GerarBoletosMB().getContaBancaria();
        this.nrRemessa = new NumerosRemessaDao().getNext();
        this.titulos = listaTitulos;
        File arquivoRemessa = new File("REM" + nrRemessa.getId() + ".REM");

        URI myURI = URI.create(this.getClass().getResource("").toString() + "/LayoutBanco/LayoutCaixaCNAB240Envio.txg.xml");
        File layout = new File(myURI);
        FlatFile<Record> ff = Texgit.createFlatFile(layout);

        i = 0;
        ff.addRecord(createHeader(ff, i));

        ff.addRecord(createHeaderLote(ff, i));
        i++;

        try {
            for (BoletoGerado boleto : listaTitulos) {
                ff.addRecord(createDetailSegmentoP(ff, boleto, i));
                i++;
                totalTitulos += boleto.getValorBoleto();
                boleto.setNumerosRemessa(nrRemessa);
            }
            i++;
            ff.addRecord(createTrailerLote(ff, i));
            i++;
            i++;//Soma mais um porque tem que contar o cabeçalho do arquivo. 
            ff.addRecord(createTrailer(ff, i));

            FileUtils.writeLines(arquivoRemessa, ff.write(), "\r\n");
            return arquivoRemessa;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Record createHeader(FlatFile<Record> ff, int seq) {

        Record headerArquivo = ff.createRecord("HeaderArquivo");

        headerArquivo.setValue("UsoExclusivo", StringUtils.repeat(" ", 9));
        headerArquivo.setValue("TipoInscricao", "2");
        headerArquivo.setValue("Inscricao", "");
        headerArquivo.setValue("Brancos1", StringUtils.repeat("0", 20));
        headerArquivo.setValue("NumAgencia", this.conta.getAgencia().getCodigo());
        headerArquivo.setValue("DigAgencia", "8");
        headerArquivo.setValue("CodCedente", "");
        headerArquivo.setValue("Brancos2", StringUtils.repeat("0", 8));
        headerArquivo.setValue("NomeEmpresa", StringUtils.substring("", 0, 30));
        headerArquivo.setValue("UsoExclusivo2", StringUtils.repeat(" ", 10));
        headerArquivo.setValue("DataGeracao", (new SimpleDateFormat("ddMMyyyy")).format(new Date()));
        headerArquivo.setValue("HoraGeracao", (new SimpleDateFormat("HHmmss")).format(new Date()));
        headerArquivo.setValue("NumRemessa", this.nrRemessa.getId());
        headerArquivo.setValue("Brancos3", StringUtils.repeat(" ", 20));
        headerArquivo.setValue("CpfCnpj", "2");
        headerArquivo.setValue("Brancos5", StringUtils.repeat(" ", 25));
        return headerArquivo;
    }

    public Record createHeaderLote(FlatFile<Record> ff, int seq) {
        Record headerLote = ff.createRecord("HeaderLote");

        headerLote.setValue("UsoExclusivo", StringUtils.repeat("0", 2));
        headerLote.setValue("UsoExclusivo1", StringUtils.repeat(" ", 1));
        headerLote.setValue("TipoInscricao", "2");
        headerLote.setValue("Inscricao", "");
        headerLote.setValue("CodConvenio", "");
        headerLote.setValue("Brancos1", StringUtils.repeat("0", 14));
        headerLote.setValue("NumAgencia", this.conta.getAgencia().getCodigo());
        headerLote.setValue("DigAgencia", "8");
        headerLote.setValue("CodCedente", "");
        headerLote.setValue("CodModPersonalizado", StringUtils.repeat("0", 7));
        headerLote.setValue("Brancos2", "0");
        headerLote.setValue("NomeEmpresa", StringUtils.substring("", 0, 30));
        headerLote.setValue("Informacao1", StringUtils.repeat(" ", 40));
        headerLote.setValue("Informacao2", StringUtils.repeat(" ", 40));
        headerLote.setValue("NumRemessa", this.nrRemessa.getId());
        headerLote.setValue("DataGravacao", (new SimpleDateFormat("ddMMyyyy")).format(new Date()));
        headerLote.setValue("DataCredito", "00000000");
        headerLote.setValue("Brancos3", StringUtils.repeat(" ", 33));
        return headerLote;
    }

    public Record createTrailerLote(FlatFile<Record> ff, int seq) {
        Record trailerLote = ff.createRecord("TraillerLote");

        trailerLote.setValue("UsoExclusivo1", StringUtils.repeat(" ", 9));
        trailerLote.setValue("QtdRegistros", i);
        trailerLote.setValue("QtdTitulosCobranca", titulos.size());
        trailerLote.setValue("ValorTotalTitulosCarteira", (String.valueOf(totalTitulos)).replace(".", ""));
        trailerLote.setValue("QtdTitulosCobranca1", StringUtils.repeat("0", 6));
        trailerLote.setValue("ValorTotalTitulosCarteira1", StringUtils.repeat("0", 17));
        trailerLote.setValue("QtdTitulosCobranca2", StringUtils.repeat("0", 6));
        trailerLote.setValue("ValorTotalTitulosCarteira2", StringUtils.repeat("0", 17));
        trailerLote.setValue("UsoExclusivo2", StringUtils.repeat(" ", 148));

        return trailerLote;
    }

    public Record createTrailer(FlatFile<Record> ff, int seq) {
        Record trailerArquivo = ff.createRecord("TraillerArquivo");
        trailerArquivo.setValue("UsoExclusivo1", StringUtils.repeat(" ", 9));
        trailerArquivo.setValue("QtdLotes", "1");
        trailerArquivo.setValue("QtdArquivos", i);
        trailerArquivo.setValue("QtdContas", 0);
        trailerArquivo.setValue("UsoExclusivo2", StringUtils.repeat(" ", 211));
        return trailerArquivo;
    }

    public Record createDetailSegmentoP(FlatFile<Record> ff, BoletoGerado boleto, int seq) throws Exception {
        Record segmentoP = ff.createRecord("SegmentoP");

        segmentoP.setValue("NumSeqRegistro", i);
        segmentoP.setValue("NumAgencia", this.conta.getAgencia().getCodigo());
        segmentoP.setValue("DigAgencia", "8");
        segmentoP.setValue("CodCedente", "");
        segmentoP.setValue("UsoExclusivo", StringUtils.repeat("0", 11));
        segmentoP.setValue("NossoNumero", boleto.getModalidade() + boleto.getNumerosTitulo().getNossoNumero());
        segmentoP.setValue("CodCarteira", boleto.getCarteira());
        segmentoP.setValue("FormaCadastroTitulo", boleto.getCarteira());
        segmentoP.setValue("NrDocto", boleto.getNumerosTitulo().getNumeroDocto());
        segmentoP.setValue("Brancos2", StringUtils.repeat(" ", 4));
        segmentoP.setValue("Vencimento", (new SimpleDateFormat("ddMMyyyy")).format(boleto.getDataVencimento()));
        segmentoP.setValue("Valor", (String.valueOf((new DecimalFormat("###0.00", new DecimalFormatSymbols(new Locale("pt", "BR")))).format(boleto.getValorBoleto()))).replace(",", ""));
        segmentoP.setValue("Aceite", "N");
        segmentoP.setValue("Emissao", (new SimpleDateFormat("ddMMyyyy")).format(boleto.getDataDocto()));
        segmentoP.setValue("CodMora", "1");
        segmentoP.setValue("DataMora", "00000000");
        double valorMulta = boleto.getValorBoleto() * 0.0003;
        segmentoP.setValue("JurosDeMora", (String.valueOf((new DecimalFormat("###0.00", new DecimalFormatSymbols(new Locale("pt", "BR")))).format(valorMulta >= 0.01 ? valorMulta : 0.01))).replace(",", ""));
        segmentoP.setValue("CodigoDesconto", "0");
        segmentoP.setValue("DataDesconto", StringUtils.repeat("0", 8));
        segmentoP.setValue("DescontoConcedido", StringUtils.repeat("0", 15));
        segmentoP.setValue("IOF_Devido", StringUtils.repeat("0", 15));
        segmentoP.setValue("AbatimentoConcedido", StringUtils.repeat("0", 15));
        segmentoP.setValue("NrDocto2", boleto.getNumerosTitulo().getNumeroDocto());
        segmentoP.setValue("CodigoProtesto", "3");
        segmentoP.setValue("DiasProtesto", "00");
        segmentoP.setValue("CodigoBaixa", "1");
        segmentoP.setValue("DiasBaixa", "005");
        for (Cliente cliente : boleto.getParcela().getContrato().getClientes()) {
            if (cliente.getFormaPagamento() == 1) {
                segmentoP.setValue("CodigoProtesto", "1");
                segmentoP.setValue("DiasProtesto", "05");
                segmentoP.setValue("CodigoBaixa", "2");
                segmentoP.setValue("DiasBaixa", "000");

            }
        }



        segmentoP.setValue("UsoExclusivo", StringUtils.repeat("0", 10));
        segmentoP.setValue("Brancos3", StringUtils.repeat(" ", 1));



        i++;
        segmentoP.addInnerRecord(createDetailSegmentoQ(ff, boleto, i));

        i++;
        segmentoP.addInnerRecord(createDetailSegmentoR(ff, boleto, i));

        return segmentoP;
    }

    public Record createDetailSegmentoQ(FlatFile<Record> ff, BoletoGerado boleto, int seq) throws Exception {

        Record segmentoQ = ff.createRecord("SegmentoQ");
        Pessoa pessoa = boleto.getParcela().getContrato().getClientes().get(0).getPessoa();
        segmentoQ.setValue("NumSeqRegistro", i);
        segmentoQ.setValue("TipoInscricaoSacado", pessoa instanceof PessoaFisica ? "1" : pessoa instanceof PessoaJuridica ? "2" : "0");
        segmentoQ.setValue("NumeroInscricaoSacado", pessoa instanceof PessoaFisica ? ((PessoaFisica) pessoa).getCPF().toString() : pessoa instanceof PessoaJuridica ? ((PessoaJuridica) pessoa).getCNPJ().toString() : "0");
        segmentoQ.setValue("NomeSacado", (pessoa.getName()).length() > 40 ? (pessoa.getName()).substring(0, 39) : pessoa.getName());
        segmentoQ.setValue("LogradouroSacado", StringUtils.substring(pessoa.getEndereco(), 0, 15));
        segmentoQ.setValue("BairroSacado", StringUtils.substring(pessoa.getBairro(), 0, 15));
        segmentoQ.setValue("CepSacado", pessoa.getCEPPrevixo());
        segmentoQ.setValue("SufixoCep", pessoa.getCEPSufixo());
        segmentoQ.setValue("Cidade", StringUtils.substring(pessoa.getCidade().getName(), 0, 15));
        segmentoQ.setValue("Estado", pessoa.getCidade().getEstado().getSigla());
        segmentoQ.setValue("TipoSacadorAvalista", "0");
        segmentoQ.setValue("SacadorAvalista", StringUtils.repeat("0", 15));
        segmentoQ.setValue("NomeSacadorAvalista", StringUtils.repeat(" ", 40));
        segmentoQ.setValue("CodBancoCorresp", StringUtils.repeat(" ", 3));
        segmentoQ.setValue("NossoNumeroBancoCorresp", StringUtils.repeat(" ", 20));
        segmentoQ.setValue("UsoExclusivo", StringUtils.repeat(" ", 8));

        return segmentoQ;

    }

    private Record createDetailSegmentoR(FlatFile<Record> ff, BoletoGerado boleto, int seq) {
        Record segmentoR = ff.createRecord("SegmentoR");
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date(boleto.getDataVencimento().getTime()));
        gc.add(6, GregorianCalendar.DAY_OF_MONTH);

        segmentoR.setValue("NumSeqRegistro", i);
        segmentoR.setValue("DataDesconto2", "00000000");
        segmentoR.setValue("DataDesconto3", "00000000");
        segmentoR.setValue("DataMulta", (new SimpleDateFormat("ddMMyyyy")).format(gc.getTime()));
        segmentoR.setValue("ValorMulta", (String.valueOf((new DecimalFormat("###0.00", new DecimalFormatSymbols(new Locale("pt", "BR")))).format(boleto.getValorBoleto() * 0.02))).replace(",", ""));
        segmentoR.setValue("InformacaoAoSacado", StringUtils.repeat(" ", 10));
        segmentoR.setValue("Mensagem3", StringUtils.repeat(" ", 40));
        segmentoR.setValue("Mensagem4", StringUtils.repeat(" ", 40));
        segmentoR.setValue("EmaiçSacado", StringUtils.repeat(" ", 50));
        segmentoR.setValue("UsoExclusivo", StringUtils.repeat(" ", 11));
        return segmentoR;
    }
}
