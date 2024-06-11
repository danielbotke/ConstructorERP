package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.Bloco;

@FacesConverter(forClass = Bloco.class)
public class BlocoConverter implements Converter {

     @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Bloco) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Bloco) {
            Bloco entity = (Bloco) value;
            if (entity != null && entity instanceof Bloco && entity.getId() != 0) {
                uiComponent.getAttributes().put( entity.getId() + "", entity);
                return entity.getId() + "";
            }
        }
        return "";
    }
}
