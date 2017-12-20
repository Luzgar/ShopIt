package shopit;

import shopit.entities.Product;

import javax.ejb.Local;

/**
 * Created by thomas on 08/06/2017.
 */
@Local
public interface ProductFinder {

    public Product getProductByName(String name);

    public double getProductPrice(String name);
}
