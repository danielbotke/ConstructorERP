package conversores;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import modelo.ContasPagar.Produto;
import utils.JpaUtil;

@FacesConverter(forClass=Produto.class)
public class ProdutoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        int id = Integer.parseInt(value);
        try {
            return JpaUtil.get().procurarObjeto(Produto.class, id);
        } catch (Exception ex) {
            Logger.getLogger(ProdutoConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Produto o = (Produto) value;
        return o.getId() + "";
    }
}
