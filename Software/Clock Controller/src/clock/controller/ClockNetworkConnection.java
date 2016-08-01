/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clock.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author joshualange
 */
public class ClockNetworkConnection implements ClockConnection {

    private Socket theConnectionSocket;
    private InputStream socketInputStream;
    private OutputStream socketOutputStream;

    private ClockDataListener theListener;

    private Thread listenerThread;

    private String ip;

    private enum ParserState {

        START, PAYLOAD, END, CHECKSUM
    };

    /**
     * Get the value of ip
     *
     * @return the value of ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set the value of ip
     *
     * @param ip new value of ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    private int port;

    /**
     * Get the value of port
     *
     * @return the value of port
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the value of port
     *
     * @param port new value of port
     */
    public void setPort(int port) {
        this.port = port;
    }

    public ClockNetworkConnection() {

    }

    public ClockNetworkConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean connect() {
        if (this.ip != null && this.port != 0) {
            try {
                theConnectionSocket = new Socket(this.ip, this.port);
                if (theConnectionSocket.isConnected()) {
                    socketInputStream = theConnectionSocket.getInputStream();
                    socketOutputStream = theConnectionSocket.getOutputStream();
                    //socketInputStream.setDataAvailableListener();
                    //set up port using pseudo-rfc2217 protocol
                    //19200, 8N1 [see page 37]
                    //not working right now, will skip at the moment
                    //byte initString[] = {0x55, (byte)0xAA, 0x55, 0x00, 0x4B, 0x00, (byte)0x83, (byte)0x83};
                    //socketOutputStream.write(initString);
                    //socketOutputStream.flush();
                    this.setupListenerThread();
                    return true;
                }

            } catch (IOException ex) {
                Logger.getLogger(ClockNetworkConnection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isConnected() {
        return (!theConnectionSocket.isClosed());
    }

    @Override
    public void disconnect() {
        try {
            socketOutputStream.flush();
            theConnectionSocket.shutdownOutput();
            socketInputStream = null;
            socketOutputStream = null;
            theConnectionSocket.close();
            theConnectionSocket = null;
        } catch (IOException ex) {
            Logger.getLogger(ClockNetworkConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean writeBytes(byte[] data) {
        if (!theConnectionSocket.isClosed()) {
            if (socketOutputStream != null) {
                try {
                    socketOutputStream.write(data);
                    return true;
                } catch (IOException ex) {
                    Logger.getLogger(ClockNetworkConnection.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void setReadHandler(ClockDataListener controller) {
        theListener = controller;
    }

    private void setupListenerThread() {
        if (listenerThread != null) {
            //listenerThread.stop();
        }
        listenerThread = new Thread() {
            byte[] tmpBuffer = new byte[100];
            int readPtr = 0;
            boolean exit = false;
            boolean skip = false;
            ParserState state = ParserState.START;

 
            @Override
            public void run() {
                while (!exit) {
                    if (socketInputStream != null) {
                        try {
                            int data = socketInputStream.read();
                            //System.out.println("State = " + state + " data = " + Integer.toHexString(data));
                            if (data == -1) { //EOF, something went horribly wrong
                                return;
                            }
                            if (readPtr > (tmpBuffer.length - 2)) {
                                //don't overflow the buffer
                                state = ParserState.START;
                            }
                            switch (state) {
                                case START:
                                    if (data == 0x02) {//start byte
                                        readPtr = 0;
                                        tmpBuffer[readPtr] = (byte) data;
                                        state = ParserState.PAYLOAD;
                                    } //any other byte doesn't change the state
                                    //todo:  We probably should be looking for 
                                    //ACK bytes and settign a semaphore appropriately
                                    break;
                                case PAYLOAD:
                                    readPtr++;
                                    tmpBuffer[readPtr] = (byte) data;
                                    if (data == 0x03) {//stop byte
                                        state = ParserState.CHECKSUM;
                                    }
                                    break;
                                case END:
                                    //don't really need this state, I guess
                                    state = ParserState.CHECKSUM;
                                    break;
                                case CHECKSUM:
                                    readPtr++;
                                    tmpBuffer[readPtr] = (byte) data;//read the checksum
                                    if (theListener != null) { //handle the end of the packet
                                        byte tmpByteBuffer[] = new byte[readPtr + 1];
                                        System.arraycopy(tmpBuffer, 0, tmpByteBuffer, 0, readPtr + 1);
                                        theListener.dataReceived(tmpByteBuffer);
                                    }//theListener != null
                                    state = ParserState.START;
                                    break;
                                default:
                                    state = ParserState.START;
                                    break;
                            }//switch
                        } catch (IOException ex) {
                            Logger.getLogger(ClockNetworkConnection.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "The application has lost the connection to the clock.\n"
                                    + "Please save your work and restart the application.", "Connection Lost", 
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };
        listenerThread.start();
    }

}
