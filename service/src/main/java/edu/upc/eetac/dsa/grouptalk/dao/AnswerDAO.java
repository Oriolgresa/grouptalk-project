package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Answer;
import edu.upc.eetac.dsa.grouptalk.entity.AnswerCollection;

import java.sql.SQLException;

/**
 * Created by Oriol on 30/03/16.
 */
public interface AnswerDAO {
    public Answer createAnswer(String userid, String themeid, String content) throws SQLException;
    public Answer getAnswerById(String id) throws SQLException;
    public AnswerCollection getAnswersByThemeId(String themeid) throws SQLException;
    public Answer updateAnswer(String id, String content) throws SQLException;
    public boolean deleteAnswer(String id) throws SQLException;
    public boolean checkUser(String id, String groupid) throws SQLException;

}
