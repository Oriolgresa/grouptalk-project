package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;

import java.sql.SQLException;

/**
 * Created by Oriol on 29/03/16.
 */
public interface GroupDAO {

    public Group createGroup(String name) throws SQLException, GroupAlreadyExistsException;

    public Group getGroupById(String id) throws SQLException;

    public Group getGroupByName(String name) throws SQLException;

    public GroupCollection getGroups() throws SQLException;

    public boolean deleteGroup(String id) throws SQLException;

}
