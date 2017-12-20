package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.MedalFinder;
import shopit.MedalOperation;
import shopit.entities.Medal;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.InvalidMedalException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by thomas on 13/06/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class MedalTest extends AbstractTCFTest {

    @EJB
    MedalOperation operation;

    @EJB
    MedalFinder finder;

    @PersistenceContext
    private EntityManager manager;
    @Inject
    private UserTransaction utx;

    private String user_pseudo = "JeanTest";
    private String user_pswd = "Test";
    private String user_login = "Jean_The_Test";
    private int user_id;

    private String user_pseudo2 = "JeanTest2";
    private String user_pswd2 = "Test2";
    private String user_login2 = "Jean_The_Test_Reborn";
    private int user_id2;

    private int shopList_id1;
    private String shopList_name1 = "TestList1";

    @Before
    public void defineContext() throws Exception{

        User user = new User(user_login,user_pswd,user_pseudo);
        manager.persist(user);
        user_id = user.getId();

        User user2 = new User(user_login2,user_pswd2,user_pseudo2);
        manager.persist(user2);
        user_id2 = user2.getId();

        ShopList sl = new ShopList(shopList_name1,user, new Date());
        manager.persist(sl);
        shopList_id1 = sl.getId();
        sl.getContributors().add(user2);
    }

    @After
    public void cleanUp() throws Exception{
        utx.begin();
        manager.remove(manager.find(User.class,user_id));
        manager.remove(manager.find(User.class,user_id2));
        manager.remove(manager.find(ShopList.class,shopList_id1));
        utx.commit();
    }

    @Test
    public void testSetAndGetUserMedal() throws Exception{
        User user = manager.find(User.class,user_id);
        user.getUnlocked().add(Medal.PHOTOGRAPHER);

        assertEquals(Medal.NOMEDAL,finder.getUserMedal(user));
        operation.setUserMedal(user, Medal.PHOTOGRAPHER);
        assertEquals(Medal.PHOTOGRAPHER,finder.getUserMedal(user));
    }

    @Test (expected = InvalidMedalException.class)
    public void testSetAndGetUserMedalFail() throws Exception{
        User user = manager.find(User.class,user_id);
        operation.setUserMedal(user, Medal.PHOTOGRAPHER);
    }

    @Test
    public void testGetUserMedals(){

        User user = manager.find(User.class,user_id);

        assertEquals(0,finder.getUserMedalsUnlocked(user).size());
        user.getUnlocked().add(Medal.COOK);
        assertEquals(1,finder.getUserMedalsUnlocked(user).size());

        user.getUnlocked().add(Medal.FISH);
        assertEquals(2,finder.getUserMedalsUnlocked(user).size());
        assertEquals(Medal.COOK.getName(),finder.getUserMedalsUnlocked(user).get(0));
        assertEquals(Medal.FISH.getName(),finder.getUserMedalsUnlocked(user).get(1));
    }

    @Test
    public void testGetMedalOfContributors() throws Exception{

        ShopList sl = manager.find(ShopList.class,shopList_id1);

        List<String> list = finder.getMedalOfContributors(sl);
        assertEquals(2, list.size());
        assertEquals(Medal.NOMEDAL.getName(),list.get(0));
        assertEquals(Medal.NOMEDAL.getName(),list.get(1));

        User user = manager.find(User.class,user_id);
        user.getUnlocked().add(Medal.PHOTOGRAPHER);
        operation.setUserMedal(user, Medal.PHOTOGRAPHER);

        User user2 = manager.find(User.class,user_id2);
        user2.getUnlocked().add(Medal.COOK);
        operation.setUserMedal(user2, Medal.COOK);

        list = finder.getMedalOfContributors(sl);
        assertEquals(2, list.size());
        assertEquals(Medal.PHOTOGRAPHER.getName(),list.get(0));
        assertEquals(Medal.COOK.getName(),list.get(1));
    }

    @Test
    public void testCheckMedalProgression(){

        User user1 = manager.find(User.class,user_id);

        List<Medal> unlocked = new ArrayList<>();
        unlocked.add(Medal.FISH);
        Map<Medal, Integer> inProgress = new HashMap<>();
        inProgress.put(Medal.FISH,1);
        inProgress.put(Medal.COOK,Medal.COOK.getNbRequired());
        inProgress.put(Medal.LEADER,Medal.LEADER.getNbRequired()+1);
        inProgress.put(Medal.MEAT,Medal.LEADER.getNbRequired()-1);

        user1.setUnlocked(unlocked);
        user1.setInProgress(inProgress);

        assertEquals(1,user1.getUnlocked().size());
        assertEquals(4,user1.getInProgress().size());

        operation.checkMedalProgression(user1);

        assertEquals(3,user1.getUnlocked().size());
        assertEquals(0,user1.getInProgress().size());
    }

    @Test
    public void testProgressionAddRecipeMedal(){
        User user1 = manager.find(User.class,user_id);
        assertEquals(0,user1.getInProgress().size());

        operation.progressionAddRecipeMedal(user1);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(1),user1.getInProgress().get(Medal.COOK));

        operation.progressionAddRecipeMedal(user1);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(2),user1.getInProgress().get(Medal.COOK));
    }

    @Test
    public void testProgressionSharingShopListMedal(){
        User user1 = manager.find(User.class,user_id);
        assertEquals(0,user1.getInProgress().size());

        operation.progressionSharingShopListMedal(user1);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(1),user1.getInProgress().get(Medal.LEADER));

        operation.progressionSharingShopListMedal(user1);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(2),user1.getInProgress().get(Medal.LEADER));
    }

    @Test
    public void testShopRecipeMedal(){
        User user1 = manager.find(User.class,user_id);
        assertEquals(0,user1.getInProgress().size());

        operation.progressionShopRecipeMedal(user1);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(1),user1.getInProgress().get(Medal.PHOTOGRAPHER));

        operation.progressionShopRecipeMedal(user1);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(2),user1.getInProgress().get(Medal.PHOTOGRAPHER));
    }

    @Test
    public void testProgressionCategoriesMedalsForShopList(){
        ShopList sl = manager.find(ShopList.class,shopList_id1);
        User user1 = manager.find(User.class,user_id);
        User user2 = manager.find(User.class,user_id2);

        ShopListElement elt1 = new ShopListElement("Steak",2,true,"viandes",0);
        ShopListElement elt2 = new ShopListElement("Evian",1,false,"boissons",0);
        ShopListElement elt3 = new ShopListElement("bombe test",1,true,"outil de test massif",0);
        ShopListElement elt4 = new ShopListElement("escalope",1,true,"viandes",0);

        elt1.setUserInCharge(user1);
        elt2.setUserInCharge(user2);
        elt3.setUserInCharge(user1);

        sl.getElements().add(elt1);
        sl.getElements().add(elt2);
        sl.getElements().add(elt3);
        sl.getElements().add(elt4);

        assertEquals(0,user1.getInProgress().size());
        assertEquals(0,user2.getInProgress().size());

        operation.progressionCategoriesMedalsForShopList(sl);
        assertEquals(1,user1.getInProgress().size());
        assertEquals(new Integer(2),user1.getInProgress().get(Medal.MEAT));
        assertEquals(0,user2.getInProgress().size());
    }

    @Test
    public void testCalculateAllUsersRewards(){

        User user1 = manager.find(User.class,user_id);

        List<Medal> unlocked = new ArrayList<>();
        unlocked.add(Medal.FISH);
        Map<Medal, Integer> inProgress = new HashMap<>();
        inProgress.put(Medal.FISH,1);
        inProgress.put(Medal.COOK,Medal.COOK.getNbRequired());
        inProgress.put(Medal.LEADER,Medal.LEADER.getNbRequired()+1);
        inProgress.put(Medal.MEAT,Medal.LEADER.getNbRequired()-1);

        user1.setUnlocked(unlocked);
        user1.setInProgress(inProgress);

        assertEquals(1,user1.getUnlocked().size());
        assertEquals(4,user1.getInProgress().size());

        operation.calculateAllUsersRewards();

        assertEquals(3,user1.getUnlocked().size());
        assertEquals(0,user1.getInProgress().size());
    }
}
