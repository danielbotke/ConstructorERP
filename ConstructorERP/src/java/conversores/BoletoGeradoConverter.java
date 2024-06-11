package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasReceber.BoletoGerado;

@FacesConverter(forClass = BoletoGerado.class)
public class BoletoGeradoConverter implements Converter {

     @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (BoletoGerado) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof BoletoGerado) {
            BoletoGerado entity = (BoletoGerado) value;
            if (entity != null && entity instanceof BoletoGerado && entity.getId() != 0) {
                uiComponent.getAttributes().put( entity.getId() + "", entity);
                return entity.getId() + "";
            }
        }
        return "";
    }
}
