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
 * Various requests the server might ask for
 * @author cheese
 */
public abstract class SMRequests {
    public static void parseRequest( String request, String cvar ) throws SMException {
        if ( !properlyFormatted( request ) ) {
            if ( !request.isEmpty() )
                SMCoD.setCvar( cvar, "\"\"" );
            
            return;
        }
        
        request = request.substring( 1, request.length() );
        String arr[] = request.split( "\\\\" );
        String req = arr[ 0 ];
        
        boolean resetCvar = true;
        
        switch ( req ) {
            // low priority requests
            case "getcurrenttime":
                SMCoD.setCvar( "sm_reply_low", getCurrentTime() );
                break;
                
            // medium priority requests
                
            // high priority requests
                
            default:
                resetCvar = true;
                break;
        }
        
        if ( resetCvar )
            SMCoD.setCvar( cvar, "\"\"" );
    }

    public static boolean properlyFormatted( String message ) {
        if ( message.isEmpty() )
            return false;
        
        if ( !message.startsWith( "\\" ) )
            return false;
        
        String tmp[] = message.substring( 1, message.length() ).split( "\\\\" );
        if ( tmp.length > 1 && tmp.length % 2 != 0 )
            return false;
        
        return true;
    }
    
    /* LOW PRIORITY REQUESTS */
    public static String getUnixTimestamp() {
        Integer time = (int)( System.currentTimeMillis() / 1000L );
        return time.toString();
    }
    
    public static String getCurrentTime() {
        Integer time = (int)( System.currentTimeMillis() / 1000L ) % ( 24 * 60 * 60 );
        return time.toString();
    }
}
