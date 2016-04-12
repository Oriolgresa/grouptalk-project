package edu.upc.eetac.dsa.grouptalk.auth;

import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.auth.UserInfo;

import java.sql.SQLException;

/**
 * Created by Aitor on 24/10/15.
 */
public interface AuthTokenDAO {
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws  SQLException;
}
