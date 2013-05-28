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
 * Servermanager runtime exception class.
 * @author cheese
 */
public class SMRuntimeException extends RuntimeException {
    /**
     * Will throw an error without any parameters.
     */
    public SMRuntimeException() { 
        super();
    }
    
    /**
     *
     * @param message A string containing the error message.
     */
    public SMRuntimeException( String message ) {
        super( message );
    }
    
    /**
     *
     * @param message A string containing the error message.
     * @param cause A throwable containing the cause.
     */
    public SMRuntimeException( String message, Throwable cause ) {
        super( message, cause );
    }
    
    /**
     *
     * @param cause A throwable containing the cause.
     */
    public SMRuntimeException( Throwable cause ) {
        super( cause );
    }
}
