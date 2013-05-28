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
    
    int lv = LEVEL_WARNING;
    Date time;
    String logname = "SMLog.log";
    FileWriter fileWriter;
    BufferedWriter logger;
    
    /**
     *
     * @throws SMException
     */
    public SMLog() throws SMException {
        try {
            File fl = new File( logname );
            if ( !fl.exists() )
                fl.createNewFile();
                
            fileWriter = new FileWriter( fl );
            logger = new BufferedWriter( fileWriter );
            
            time = new Date();
            
            write( "===============", LEVEL_OVERRIDE );
            write( "Log initialized", LEVEL_OVERRIDE );
            write( "===============", LEVEL_OVERRIDE );
        }
        catch ( IOException ex ) {
            throw new SMException( ex.getMessage() );
        }
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
            
            logger.write( time.getTime() + ";" + message );
            if ( !message.contains( "\n" ) )
                logger.write( "\n" );
            logger.flush();
        }
        catch ( IOException ex ) {
            throw new SMException( ex.getMessage() );
        }
    }
}
