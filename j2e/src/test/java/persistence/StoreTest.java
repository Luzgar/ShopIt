package persistence;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

/**
 * Created by Charly on 06/06/2017.
 */

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class StoreTest extends AbstractTCFTest
{
    @PersistenceContext EntityManager manager;

    @Test
    public void testStore()
    {
        User user = new User("l", "k", "l");
        assertEquals(0, user.getId());
        manager.persist(user);
        assertNotEquals(0, user.getId());

        User stored = manager.find(User.class, user.getId());
        assertEquals(user, stored);
    }
}
