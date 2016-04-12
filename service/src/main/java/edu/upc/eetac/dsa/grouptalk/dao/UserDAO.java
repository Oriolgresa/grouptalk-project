package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.User;

import java.sql.SQLException;

/**
 * Created by Aitor on 24/10/15.
 */
public interface UserDAO {
    public User createUser(String loginid, String password) throws SQLException, UserAlreadyExistsException;

    public User getUserById(String id) throws SQLException;

    public User getUserByLoginid(String loginid) throws SQLException;

    public boolean deleteUser(String id) throws SQLException;

    public boolean subscribetoGroup(String id, String groupid) throws SQLException, UserAlreadySubscribedException;

    public boolean leaveGroup(String id, String groupid) throws SQLException, UserDidntSubscribedException;

    public boolean checkPassword(String id, String password) throws SQLException;

    public boolean checkUser(String userid, String id) throws SQLException;
}
