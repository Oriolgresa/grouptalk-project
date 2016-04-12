package edu.upc.eetac.dsa.grouptalk.dao;

/**
 * Created by Oriol on 30/03/16.
 */
public interface AnswerDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_ANSWER = "insert into answers (id, userid, themeid, content) values (UNHEX(?), unhex(?), unhex(?), ?)";
    public final static String GET_ANSWER_BY_ID = "select hex(a.id) as id, hex(a.userid) as userid, hex(a.themeid) as themeid, a.content, a.creation_timestamp, a.last_modified from answers a , users u where a.id=unhex(?) and u.id=a.userid";
    public final static String GET_ANSWERS_BY_THEMEID = "select hex(id) as id, hex(userid) as userid, hex(themeid) as themeid, content, creation_timestamp, last_modified from answers where themeid=unhex(?)";
    public final static String UPDATE_ANSWER = "update answers set content=? where id=unhex(?) ";
    public final static String DELETE_ANSWER = "delete from answers where id=unhex(?)";
}
