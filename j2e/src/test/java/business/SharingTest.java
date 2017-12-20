package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.ContributorOperation;
import shopit.entities.Medal;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.ContributorAlreadyExistingException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.util.Date;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by thomas on 30/05/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class SharingTest extends AbstractTCFTest {

    @EJB
    ContributorOperation operator;

    @PersistenceContext
    private EntityManager manager;
    @Inject
    private UserTransaction utx;

    private int shopList_id1;
    private String shopList_name1 = "TestList1";

    private String user_pseudo = "JeanTest";
    private String user_pswd = "Test";
    private String user_login = "Jean_The_Test";
    private int user_id;

    private String user_pseudo2 = "JeanTest2";
    private String user_pswd2 = "Test2";
    private String user_login2 = "Jean_The_Test_Reborn";
    private int user_id2;

    @Before
    public void defineContext()
    {
        User user1 = new User(user_login,user_pswd,user_pseudo);
        User user2 = new User(user_login2,user_pswd2,user_pseudo2);
        manager.persist(user1);
        manager.persist(user2);
        user_id=user1.getId();
        user_id2=user2.getId();

        ShopList sl = new ShopList(shopList_name1,user1, new Date());
        manager.persist(sl);
        shopList_id1 = sl.getId();
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();
        manager.remove(manager.find(ShopList.class, shopList_id1));

        manager.remove(manager.find(User.class, user_id));
        manager.remove(manager.find(User.class, user_id2));
        utx.commit();
    }

    @Test
    public void testAddContributorToShopList() throws Exception{
        ShopList sl = manager.find(ShopList.class, shopList_id1);
        User user1 = manager.find(User.class, user_id);
        User user2 = manager.find(User.class, user_id2);

        assertEquals(0,sl.getContributors().size());
        assertFalse(user1.getInProgress().containsKey(Medal.LEADER));
        operator.addContributorToShopList(sl,user2);
        assertEquals(1,sl.getContributors().size());
        assertTrue(user1.getInProgress().containsKey(Medal.LEADER));
        assertEquals(new Integer(1),user1.getInProgress().get(Medal.LEADER));
    }

    @Test(expected = ContributorAlreadyExistingException.class)
    public void testCannotAddContributorToShopList1() throws Exception {
        ShopList sl = manager.find(ShopList.class, shopList_id1);
        User user1 = manager.find(User.class, user_id);
        User user2 = manager.find(User.class, user_id2);

        assertEquals(0,sl.getContributors().size());
        operator.addContributorToShopList(sl,user1);
    }

    @Test(expected = ContributorAlreadyExistingException.class)
    public void testCannotAddContributorToShopList2() throws Exception {
        ShopList sl = manager.find(ShopList.class, shopList_id1);
        User user1 = manager.find(User.class, user_id);
        User user2 = manager.find(User.class, user_id2);

        assertEquals(0,sl.getContributors().size());
        operator.addContributorToShopList(sl,user2);
        assertEquals(1,sl.getContributors().size());
        operator.addContributorToShopList(sl,user2);
        assertEquals(1,sl.getContributors().size());
    }

    @Test
    public void testRemoveContributorFromShopList() throws Exception{

        ShopList sl = manager.find(ShopList.class, shopList_id1);
        User user2 = manager.find(User.class, user_id2);
        operator.addContributorToShopList(sl,user2);
        assertEquals(1,sl.getContributors().size());

        sl.getElements().add(new ShopListElement("Mangue",1,false,"Fruit",0));
        sl.getElements().get(0).setUserInCharge(user2);
        assertEquals(user2,sl.getElements().get(0).getUserInCharge());

        operator.removeContributorFromShopList(sl,user2);
        assertEquals(0,sl.getContributors().size());
        assertEquals(null,sl.getElements().get(0).getUserInCharge());

        operator.removeContributorFromShopList(sl,user2);
        assertEquals(0,sl.getContributors().size());
    }
}
