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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Servermanager logger class
 * @author cheese
 */
public class SMLog {
    /**
     * Log level 'none' will print nothing to the log.
     */
    public static int LEVEL_NONE = 0x00;
    /**
     * Log level 'info' will print any message flagged as info or higher.
     */
    public static int LEVEL_INFO = 0x01;
    /**
     * Log level 'warning' will print any message flagged as warning or higher.
     */
    public static int LEVEL_WARNING = 0x02;
    /**
     * Log level 'error' will print any message flagged as error or higher.
     */
    public static int LEVEL_ERROR = 0x03;
    /**
     * Log level 'critical' will only print messages flagged as critical.
     */
    public static int LEVEL_CRITICAL = 0x04;
    /**
     * Override for various required log messages.
     */
    public static int LEVEL_OVERRIDE = 0x05;
    
    private int lv = LEVEL_WARNING;
    private Date time;
    private String logname = "SMLog.log";
    private FileWriter fileWriter;
    private BufferedWriter logger;
    
    /**
     *
     * @throws SMException
     */
    public SMLog() throws SMException {
        try {
            File fl = new File( logname );
            if ( !fl.exists() )
                fl.createNewFile();
                
            this.fileWriter = new FileWriter( fl );
            this.logger = new BufferedWriter( fileWriter );
            
            this.time = new Date();
            
            write( "===============", LEVEL_OVERRIDE );
            write( "Log initialized", LEVEL_OVERRIDE );
            write( "===============", LEVEL_OVERRIDE );
        }
        catch ( IOException ex ) {
            throw new SMException( ex.getMessage() );
        }
    }
    
    /**
     * Sets the log level.
     * @param lvl Log level to be set.
     * @throws SMException 
     */
    public void setLogLevel( int lvl ) throws SMException {
        this.lv = lvl;
        write( "Log level set to " + lvl );
    }
    
    /**
     * Writes a message to the log with the default log level of 'info'.
     * @param message The message to write.
     * @throws SMException
     */
    public void write( String message ) throws SMException {
        write( message, LEVEL_INFO );
    }
    
    /**
     * Writes a message to the log with custom log level.
     * @param message The message to write.
     * @param lvl The log level of the message.
     * @throws SMException
     */
    public void write( String message, int lvl ) throws SMException {
        try {
            if ( lvl != LEVEL_OVERRIDE && ( lvl < lv || lv == LEVEL_NONE ) )
                return;

            this.logger.write( time.getTime() + ";" );
     
            if ( lvl == LEVEL_WARNING )
                this.logger.write( "*** WARNING *** " );
            else if ( lvl == LEVEL_ERROR )
                this.logger.write( "***** ERROR ***** " );

            this.logger.write( message );
            
            if ( !message.contains( "\n" ) )
                this.logger.write( "\n" );
            this.logger.flush();
        }
        catch ( IOException ex ) {
            throw new SMException( ex.getMessage() );
        }
    }
    
    /**
     * 'Safe' and exception free logging from our exception calls to prevent infinite loops
     */
    public void logRuntimeException() {
        logRuntimeException( "Runtime exception" );
    }
    
    /**
     * 'Safe' and exception free logging from our exception calls to prevent infinite loops
     * @param message The exception message.
     */
    public void logRuntimeException( String message ) {
        try {
            write( message, LEVEL_ERROR );
        }
        catch ( SMException ex ) {
        }
    }
    
    /**
     * 'Safe' and exception free logging from our exception calls to prevent infinite loops
     */
    public void logException() {
        logException( "Exception" );
    }
    
    /**
     * 'Safe' and exception free logging from our exception calls to prevent infinite loops
     * @param message The exception message.
     */
    public void logException( String message ) {
        try {
            write( message, LEVEL_WARNING );
        }
        catch ( SMException ex ) {
        }
    }
}
