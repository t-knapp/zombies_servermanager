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
 * Main class.
 */
public class Servermanager {   
    /**
     * Publicly available database.
     */
    public static SMDatabase db;
    
    /**
     * Main entry point to program.
     * @param args Any command line arguments.
     * @throws SMException
     */
    public static void main( String[] args ) throws SMException {
        SMLog.load();
        SMLog.setLogLevel( SMLog.LEVEL_INFO );
        
        SMConfig.load();
        
        if ( SMConfig.containsKey( "server_ip" ) )
            SMCoD.setIP( SMConfig.getString( "server_ip" ) );
        if ( SMConfig.containsKey( "server_port" ) )
            SMCoD.setPort( SMConfig.getInt( "server_port" ) );
        if ( SMConfig.containsKey( "server_rconpassword" ) )
            SMCoD.setRconPassword( SMConfig.getString( "server_rconpassword" ) );
        if ( SMConfig.containsKey( "server_delay" ) )
            SMCoD.setRconDelay( SMConfig.getInt( "server_delay" ) );
        
        //db = new SMDatabase( "localhost", "test", "test", "test123" );
    }
}
