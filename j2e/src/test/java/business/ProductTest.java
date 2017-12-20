package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.ProductReader;
import shopit.ProductFinder;
import shopit.entities.Product;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by thomas on 07/06/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class ProductTest extends AbstractTCFTest {

    @EJB
    ProductReader reader;
    @EJB
    ProductFinder finder;

    @PersistenceContext
    private EntityManager manager;
    @Inject
    private UserTransaction utx;

    private double epsilon = 0.001;

    private String img_path = "./src/test/resources/images/ticket_casino_4.jpg";
    private String img2_path = "./src/test/resources/images/ticket_casino_5.jpg";
    private String img_path_invalid = "./src/test/resources/images/ticket_bk.jpg";

    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage img_invalid;

    @Before
    public void defineContext() throws Exception
    {
        img1 = ImageIO.read( new File(img_path));
        img2 = ImageIO.read( new File(img2_path));
        img_invalid = ImageIO.read( new File(img_path_invalid));
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();

        Query q = manager.createNativeQuery("DELETE FROM Product");
        q.executeUpdate();

        utx.commit();
    }

    @Test
    @Ignore
    public void testReadCashRecipe() throws Exception{

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root =  criteria.from(Product.class);
        criteria.select(root);
        TypedQuery<Product> query = manager.createQuery(criteria);

        reader.readCashRecipe(img1);
        assertEquals(5.60,
                finder.getProductByName("TROPICANA 0R S/P").getPrice(),epsilon);
        assertEquals(1.75,
                finder.getProductByName("SIROP GREN 750L C0").getPrice(),epsilon);
        assertEquals(1.23,
                finder.getProductByName("MENT.BIEN NOIR 150").getPrice(),epsilon);
        assertEquals(0.24,
                finder.getProductByName("EAU CAS.50CL").getPrice(),epsilon);
        assertEquals(4,query.getResultList().size());

        reader.readCashRecipe(img1);
        assertEquals(5.60,
                finder.getProductByName("TROPICANA 0R S/P").getPrice(),epsilon);
        assertEquals(1.75,
                finder.getProductByName("SIROP GREN 750L C0").getPrice(),epsilon);
        assertEquals(1.23,
                finder.getProductByName("MENT.BIEN NOIR 150").getPrice(),epsilon);
        assertEquals(0.24,
                finder.getProductByName("EAU CAS.50CL").getPrice(),epsilon);
        assertEquals(4,query.getResultList().size());

        reader.readCashRecipe(img2);

        assertEquals(5.60,
                finder.getProductByName("TROPICANA 0R S/P").getPrice(),epsilon);
        assertEquals(1.75,
                finder.getProductByName("SIROP GREN 750L C0").getPrice(),epsilon);
        assertEquals(1.33,
                finder.getProductByName("MENT.BIEN NOIR 150").getPrice(),epsilon);
        assertEquals(0.24,
                finder.getProductByName("EAU CAS.50CL").getPrice(),epsilon);
        assertEquals(4,query.getResultList().size());


        // Should not throw an exception
        reader.readCashRecipe(img_invalid);
    }

    @Test
    public void testGetProductPrice(){

        Product p1 = new Product("COCA COLA 1L",2.0);
        Product p2 = new Product("COCA COLA ZERO 1L",2.2);
        Product p3 = new Product("COCA COLA 50CL",1);
        Product p4 = new Product("COCA COLA 2L",3.5);
        Product p5 = new Product("RIZ CASINO 500G",1);
        Product p6 = new Product("RIZ SUPER 500G",3);
        Product p7 = new Product("RIZ CASINO 500G",2);

        manager.persist(p1);
        manager.persist(p2);
        manager.persist(p3);
        manager.persist(p4);
        manager.persist(p5);
        manager.persist(p6);
        manager.persist(p7);

        assertEquals(2.175, finder.getProductPrice("coca"),epsilon);
        assertEquals(2.1, finder.getProductPrice("coca cola 1l"),epsilon);
        assertEquals(2.2, finder.getProductPrice("coca zero"),epsilon);
        assertEquals(2, finder.getProductPrice("riz"),epsilon);
    }
}
