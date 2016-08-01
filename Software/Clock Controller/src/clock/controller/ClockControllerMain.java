/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clock.controller;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author joshualange
 */
public class ClockControllerMain {

    static String VERSION = "1.0";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        ConnectWindow connectWindow = new ConnectWindow();
        connectWindow.setVisible(true);
    }
}
