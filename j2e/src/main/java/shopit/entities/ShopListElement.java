package shopit.entities;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;

/**
 * Created by thomas on 29/05/2017.
 */
@Embeddable
public class ShopListElement {

    @NotNull
    private String item;

    private int number;

    private boolean taken;
    private boolean notFound;

    private String category;

    private double price;

    private String recipeContainer;

    @OneToOne(fetch = FetchType.EAGER)
    private User userInCharge;

    public ShopListElement(){
        //Necessary for JPA
    }

    public ShopListElement(String item, int number, boolean taken, String category, double price) {
        this.item = item;
        this.number = number;
        this.taken = taken;
        notFound = false;
        this.category = category;
        this.userInCharge = null;
        this.price = price;
    }

    public ShopListElement(String item, String category, double price)
    {
        this.item = item;
        this.number = 1;
        this.taken = false;
        notFound = false;
        this.category = category;
        this.userInCharge = null;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public User getUserInCharge() {
        return userInCharge;
    }

    public void setUserInCharge(User userInCharge) {
        this.userInCharge = userInCharge;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isNotFound() {
        return notFound;
    }

    public void setNotFound(boolean notFound) {
        this.notFound = notFound;
    }

    public String getRecipeContainer() {
        return recipeContainer;
    }

    public void setRecipeContainer(String recipeContainer) {
        this.recipeContainer = recipeContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopListElement that = (ShopListElement) o;

        if (number != that.number) return false;
        if (taken != that.taken) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        return userInCharge != null ? userInCharge.equals(that.userInCharge) : that.userInCharge == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = item != null ? item.hashCode() : 0;
        result = 31 * result + number;
        result = 31 * result + (taken ? 1 : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (userInCharge != null ? userInCharge.hashCode() : 0);
        return result;
    }
}
