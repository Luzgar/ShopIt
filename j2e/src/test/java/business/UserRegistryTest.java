package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.UserFinder;
import shopit.UserOperation;
import shopit.UserRegistration;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;
import shopit.exceptions.UserAlreadyExistingException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by thomas on 29/05/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class UserRegistryTest extends AbstractTCFTest {

    @EJB
    UserFinder finder;
    @EJB
    UserRegistration registration;
    @EJB
    UserOperation operation;

    @PersistenceContext
    private EntityManager manager;
    @Inject
    private UserTransaction utx;

    private String user_pseudo = "JeanTest";
    private String user_pswd = "Test";
    private String user_login = "Jean_The_Test";
    private int user_id;

    @Before
    public void defineContext() throws Exception
    {
        user_id = registration.createUser(user_pseudo,user_login,user_pswd);
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();
        manager.remove(manager.find(User.class, user_id));
        utx.commit();
    }

    @Test(expected = UserAlreadyExistingException.class)
    public void testAddUser() throws Exception
    {
        assertNotNull(finder.findUserById(user_id));
        assertNotNull(finder.findUserByPseudo(user_pseudo));
        assertNotNull(finder.findUserByLogin(user_login));
        registration.createUser(user_pseudo,user_login,user_pswd);
    }

    @Test
    public void testGetAllUsers() throws Exception{

        assertEquals(1,finder.getAllUsers().size());
        int id2 = registration.createUser("a","b","c");
        assertEquals(2,finder.getAllUsers().size());
        manager.remove(manager.find(User.class, id2));
    }

    @Test
    public void testSetUserDoReccurency() throws Exception{
        User user = manager.find(User.class,user_id);

        assertTrue(user.isDoReccurency());
        operation.setUserDoReccurency(user_id,false);
        assertFalse(user.isDoReccurency());
    }
}
