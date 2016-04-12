package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.dao.GroupAlreadyExistsException;
import edu.upc.eetac.dsa.grouptalk.dao.GroupDAO;
import edu.upc.eetac.dsa.grouptalk.dao.GroupDAOImpl;
import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Aitor on 26/10/15.
 */
@Path("groups")
public class GroupResource {
    @Context
    private SecurityContext securityContext;
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void registerGroup(@FormParam("name") String name, @Context UriInfo uriInfo) throws URISyntaxException {
        boolean a= securityContext.isUserInRole("admin");
        if (a==true) {
            if (name == null)
                throw new BadRequestException("all parameters are mandatory");
            GroupDAO groupDAO = new GroupDAOImpl();
            Group group = null;
            try {
                group = groupDAO.createGroup(name);
            } catch (GroupAlreadyExistsException e) {
                throw new WebApplicationException("group name already exists", Response.Status.CONFLICT);
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
        }
        else
         throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }


    @Path("/{id}")
    @GET
    @Produces(GrouptalkMediaType.GROUPTALK_GROUP)
    public Group getGroup(@PathParam("id") String id) {
        Group group = null;
        try {
            group = (new GroupDAOImpl()).getGroupById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (group == null)
            throw new NotFoundException("Group with id = " + id + " doesn't exist");
        return group;
    }

    @GET
    @Produces(GrouptalkMediaType.GROUPTALK_GROUP_COLLECTION)
    public GroupCollection getGroups() {
        GroupCollection groups = null;
        try {
            groups = (new GroupDAOImpl()).getGroups();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (groups == null)
            throw new NotFoundException("No groups created");
        return groups;
    }

    @Path("/{id}")
    @DELETE
    public void deleteGroup(@PathParam("id") String id) {
        boolean a = securityContext.isUserInRole("admin");
        if (a == true) {
            GroupDAO groupDAO = new GroupDAOImpl();
            try {
                if (!groupDAO.deleteGroup(id))
                    throw new NotFoundException("User with id = " + id + " doesn't exist");
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
        }
        else
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
