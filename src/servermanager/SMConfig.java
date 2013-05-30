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
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author cheese
 */
public abstract class SMConfig {
    private static String configname = "config.cfg";
    private static BufferedReader reader;
    private static FileReader fileReader;
    private static HashMap< String, Object > dictionary;
    
    public static void load() throws SMException {
        dictionary = new HashMap();
        
        try {
            SMLog.write( "Loading config file..." );
            
            File fl = new File( configname );
            if ( !fl.exists() ) {
                SMLog.write( "Config does not exist, creating..." );
                fl.createNewFile();
                return;
            }
            
            fileReader = new FileReader( fl );
            reader = new BufferedReader( fileReader );
            
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
                        value = new String( tmp.substring( 1, tmp.length() - 1 ) );
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
    
    public static void setConfigName( String name ) throws SMException {
        if ( name.contains( "." ) ) {
            int pos = name.indexOf( "." );
            name = name.substring( pos, name.length() );
        }
        
        name += ".cfg";
        
        configname = name;
    }
    
    public static String getString( String key ) throws SMException {
        if ( containsKey( key ) )
            return dictionary.get( key ).toString();
        else
            return "";
    }
    
    public static int getInt( String key ) throws SMException {
        if ( containsKey( key ) )
            return (int)dictionary.get( key );
        else
            return 0;
    }
    
    public static float getFloat( String key ) throws SMException {
        if ( containsKey( key ) )
            return (float)dictionary.get( key );
        else
            return 0;
    }
    
    public static boolean getBoolean( String key ) throws SMException {
        if ( containsKey( key ) )
            return (boolean)dictionary.get( key );
        else
            return false;
    }
    
    public static boolean containsKey( String key ) throws SMException {
        return dictionary.containsKey( key );
    }
}
