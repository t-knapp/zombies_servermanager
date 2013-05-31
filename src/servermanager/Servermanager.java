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
     * Main entry point and main thread.
     * @param args Any command line arguments. Not used.
     * @throws SMException
     */
    public static void main( String[] args ) throws SMException {
        SMLog.load();
        SMLog.setLogLevel( SMLog.LEVEL_INFO );
        
        SMConfig.load();
        
        // apply cod settings from config file
        if ( SMConfig.containsKey( "server_ip" ) )
            SMCoD.setIP( SMConfig.getString( "server_ip" ) );
        if ( SMConfig.containsKey( "server_port" ) )
            SMCoD.setPort( SMConfig.getInt( "server_port" ) );
        if ( SMConfig.containsKey( "server_rconpassword" ) )
            SMCoD.setRconPassword( SMConfig.getString( "server_rconpassword" ) );
        if ( SMConfig.containsKey( "server_delay" ) )
            SMCoD.setRconDelay( SMConfig.getInt( "server_delay" ) );
        
        // apply mysql settings from config file
        if ( SMConfig.containsKey( "mysql_url" ) )
            SMDatabase.setURL( SMConfig.getString( "mysql_url" ) );
        if ( SMConfig.containsKey( "mysql_port" ) )
            SMDatabase.setPort( SMConfig.getInt( "mysql_port" ) );
        if ( SMConfig.containsKey( "mysql_database" ) )
            SMDatabase.setDatabase( SMConfig.getString( "mysql_database" ) );
        if ( SMConfig.containsKey( "mysql_username" ) )
            SMDatabase.setUsername( SMConfig.getString( "mysql_username" ) );
        if ( SMConfig.containsKey( "mysql_password" ) )
            SMDatabase.setPassword( SMConfig.getString( "mysql_password" ) );
        
        SMDatabase.connect();
        
        SMCoD.initPlayers();
        
        try {         
            int ticks = 0;
            boolean serveralive = true;
            boolean goodrcon = false;
            boolean initcvars = false;
            
            // temporary values
            String tmp = null;
            String tmparray[] = null;
            double tmpdouble = 0;
            int tmpint = 0;

            if ( SMCoD.checkServerStatus() )
                goodrcon = SMCoD.checkRconPassword();
            else
                serveralive = false;
            
            // defaults
            int frametime = 50;
            double fps = 20.0;
            
            while ( true ) {
                try {
                    ticks++;
                    Thread.sleep( frametime );
                    
                    // every 10 seconds check if the server is alive
                    if ( ticks % (int)( fps * 10 ) == 0 ) {
                        if ( !SMCoD.checkServerStatus() ) {
                            serveralive = false;
                            throw new SMException( "Server not alive" );
                        }
                        else
                            serveralive = true;
                        
                        if ( !goodrcon )
                            goodrcon = SMCoD.checkRconPassword();
                    }
                    
                    if ( goodrcon ) {
                        if ( !initcvars ) {
                            initcvars = initCvars();
                        
                            // if our frametime hasn't been set yet, grab it
                            tmp = SMCoD.stripRequest( SMCoD.rcon( "sv_fps" ) );
                            fps = Double.parseDouble( tmp );
                            tmpdouble = (double)( 1 / fps );
                            frametime = (int)( tmpdouble * 1000 );

                            SMLog.write( "Server tickrate: 1/" + fps + " = " + frametime + " ms frametime" );
                        }

                        // do various things per tick
                        if ( serveralive ) {
                            // every 5 seconds, check if there's a low priority server request
                            // e.g. unix timestamp update, tickcount update
                            if ( ticks % (int)( fps * 5 ) == 0 ) {
                                tmp = SMCoD.stripRequest( SMCoD.rcon( "sm_request_low" ) );
                                SMRequests.parseRequest( tmp, "sm_request_low" );

                                tmp = SMCoD.stripRequest( SMCoD.rcon( "sm_tickcount" ) );
                                if ( !tmp.isEmpty() ) {
                                    tmpint = Integer.parseInt( tmp );
                                    if ( tmpint != ticks ) {
                                        SMLog.write( "Updating tickcount (was " + ticks + ") to " + tmpint );
                                        ticks = tmpint;
                                    }
                                }

                                tmp = null;
                            }

                            // every second, check if there's a medium priority server request
                            // e.g. idk
                            // also, update player list + check for bans
                            if ( ticks % fps == 0 ) {
                                tmp = SMCoD.stripRequest( SMCoD.rcon( "sm_request_med" ) );
                                SMRequests.parseRequest( tmp, "sm_request_med" );
                                
                                SMCoD.updatePlayers();

                                tmp = null;
                            }

                            // every 1/5 of a second, check if there's a high priority server request
                            // e.g. players connecting, requesting stats, etc.
                            if ( ticks % (int)( fps / 5 ) == 0 ) {
                                tmp = SMCoD.stripRequest( SMCoD.rcon( "sm_request_high" ) );
                                SMRequests.parseRequest( tmp, "sm_request_high" );

                                tmp = null;
                            }

                            tmp = null;
                            tmparray = null;
                            tmpdouble = 0;
                            tmpint = 0;
                        }
                    }
                }
                catch ( SMException ex ) {
                }
            }
        }
        catch ( SMException | InterruptedException | NumberFormatException ex ) {
            throw new SMRuntimeException( ex.getMessage() );
        }
    }
    
    /**
     * If cvars are not set, will make them blank to prevent "Server: [cvar]" message spamming
     * @return Always returns true.
     * @throws SMException
     */
    public static boolean initCvars() throws SMException {
        // set our cvars to blank if they are not initialized yet
        if ( !SMCoD.isCvarSet( "sm_request_low" ) )
            SMCoD.setCvar( "sm_request_low", "\"\"" );
        if ( !SMCoD.isCvarSet( "sm_request_med" ) )
            SMCoD.setCvar( "sm_request_med", "\"\"" );
        if ( !SMCoD.isCvarSet( "sm_request_high" ) )
            SMCoD.setCvar( "sm_request_high", "\"\"" );
        if ( !SMCoD.isCvarSet( "sm_tickcount" ) )
            SMCoD.setCvar( "sm_tickcount", "\"\"" );
        
        return true;
    }
}
