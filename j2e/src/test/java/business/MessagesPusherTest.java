package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.TokenManaging;
import shopit.entities.User;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static org.junit.Assert.*;

/**
 * Created by Charly on 31/05/2017.
 */

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class MessagesPusherTest extends AbstractTCFTest
{
    @EJB TokenManaging tokenManager;

    @PersistenceContext EntityManager manager;
    @Inject UserTransaction utx;

    private User user;
    private int userId;
    private final String TOKEN = "ABCD1234";

    @Before
    public void defineContext()
    {
        user = new User("l", "l", "l");
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
    public void testSetToken()
    {
        user = manager.merge(user);
        assertNull(user.getToken());

        tokenManager.setUserDeviceToken(user, TOKEN);
        assertEquals(TOKEN, manager.find(User.class, userId).getToken());
    }

}
