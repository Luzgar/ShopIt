package shopit.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 31/05/2017.
 */
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    private int numberPerson;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ShopListElement> ingredients = new ArrayList<ShopListElement>();

    @NotNull
    private String description;

    public Recipe()
    {
        //Necessary for JPA
    }

    public Recipe(String name, int numberPerson, List<ShopListElement> ingredients, String description) {
        this.name = name;
        this.numberPerson = numberPerson;
        this.ingredients = ingredients;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberPerson() {
        return numberPerson;
    }

    public void setNumberPerson(int numberPerson) {
        this.numberPerson = numberPerson;
    }

    public List<ShopListElement> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ShopListElement> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if(numberPerson != recipe.getNumberPerson()) return false;
        if (ingredients != null ? !ingredients.equals(recipe.ingredients) : recipe.ingredients != null) return false;
        return description != null ? description.equals(recipe.description) : recipe.description == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (numberPerson ^ (numberPerson >>> 32));
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
