package shopit.webservice;

import org.json.JSONObject;
import shopit.ContributorOperation;
import shopit.ShopListFinder;
import shopit.UserFinder;
import shopit.entities.ShopList;
import shopit.entities.User;
import shopit.exceptions.ContributorAlreadyExistingException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Charly on 30/05/2017.
 */

@Stateless
public class SharingWebServiceImpl implements SharingWebService {

    @EJB ContributorOperation contributorOperation;
    @EJB UserFinder userFinder;
    @EJB ShopListFinder shopListFinder;

    public Response addContributor(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");
        String pseudoUser = jsonObject.getString("pseudo");

        ShopList list = shopListFinder.getShopListById(idList);
        User contributor = userFinder.findUserByPseudo(pseudoUser);

        if(contributor == null)
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("User not found").build();

        try
        {
            contributorOperation.addContributorToShopList(list, contributor);
        }
        catch(ContributorAlreadyExistingException e){
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response removeContributor(String json) {

        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");
        String pseudoUser = jsonObject.getString("pseudo");

        ShopList list = shopListFinder.getShopListById(idList);
        User contributor = userFinder.findUserByPseudo(pseudoUser);

        if(contributor == null)
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("User not found").build();

        contributorOperation.removeContributorFromShopList(list, contributor);

        return Response.ok().build();
    }
}
