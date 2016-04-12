package edu.upc.eetac.dsa.grouptalk.dao;

/**
 * Created by Oriol on 29/03/16.
 */
public interface UserDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_USER = "insert into users (id, loginid, password) values (UNHEX(?), ?, UNHEX(MD5(?)))";
    public final static String ASSIGN_ROLE_REGISTERED = "insert into user_roles (userid, role) values (UNHEX(?), 'registered')";
    public final static String GET_USER_BY_ID = "select hex(u.id) as id, u.loginid as loginid from users u where id=unhex(?)";
    public final static String GET_USER_BY_USERNAME = "select hex(u.id) as id, u.loginid as loginid from users u where u.loginid=?";
    public final static String DELETE_USER = "delete from users where id=unhex(?)";
    public final static String GET_PASSWORD =  "select hex(password) as password from users where id=unhex(?)";
    public final static String COMPARE_USER_GROUP =  "select hex(groupid) as groupid from user_group where userid=UNHEX(?) and groupid=UNHEX(?)";
    public final static String SUBSCRIBE_GROUP = "insert into user_group (userid, groupid) values (UNHEX(?), UNHEX(?))";
    public final static String UNSUBSCRIBE_GROUP = "delete from user_group where userid=unhex(?) and groupid=unhex(?)";
}
