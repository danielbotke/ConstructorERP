/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.ContasReceber;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Classe modelo para a criação do objeto bloco, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
public class NumerosTitulo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroDocto() {
        String numeroDocto = "9";
        for (int i = 0; i < (10 - Integer.toString(id).length()); i++) {
            numeroDocto += "0";
        }
        numeroDocto += id;
        return numeroDocto;
    }

    public String getNossoNumero() {
        String nossoNumero = "";
        for (int i = 0; i < (15 - Integer.toString(id).length()); i++) {
            nossoNumero += "0";
        }
        nossoNumero += id;
        return nossoNumero;
    }

    public String getDigitoVerificador(String modalidade) {
        int[] multiplicadores = {2, 3, 4, 5, 6, 7, 8, 9};
        StringBuffer buffer = new StringBuffer(modalidade + this.getNossoNumero());
        int soma = 0;
        int j = 0;

        for (int i = buffer.length() - 1; i >= 0; i--) {
            soma += Integer.parseInt(buffer.toString().charAt(i) + "") * multiplicadores[j];
            if (j == multiplicadores.length - 1) {
                j = 0;
            } else {
                j++;
            }
        }
        int resto = (soma % 11);

        return (11 - resto) > 9 ? 0 + "" : (11 - resto) + "";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NumerosTitulo other = (NumerosTitulo) obj;
        if (id == 0) {
            if (other.id != 0) {
                return false;
            }
        } else if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        return hash;
    }
}
