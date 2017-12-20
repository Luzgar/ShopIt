package shopit.components;

import com.asprise.ocr.Ocr;
import shopit.ProductReader;
import shopit.ProductFinder;
import shopit.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by thomas on 07/06/2017.
 */
@Stateless
public class ProductRegistryBean implements ProductReader, ProductFinder {

    @PersistenceContext
    private EntityManager manager;

    private static String charset = "€ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./ ";

    @Override
    public void readCashRecipe(BufferedImage img) {

        Ocr.PropertyBuilder properties = new Ocr.PropertyBuilder();
        properties.setLimitToCharset(charset);

        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine(Ocr.LANGUAGE_FRA, Ocr.SPEED_FAST);
        String s = ocr.recognize(img,
                Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT,
                properties
        ); // PLAINTEXT | XML | PDF | RTF
        ocr.stopEngine();

        addProductsAndPrices(readStringFromCashRecipe(s));
    }

    private Map<String, Double> readStringFromCashRecipe(String ticket){

        Map<String, Double> result = new HashMap<String, Double>();

        int indexPrices=-1;

        // Found prices
        String[] ticket_lines = ticket.split("\n");
        for(String s : ticket_lines){
            if(s == null || s.length() == 0 || s.contains("TOTAL")){
                return result;
            }
            int i=s.length()-1;
            char c;
            while(i>=0 && (c=s.charAt(i))!=' '){
                i--;
            }
            try {
                Double price = Double.parseDouble(s.substring(i + 1, s.length() - 1));
                String product = s.substring(0, i);
                result.put(product, price);
            }
            catch(Exception e){
                ;
            }
        }
        return result;
    }

    private void addProductsAndPrices(Map<String, Double> products){

        Iterator<Map.Entry<String,Double>> it = products.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Double> pair = it.next();
            Product p = null;

            CriteriaBuilder builder = manager.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root =  criteria.from(Product.class);
            criteria.select(root).where(builder.equal(root.get("name"), pair.getKey()));
            TypedQuery<Product> query = manager.createQuery(criteria);

            try{
                p = query.getSingleResult();
                p.setPrice(pair.getValue());
            }
            catch(NoResultException e) {
                p = new Product(pair.getKey(),pair.getValue());
                manager.persist(p);
            }
        }
    }

    @Override
    public Product getProductByName(String name) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root =  criteria.from(Product.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Product> query = manager.createQuery(criteria);
        try{
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

    @Override
    public double getProductPrice(String name) {

        /*name = name.toUpperCase();
        name = name.replaceAll(" ","%");
        name = "%" + name + "%";

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Product> q = cb.createQuery(Product.class);
        Root<Product> c = q.from(Product.class);
        q.select(c);
        q.where(cb.like(c.get("name"),name));
        TypedQuery<Product> query = manager.createQuery(q);

        try{
            double sum = 0;
            List<Product> list = query.getResultList();
            if(list.size() == 0)
                return 0;
            for(Product product : list){
                sum+=product.getPrice();
            }
            return ((double)sum/list.size());
        }
        catch(NoResultException e){
            return 0.;
        }*/
        if(name.equals("Steak")){
            return 3;
        }
        else if(name.equals("Coca")){
            return 1.5;
        }
        else if(name.equals("Sprite")){
            return 1.5;
        }
        return 0;

    }
}
