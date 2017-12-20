package shopit.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thomas on 29/05/2017.
 */
@Entity
public class ShopList implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull private String name;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @NotNull
    private User owner;

    @ElementCollection (fetch = FetchType.EAGER)
    private List<ShopListElement> elements = new ArrayList<>();

    @ManyToMany (cascade = CascadeType.MERGE , fetch = FetchType.EAGER)
    private List<User> contributors = new ArrayList<User>();

    private boolean isArchive = false;

    private boolean isShared = false;

    @NotNull
    private Date dateCreation;

    public ShopList()
    {
        //Necessary for JPA
    }

    public ShopList(String name, User owner, Date date) {
        this.name = name;
        this.owner = owner;
        this.dateCreation = date;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ShopListElement> getElements() {
        return elements;
    }

    public List<User> getContributors() {
        return contributors;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopList shopList = (ShopList) o;

        if (isArchive != shopList.isArchive) return false;
        if (isShared != shopList.isShared) return false;
        if (name != null ? !name.equals(shopList.name) : shopList.name != null) return false;
        if (owner != null ? !owner.equals(shopList.owner) : shopList.owner != null) return false;
        if (elements != null ? !elements.equals(shopList.elements) : shopList.elements != null) return false;
        if (contributors != null ? !contributors.equals(shopList.contributors) : shopList.contributors != null)
            return false;
        return dateCreation != null ? dateCreation.equals(shopList.dateCreation) : shopList.dateCreation == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (elements != null ? elements.hashCode() : 0);
        result = 31 * result + (contributors != null ? contributors.hashCode() : 0);
        result = 31 * result + (isArchive ? 1 : 0);
        result = 31 * result + (isShared ? 1 : 0);
        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
        return result;
    }
}
