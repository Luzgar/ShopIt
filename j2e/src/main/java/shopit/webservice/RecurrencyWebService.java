package shopit.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Charly on 13/06/2017.
 */

@Path("/recurrency")
public interface RecurrencyWebService
{
    /**
     * Expected body request :
     {
        "id": 2,
        "lists":
        [
            [
                {
                    "item": "Beurre",
                    "quantity": 1
                },
                {
                    "item": "Peche",
                    "quantity": 2
                }
            ],
            [
                {
                    "item": "Beurre",
                    "quantity": 3
                }
            ]
        ]
     }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/highest_occurrences")
    Response suggestHighestOccurrences(String json);

}
