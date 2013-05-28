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

/**
 * Servermanager SQL exception class.
 * @author cheese
 */
public class SMSQLException extends Exception {
    /**
     * Will throw an error without any parameters.
     */
    public SMSQLException() { 
        super();
        Servermanager.log.logException();
    }
    
    /**
     * 
     * @param message A string containing the error message.
     */
    public SMSQLException( String message ) {
        super( message );
        Servermanager.log.logException( message );
    }
    
    /**
     *
     * @param message A string containing the error message.
     * @param cause A throwable containing the cause.
     */
    public SMSQLException( String message, Throwable cause ) {
        super( message, cause );
        Servermanager.log.logException( message );
    }
    
    /**
     *
     * @param cause A throwable containing the cause.
     */
    public SMSQLException( Throwable cause ) {
        super( cause );
        Servermanager.log.logException( cause.getMessage() );
    }
}
