package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;

import java.sql.SQLException;

/**
 * Created by Aitor on 24/10/15.
 */
public interface GroupDAO {

    public Group createGroup(String name) throws SQLException, GroupAlreadyExistsException;

    public Group getGroupById(String id) throws SQLException;

    public Group getGroupByName(String name) throws SQLException;

    public GroupCollection getGroups() throws SQLException;

    public boolean deleteGroup(String id) throws SQLException;

}
