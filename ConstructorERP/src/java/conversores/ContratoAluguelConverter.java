package conversores;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasReceber.ContratoAluguel;
import utils.JpaUtil;

@FacesConverter(forClass=ContratoAluguel.class)
public class ContratoAluguelConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        int id = Integer.parseInt(value);
        try {
            return JpaUtil.get().procurarObjeto(ContratoAluguel.class, id);
        } catch (Exception ex) {
            Logger.getLogger(ContratoAluguelConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        ContratoAluguel o = (ContratoAluguel) value;
        return o.getId() + "";
    }
}
