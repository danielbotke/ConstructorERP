package beans.ContasReceber;

import dao.ContasReceber.ContratoDao;
import dao.ContasReceber.GrupoParcelasDao;
import dao.IndiceDao;
import dao.ContasReceber.ParcelaDao;
import dao.TipoIndiceDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.ContasReceber.Contrato;
import modelo.ContasReceber.GrupoParcelas;
import modelo.Indice;
import modelo.ContasReceber.Parcela;

/**
 * Classe do menagedBean do parcela, utilizada para interar as informações deste
 * objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "parcelaMB")
@ViewScoped
public class ParcelaMB implements Serializable {

    private Parcela bean = new Parcela();
    private ParcelaDao dao = new ParcelaDao();
    private GrupoParcelasMB grupoParcelasMB = new GrupoParcelasMB();
    private Contrato contrato;
    private List<Parcela> parcelas;
    private double valorFaltante;
    private boolean renegociada = false;
    private boolean editando = false;
    /*
     @ManagedProperty("#{param.parc}")
     int parc;*/

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public double getValorFaltante() throws Exception {
        if (contrato != null) {
            valorFaltante = this.getCUBsFaltantes() * contrato.getCUB().getValorIndice();
        }
        return valorFaltante;
    }

    public double getCUBsFaltantes() throws Exception {
        if (null != contrato) {
            return contrato.getTotalCUBs() - this.totalCUBParcelasContrato(contrato.getId());
        } else {
            return 0;
        }
    }

    public Parcela getBean() {
        if (bean == null) {
            bean = new Parcela();
        }
        return bean;
    }

    public void setBean(Parcela bean) {
        this.bean = bean;
    }
    /*
     public int getParc() {
     return parc;
     }

     public void setParc(int parc) {
     this.parc = parc;
     }*/

    public GrupoParcelasMB getGrupoParcelasMB() {
        return grupoParcelasMB;
    }

    public void setGrupoParcelasMB(GrupoParcelasMB grupoParcelasMB) {
        this.grupoParcelasMB = grupoParcelasMB;
    }

    public boolean isRenegociada() {
        return renegociada;
    }

    public void setRenegociada(boolean renegociada) {
        this.renegociada = renegociada;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    public String salvar() throws Exception {
        bean.setContrato(contrato);
        if (isRenegociada()) {
            bean.setPaga(true);
            bean.setSituacao(3);
            try {
                dao.save(bean);
                editando = false;
                bean = new Parcela();
                this.setRenegociada(false);
                return "";
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar parcela. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                return "";
            }
        }
        if (bean.getValorParcelaFixa() > 0 || grupoParcelasMB.isSalvandoGrupo() || bean.getFatorIndice() > 0) {
            if (bean.isValorFixo()) {
                bean.setTipoIndice((new TipoIndiceDao()).get("CUB"));
                Indice indice = (new IndiceDao()).getIndiceMonth(new Date(bean.getDataVencimento().getTime()), bean.getTipoIndice().getId());
                if (indice == null) {
                    indice = (new IndiceDao()).getLast(bean.getTipoIndice().getId());
                }
                bean.setFatorIndice(bean.getValorParcelaFixa() / indice.getValorIndice());
            }
            if (!bean.isValorFixo()) {
                bean.setValorParcela(0);
            }
            try {
                dao.save(bean);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar parcela. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                return "";
            }
        } else {
            return grupoParcelasMB.salvar(this);
        }
        if (!grupoParcelasMB.isSalvandoGrupo()) {
            bean = new Parcela();
        }
        editando = false;
        return "";

    }

    public String novo() {
        bean = new Parcela();
        return "formParcelas";
    }

    public void editar(Parcela parc) {
        bean = parc;
        editando = true;
    }

    public String deletar(Parcela parc) {
        try {
            dao.delete(parc);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir parcela. Verifique se a parcela não está sendo utilizada em nenhum outro cadastro, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
        }
        return "";
    }

    public String protestar(Parcela parc) {
        if (parc != null) {
            if (parc.getSituacao() == 0) {
                parc.setSituacao(6);
            } else if (parc.getSituacao() == 6) {
                parc.setSituacao(0);
            }
        }
        try {
            dao.save(parc);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcela."));
        }
        return "";
    }

    public String gerarNueracao() throws Exception {
        List<Contrato> contratos;
        List<Parcela> parcelasNumeracao;
        List<GrupoParcelas> GruposParcelas;
        GrupoParcelasDao grupoDao = new GrupoParcelasDao();
        if (contrato == null) {
            contratos = (new ContratoDao()).listAll();
        } else {
            contratos = new ArrayList<>();
            contratos.add(contrato);
        }
        for (int c = 0; c < contratos.size(); c++) {
            GruposParcelas = grupoDao.listGruposContrato(contratos.get(c).getId());
            for (int g = 0; g < GruposParcelas.size(); g++) {
                parcelasNumeracao = dao.listParcelasGrupo(GruposParcelas.get(g).getId());
                for (int i = 0; i < parcelasNumeracao.size(); i++) {
                    Parcela parcela = parcelasNumeracao.get(i);
                    try {
                        parcela.setNumeracao((i + 1) + "/" + parcelasNumeracao.size());
                        dao.save(parcela);
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar parcela. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                }
            }
            parcelasNumeracao = dao.listReforcos(contratos.get(c).getId());
            String aux = "";
            for (int i = 0; i < parcelasNumeracao.size(); i++) {
                Parcela parcela = parcelasNumeracao.get(i);
                if (parcela.getDescricao().trim().contains("Chave") || parcela.getDescricao().trim().contains("chave")) {
                    try {
                        parcela.setNumeracao("Chaves");
                        dao.save(parcela);
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar parcela. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                    parcelasNumeracao.remove(i);
                    i--;
                } else {
                    if (i < 9) {
                        aux = "0";
                    } else {
                        aux = "";
                    }
                    try {
                        parcela.setNumeracao("R" + aux + (i + 1));
                        dao.save(parcela);
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao salvar parcela. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                }
            }
        }
        return "";
    }

    public String alterarDiaParcelasAbertas() throws Exception {
        if (contrato != null) {
            List<Parcela> parcelasContrato = dao.listAbertasContrato(contrato.getId());
            int novoDia = grupoParcelasMB.getBean().getDiaMesVencimento();
            if (novoDia > 0 && parcelasContrato.size() > 0) {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeZone(TimeZone.getTimeZone("GMT-3"));
                Date aux;
                for (int i = 0; i < parcelasContrato.size(); i++) {
                    gc.setTime(parcelasContrato.get(i).getDataVencimento());
                    if (gc.getActualMaximum(Calendar.DAY_OF_MONTH) < novoDia) {
                        gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
                    } else {
                        gc.set(Calendar.DAY_OF_MONTH, novoDia);
                    }
                    gc.set(Calendar.HOUR_OF_DAY, 12);
                    aux = gc.getTime();
                    parcelasContrato.get(i).setDataVencimento(aux);
                    try {
                        dao.save(parcelasContrato.get(i));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                }
            }
        }
        return "";
    }

    public List<Parcela> getParcelas() throws Exception {
        if (parcelas == null || parcelas.isEmpty()) {
            parcelas = dao.listAll();
        }
        return parcelas;
    }

    public double totalParcelasContrato(int contratoID) throws Exception {
        return (new ParcelaDao().totalParcelasContrato(contratoID));
    }

    public double totalCUBParcelasContrato(int contratoID) throws Exception {
        return dao.totalCUBParcelasContrato(contratoID);
    }
}
