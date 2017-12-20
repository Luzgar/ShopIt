package shopit.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 31/05/2017.
 */
@Path("/repartition")
public interface RepartitionWebService {

    /**
     * Expected request body :
     {
        "id_shoplist": 1,
        "id_user": 2,
        "item": "courgette"
     }
     */
    @POST
    @Path("/setuserincharge")
    @Consumes(MediaType.APPLICATION_JSON)
    Response setUserInCharge(String json);

    @GET
    @Path("/docategoryrepartition/{idlist}")
    Response doCategoryRepartition(@PathParam("idlist") int idList);

    @GET
    @Path("/dopricerepartition/{idlist}")
    Response doPriceRepartition(@PathParam("idlist") int idList);
}
