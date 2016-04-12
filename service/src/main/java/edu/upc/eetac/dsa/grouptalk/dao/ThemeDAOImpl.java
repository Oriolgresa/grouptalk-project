package edu.upc.eetac.dsa.grouptalk.dao;

import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Oriol on 29/03/16.
 */
public class ThemeDAOImpl implements ThemeDAO {
   
    @Override
    public Theme createTheme(String userid, String groupid, String subject, String content) throws SQLException, UserDidntSubscribedException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;

        boolean isInGroup = checkUser(userid, groupid);
        System.out.println(isInGroup);
        if (isInGroup != true)
            throw new UserDidntSubscribedException();

        try {
            connection = Database.getConnection();
            System.out.println("antes del UUID");
            stmt = connection.prepareStatement(ThemeDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            System.out.println("antes del if");
            if (rs.next()) {
                System.out.println("NEXT");
                id = rs.getString(1);
            }
            else
                throw new SQLException();
            System.out.println("paso UUID");
            stmt = connection.prepareStatement(ThemeDAOQuery.CREATE_THEME);
            stmt.setString(1, id);
            stmt.setString(2, userid);
            stmt.setString(3, groupid);
            stmt.setString(4, subject);
            stmt.setString(5, content);
            stmt.executeUpdate();
            System.out.println("PASO CREATE THEME");
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getThemeById(id);
    }

    @Override
    public Theme getThemeById(String id) throws SQLException, UserDidntSubscribedException {
        Theme theme = null;

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEME_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setGroupid(rs.getString("groupid"));
                theme.setSubject(rs.getString("subject"));
                theme.setContent(rs.getString("content"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        boolean isInGroup = checkUser(theme.getUserid(), theme.getGroupid());
        System.out.println(isInGroup);
        if (isInGroup != true)
            throw new UserDidntSubscribedException();
        return theme;
    }

    @Override
    public Theme getThemeByName(String name) throws SQLException, UserDidntSubscribedException {
        Theme theme = null;

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEME_BY_NAME);
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                theme = new Theme();
                System.out.println(rs.getString("id"));
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setGroupid(rs.getString("groupid"));
                theme.setSubject(rs.getString("subject"));
                theme.setContent(rs.getString("content"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return theme;
    }


    @Override
    public ThemeCollection getThemes() throws SQLException{
        ThemeCollection themeCollection = new ThemeCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEMES);


            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Theme theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setGroupid(rs.getString("groupid"));
                theme.setSubject(rs.getString("subject"));
                theme.setContent(rs.getString("content"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    themeCollection.setNewestTimestamp(theme.getLastModified());
                    first = false;
                }
                themeCollection.setOldestTimestamp(theme.getLastModified());
                themeCollection.getThemes().add(theme);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return themeCollection;
    }

    @Override
    public ThemeCollection getThemesByGroupId(String groupid) throws SQLException, UserDidntSubscribedException {
        ThemeCollection themeCollection = new ThemeCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();
            stmt = connection.prepareStatement(ThemeDAOQuery.GET_THEMES_BY_GROUPID);
            stmt.setString(1, groupid);

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Theme theme = new Theme();
                theme.setId(rs.getString("id"));
                theme.setUserid(rs.getString("userid"));
                theme.setGroupid(rs.getString("groupid"));
                theme.setSubject(rs.getString("subject"));
                theme.setContent(rs.getString("content"));
                theme.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                theme.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    themeCollection.setNewestTimestamp(theme.getLastModified());
                    first = false;
                }
                themeCollection.setOldestTimestamp(theme.getLastModified());
                boolean isInGroup = checkUser(theme.getUserid(), groupid);
                System.out.println(isInGroup);
                if (isInGroup != true)
                    throw new UserDidntSubscribedException();
                themeCollection.getThemes().add(theme);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return themeCollection;
    }

    @Override
    public Theme updateTheme(String id, String content) throws SQLException{
        Theme theme = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.UPDATE_THEME);
            stmt.setString(2, content);
            stmt.setString(3, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                try {
                    theme = getThemeById(id);
                }catch (UserDidntSubscribedException e){
                    e.printStackTrace();
                }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return theme;
    }

    @Override
    public boolean deleteTheme(String id) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(ThemeDAOQuery.DELETE_THEME);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

    @Override
    public boolean checkUser(String id, String groupid) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean a=false;
        System.out.println(groupid);
        System.out.println(id);
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.COMPARE_USER_GROUP);

            stmt.setString(2, groupid);
            stmt.setString(1, id);



            ResultSet rs = stmt.executeQuery();

            if(rs.next()) a=true;

            //String resultado = rs.getString("groupid");



        }catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return a;
    }
}
