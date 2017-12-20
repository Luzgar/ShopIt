package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.ShopListFinder;
import shopit.ShopListOperation;
import shopit.ShopListRegistration;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by thomas on 29/05/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class ShopListRegistryTest extends AbstractTCFTest {

    @EJB
    private ShopListFinder finder;
    @EJB
    private ShopListRegistration registration;
    @EJB
    private ShopListOperation operator;

    private String user_pseudo = "JeanTest";
    private String user_pswd = "Test";
    private String user_login = "Jean_The_Test";
    private int user_id;
    private User user1;

    private String user_pseudo2 = "JeanTest2";
    private String user_pswd2 = "Test2";
    private String user_login2 = "Jean_The_Test_Reborn";
    private int user_id2;
    private User user2;

    private int shopList_id1;
    private String shopList_name1 = "TestList1";

    private String shopList_name2 = "TestList2";
    private int shopList_id2;
    private String shopList_name3 = "TestList3";
    private int shopList_id3;

    @PersistenceContext
    private EntityManager manager;
    @Inject
    private UserTransaction utx;

    @Before
    public void defineContext()
    {
        user1 = new User(user_login,user_pswd,user_pseudo);
        user2 = new User(user_login2,user_pswd2,user_pseudo2);
        manager.persist(user1);
        manager.persist(user2);
        user_id=user1.getId();
        user_id2=user2.getId();

        shopList_id1 = registration.createShopList(user1, shopList_name1,null, new Date());
        shopList_id2 = registration.createShopList(user2, shopList_name2,null, new Date());
        shopList_id3 = registration.createShopList(user1, shopList_name3,null, new Date());
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();
        manager.remove(manager.find(ShopList.class, shopList_id1));
        manager.remove(manager.find(ShopList.class, shopList_id2));
        manager.remove(manager.find(ShopList.class, shopList_id3));

        manager.remove(manager.find(User.class, user_id));
        manager.remove(manager.find(User.class, user_id2));
        utx.commit();
    }

    @Test
    public void testGetAllCurrentShopList()
    {
        List<ShopList> list = finder.getAllShopList(user1, false);
        assertEquals(2,list.size());
        assertEquals(shopList_id1,list.get(0).getId());
        assertEquals(shopList_id3,list.get(1).getId());

        // Add a contributor
        ShopList sl = manager.find(ShopList.class,shopList_id2);
        sl.getContributors().add(user1);

        list = finder.getAllShopList(user1, false);
        assertEquals(3,list.size());
        assertEquals(shopList_id1,list.get(0).getId());
        assertEquals(shopList_id3,list.get(1).getId());
        assertEquals(shopList_id2,list.get(2).getId());
    }

    @Test
    public void testGetAllTitleCurrentShopList(){
        Map<Integer, String> map = finder.getAllTitleShopList(user1, false);
        assertEquals(2,map.size());
        assertEquals(shopList_name1,map.get(shopList_id1));
        assertEquals(shopList_name3,map.get(shopList_id3));
    }

    @Test
    public void testAddAndRemoveItemToShopList() throws Exception{
        ShopList sl = finder.getShopListById(shopList_id1);

        // Add
        assertEquals(0,sl.getElements().size());
        ShopListElement elt1 = new ShopListElement("Beurre 250g",1,false, "",0);
        ShopListElement elt2 = new ShopListElement("Courgette",4,false, "",0);
        operator.addItemToShopList(shopList_id1,elt1);
        operator.addItemToShopList(shopList_id1,elt2);
        assertEquals(2,sl.getElements().size());
        assertEquals("Beurre 250g",sl.getElements().get(0).getItem());
        assertEquals("Courgette",sl.getElements().get(1).getItem());

        // Remove
        operator.removeItemFromShopList(shopList_id1,"Beurre 250g");
        assertEquals(1,sl.getElements().size());
        assertEquals("Courgette",sl.getElements().get(0).getItem());
    }

    @Test(expected = NoItemFoundException.class)
    public void cannotRemoveItemFromShopList() throws Exception {
        ShopList sl = finder.getShopListById(shopList_id1);

        // Add
        assertEquals(0,sl.getElements().size());
        ShopListElement elt1 = new ShopListElement("Beurre 250g",1,false, "",0);
        ShopListElement elt2 = new ShopListElement("Courgette",4,false, "",0);
        operator.addItemToShopList(shopList_id1,elt1);
        operator.addItemToShopList(shopList_id1,elt2);
        assertEquals(2,sl.getElements().size());
        assertEquals("Beurre 250g",sl.getElements().get(0).getItem());
        assertEquals("Courgette",sl.getElements().get(1).getItem());

        // Remove
        operator.removeItemFromShopList(shopList_id1,"Beurre 250g");
        assertEquals(1,sl.getElements().size());
        assertEquals("Courgette",sl.getElements().get(0).getItem());
        // Should remove nothing
        operator.removeItemFromShopList(shopList_id1, "Beurre 250g");
        assertEquals(1,sl.getElements().size());
    }

    @Test
    public void testSetItemQuantityToShopList() throws Exception{
        ShopList sl = finder.getShopListById(shopList_id1);

        // Add
        assertEquals(0, sl.getElements().size());
        ShopListElement elt1 = new ShopListElement("Beurre 250g", 1, false, "",0);
        ShopListElement elt2 = new ShopListElement("Courgette", 4, false, "",0);
        operator.addItemToShopList(shopList_id1, elt1);
        operator.addItemToShopList(shopList_id1, elt2);
        assertEquals(2, sl.getElements().size());
        assertEquals("Beurre 250g", sl.getElements().get(0).getItem());
        assertEquals("Courgette", sl.getElements().get(1).getItem());

        // Set quantity
        assertEquals(4, sl.getElements().get(1).getNumber());
        operator.setItemQuantityFromShopList(shopList_id1,"Courgette",3);
        assertEquals(3, sl.getElements().get(1).getNumber());
        assertEquals("Courgette", sl.getElements().get(1).getItem());
    }

    @Test
    public void testSetItemTaken() throws Exception{

        ShopList sl = finder.getShopListById(shopList_id1);
        assertEquals(0,sl.getElements().size());
        ShopListElement elt1 = new ShopListElement("Beurre 250g",1,false, "",0);
        ShopListElement elt2 = new ShopListElement("Courgette",4,false, "",0);
        operator.addItemToShopList(shopList_id1,elt1);
        operator.addItemToShopList(shopList_id1,elt2);

        assertEquals(false,sl.getElements().get(0).isTaken());
        assertEquals(false,sl.getElements().get(1).isTaken());

        operator.setItemTaken(shopList_id1,"Courgette",true);
        assertEquals(false,sl.getElements().get(0).isTaken());
        assertEquals(true,sl.getElements().get(1).isTaken());

        operator.setItemTaken(shopList_id1,"Courgette",false);
        assertEquals(false,sl.getElements().get(0).isTaken());
        assertEquals(false,sl.getElements().get(1).isTaken());
    }

    @Test
    public void testRemoveShopList(){
        user1 = manager.find(User.class,user_id);
        assertEquals(2,finder.getAllShopList(user1, false).size());

        registration.removeShopList(shopList_id1);
        assertEquals(1,finder.getAllShopList(user1, false).size());

        registration.removeShopList(shopList_id1);
        assertEquals(1,finder.getAllShopList(user1, false).size());
    }

    @Test
    public void testArchiveShopList(){
        user1 = manager.find(User.class,user_id);
        user2 = manager.find(User.class,user_id2);
        ShopList sl = manager.find(ShopList.class,shopList_id1);
        sl.getContributors().add(user2);

        assertEquals(0,finder.getAllShopList(user1, true).size());
        assertEquals(0,finder.getAllShopList(user2, true).size());

        assertEquals(false,sl.isArchive());
        assertEquals(2,finder.getAllShopList(user1, false).size());
        assertEquals(2,finder.getAllShopList(user2, false).size());

        operator.archiveShopList(shopList_id1);
        assertEquals(true,sl.isArchive());
        assertEquals(1,finder.getAllShopList(user1, false).size());
        assertEquals(1,finder.getAllShopList(user2, false).size());

        assertEquals(1,finder.getAllShopList(user1, true).size());
        assertEquals(1,finder.getAllShopList(user2, true).size());
    }
}
