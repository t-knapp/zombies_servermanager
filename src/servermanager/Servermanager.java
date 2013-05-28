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
     * Publicly available log.
     */
    public static SMLog log;
    
    /**
     * Main entry point to program.
     * @param args Any command line arguments.
     * @throws SMException
     */
    public static void main( String[] args ) throws SMException {
        log = new SMLog();
    }
}
