package shopit;

import javax.ejb.Local;
import java.awt.image.BufferedImage;

/**
 * Created by thomas on 07/06/2017.
 */
@Local
public interface ProductReader {

    public void readCashRecipe(BufferedImage img);

}
