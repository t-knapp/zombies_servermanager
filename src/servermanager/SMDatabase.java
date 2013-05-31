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
public abstract class SMDatabase {
    private static Connection connection = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    
    private static String MYSQL_URL = "localhost";
    private static int MYSQL_PORT = 3306;
    private static String MYSQL_DATABASE = "zombies";
    private static String MYSQL_USERNAME = "zombies_sm";
    private static String MYSQL_PASSWORD = "zombies123";
    
    /**
     * Main constructor for the database.
     * @param url URL of the server.
     * @param database Database name.
     * @param username Username to connect with.
     * @param password Password of the user.
     * @throws SMException
     */
    public static void connect() throws SMException {
        try {
            connection = DriverManager.getConnection( "jdbc:mysql://" + MYSQL_URL + ":" + MYSQL_PORT + 
                    "/" + MYSQL_DATABASE, MYSQL_USERNAME, MYSQL_PASSWORD );
            statement = connection.createStatement();
            resultSet = statement.executeQuery( "SELECT VERSION()" );
            if ( resultSet.next() )
                SMLog.write( "MySQL version : " + resultSet.getString( 1 ) );
        }
        catch ( SQLException ex ) {
            throw new SMRuntimeException( ex.getMessage() );
        }
    }
    
    public static void setURL( String url ) {
        MYSQL_URL = url;
    }
    
    public static void setPort( int port ) {
        MYSQL_PORT = port;
    }
    
    public static void setDatabase( String database ) {
        MYSQL_DATABASE = database;
    }
    
    public static void setUsername( String username ) {
        MYSQL_USERNAME = username;
    }
    
    public static void setPassword( String password ) {
        MYSQL_PASSWORD = password;
    }
}
