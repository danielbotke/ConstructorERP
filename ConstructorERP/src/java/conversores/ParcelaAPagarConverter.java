package conversores;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasPagar.ParcelaAPagar;
import utils.JpaUtil;

@FacesConverter(forClass=ParcelaAPagar.class)
public class ParcelaAPagarConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        int id = Integer.parseInt(value);
        try {
            return JpaUtil.get().procurarObjeto(ParcelaAPagar.class, id);
        } catch (Exception ex) {
            Logger.getLogger(ParcelaAPagarConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        ParcelaAPagar o = (ParcelaAPagar) value;
        return o.getId() + "";
    }
}
