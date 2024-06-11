package beans.ContasReceber;

import dao.ContasReceber.GrupoParcelasDao;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import modelo.ContasReceber.GrupoParcelas;
import modelo.ContasReceber.Parcela;
import utils.DateUtils;

/**
 * Classe do menagedBean do grupoParcelas, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "grupoParcelasMB")
public class GrupoParcelasMB {

    private GrupoParcelas bean = new GrupoParcelas();
    private GrupoParcelasDao dao = new GrupoParcelasDao();
    private boolean salvandoGrupo = false;
    /**
     * Variável utilizada para armazenar a modalidade do grupo de parcelas
     * durante a sua geração. 1 = diaMesVencimento 2 = diasEntreParcelas 3 =
     * primeiroDiaDaSemana 4 = primeiroDiaUtil
     *
     */
    private int modalidade;
    @ManagedProperty("#{param.contract}")
    int contract;

    public int getContract() {
        return contract;
    }

    public void setContract(int contract) {
        this.contract = contract;
    }

    public GrupoParcelas getBean() {
        if (bean == null) {
            bean = new GrupoParcelas();
        }
        return bean;
    }

    public void setBean(GrupoParcelas bean) {
        this.bean = bean;
    }

    public int getModalidade() {
        return modalidade;
    }

    public void setModalidade(int modalidade) {
        this.modalidade = modalidade;
    }

    public boolean isSalvandoGrupo() {
        return salvandoGrupo;
    }

    public void setSalvandoGrupo(boolean salvandoGrupo) {
        this.salvandoGrupo = salvandoGrupo;
    }

    public String salvar(ParcelaMB parcelaMB) throws Exception {
        salvandoGrupo = true;
        try {
            bean.setContrato(parcelaMB.getContrato());
            bean.setParcelas(null);
            bean.setTipoIndice(parcelaMB.getBean().getTipoIndice());
            dao.save(bean);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
            return "";
        }
        bean = dao.get(bean);
        Parcela parcela = parcelaMB.getBean();
        parcela.setGrupoParcelas(bean);
        parcela.setFatorIndice(bean.getFatorIndicePorParcela());
        parcela.setTipoIndice(bean.getTipoIndice());
        Date aux;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getTimeZone("GMT-3"));

        switch (modalidade) {
            case 1: //diaMesVencimento
                if (bean.getDiaMesVencimento() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar o dia do mês de vencimento da parcela."));
                    return "";
                }
                aux = new Date();
                gc.setTime(bean.getDataInicioParcelas());
                gc.set(Calendar.HOUR_OF_DAY, 12);
                gc.set(Calendar.DAY_OF_MONTH, bean.getDiaMesVencimento());
                /*while (gc.getTime().before(aux)) {
                 gc.add(Calendar.MONTH, 1);
                 }*/
                aux = gc.getTime();
                for (int i = 0; i < bean.getQntParcelas(); i++) {
                    parcela.setDataVencimento(aux);
                    try {
                        parcelaMB.salvar();
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                    parcelaMB.getBean().setId(0);
                    gc.add(Calendar.MONTH, 1);
                    if (bean.getDiaMesVencimento() > 28) {
                        gc.set(Calendar.DAY_OF_MONTH, 1);
                        if (gc.getActualMaximum(Calendar.DAY_OF_MONTH) < bean.getDiaMesVencimento()) {
                            gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
                        } else {
                            gc.set(Calendar.DAY_OF_MONTH, bean.getDiaMesVencimento());
                        }
                    }
                    aux = gc.getTime();
                }
                break;
            case 2: //diasEntreParcelas
                if (bean.getDiasEntreParcelas() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar a quantidade de dias entre parcelas."));
                    return "";
                }
                aux = new Date();
                gc.setTime(bean.getDataInicioParcelas());
                gc.set(Calendar.DAY_OF_MONTH, bean.getDiaMesVencimento());
                while (gc.getTime().before(aux)) {
                    bean.setDataInicioParcelas(DateUtils.adicionarDias(bean.getDataInicioParcelas(), bean.getDiasEntreParcelas()));
                }
                aux = bean.getDataInicioParcelas();
                for (int i = 0; i < bean.getQntParcelas(); i++) {
                    parcela.setDataVencimento(aux);
                    try {
                        parcelaMB.salvar();
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                    parcelaMB.getBean().setId(0);

                    aux = DateUtils.adicionarDias(aux, bean.getDiasEntreParcelas());
                }
                break;
            case 3: //primeiroDiaDaSemana
                if (bean.getPrimeiroDiaDaSemana() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar o dia da semana em que a parcela deve vencer."));
                    return "";
                }
                aux = new Date();
                bean.setDataInicioParcelas(DateUtils.primeiroDiaSemana(bean.getDataInicioParcelas(), bean.getPrimeiroDiaDaSemana()));
                while (bean.getDataInicioParcelas().before(aux)) {
                    gc.setTime(bean.getDataInicioParcelas());
                    gc.add(Calendar.MONTH, 1);
                    bean.setDataInicioParcelas(gc.getTime());
                    bean.setDataInicioParcelas(DateUtils.primeiroDiaSemana(bean.getDataInicioParcelas(), bean.getPrimeiroDiaDaSemana()));
                }
                aux = bean.getDataInicioParcelas();
                for (int i = 0; i < bean.getQntParcelas(); i++) {
                    parcela.setDataVencimento(aux);
                    try {
                        parcelaMB.salvar();
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                    parcelaMB.getBean().setId(0);
                    aux = DateUtils.proximoMes(aux);
                    aux = DateUtils.primeiroDiaSemana(aux, bean.getPrimeiroDiaDaSemana());
                }
                break;
            case 4: //primeiroDiaUtil
                if (bean.getDiasEntreParcelas() == 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar grupo de parcelas. Faltou informar a quantidade de dias entre parcelas."));
                    return "";
                }
                aux = new Date();
                bean.setDataInicioParcelas(DateUtils.primeiroDiaUtilMes(bean.getDataInicioParcelas()));
                while (bean.getDataInicioParcelas().before(aux)) {
                    bean.setDataInicioParcelas(DateUtils.primeiroDiaUtilMes(bean.getDataInicioParcelas()));
                }
                aux = DateUtils.formatedDate(bean.getDataInicioParcelas());
                for (int i = 0; i < bean.getQntParcelas(); i++) {
                    parcela.setDataVencimento(aux);
                    try {
                        parcelaMB.salvar();
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                    parcelaMB.getBean().setId(0);
                    aux = DateUtils.proximoMes(aux);
                    aux = DateUtils.primeiroDiaUtilMes(aux);
                }
                break;
            case 5: //parcelasAnuais
                aux = new Date();
                gc.setTime(bean.getDataInicioParcelas());
                aux = gc.getTime();
                for (int i = 0; i < bean.getQntParcelas(); i++) {
                    parcela.setDataVencimento(aux);
                    try {
                        parcelaMB.salvar();
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar parcelas. Verifique se todas as informações foram preenchidas, e se mesmo assim o erro persistir, contacte o administrador do sistema."));
                        return "";
                    }
                    parcelaMB.getBean().setId(0);
                    gc.add(Calendar.YEAR, 1);
                    aux = gc.getTime();
                }
                break;

        }
        parcelaMB.setBean(new Parcela());
        bean = new GrupoParcelas();
        salvandoGrupo = false;
        return "";
    }

    public void novo() {
        bean = new GrupoParcelas();
    }

    public List<GrupoParcelas> getGrupoParcelass() throws Exception {
        return dao.listAll();
    }
}
