package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasReceber.NumerosRemessa;

@FacesConverter(forClass = NumerosRemessa.class)
public class NumerosRemessaConverter implements Converter {

     @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (NumerosRemessa) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof NumerosRemessa) {
            NumerosRemessa entity = (NumerosRemessa) value;
            if (entity != null && entity instanceof NumerosRemessa && entity.getId() != 0) {
                uiComponent.getAttributes().put( entity.getId() + "", entity);
                return entity.getId() + "";
            }
        }
        return "";
    }
}
