package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import java.sql.SQLException;

/**
 * Created by Oriol on 29/03/16.
 */
public interface ThemeDAO {
    public Theme createTheme(String userid, String groupid, String subject, String content) throws SQLException,UserDidntSubscribedException;
    public Theme getThemeById(String id) throws SQLException, UserDidntSubscribedException;
    public Theme getThemeByName(String name) throws SQLException, UserDidntSubscribedException;
    public ThemeCollection getThemes() throws SQLException;
    public ThemeCollection getThemesByGroupId(String groupid) throws SQLException, UserDidntSubscribedException;
    public Theme updateTheme(String id,String content) throws SQLException;
    public boolean deleteTheme(String id) throws SQLException;
    public boolean checkUser(String id, String groupid) throws SQLException;}
