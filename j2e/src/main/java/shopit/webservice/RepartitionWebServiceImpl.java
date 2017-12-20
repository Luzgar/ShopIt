package shopit.webservice;

import org.json.JSONObject;
import shopit.CriterionBuilder;
import shopit.RepartitionProcessing;
import shopit.ShopListFinder;
import shopit.UserFinder;
import shopit.entities.ShopList;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;
import shopit.exceptions.UserIsNotAContributorException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 31/05/2017.
 */
@Stateless
public class RepartitionWebServiceImpl implements RepartitionWebService
{
    @EJB private CriterionBuilder criterionBuilder;
    @EJB private RepartitionProcessing repartitionProcessing;
    @EJB private ShopListFinder shopListFinder;
    @EJB private UserFinder userFinder;

    @Override
    public Response setUserInCharge(String json) {

        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("idshoplist");
        int idUser = jsonObject.getInt("iduser");
        String item = jsonObject.getString("item");

        ShopList shopList = shopListFinder.getShopListById(idList);
        User contributor = userFinder.findUserById(idUser);

        try{
            criterionBuilder.setUserInCharge(shopList,contributor,item);
        }catch (NoItemFoundException e){
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }catch (UserIsNotAContributorException e){
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response doCategoryRepartition(int idList)
    {
        ShopList shopList = shopListFinder.getShopListById(idList);

        repartitionProcessing.doCategoryRepartition(shopList);

        return Response.ok().build();
    }

    @Override
    public Response doPriceRepartition(int idList)
    {
        ShopList shopList = shopListFinder.getShopListById(idList);

        repartitionProcessing.doPriceRepartition(shopList);

        return Response.ok().build();
    }
}
