/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clock.controller;

/**
 *
 * @author joshualange
 */
public interface ClockConnection {
    
    public boolean connect();
    public boolean isConnected();
    public void disconnect();
    public boolean writeBytes(byte[] data);
    public void setReadHandler(ClockDataListener controller);
}
