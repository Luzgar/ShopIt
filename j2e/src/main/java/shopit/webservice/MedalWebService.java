package shopit.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 13/06/2017.
 */
@Path("/medal")
public interface MedalWebService {

    @GET
    @Path("/getusermedalexposed/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserMedalExposed(@PathParam("id") int id);

    @GET
    @Path("/getusermedalsunlocked/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserMedalsUnlocked(@PathParam("id") int id);

    @GET
    @Path("/getmedalofcontributors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getMedalOfContributors(@PathParam("id") int id);

    /**
     * Expected body request (refer to Medal enum):
     {
     "id": 5,
     "medal": "NOMEDAL"
     }
     */
    @POST
    @Path("/setusermedalexposed")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    Response setUserMedalExposed(String json);

    @GET
    @Path("/addmedaluser/{id}/{medal}")
    @Produces(MediaType.APPLICATION_JSON)
    Response addUserMedal(@PathParam("id") int id,@PathParam("medal") String medal);
}
