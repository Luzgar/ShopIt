package shopit.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by Charly on 08/06/2017.
 */

@Path("/price")
public interface PriceWebService
{
    /**
     * Expected body request (refer to Medal enum):
     {
     "author_id": 5,
     "img": picture data
     }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/processimage")
    Response processImage(String json);

    @GET
    @Path("/getprice/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPrice(@PathParam("name") String name);
}
