package shopit.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Charly on 30/05/2017.
 */

@Path("/sharing")
public interface SharingWebService
{
    /**
     * Expected request body :
     {
        "id": 2,
        "pseudo": "fufu"
     }
     */
    @POST
    @Path("/addcontributor")
    @Consumes(MediaType.APPLICATION_JSON)
    Response addContributor(String json);

    /**
     * Expected request body :
     {
     "id": 2,
     "pseudo": "fufu"
     }
     */
    @POST
    @Path("/removecontributor")
    @Consumes(MediaType.APPLICATION_JSON)
    Response removeContributor(String json);
}
