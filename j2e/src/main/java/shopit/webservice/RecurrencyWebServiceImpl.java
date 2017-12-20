package shopit.webservice;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import shopit.ProductFinder;
import shopit.RecurrencyProcessing;
import shopit.ShopListFinder;
import shopit.UserFinder;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.utils.RecurrencyStrategy;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Charly on 13/06/2017.
 */

@Stateless
public class RecurrencyWebServiceImpl implements RecurrencyWebService
{
    @EJB RecurrencyProcessing recurrency;
    @EJB UserFinder userFinder;
    @EJB ShopListFinder shopListFinder;
    @EJB ProductFinder productFinder;

    private static final Logger log = Logger.getLogger(RecurrencyWebServiceImpl.class.getName());

    @Override
    public Response suggestHighestOccurrences(String json)
    {
        JSONObject jsonObject = new JSONObject(json);

        int idUser = jsonObject.getInt("id");
        User user = userFinder.findUserById(idUser);

        JSONArray jsonArray = jsonObject.getJSONArray("lists");
        List<ShopList> localLists = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONArray listJson = jsonArray.getJSONArray(i);
            ShopList shopList = new ShopList("useless", new User("l", "m", "n"), new Date());
            for(int j = 0; j < listJson.length(); j++)
            {
                JSONObject itemJson = listJson.getJSONObject(j);
                String nameItem = itemJson.getString("item");
                int quantity = itemJson.getInt("quantity");

                shopList.getElements().add(new ShopListElement(nameItem, quantity, false, "", 0.0));
            }

            localLists.add(shopList);
        }

        List<ShopList> sharedLists = shopListFinder.getAllShopList(user, true);

        /*** Merging localLists and sharedLists into one in order to pass it to the recurrency bean ***/
        List<ShopList> mergedLists = new ArrayList<>();
        mergedLists.addAll(localLists); mergedLists.addAll(sharedLists);

        List<String> recurrentItems = recurrency.findRecurrentItems(mergedLists, RecurrencyStrategy.HIGHEST_OCCURRENCE);

        JSONArray outArrayJson = new JSONArray();
        for(String item : recurrentItems)
        {
            JSONObject temp = new JSONObject();
            temp.put("item", item);
            temp.put("price", productFinder.getProductPrice(item));

            outArrayJson.put(temp);
        }

        JSONObject outObjectJson = new JSONObject();
        outObjectJson.put("recurrent_items", outArrayJson);
        
        log.info(outObjectJson.toString());
        
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(outObjectJson).build();
    }
}
