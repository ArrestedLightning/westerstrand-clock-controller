/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clock.controller;

import java.util.Date;

/**
 *
 * @author joshualange
 */
public interface ScheduleEventListener {
    public void eventReceived(int id, ScheduleDate date);
    public void lastEventReceived();
}
