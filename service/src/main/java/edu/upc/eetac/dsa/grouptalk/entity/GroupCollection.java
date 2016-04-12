package edu.upc.eetac.dsa.grouptalk.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 27/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupCollection {
    @InjectLinks({ })
    private List<Link> links;
    private List<Group> groups = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> stings) {
        this.groups = stings;
    }
}

