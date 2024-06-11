/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import org.apache.commons.lang.StringUtils;

/**
 * Classe modelo para a criação do objeto pessoa, bem como para persistência e
 * acesso.
 *
 * @author Daniel
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String endereco;
    @Column(nullable = false, length = 9)
    private String CEP;
    @Column(nullable = false)
    private String bairro;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Cidade cidade;
    @Column(nullable = false, length = 30)
    private String tel1;
    @Column(nullable = false, length = 30)
    private String tel2;
    @Column(nullable = false, length = 100)
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCEP() {
        return CEP;
    }
    public String getCEPPrevixo() {
        return StringUtils.left(String.valueOf(CEP), 5);
    }
    public String getCEPSufixo() {
        return StringUtils.right(String.valueOf(CEP), 3);
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
