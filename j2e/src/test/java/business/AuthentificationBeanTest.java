package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.Logging;
import shopit.entities.User;
import shopit.exceptions.UserNotFoundException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static org.junit.Assert.*;

/**
 * Created by Charly on 06/06/2017.
 */

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class AuthentificationBeanTest extends AbstractTCFTest
{
    @EJB private Logging logging;

    @PersistenceContext private EntityManager manager;
    @Inject UserTransaction utx;

    private User user;
    private int userId;
    private String login = "wazza", password = "passwazza";

    @Before
    public void defineContext()
    {
        user = new User(login, password, "pseudo");
        manager.persist(user);
        userId = user.getId();
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();
        user = manager.merge(user);
        manager.remove(user);
        user = null;
        utx.commit();
    }

    @Test
    public void testLogIn() throws UserNotFoundException
    {
        assertEquals(userId, logging.logIn(login, password));
    }

    @Test(expected = UserNotFoundException.class)
    public void testWrongLogIn() throws UserNotFoundException
    {
        assertEquals(userId, logging.logIn("marchepas", "marchepaaas"));
    }
}
