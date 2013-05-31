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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Custom config (.cfg) parser. Features:
 * - Four variable types: integer, float, boolean, and string
 * - No whitespace restrictions
 * - Easily extensible
 * - Comments via #
 * @author cheese
 */
public abstract class SMConfig {
    private static String configname = "config.cfg";
    private static BufferedReader reader;
    private static FileInputStream fileReader;
    private static HashMap< String, Object > dictionary;
    
    /**
     * Loads the config.
     * @throws SMException
     */
    public static void load() throws SMException {
        dictionary = new HashMap();
        
        try {
            SMLog.write( "Loading config file..." );
            
            File fl = new File( configname );
            if ( !fl.exists() ) {
                SMLog.write( "Config does not exist, creating..." );
                if ( !fl.createNewFile() )
                    throw new SMException( "Failed to create config file" );
                return;
            }
            
            fileReader = new FileInputStream( fl );
            reader = new BufferedReader( new InputStreamReader( fileReader, "US-ASCII" ) );
            
            String line = "";
            int linenumber = 0;
            // This is an incredibly simple config reader
            // The syntax is this:
            // [key] = [value]
            while ( ( line = reader.readLine() ) != null ) {
                try {
                    linenumber++;
                    
                    if ( line.startsWith( "#" ) )
                        continue;
                    
                    if ( line.trim().isEmpty() )
                        continue;
                    
                    String[] split = line.split( "=" );
                    if ( split.length != 2 )
                        throw new SMException( "Config mismatch [" + linenumber + "]" );
                    
                    // key is always a string
                    String key = split[ 0 ].trim();

                    // value is tbd
                    Object value;
                    String tmp = split[ 1 ].trim();
                    
                    // parse as string, boolean, or int
                    if ( tmp.charAt( 0 ) == '\"' && tmp.charAt( tmp.length() - 1 ) == '\"' )
                        value = tmp.substring( 1, tmp.length() - 1 );
                    else if ( tmp.toLowerCase().equals( "true" ) )
                        value = true;
                    else if ( tmp.toLowerCase().equals( "false" ) )
                        value = false;
                    else {
                        try {
                            if ( tmp.contains( "." ) )
                                value = new Float( tmp );
                            else
                                value = new Integer( tmp );
                        }
                        catch ( NumberFormatException ex ) {
                            throw new SMException( "Invalid type (must be string, boolean, integer, or float) [" + linenumber + "]" );
                        }
                    }
                    
                    if ( dictionary.containsKey( key ) )
                        throw new SMException( "Duplicate config keys [" + linenumber + "]" );
                    
                    dictionary.put( key, value );
                }
                catch ( SMException ex ) {
                    // nothing to do
                    // simple config mistakes will not break our program
                }
            }
        }
        catch ( IOException ex ) {
            throw new SMRuntimeException( ex.getMessage() );
        }
        
        SMLog.write( "Config loaded." );
    }
    
    /**
     * Sets the config name.
     * @param name The config name to use.
     * @throws SMException
     */
    public static void setConfigName( String name ) throws SMException {
        if ( name.contains( "." ) ) {
            int pos = name.indexOf( "." );
            name = name.substring( pos, name.length() );
        }
        
        name += ".cfg";
        
        configname = name;
    }
    
    /**
     * Grabs a string value from the config.
     * @param key The name of the key.
     * @return A string containing the value, or "" if the key doesn't exist.
     * @throws SMException
     */
    public static String getString( String key ) throws SMException {
        if ( containsKey( key ) )
            return dictionary.get( key ).toString();
        else
            return "";
    }
    
    /**
     * Grabs an integer value from the config.
     * @param key The name of the key.
     * @return An integer containing the value, or 0 if the key doesn't exist.
     * @throws SMException
     */
    public static int getInt( String key ) throws SMException {
        if ( containsKey( key ) )
            return (int)dictionary.get( key );
        else
            return 0;
    }
    
    /**
     * Grabs a float value from the config.
     * @param key The name of the key.
     * @return Returns a float containing the value, otherwise 0 if the key doesn't exist.
     * @throws SMException
     */
    public static float getFloat( String key ) throws SMException {
        if ( containsKey( key ) )
            return (float)dictionary.get( key );
        else
            return 0;
    }
    
    /**
     * Grabs a boolean value from the config.
     * @param key The name of the key.
     * @return A boolean containing the value, otherwise false if the key doesn't exist.
     * @throws SMException
     */
    public static boolean getBoolean( String key ) throws SMException {
        if ( containsKey( key ) )
            return (boolean)dictionary.get( key );
        else
            return false;
    }
    
    /**
     * Checks if the config contains the key.
     * @param key The name of the key.
     * @return True if the key exists, otherwise false.
     * @throws SMException
     */
    public static boolean containsKey( String key ) throws SMException {
        return dictionary.containsKey( key );
    }
}
