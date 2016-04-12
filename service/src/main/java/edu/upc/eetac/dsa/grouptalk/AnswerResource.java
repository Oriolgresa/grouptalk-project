package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.dao.*;
import edu.upc.eetac.dsa.grouptalk.entity.Answer;
import edu.upc.eetac.dsa.grouptalk.entity.AnswerCollection;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Oriol on 30/03/16.
 */
@Path("answers")
public class AnswerResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createAnswer(@FormParam("nametheme") String nametheme, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if (nametheme == null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        String userid = securityContext.getUserPrincipal().getName();
        System.out.println(userid);

        ThemeDAO themeDAO = new ThemeDAOImpl();
        Theme theme = null;
        try {
            System.out.println("Try de Coger IdTHeme");
            theme = themeDAO.getThemeByName(nametheme);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserDidntSubscribedException e) {
            e.printStackTrace();
        }
        AnswerDAO answerDAO = new AnswerDAOImpl();
        Answer answer = null;

        try {
            System.out.println("Try del Crear Answer");
            answer = answerDAO.createAnswer(userid,theme.getId(), content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + answer.getId());
        return Response.created(uri).type(GrouptalkMediaType.GROUPTALK_THEME).entity(answer).build();
    }

    @Path("/{nametheme}")
    @GET
    @Produces(GrouptalkMediaType.GROUPTALK_ANSWER_COLLECTION)
    public Response getAnswersByNameTheme(@PathParam("nametheme") String nametheme, @Context Request request) {
        // Create cache-control
        CacheControl cacheControl = new CacheControl();
        ThemeDAO themeDAO = new ThemeDAOImpl();
        Theme theme = null;
        try{
            theme = themeDAO.getThemeByName(nametheme);
        }catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserDidntSubscribedException e) {
            e.printStackTrace();
        }
        AnswerCollection answers = null;
        try {
            System.out.println("\n\n\n");
            answers = (new AnswerDAOImpl().getAnswersByThemeId(theme.getId()));

            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(answers.getNewestTimestamp()));

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
            rb = Response.ok(answers).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(GrouptalkMediaType.GROUPTALK_ANSWER)
    @Produces(GrouptalkMediaType.GROUPTALK_ANSWER)
    public Answer updateAnswer(@PathParam("id") String id, Answer answer) {

        System.out.println("Esta dentro bro");
        if(answer == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(answer.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");
        System.out.println(answer.getId());
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(answer.getUserid()))
            throw new ForbiddenException("operation not allowed");

        AnswerDAO answerDAO = new AnswerDAOImpl();
        try {
            System.out.println("Try antes del update");
            answer = answerDAO.updateAnswer(id, answer.getContent());
            if(answer == null)
                throw new NotFoundException("theme with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return answer;
    }

    @Path("/{id}")
    @DELETE
    public void deleteAnswer(@PathParam("id") String id) {
        String userid = securityContext.getUserPrincipal().getName();
        AnswerDAO answerDAO = new AnswerDAOImpl();
        try {
            String ownerid = answerDAO.getAnswerById(id).getUserid();
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!answerDAO.deleteAnswer(id))
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
