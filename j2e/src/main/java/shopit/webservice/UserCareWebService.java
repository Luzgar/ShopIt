package shopit.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Charly on 29/05/2017.
 */

@Path("/usercare")
public interface UserCareWebService
{
    /**
     * Expected body request :
     {
        "login":"lgn",
        "password":"pwd",
        "pseudo":"fufu"
     }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/adduser")
    Response addUser(String json);

    /**
     * Expected body request :
     {
        "id": 51,
        "token": "letoken"
     }
     */
    @POST
    @Path("/settoken")
    @Consumes(MediaType.APPLICATION_JSON)
    Response setToken(String json);


    /**
     * Expected body request :
     {
        "login": "lgn",
        "password": "pwd"
     }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    Response logIn(String json);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/loginexists/{login}")
    Response doesLoginExist(@PathParam("login") String login);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/pseudoexists/{pseudo}")
    Response doesPseudoExist(@PathParam("pseudo") String pseudo);

    /**
     * Expected body request :
     {
        "id": "1",
        "lifespan": "30"
     }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/setlifespanlist")
    Response setLifespanList(String json);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("getlifespanlist/{userid}")
    Response getLifespanList(@PathParam("userid") int userId);

    /**
     * Expected body request :
     {
     "id": "1",
     "reccurency": false
     }
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/setuserdoreccurency")
    Response setUserDoReccurency(String json);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("getuserdoreccurency/{userid}")
    Response getUserDoReccurency(@PathParam("userid") int userId);
}
