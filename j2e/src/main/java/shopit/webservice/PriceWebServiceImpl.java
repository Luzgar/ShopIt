package shopit.webservice;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import shopit.MedalOperation;
import shopit.ProductFinder;
import shopit.ProductReader;
import shopit.UserFinder;
import shopit.entities.User;
import sun.misc.BASE64Decoder;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Charly on 08/06/2017.
 */
public class PriceWebServiceImpl implements PriceWebService
{
    @EJB ProductReader productReader;
    @EJB ProductFinder productFinder;
    @EJB UserFinder userFinder;
    @EJB MedalOperation medalOperation;

    @Override
    public Response processImage(String json)
    {
        JSONObject jsonObject = new JSONObject(json);

        int author_id = jsonObject.getInt("author_id");
        String str = jsonObject.getString("img");

        str = str.replace("data:image/jpeg;base64,", "");
        BufferedImage buffImage = null;
        try {

            byte[] imageByteArray = Base64.decodeBase64(str);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);

            buffImage = ImageIO.read(bis);
            productReader.readCashRecipe(buffImage);
            bis.close();

        } catch (IOException e) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("malformed image").build();
        }
        User author = userFinder.findUserById(author_id);
        if(author!=null){
            medalOperation.progressionShopRecipeMedal(author);
        }

        return Response.ok().build();
    }

    @Override
    public Response getPrice(String name) {

        double avg = productFinder.getProductPrice(name);

        Gson gson = new Gson();
        String json = gson.toJson(avg);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    }
}
