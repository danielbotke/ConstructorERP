/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/** Classe modelo para a criação do objeto de tipo de índice (CUB), bem como para persistência e acesso.
 *
 * @author Daniel
 */
@Entity
public class Indice implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private TipoIndice tipoIndice;
    
    @Column(nullable = false)
    private float valorIndice;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataIndice;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoIndice getTipoIndice() {
        return tipoIndice;
    }

    public void setTipoIndice(TipoIndice tipoIndice) {
        this.tipoIndice = tipoIndice;
    }

    public float getValorIndice() {
        BigDecimal bd1 = new BigDecimal(valorIndice).setScale(2, RoundingMode.HALF_UP);
        return bd1.floatValue();
    }

    public void setValorIndice(float valorIndice) {
        this.valorIndice = valorIndice;
    }

    public Date getDataIndice() {
        return dataIndice;
    }

    public void setDataIndice(Date dataIndice) {
        this.dataIndice = dataIndice;
    }
    

}
