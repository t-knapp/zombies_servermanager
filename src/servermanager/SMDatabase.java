/*
    zombies_servermanager
    Copyright (C) 2013 DJ Hepburn

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package servermanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database entry point.
 * @author cheese
 */
public class SMDatabase {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    /**
     * Basic constructor. Does nothing.
     * @throws SMException
     */
    public SMDatabase() throws SMException {
    }
    
    /**
     * Main constructor for the database.
     * @param url URL of the server.
     * @param database Database name.
     * @param username Username to connect with.
     * @param password Password of the user.
     * @throws SMException
     */
    public SMDatabase( String url, String database, String username, String password ) throws SMException {
        try {
            connection = DriverManager.getConnection( "jdbc:mysql://" + url + "/" + database + "?user=" + username + "&password=" + password );
            statement = connection.createStatement();
            resultSet = statement.executeQuery( "SELECT VERSION()" );
            Servermanager.log.write( "MySQL version : " + resultSet.getString( 1 ) );
        }
        catch ( SQLException ex ) {
            throw new SMRuntimeException( ex.getMessage() );
        }
    }
}
