package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasReceber.Renegociacao;

@FacesConverter(forClass = Renegociacao.class)
public class RenegociacaoConverter implements Converter {

     @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Renegociacao) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Renegociacao) {
            Renegociacao entity = (Renegociacao) value;
            if (entity != null && entity instanceof Renegociacao && entity.getId() != 0) {
                uiComponent.getAttributes().put( entity.getId() + "", entity);
                return entity.getId() + "";
            }
        }
        return "";
    }
}
