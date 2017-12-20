package shopit.components;

import shopit.UserFinder;
import shopit.UserOperation;
import shopit.UserRegistration;
import shopit.entities.Medal;
import shopit.entities.User;
import shopit.exceptions.UserAlreadyExistingException;
import shopit.exceptions.UserNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 29/05/2017.
 */
@Stateless
public class UserRegistryBean implements UserFinder, UserRegistration, UserOperation {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public User findUserById(int id) {
        return manager.find(User.class,id);
    }

    @Override
    public User findUserByPseudo(String pseudo) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root =  criteria.from(User.class);
        criteria.select(root).where(builder.equal(root.get("pseudo"), pseudo));
        TypedQuery<User> query = manager.createQuery(criteria);

        try{
            return query.getSingleResult();
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public User findUserByLogin(String login) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root =  criteria.from(User.class);
        criteria.select(root).where(builder.equal(root.get("login"), login));
        TypedQuery<User> query = manager.createQuery(criteria);

        try{
            return query.getSingleResult();
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root =  criteria.from(User.class);
        criteria.select(root);
        TypedQuery<User> query = manager.createQuery(criteria);

        try{
            return query.getResultList();
        }
        catch(NoResultException e) {
            return new ArrayList<User>();
        }
    }

    @Override
    public int createUser(String pseudo, String login, String pswd) throws UserAlreadyExistingException{

        if(findUserByLogin(login)!=null || findUserByPseudo(pseudo)!=null){
            throw new UserAlreadyExistingException("Login or pseudo already exists");
        }

        User user = new User(login,pswd,pseudo);
        user.getUnlocked().add(Medal.NOMEDAL);
        manager.persist(user);
        return user.getId();
    }

    @Override
    public void setUserLifespanList(int userId, int nb) throws UserNotFoundException
    {
        User user = findUserById(userId);
        if(user == null)
            throw new UserNotFoundException("Id does not match any user.");

        user.setLifespanList(nb);
    }

    @Override
    public int getUserLifespanList(int userId) throws UserNotFoundException
    {
        User user = findUserById(userId);
        if(user == null)
            throw new UserNotFoundException("Id does not match any user.");

        return user.getLifespanList();
    }

    @Override
    public void setUserDoReccurency(int userId, boolean doReccurency) throws UserNotFoundException {

        User user = findUserById(userId);
        if(user == null)
            throw new UserNotFoundException("Id does not match any user.");
        user.setDoReccurency(doReccurency);
    }
}
