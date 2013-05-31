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
 * Class containing all /rcon status related player information.
 * @author cheese
 */
public final class SMCoDPlayer {
    // this is essentially a flag that tells me if this player
    // has ANY defined values on it, or if it's just blank
    private boolean initialized = false;
    
    private int clientNumber;
    private int score;
    private int ping;
    private String name;
    private int lastMessage;
    private String IP;
    private int qport;
    private int rate;
    
    /**
     * Basic constructor. Resets everything to either -1 or a blank string.
     */
    public SMCoDPlayer() {
        reset();
    }
    
    /**
     * Preferred constructor. Resets everything to either -1 or a blank string,
     * and sets the clientNumber to the proper slot.
     * @param slot
     */
    public SMCoDPlayer( int slot ) {
        reset();
        this.clientNumber = slot;
    }
    
    /**
     * Resets everything to either -1 or a blank string.
     */
    public void reset() {
        this.score = -1;
        this.ping = -1;
        this.name = "";
        this.lastMessage = -1;
        this.IP = "";
        this.qport = -1;
        this.rate = -1;
        this.initialized = false;
    }
    
    /**
     * Sets initialized to be true.
     */
    public void init() {
        this.initialized = true;
    }
    
    /**
     * Updates the score.
     * @param newscore The new score.
     */
    public void setScore( int newscore ) {
        this.score = newscore;
    }
    
    /**
     * Updates the ping.
     * @param newping The new ping.
     */
    public void setPing( int newping ) {
        this.ping = newping;
    }
    
    /**
     * Updates the name.
     * @param newname The new name.
     */
    public void setName( String newname ) {
        this.name = newname;
    }
    
    /**
     * Updates the last message.
     * @param newlastmessage The new last message.
     */
    public void setLastMessage( int newlastmessage ) {
        this.lastMessage = newlastmessage;
    }
    
    /**
     * Updates the IP address.
     * @param newip The new IP address.
     */
    public void setIP( String newip ) {
        this.IP = newip;
    }
    
    /**
     * Updates the qport.
     * @param newqport The new qport.
     */
    public void setQPort( int newqport ) {
        this.qport = newqport;
    }
    
    /**
     * Updates the rate.
     * @param newrate The new rate.
     */
    public void setRate( int newrate ) {
        this.rate = newrate;
    }
    
    /**
     * Gets if this slot is initialized.
     * @return True if initialized, false otherwise.
     */
    public boolean isInitialized() {
        return this.initialized;
    }
    
    /**
     * Gets the player's client number.
     * @return An integer between 0 - 63.
     */
    public int getClientNumber() {
        return this.clientNumber;
    }
    
    /**
     * Gets the player's score.
     * @return An integer containing the score.
     */
    public int getScore() {
        return this.score;
    }
    
    /**
     * Gets the player's ping.
     * @return An integer between 0 - 999.
     */
    public int getPing() {
        return this.ping;
    }
    
    /**
     * Gets the player's name.
     * @return A string containing the player's name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the player's last message.
     * @return An integer containing the player's last message.
     */
    public int getLastMessage() {
        return this.lastMessage;
    }
    
    /**
     * Gets the player's IP address and port.
     * @return A string containing both the IP address and port of the player.
     */
    public String getIP() {
        return this.IP;
    }
    
    /**
     * Gets the player's qport.
     * @return An integer between 0 - 99999.
     */
    public int getQPort() {
        return this.qport;
    }
    
    /**
     * Gets the player's rate.
     * @return An integer between 0 - 99999.
     */
    public int getRate() {
        return this.rate;
    }
}
