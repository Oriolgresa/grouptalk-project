package edu.upc.eetac.dsa.grouptalk.dao;

/**
 * Created by Aitor on 24/10/15.
 */
public interface ThemeDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_THEME = "insert into themes (id, userid, groupid, subject, content) values (unhex(?), unhex(?), unhex(?), ?, ?)";
    public final static String GET_THEMES = "select hex(id) as id, hex(userid) as userid, hex(groupid) as groupid, subject, content, creation_timestamp, last_modified from themes";
    public final static String GET_THEME_BY_ID = "select hex(t.id) as id, hex(t.userid) as userid, hex(t.groupid) as groupid, t.content, t.subject, t.creation_timestamp, t.last_modified from themes t , users u where t.id=unhex(?) and u.id=t.userid";
    public final static String GET_THEME_BY_NAME = "select hex(t.id) as id, hex(t.userid) as userid, hex(t.groupid) as groupid, t.content, t.subject, t.creation_timestamp, t.last_modified from themes t , users u where t.subject=? and u.id=t.userid";
    public final static String GET_THEMES_BY_GROUPID = "select hex(t.id) as id, hex(t.userid) as userid, hex(t.groupid) as groupid, t.subject, t.content, t.creation_timestamp, t.last_modified from themes t, groups g where g.id=unhex(?) and t.groupid=g.id";
    public final static String UPDATE_THEME = "update themes set  content=? where id=unhex(?) ";
    public final static String DELETE_THEME = "delete from themes where id=unhex(?)";
}

