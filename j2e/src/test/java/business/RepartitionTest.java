package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.CriterionBuilder;
import shopit.RepartitionProcessing;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;
import shopit.exceptions.UserIsNotAContributorException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by thomas on 30/05/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class RepartitionTest extends AbstractTCFTest {

    @EJB
    CriterionBuilder criterionBuilder;
    @EJB
    RepartitionProcessing repartitionProcessing;

    @PersistenceContext
    private EntityManager manager;
    @Inject
    private UserTransaction utx;


    private int shopList_id1;
    private String shopList_name1 = "TestList1";

    private int shopList_id2;
    private String shopList_name2 = "TestListCategory";

    private String user_pseudo = "JeanTest";
    private String user_pswd = "Test";
    private String user_login = "Jean_The_Test";
    private int user_id;

    private String user_pseudo2 = "JeanTest2";
    private String user_pswd2 = "Test2";
    private String user_login2 = "Jean_The_Test_Reborn";
    private int user_id2;
    private int user_id3,user_id4;

    @Before
    public void defineContext()
    {
        User user1 = new User(user_login,user_pswd,user_pseudo);
        User user2 = new User(user_login2,user_pswd2,user_pseudo2);
        User user3 = new User(user_login+'3',user_pswd+'3',user_pseudo+'3');
        User user4 = new User(user_login+'4',user_pswd+'4',user_pseudo+'4');
        manager.persist(user1);
        manager.persist(user2);
        manager.persist(user3);
        manager.persist(user4);
        user_id=user1.getId();
        user_id2=user2.getId();

        ShopList sl = new ShopList(shopList_name1,user1, new Date());
        ShopList sl2 = new ShopList(shopList_name2,user1, new Date());

        ShopListElement elt1 = new ShopListElement("Beurre 250g", 1, false, "",0);
        sl.getElements().add(elt1);

        manager.persist(sl);
        shopList_id1 = sl.getId();
        manager.persist(sl2);
        shopList_id2 = sl2.getId();
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();
        manager.remove(manager.find(ShopList.class, shopList_id1));
        manager.remove(manager.find(ShopList.class, shopList_id2));

        manager.remove(manager.find(User.class, user_id));
        manager.remove(manager.find(User.class, user_id2));
        manager.remove(manager.find(User.class, user_id3));
        manager.remove(manager.find(User.class, user_id4));
        utx.commit();
    }

    @Test
    public void testSetUserInCharge() throws Exception{
        ShopList sl = manager.find(ShopList.class,shopList_id1);
        User user1 = manager.find(User.class,user_id);

        criterionBuilder.setUserInCharge(sl,user1,"Beurre 250g");
        assertEquals(user1,sl.getElements().get(0).getUserInCharge());
        criterionBuilder.setUserInCharge(sl,null,"Beurre 250g");
        assertEquals(null,sl.getElements().get(0).getUserInCharge());
    }

    @Test (expected = NoItemFoundException.class)
    public void testSetUserInChargeFail1() throws Exception{
        ShopList sl = manager.find(ShopList.class,shopList_id1);
        User user = manager.find(User.class,user_id);

        criterionBuilder.setUserInCharge(sl,user,"Courgette");
    }

    @Test (expected = UserIsNotAContributorException.class)
    public void testSetUserInChargeFail2() throws Exception{
        ShopList sl = manager.find(ShopList.class,shopList_id1);
        User user = manager.find(User.class,user_id2);

        criterionBuilder.setUserInCharge(sl,user,"Beurre 250g");
    }

    @Test
    public void testDoCategoryRepartition(){

        ShopList sl = manager.find(ShopList.class,shopList_id2);

        // Add elements
        ShopListElement elt1 = new ShopListElement("A", 1, false, "a",0);
        ShopListElement elt2 = new ShopListElement("B", 1, false, "a",0);
        ShopListElement elt3 = new ShopListElement("C", 1, false, "b",0);
        ShopListElement elt4 = new ShopListElement("D", 1, false, "b",0);
        ShopListElement elt5 = new ShopListElement("E", 1, false, null,0);
        sl.getElements().add(elt1);
        sl.getElements().add(elt2);
        sl.getElements().add(elt3);
        sl.getElements().add(elt4);
        sl.getElements().add(elt5);

        // Add user to the list
        User user1 = manager.find(User.class,user_id);
        User user2 = manager.find(User.class,user_id2);
        User user3 = manager.find(User.class,user_id3);
        User user4 = manager.find(User.class,user_id4);
        sl.getContributors().add(user2);
        sl.getContributors().add(user3);
        sl.getContributors().add(user4);

        // Add constraint
        sl.getElements().get(0).setUserInCharge(user2);

        // Pre test
        assertEquals(user2,sl.getElements().get(0).getUserInCharge());
        assertEquals(null,sl.getElements().get(1).getUserInCharge());
        assertEquals(null,sl.getElements().get(2).getUserInCharge());
        assertEquals(null,sl.getElements().get(3).getUserInCharge());
        assertEquals(null,sl.getElements().get(4).getUserInCharge());

        // Do repartition
        repartitionProcessing.doCategoryRepartition(sl);

        // Test
        assertEquals("A",sl.getElements().get(0).getItem());
        assertEquals(user2.getPseudo(),sl.getElements().get(0).getUserInCharge().getPseudo());
        assertEquals("B",sl.getElements().get(1).getItem());
        assertEquals(user2.getPseudo(),sl.getElements().get(1).getUserInCharge().getPseudo());
        assertEquals("C",sl.getElements().get(2).getItem());
        assertEquals(user1.getPseudo(),sl.getElements().get(2).getUserInCharge().getPseudo());
        assertEquals("D",sl.getElements().get(3).getItem());
        assertEquals(user1.getPseudo(),sl.getElements().get(3).getUserInCharge().getPseudo());
        assertEquals("E",sl.getElements().get(4).getItem());
        assertEquals(user3.getPseudo(),sl.getElements().get(4).getUserInCharge().getPseudo());
    }

    @Test
    public void testDoCategoryRepartition2(){

        ShopList sl = manager.find(ShopList.class,shopList_id2);

        // Add elements
        ShopListElement elt1 = new ShopListElement("A", 1, false, "a",0);
        ShopListElement elt2 = new ShopListElement("B", 1, false, "a",0);
        ShopListElement elt3 = new ShopListElement("C", 1, false, "b",0);
        ShopListElement elt4 = new ShopListElement("D", 1, false, "b",0);
        ShopListElement elt5 = new ShopListElement("E", 1, false, null,0);
        sl.getElements().add(elt1);
        sl.getElements().add(elt2);
        sl.getElements().add(elt3);
        sl.getElements().add(elt4);
        sl.getElements().add(elt5);

        // Add user to the list
        User user1 = manager.find(User.class,user_id);
        User user2 = manager.find(User.class,user_id2);
        User user3 = new User(user_login+'3',user_pswd+'3',user_pseudo+'3');
        User user4 = new User(user_login+'4',user_pswd+'4',user_pseudo+'4');
        manager.persist(user3);
        manager.persist(user4);
        sl.getContributors().add(user2);
        sl.getContributors().add(user3);
        sl.getContributors().add(user4);

        // Pre test
        assertEquals(null,sl.getElements().get(0).getUserInCharge());
        assertEquals(null,sl.getElements().get(1).getUserInCharge());
        assertEquals(null,sl.getElements().get(2).getUserInCharge());
        assertEquals(null,sl.getElements().get(3).getUserInCharge());
        assertEquals(null,sl.getElements().get(4).getUserInCharge());

        // Do repartition
        repartitionProcessing.doCategoryRepartition(sl);

        // Test
        assertEquals(user1.getPseudo(),sl.getElements().get(0).getUserInCharge().getPseudo());
        assertEquals(user1,sl.getElements().get(0).getUserInCharge());
        assertEquals(user1.getPseudo(),sl.getElements().get(1).getUserInCharge().getPseudo());
        assertEquals(user1,sl.getElements().get(1).getUserInCharge());
        assertEquals(user2.getPseudo(),sl.getElements().get(2).getUserInCharge().getPseudo());
        assertEquals(user2,sl.getElements().get(2).getUserInCharge());
        assertEquals(user2.getPseudo(),sl.getElements().get(3).getUserInCharge().getPseudo());
        assertEquals(user2,sl.getElements().get(3).getUserInCharge());
        assertEquals(user3.getPseudo(),sl.getElements().get(4).getUserInCharge().getPseudo());
        assertEquals(user3,sl.getElements().get(4).getUserInCharge());
    }

    public void testDoPriceRepartition(){

        ShopList sl = manager.find(ShopList.class,shopList_id2);
        // Add elements
        ShopListElement elt1 = new ShopListElement("A", 1, false, "a",2);
        ShopListElement elt2 = new ShopListElement("B", 1, false, "a",1);
        ShopListElement elt3 = new ShopListElement("C", 1, false, "b",2);
        ShopListElement elt4 = new ShopListElement("D", 1, false, "b",5);
        sl.getElements().add(elt1);
        sl.getElements().add(elt2);
        sl.getElements().add(elt3);
        sl.getElements().add(elt4);

        User user1 = manager.find(User.class,user_id);
        User user2 = manager.find(User.class,user_id2);
        sl.getContributors().add(user2);

        // Init Test
        assertEquals(elt1,sl.getElements().get(0));
        assertEquals(elt2,sl.getElements().get(1));
        assertEquals(elt3,sl.getElements().get(2));
        assertEquals(elt4,sl.getElements().get(3));

        repartitionProcessing.doPriceRepartition(sl);

        // Check the order of ShopListElement
        assertEquals(elt4.getItem(),sl.getElements().get(0).getItem());
        assertEquals(elt1.getItem(),sl.getElements().get(1).getItem());
        assertEquals(elt3.getItem(),sl.getElements().get(2).getItem());
        assertEquals(elt2.getItem(),sl.getElements().get(3).getItem());

        // Check the good repartition
        assertEquals(user1,sl.getElements().get(0).getUserInCharge());
        assertEquals(user2,sl.getElements().get(1).getUserInCharge());
        assertEquals(user2,sl.getElements().get(2).getUserInCharge());
        assertEquals(user2,sl.getElements().get(3).getUserInCharge());
    }

    @Test
    public void testDoPriceRepartition2(){

        ShopList sl = manager.find(ShopList.class,shopList_id2);
        // Add elements
        ShopListElement elt1 = new ShopListElement("A", 1, false, "a",5);
        ShopListElement elt2 = new ShopListElement("B", 1, false, "a",4);
        ShopListElement elt3 = new ShopListElement("C", 1, false, "b",3);
        ShopListElement elt4 = new ShopListElement("D", 1, false, "b",2.5);
        ShopListElement elt5 = new ShopListElement("E", 1, false, "b",2.5);
        ShopListElement elt6 = new ShopListElement("F", 1, false, "b",2);
        ShopListElement elt7 = new ShopListElement("G", 1, false, "b",2);
        ShopListElement elt8 = new ShopListElement("H", 1, false, "b",2);
        ShopListElement elt9 = new ShopListElement("I", 1, false, "b",1);
        sl.getElements().add(elt1);
        sl.getElements().add(elt2);
        sl.getElements().add(elt3);
        sl.getElements().add(elt4);
        sl.getElements().add(elt5);
        sl.getElements().add(elt6);
        sl.getElements().add(elt7);
        sl.getElements().add(elt8);
        sl.getElements().add(elt9);

        User user1 = manager.find(User.class,user_id);
        User user2 = manager.find(User.class,user_id2);
        User user3 = manager.find(User.class,user_id3);
        sl.getContributors().add(user2);
        sl.getContributors().add(user3);

        repartitionProcessing.doPriceRepartition(sl);

        // Check the good repartition
        assertEquals(elt1.getItem(),sl.getElements().get(0).getItem());
        assertEquals(elt2.getItem(),sl.getElements().get(1).getItem());
        assertEquals(elt3.getItem(),sl.getElements().get(2).getItem());
        assertEquals(elt4.getItem(),sl.getElements().get(3).getItem());
        assertEquals(elt5.getItem(),sl.getElements().get(4).getItem());
        assertEquals(elt6.getItem(),sl.getElements().get(5).getItem());
        assertEquals(elt7.getItem(),sl.getElements().get(6).getItem());
        assertEquals(elt8.getItem(),sl.getElements().get(7).getItem());
        assertEquals(elt9.getItem(),sl.getElements().get(8).getItem());

        assertEquals(user1,sl.getElements().get(0).getUserInCharge());
        assertEquals(user2,sl.getElements().get(1).getUserInCharge());
        assertEquals(user3,sl.getElements().get(2).getUserInCharge());
        assertEquals(user3,sl.getElements().get(3).getUserInCharge());
        assertEquals(user2,sl.getElements().get(4).getUserInCharge());
        assertEquals(user1,sl.getElements().get(5).getUserInCharge());
        assertEquals(user3,sl.getElements().get(6).getUserInCharge());
        assertEquals(user2,sl.getElements().get(7).getUserInCharge());
        assertEquals(user1,sl.getElements().get(8).getUserInCharge());
    }
}
