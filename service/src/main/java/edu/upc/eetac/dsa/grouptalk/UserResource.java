package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.auth.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.grouptalk.dao.*;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Aitor on 25/10/15.
 */
@Path("users")
public class UserResource {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GrouptalkMediaType.GROUPTALK_AUTH_TOKEN)
    public Response registerUser(@FormParam("loginid") String loginid, @FormParam("password") String password, @Context UriInfo uriInfo) throws URISyntaxException {
        if (loginid == null || password == null)
            throw new BadRequestException("all parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl();
        User user = null;
        AuthToken authenticationToken = null;
        try {
            user = userDAO.createUser(loginid, password);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        } catch (UserAlreadyExistsException e) {
            throw new WebApplicationException("loginid already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        return Response.created(uri).type(GrouptalkMediaType.GROUPTALK_AUTH_TOKEN).entity(authenticationToken).build();
    }

    @Path("/{id}")
    @GET
    @Produces(GrouptalkMediaType.GROUPTALK_USER)
    public User getUser(@PathParam("id") String id) {
        User user = null;
        try {
            user = (new UserDAOImpl()).getUserById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (user == null)
            throw new NotFoundException("User with id = " + id + " doesn't exist");
        return user;
    }

    @Context
    private SecurityContext securityContext;
    @Path("/{id}")
    @DELETE
    public void deleteUser(@PathParam("id") String id){
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        UserDAO userDAO = new UserDAOImpl();
        try {
            if(!userDAO.deleteUser(id))
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id}/{namegroup}")
    @POST
    public void suscribe(@PathParam("id") String id, @PathParam("namegroup") String namegroup){
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        GroupDAO groupDAO = new GroupDAOImpl();
        Group group;
        try{
            group = groupDAO.getGroupByName(namegroup);
        }catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        UserDAO userDAO = new UserDAOImpl();
        try {
            userDAO.subscribetoGroup(id, group.getId());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserAlreadySubscribedException e) {
            e.printStackTrace();
        }
    }

    @Path("/{id}/{namegroup}")
    @DELETE
    public void unsuscribe(@PathParam("id") String id, @PathParam("namegroup") String namegroup){
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        GroupDAO groupDAO = new GroupDAOImpl();
        Group group;
        try{
            group = groupDAO.getGroupByName(namegroup);
        }catch (SQLException e) {
            throw new InternalServerErrorException();
        }
            UserDAO userDAO = new UserDAOImpl();
        try {
            userDAO.leaveGroup(userid, group.getId());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserDidntSubscribedException e) {
            throw new ForbiddenException("DIDNT SUSCRIBE");
        }
    }
}