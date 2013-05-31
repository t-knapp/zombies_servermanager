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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * Contains all methods important to Call of Duty.
 * @author cheese
 */
public abstract class SMCoD {
    private static String IP_ADDRESS = "localhost";
    private static int PORT = 28960;
    private static String RCON_PASSWORD = "password";
    private static int RCON_DELAY = 500;
    private static HashMap< Integer, SMCoDPlayer > players;
    
    /**
     * Initializes our hashmap of client slots with blank SMCoDPlayers.
     * @throws SMException
     */
    public static void initPlayers() throws SMException {
        players = new HashMap();
        for ( int i = 0; i < 64; i++ ) {
            players.put( i, new SMCoDPlayer( i ) );
        }
    }
    
    /**
     * Gets /rcon status from the server and updates our hashmap with current player's information.
     * @throws SMException
     */
    public static void updatePlayers() throws SMException {
        String status = command( "rcon " + RCON_PASSWORD + " status" );
        
        if ( status.contains( "Bad rconpassword." ) )
            return;
        
        for ( int p = 0; p < 64; p++ ) {
            SMCoDPlayer ply = players.get( p );
            ply.reset();
        }
        
        status = status.trim().substring( 10 );
        
        String data[] = status.split( "\n" );
        String tmp;
        String tmparray[];
        
        // first three are unimportant
        for ( int i = 3; i < data.length; i++ ) {
            try {
                tmp = data[ i ].trim().replaceAll( " +", " " );
                tmparray = tmp.split( " " );

                int slot = Integer.parseInt( tmparray[ 0 ] );
                int score = Integer.parseInt( tmparray[ 1 ] );
                int ping = 999;
                if ( !tmparray[ 2 ].equals( "CNCT" ) )
                    ping = Integer.parseInt( tmparray[ 2 ] );
                
                StringBuffer buf = new StringBuffer();
                int x = 0;
                for ( ; x < tmparray.length - 7; x++ )
                    buf.append(" ").append( tmparray[ 3 + x ]);
                
                String name = buf.toString().trim();
                
                if ( tmparray.length - 7 == 0 )
                    name = tmparray[ 3 ];
                
                int lastMessage = Integer.parseInt( tmparray[ 3 + x ] );
                String IP = tmparray[ 4 + x ];

                int qport = Integer.parseInt( tmparray[ 5 + x ] );
                int rate = Integer.parseInt( tmparray[ 6 + x ] );
                
                SMCoDPlayer ply = players.get( i );
                
                ply.init();
                ply.setScore( score );
                ply.setPing( ping );
                ply.setName( name );
                ply.setLastMessage( lastMessage );
                ply.setIP( IP );
                ply.setQPort( qport );
                ply.setRate( rate );
                
                players.put( i, ply );
            }
            catch ( Exception ex ) {
            }
        }
    }
    
    /**
     * Gets a player from the hashmap.
     * @param slot The client slot to retrieve.
     * @return An SMCoDPlayer.
     * @throws SMException
     */
    public static SMCoDPlayer getPlayer( int slot ) throws SMException {
        SMCoDPlayer ply = players.get( slot );
        return ply;
    }
    
    /**
     * Basic implementation of a 'rconcommand' method.
     * @param command The command you want to run on the server.
     * @return A blank string if bad rconpassword, or a string containing the results of the command.
     * @throws SMException
     */
    public static String rcon( String command ) throws SMException {
        String ret = command( "rcon " + RCON_PASSWORD + " " + command );
        
        if ( ret.contains( "Bad rconpassword." ) )
                return "";
        
        if ( ret.trim().length() == 9 )
            return "";

        return removeColors( ret.trim().substring( 10 ) );
    }
    
    /**
     * Checks if the rconpassword is correct.
     * @return True if it is correct, false otherwise.
     * @throws SMException
     */
    public static boolean checkRconPassword() throws SMException {
        String ret = command( "rcon " + RCON_PASSWORD + " status" );
        
        if ( ret.contains( "Bad rconpassword." ) )
            return false;
        
        return true;
    }
    
    /**
     * Checks if the server is alive.
     * @return True if it is alive, false otherwise.
     */
    public static boolean checkServerStatus() {
        try {
            String ret = command( "getstatus" );

            if ( ret.contains( "statusResponse" ) )
                return true;

            return false;
        }
        catch ( Exception ex ) {
            return false;
        }
    }
    
    /**
     * Strips a cvar 'get' reply to its value.
     * @param request The request.
     * @return A string containing the value of the cvar.
     * @throws SMException
     */
    public static String stripRequest( String request ) throws SMException {
        if ( request.isEmpty() )
            return "";

        return request.substring( request.indexOf( ":" ) + 2, request.indexOf( "\"", request.indexOf( ":" ) + 2 ) );
    }
    
    /**
     * Sets a cvar to whatever value you want.
     * @param cvar The cvar to set.
     * @param value The value to set the cvar to.
     * @throws SMException
     */
    public static void setCvar( String cvar, String value ) throws SMException {
        command( "rcon " + RCON_PASSWORD + " set " + cvar + " " + value );
    }
    
    /**
     * Checks if a cvar is initialized (i.e. not set)
     * @param cvar The cvar to check.
     * @return Returns true if the cvar is initialized, otherwise false.
     * @throws SMException
     */
    public static boolean isCvarSet( String cvar ) throws SMException {
        String ret = command( "rcon " + RCON_PASSWORD + " " + cvar );
        
        if ( ret.contains( "Bad rconpassword." ) )
            return false;
        
        if ( ret.trim().length() == 9 )
            return false;
        
        return true;
    }
    
    /**
     * Removes Quake-style color codes from a string.
     * @param str The string to remove colors from.
     * @return A colorless string.
     * @throws SMException
     */
    public static String removeColors( String str ) throws SMException {
        str = str.replaceAll( "(\\^\\d)", "" );
        str = str.replaceAll( "(\\^\\d)", "" );
        return str;
    }
    
    /**
     * Update the IP.
     * @param newip
     */
    public static void setIP( String newip ) {
        IP_ADDRESS = newip;
    }
    
    /**
     * Update the port.
     * @param newport
     */
    public static void setPort( int newport ) {
        PORT = newport;
    }
    
    /**
     * Update the rconpassword.
     * @param newrcon
     */
    public static void setRconPassword( String newrcon ) {
        RCON_PASSWORD = newrcon;
    }
    
    /**
     * Update the rcon delay.
     * @param newdelay
     */
    public static void setRconDelay( int newdelay ) {
        RCON_DELAY = newdelay;
    }
    
    private static String command( String command ) throws SMException {
        String ret = "";
        
        try {
            String p = "    " + command;
            
            byte[] packet = p.getBytes( "US-ASCII" );
            packet[ 0 ] = (byte)0xFF;
            packet[ 1 ] = (byte)0xFF;
            packet[ 2 ] = (byte)0xFF;
            packet[ 3 ] = (byte)0xFF;
            
            byte[] buffer = new byte[ 2048 ];
            
            InetAddress ip = InetAddress.getByName( IP_ADDRESS );
            DatagramPacket s = new DatagramPacket( packet, packet.length, ip, PORT );
            DatagramPacket r = new DatagramPacket( buffer, buffer.length );
            
            DatagramSocket sock = new DatagramSocket();
            sock.setSoTimeout( 1000 );
            sock.send( s );
            sock.receive( r );
            
            String recv = new String( buffer, "US-ASCII" );
            ret = recv;
        }
        catch ( IOException ex ) {
            throw new SMException( ex.getMessage() );
        }
        
        return ret;
    }
}
