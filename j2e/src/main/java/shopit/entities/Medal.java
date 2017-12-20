package shopit.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by thomas on 12/06/2017.
 */
public enum Medal {

    NOMEDAL("Rodeur des rayons",0,""),
    LEADER("leader",5,""),
    COOK("cuistot",3,""),
    PHOTOGRAPHER("photographe",5,""),
    MEAT("boucher",10,"VIANDES"),
    VEGETEBALES("vegan",10,"ALIMENTS ET BOISSONS À BASE DE VÉGÉTAUX"),
    FISH("pecheur",10,"POISSONS"),
    BEVERAGE("barman",10,"BOISSONS"),
    OTHER("Ali Baba",10,"AUTRES")
    ;

    private String name;
    private int nbRequired;
    private String category;

    Medal(String name, int nbRequired, String category) {
        this.name = name;
        this.nbRequired = nbRequired;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbRequired() {
        return nbRequired;
    }

    public void setNbRequired(int nbRequired) {
        this.nbRequired = nbRequired;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static Medal getMedal(String value) throws IllegalArgumentException{
        value = value.toUpperCase();
        for(Medal m : Medal.values()){
            if(m.getCategory().equals(value)){
                return m;
            }
        }
        throw new IllegalArgumentException("No medal corresponding to that value");
    }
}
