package shopit;

import shopit.entities.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by thomas on 29/05/2017.
 */
@Local
public interface UserFinder {
    public User findUserById(int id);
    public User findUserByPseudo(String pseudo);
    public User findUserByLogin(String login);

    public List<User> getAllUsers();
}
