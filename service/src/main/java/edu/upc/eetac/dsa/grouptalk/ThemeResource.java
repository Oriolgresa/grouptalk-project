package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.dao.*;
import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Aitor on 28/10/15.
 */
@Path("themes")
public class ThemeResource {

    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createTheme(@FormParam("namegroup") String namegroup , @FormParam("subject") String subject, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if(namegroup==null || subject==null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        String userid = securityContext.getUserPrincipal().getName();
        GroupDAO groupDAO = new GroupDAOImpl();
        Group group;
        try{
            group = groupDAO.getGroupByName(namegroup);
        }catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        ThemeDAO themeDAO = new ThemeDAOImpl();

        Theme theme = null;

        try {
            theme = themeDAO.createTheme(userid,group.getId(),subject,content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        catch (UserDidntSubscribedException e) {
            throw new ForbiddenException("NOP");
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + theme.getId());
        return Response.created(uri).type(GrouptalkMediaType.GROUPTALK_THEME).entity(theme).build();
    }

    @Path("/{id}")
    @GET
    @Produces(GrouptalkMediaType.GROUPTALK_THEME)
    public Response getTheme(@PathParam("id") String id, @Context Request request) {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        Theme theme = null;
        try {
            theme = (new ThemeDAOImpl()).getThemeById(id);
            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(theme.getLastModified()));

            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

            // If ETag matches the rb will be non-null;
            // Use the rb to return the response without any further processing
            if (rb != null) {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }

            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(theme).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }catch (UserDidntSubscribedException e) {
            throw new ForbiddenException("NOP");
        }
    }

    @Path("/{userid}/{namegroup}")
    @GET
    @Produces(GrouptalkMediaType.GROUPTALK_THEME_COLLECTION)
    public Response getThemesByGroupId(@PathParam("namegroup") String namegroup, @PathParam("userid") String userid, @Context Request request) {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        String id = securityContext.getUserPrincipal().getName();
        if (!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        GroupDAO groupDAO = new GroupDAOImpl();
        Group group;
        try {
            group = groupDAO.getGroupByName(namegroup);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        ThemeCollection themes = null;
        try {
            themes = (new ThemeDAOImpl().getThemesByGroupId(group.getId()));
            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(themes.getNewestTimestamp()));

            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

            // If ETag matches the rb will be non-null;
            // Use the rb to return the response without any further processing
            if (rb != null) {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }

            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(themes).cacheControl(cacheControl).tag(eTag);
            return rb.build();

        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (UserDidntSubscribedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("/{id}")
    @PUT
    @Consumes(GrouptalkMediaType.GROUPTALK_THEME)
    @Produces(GrouptalkMediaType.GROUPTALK_THEME)
    public Theme updateTheme(@PathParam("id") String id, Theme theme) {
        if(theme == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(theme.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(theme.getUserid()))
            throw new ForbiddenException("operation not allowed");

        ThemeDAO themeDAO = new ThemeDAOImpl();
        try {
            theme = themeDAO.updateTheme(id, theme.getContent());
            if(theme == null)
                throw new NotFoundException("theme with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return theme;
    }

    @Path("/{id}")
    @DELETE
    public void deleteTheme(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        ThemeDAO themeDAO = new ThemeDAOImpl();
        try {
            String ownerid = themeDAO.getThemeById(id).getUserid();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!themeDAO.deleteTheme(id))
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserDidntSubscribedException e) {
            e.printStackTrace();
        }
    }
}
