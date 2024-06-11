package beans.ContasReceber;

import dao.ContasReceber.BoletoGeradoDao;
import dao.ContasReceber.ParcelaDao;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasReceber.BoletoGerado;
import modelo.ContasReceber.Recebimento;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Classe do menagedBean do bloco, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "retornoMB")
@ViewScoped
public class RetornoMB implements Serializable {

    private UploadedFile file;
    private String resultado;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void doUpload(FileUploadEvent fileUploadEvent) throws ParseException, FileNotFoundException, IOException, Exception {
        file = fileUploadEvent.getFile();
        File arquivo = new File("retorno.ret"); //Criamos um nome para o arquivo  
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(arquivo)); //Criamos o arquivo  
        bos.write(fileUploadEvent.getFile().getContents()); //Gravamos os bytes lá  
        bos.close();
        int numBoleto = 0;
        int situacao = 0;
        float valorRec = 0;
        double saldoParc = 0;
        float valorMulta = 0;
        resultado = "";
        Date dataCredito = null;
        RecebimentoMB recebMB = new RecebimentoMB();
        ParcelaDao parcDao = new ParcelaDao();
        BigDecimal bd;
        BoletoGerado boleto = null;
        BoletoGeradoDao boletoDao = new BoletoGeradoDao();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            while (linha != null) {
                if (linha.charAt(13) == 'T') {
                    try {
                        numBoleto = Integer.parseInt(linha.substring(41, 56));
                    } catch (Exception e) {
                        numBoleto = 0;
                    }
                    boleto = boletoDao.getNumeroTitulo(numBoleto);
                    if (boleto == null) {
                        linha = lerArq.readLine();
                        continue;
                    }
                    switch (Integer.parseInt(linha.substring(15, 17))) {
                        case 6:
                            situacao = 2;
                            break;
                        case 2:
                            situacao = 1;
                            break;
                        case 9:
                            situacao = 3;
                            break;
                        case 23:
                            situacao = 4;
                            break;
                        case 25:
                            situacao = 5;
                            break;
                        case 3:
                            situacao = 6;
                            break;
                        default:
                            situacao = 99;
                            break;
                    }
                    linha = lerArq.readLine();
                    while (linha.charAt(13) != 'U') {
                        if (linha.charAt(13) == 'T') {
                            throw new IOException("Arquivo de retorno inválido");
                        } else {
                            linha = lerArq.readLine();
                        }
                    }
                    if (linha.charAt(13) == 'U') {
                        valorMulta = Float.parseFloat(linha.substring(17, 30) + "." + linha.substring(31, 32));
                        valorRec = Float.parseFloat(linha.substring(92, 105) + "." + linha.substring(105, 107));
                        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
                        dataCredito = new Date(format.parse(linha.substring(145, 153)).getTime());

                    }
                    if (boleto != null) {
                        switch (situacao) {
                            case 2:
                                if (!boleto.getParcela().isPaga() && boleto.getSituacao() != 2) {
                                    if (numBoleto > 0 && valorRec > 0 && dataCredito != null) {
                                        recebMB.setBean(new Recebimento());
                                        recebMB.getBean().setDataPagamento(dataCredito);
                                        recebMB.getBean().setFormaPagamento(1);
                                        recebMB.getBean().setParcela(parcDao.parcelaBoleto(numBoleto));
                                        if (recebMB.getBean().getParcela() == null) {
                                            throw new IOException("Boleto não encontrato no sistema");
                                        }
                                        saldoParc = recebMB.getBean().getParcela().saldoParcela();
                                        bd = new BigDecimal(valorRec - valorMulta).setScale(2, RoundingMode.HALF_DOWN);
                                        recebMB.getBean().setValorRecebido(bd.floatValue());
                                        if (valorMulta > 0) {
                                            bd = new BigDecimal(saldoParc * 0.02).setScale(2, RoundingMode.HALF_DOWN);
                                            recebMB.getBean().setValorMulta(bd.floatValue());
                                            bd = new BigDecimal(valorMulta - bd.floatValue()).setScale(2, RoundingMode.HALF_DOWN);
                                            recebMB.getBean().setValorJuros(bd.floatValue());
                                        }
                                        recebMB.salvar();
                                        resultado += "Recebimento: " + boleto.getParcela().getContrato().nomePrimeiroCliente() + " - " + boleto.getParcela().getContrato().apartamentosDesc() + " - " + boleto.getParcela().getNumeracao() + " - Valor recebido: " + (valorRec) + " - Data Crédito: " + dateFormat.format(dataCredito) + "\n";
                                    } else {
                                        throw new IOException("Arquivo de retorno inválido");
                                    }
                                }
                                break;
                            case 1:
                                resultado += "Boleto: " + boleto.getParcela().getContrato().nomePrimeiroCliente() + " - " + boleto.getParcela().getContrato().apartamentosDesc() + " - " + boleto.getModalidade() + "/" + boleto.getNumerosTitulo().getNossoNumero() + " - Valor: " + boleto.getValorBoleto() + " - Vencimento: " + dateFormat.format(boleto.getDataVencimento()) + " - Entrada Confirmada\n";
                                break;
                            case 3:
                                resultado += "Boleto: " + boleto.getParcela().getContrato().nomePrimeiroCliente() + " - " + boleto.getParcela().getContrato().apartamentosDesc() + " - " + boleto.getModalidade() + "/" + boleto.getNumerosTitulo().getNossoNumero() + " - Valor: " + boleto.getValorBoleto() + " - Vencimento: " + dateFormat.format(boleto.getDataVencimento()) + " - Cancelado\n";
                                break;
                            case 4:
                                resultado += "Boleto: " + boleto.getParcela().getContrato().nomePrimeiroCliente() + " - " + boleto.getParcela().getContrato().apartamentosDesc() + " - " + boleto.getModalidade() + "/" + boleto.getNumerosTitulo().getNossoNumero() + " - Valor: " + boleto.getValorBoleto() + " - Vencimento: " + dateFormat.format(boleto.getDataVencimento()) + " - Remessa à cartório\n";
                                break;
                            case 5:
                                boleto.getParcela().setSituacao(6);
                                (new ParcelaDao()).save(boleto.getParcela());
                                resultado += "Boleto: " + boleto.getParcela().getContrato().nomePrimeiroCliente() + " - " + boleto.getParcela().getContrato().apartamentosDesc() + " - " + boleto.getModalidade() + "/" + boleto.getNumerosTitulo().getNossoNumero() + " - Valor: " + boleto.getValorBoleto() + " - Vencimento: " + dateFormat.format(boleto.getDataVencimento()) + " - Protestado\n";
                                break;
                            case 6:
                                resultado += "Boleto: " + boleto.getParcela().getContrato().nomePrimeiroCliente() + " - " + boleto.getParcela().getContrato().apartamentosDesc() + " - " + boleto.getModalidade() + "/" + boleto.getNumerosTitulo().getNossoNumero() + " - Valor: " + boleto.getValorBoleto() + " - Vencimento: " + dateFormat.format(boleto.getDataVencimento()) + " - Entrada rejeitada\n";
                                break;
                            default:
                                resultado += "";
                                break;
                        }
                        boleto.setSituacao(situacao);
                        boletoDao.save(boleto);
                    } else {
                        throw new IOException("Arquivo de retorno inválido");
                    }

                }
                numBoleto = 0;
                situacao = 0;
                valorMulta = 0;
                valorRec = 0;
                dataCredito = null;

                linha = lerArq.readLine();
            }
            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        if (!"".equalsIgnoreCase(resultado)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", resultado));
        }
    }

    public String novo() {
        return "formProcessarRetorno";
    }
}
