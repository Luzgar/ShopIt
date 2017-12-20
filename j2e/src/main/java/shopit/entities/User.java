package shopit.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Charly on 27/05/2017.
 */

@Entity
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull private String login;
    @NotNull private String pswd;

    @NotNull private String pseudo;
    private String token;

    private int lifespanList;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Medal> unlocked = new ArrayList<Medal>();

    @Enumerated(EnumType.STRING)
    private Medal exposed = Medal.NOMEDAL;

    @ElementCollection
    private Map<Medal, Integer> inProgress = new HashMap<Medal, Integer>();

    private boolean doReccurency = true;

    public User()
    {
        //Necessary for JPA
    }

    public User(String login, String pswd, String pseudo) {
        this.login = login;
        this.pswd = pswd;
        this.pseudo = pseudo;
    }

    /** More constructors to come **/

    public int getLifespanList() { return lifespanList; }

    public void setLifespanList(int lifespanList) { this.lifespanList = lifespanList; }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getToken() {return token;}

    public void setToken(String newToken) {token = newToken;}

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public List<Medal> getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(List<Medal> unlocked) {
        this.unlocked = unlocked;
    }

    public Medal getExposed() {
        return exposed;
    }

    public void setExposed(Medal exposed) {
        this.exposed = exposed;
    }

    public Map<Medal, Integer> getInProgress() {
        return inProgress;
    }

    public void setInProgress(Map<Medal, Integer> inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isDoReccurency() {
        return doReccurency;
    }

    public void setDoReccurency(boolean doReccurency) {
        this.doReccurency = doReccurency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (lifespanList != user.lifespanList) return false;
        if (doReccurency != user.doReccurency) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (pswd != null ? !pswd.equals(user.pswd) : user.pswd != null) return false;
        if (pseudo != null ? !pseudo.equals(user.pseudo) : user.pseudo != null) return false;
        if (token != null ? !token.equals(user.token) : user.token != null) return false;
        if (unlocked != null ? !unlocked.equals(user.unlocked) : user.unlocked != null) return false;
        if (exposed != user.exposed) return false;
        return inProgress != null ? inProgress.equals(user.inProgress) : user.inProgress == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (pswd != null ? pswd.hashCode() : 0);
        result = 31 * result + (pseudo != null ? pseudo.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + lifespanList;
        result = 31 * result + (unlocked != null ? unlocked.hashCode() : 0);
        result = 31 * result + (exposed != null ? exposed.hashCode() : 0);
        result = 31 * result + (inProgress != null ? inProgress.hashCode() : 0);
        result = 31 * result + (doReccurency ? 1 : 0);
        return result;
    }
}
