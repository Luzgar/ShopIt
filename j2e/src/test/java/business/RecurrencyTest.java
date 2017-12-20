package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.RecurrencyProcessing;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.utils.RecurrencyStrategy;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Charly on 13/06/2017.
 */

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class RecurrencyTest extends AbstractTCFTest
{
    @EJB private RecurrencyProcessing recurrency;

    private ShopList list1, list2, list3;
    private List<ShopList> lists;
    private User mockUser;

    @Before
    public void defineContext()
    {
        mockUser = new User("l", "m", "n");
        list1 = new ShopList("list1", mockUser, new Date());
        list2 = new ShopList("list2", mockUser, new Date());
        list3 = new ShopList("list3", mockUser, new Date());

        list1.getElements().add(new ShopListElement("Beurre", "l", 0.0));
        list1.getElements().add(new ShopListElement("Poire", "l", 0.0));
        list1.getElements().add(new ShopListElement("Rhum", "l", 0.0));
        list1.getElements().add(new ShopListElement("Coca", "l", 0.0));

        list2.getElements().add(new ShopListElement("Beurre", "l", 0.0));
        list2.getElements().add(new ShopListElement("Pomme", "l", 0.0));
        list2.getElements().add(new ShopListElement("Vin", "l", 0.0));
        list2.getElements().add(new ShopListElement("Coconut", "l", 0.0));

        list3.getElements().add(new ShopListElement("Coca", "l", 0.0));
        list3.getElements().add(new ShopListElement("Poire", "l", 0.0));
        list3.getElements().add(new ShopListElement("Rhum", "l", 0.0));
        list3.getElements().add(new ShopListElement("Pomme", "l", 0.0));
        list3.getElements().add(new ShopListElement("Beurre", "l", 0.0));

        lists = new ArrayList<>();
        lists.add(list1); lists.add(list2); lists.add(list3);
    }

    @After
    public void cleanUp()
    {
        list1 = null; list2 = null; list3 = null;
        mockUser = null;
        lists = null;
    }

    @Test
    public void testHighestOccurence()
    {
        List<String> suggestedItems = recurrency.findRecurrentItems(lists, RecurrencyStrategy.HIGHEST_OCCURRENCE);
        assertEquals(5, suggestedItems.size());

        assertEquals("Beurre", suggestedItems.get(0));
        assertFalse(suggestedItems.contains("Vin"));
        assertTrue(suggestedItems.contains("Poire"));
        assertTrue(suggestedItems.contains("Rhum"));
        assertTrue(suggestedItems.contains("Coca"));
        assertTrue(suggestedItems.contains("Pomme"));
    }

}
