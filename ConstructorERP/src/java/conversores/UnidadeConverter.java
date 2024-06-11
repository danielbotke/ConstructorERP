package conversores;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasPagar.Unidade;
import utils.JpaUtil;

@FacesConverter(forClass=Unidade.class)
public class UnidadeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        int id = Integer.parseInt(value);
        try {
            return JpaUtil.get().procurarObjeto(Unidade.class, id);
        } catch (Exception ex) {
            Logger.getLogger(UnidadeConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Unidade o = (Unidade) value;
        return o.getId() + "";
    }
}
