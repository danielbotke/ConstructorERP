package beans;

import dao.ReservaAptoDao;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import login.SessionContext;
import modelo.Apartamento;
import modelo.ReservaApto;

/**
 * Classe do menagedBean do Reserva Apto, utilizada para interar as informações
 * deste objeto entre interface e classes de negócio
 *
 * @author Daniel
 */
@ManagedBean(name = "reservaAptoMB")
@ViewScoped
public class ReservaAptoMB implements Serializable {

    private ReservaApto bean = new ReservaApto();
    private Apartamento apartamentoSelecionado;
    private ReservaApto reservaSelecionado;

    public ReservaApto getBean() {
        return bean;
    }

    public void setBean(ReservaApto bean) {
        this.bean = bean;
    }

    public Apartamento getApartamentoSelecionado() {
        return apartamentoSelecionado;
    }

    public void setApartamentoSelecionado(Apartamento apartamentoSelecionado) {
        this.apartamentoSelecionado = apartamentoSelecionado;
    }

    public ReservaApto getReservaSelecionado() {
        return reservaSelecionado;
    }

    public void setReservaSelecionado(ReservaApto reservaSelecionado) {
        this.reservaSelecionado = reservaSelecionado;
    }

    public void reservarApto() throws Exception {
        bean.setApartamento(apartamentoSelecionado);
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("America/Sao_Paulo"));
        bean.setDataReserva(gc.getTime());
        bean.setEmailUsuario(SessionContext.getInstance().getUsuarioLogado().getEmail());
        (new ReservaAptoDao()).save(bean);
        bean = new ReservaApto();
    }

    public void cancelarReserva() throws Exception {
        reservaSelecionado.setAtiva(false);
        (new ReservaAptoDao()).save(reservaSelecionado);
        reservaSelecionado = new ReservaApto();
    }
}
